package utils;

import static io.restassured.RestAssured.given;

import com.parabank.config.ConfigReader;

public class DataControl {
    private static String baseURL = ConfigReader.get("BASE_API_URL");

    public static void resetData() {
        given().baseUri(baseURL)
                .when().post("/cleanDB")
                .then().statusCode(204);
    }

    public static void initializeData() {
        given().baseUri(baseURL)
                .when().post("/initializeDB")
                .then().statusCode(204);
    }

}
