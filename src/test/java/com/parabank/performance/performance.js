import http from 'k6/http';
import { check, sleep, group } from 'k6';
import { Trend } from 'k6/metrics';
import { BASE_API_URL, performanceTestingEndpoints } from './config.js';

// One custom Trend metric per endpoint, so each endpoint's response time
// is tracked and thresholded independently rather than lumped into one global number
const endpointTrends = {};
performanceTestingEndpoints.forEach((endpoint) => {
    endpointTrends[endpoint.name] = new Trend(`duration_${endpoint.name}`);
});

// Build per-endpoint thresholds dynamically from config.js, so adding a new
// endpoint to performanceTestingEndpoints automatically gets its own threshold —
// no need to hand-edit this block later
const thresholds = {};
performanceTestingEndpoints.forEach((endpoint) => {
    thresholds[`duration_${endpoint.name}`] = [`p(95)<${endpoint.threshold}`];
});
thresholds['http_req_failed'] = ['rate<0.01']; // fewer than 1% of requests should fail, across all scenarios

export const options = {
    scenarios: {
        // --- SMOKE: minimal load, confirms the endpoints work at all before running anything heavier ---
        smoke: {
            executor: 'constant-vus',
            vus: 1,
            duration: '30s',
            exec: 'runEndpoints',
            tags: { test_type: 'smoke' },
        },

        // --- LOAD: realistic expected traffic, ramping up and back down gradually ---
        load: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                { duration: '30s', target: 5 },  // ramp up gently — conservative given rate-limit history
                { duration: '1m', target: 5 },   // hold at steady realistic load
                { duration: '30s', target: 0 },  // ramp back down
            ],
            exec: 'runEndpoints',
            tags: { test_type: 'load' },
            startTime: '35s', // starts right after smoke finishes
        },

        // --- SPIKE: sudden burst, then drop — HIGH RISK of tripping Cloudflare, see notes below ---
        spike: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                { duration: '10s', target: 20 }, // sharp burst — deliberately aggressive, that's the point of a spike test
                { duration: '20s', target: 20 },
                { duration: '10s', target: 0 },
            ],
            exec: 'runEndpoints',
            tags: { test_type: 'spike' },
            startTime: '2m35s', // starts after load finishes
        },

        // --- SOAK: sustained moderate load over a longer duration, surfaces slow leaks/degradation ---
        soak: {
            executor: 'constant-vus',
            vus: 3,
            duration: '5m', // real soak tests run much longer (hours) — kept short here to respect the shared demo
            exec: 'runEndpoints',
            tags: { test_type: 'soak' },
            startTime: '3m15s', // starts after spike finishes
        },
    },
    thresholds,
};

export function runEndpoints() {
    performanceTestingEndpoints.forEach((endpoint) => {
        group(endpoint.name, () => {
            const response = http.get(`${BASE_API_URL}${endpoint.path}`, {
                headers: { Accept: 'application/json' },
            });

            endpointTrends[endpoint.name].add(response.timings.duration);

            check(response, {
                [`${endpoint.name}: status is 200`]: (r) => r.status === 200,
                [`${endpoint.name}: not rate limited (429)`]: (r) => r.status !== 429,
                [`${endpoint.name}: response time under ${endpoint.threshold}ms`]: (r) =>
                    r.timings.duration < endpoint.threshold,
            });
        });
    });

    sleep(1); // small pacing gap between iterations — helps avoid hammering the endpoint back-to-back
}