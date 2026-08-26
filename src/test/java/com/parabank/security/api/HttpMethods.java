package com.parabank.security.api;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.testng.annotations.Test;

import com.parabank.config.ConfigReader;

import io.restassured.response.Response;
import utils.TestDataGenerator;

public class HttpMethods {
    int customerID = TestDataGenerator.getCustomerId();
    String baseURL = ConfigReader.get("BASE_API_URL");
    String path = "/customers/" + customerID + "/accounts";

    @Test(groups = { "api", "security" })
    public void getRequestSupported() {
        Response response = given()
                .baseUri(baseURL)
                .when()
                .get(path);
        assertEquals(response.statusCode(), 200);
    }

    @Test(groups = { "api", "security" })
    public void postRequestIsNotSupported() {
        Response response = given()
                .baseUri(baseURL)
                .when()
                .post(path);
        assertEquals(response.statusCode(), 405);
    }

    @Test(groups = { "api", "security" })
    public void putRequestIsNotSupported() {
        Response response = given()
                .baseUri(baseURL)
                .when()
                .put(path);
        assertEquals(response.statusCode(), 405);
    }

    @Test(groups = { "api", "security" })
    public void patchRequestIsNotSupported() {
        Response response = given()
                .baseUri(baseURL)
                .when()
                .patch(path);
        assertEquals(response.statusCode(), 405);
    }

    @Test(groups = { "api", "security" })
    public void deleteRequestIsNotSupported() {
        Response response = given()
                .baseUri(baseURL)
                .when()
                .delete(path);
        assertEquals(response.statusCode(), 405);
    }
}
