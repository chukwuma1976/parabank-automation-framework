package com.parabank.security.api;

import com.parabank.base.BaseApiTest;
import com.parabank.config.ConfigReader;
import static io.restassured.RestAssured.given;

import org.testng.annotations.Test;

public class SqlAndXssInjection extends BaseApiTest {
    String sqlInjection = "999 OR 1 = 1";
    String XSSInjection = "<script>alert('XSS Injection Detected')</script>";

    @Test(groups = { "api", "security" })
    public void testXssInjectionInAPI() {
        String path = "login/" + XSSInjection + "/anything";
        given()
                .baseUri(ConfigReader.get("BASE_API_URL"))
                .when()
                .get(path)
                .then()
                .statusCode(403);
    }

    @Test(groups = { "api", "security" })
    public void testSQLInjectionInAPI() {
        String path = "login/" + sqlInjection + "/anything";
        given()
                .baseUri(ConfigReader.get("BASE_API_URL"))
                .when()
                .get(path)
                .then()
                .statusCode(400);
    }
}
