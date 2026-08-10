package utils;

import com.parabank.config.ConfigReader;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class BankActions {
    private static String baseURL = ConfigReader.get("BASE_API_URL");

    public static double getBalance(int accountId) {
        Response response = given()
                .baseUri(baseURL)
                .headers("Accept", "application/json")
                .when()
                .get("/accounts/" + accountId)
                .then()
                .statusCode(200)
                .extract().response();

        return response.jsonPath().getDouble("balance");
    }
}
