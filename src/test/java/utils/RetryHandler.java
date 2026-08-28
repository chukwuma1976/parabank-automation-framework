package utils;

import java.util.function.Supplier;

import io.restassured.response.Response;

public class RetryHandler {
    public static Response withRetry(Supplier<Response> request, int maxAttempts) {
        Response response = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            response = request.get();
            if (response.getStatusCode() != 429) {
                return response;
            }
            long backoffMillis = (long) Math.pow(2, attempt) * 1000; // 2s, 4s, 8s...
            System.out.println("Rate limited (429). Retrying in " + backoffMillis + "ms (attempt " + attempt + "/"
                    + maxAttempts + ")");
            try {
                Thread.sleep(backoffMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return response;
    }
}