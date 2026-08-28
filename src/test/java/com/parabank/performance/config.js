const customerId = 12212;

export const BASE_API_URL = "https://parabank.parasoft.com/parabank/services/bank";

export const performanceTestingEndpoints = [
    {
        name: "Search",
        path: `/customers/${customerId}/accounts`,
        threshold: 500
    }
];