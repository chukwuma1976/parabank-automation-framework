package com.parabank.security.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.parabank.base.BaseApiTest;
import com.parabank.config.ConfigReader;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class PathTraversal extends BaseApiTest {

    @DataProvider(name = "pathsToTraverse")
    public Object[][] pathsToTraverse() {
        return new Object[][] {
                { "../../../../etc/passwd" },
                { "..\\..\\..\\..\\windows\\win.ini" },
                { "..%2f..%2f..%2f..%2fetc%2fpasswd" }
        };
    }

    @Test(dataProvider = "pathsToTraverse", groups = { "api", "security" })
    public void testPathToTraversal(String path) {
        Response response = given()
                .baseUri(ConfigReader.get("BASE_API_URL"))
                .when()
                .queryParam(path)
                .get();

        // expect 403 unauthorized status code
        assertEquals(response.statusCode(), 403);
    }
}