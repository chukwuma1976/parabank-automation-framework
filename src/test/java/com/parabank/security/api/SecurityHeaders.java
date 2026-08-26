package com.parabank.security.api;

import java.util.*;

import org.testng.annotations.Test;

import com.parabank.base.BaseApiTest;
import com.parabank.config.ConfigReader;
import com.parabank.model.Customer;

import io.restassured.http.Headers;
import io.restassured.response.Response;
import utils.TestDataGenerator;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SecurityHeaders extends BaseApiTest {
    List<String> securityHeaders = TestDataGenerator.getListOfSecurityHeaders();
    Boolean isCacheControlSecurity = false;
    Boolean isAtLeastOneSecurityHeaderPresent;
    List<String> actualSecurityHeaders = new ArrayList<>();

    @Test(groups = { "api", "security" })
    public void testSecurityHeaders() {
        Customer customer = TestDataGenerator.getLoginCustomer();
        String path = "login/" + customer.getUsername() + "/" + customer.getPassword();
        Response response = given()
                .baseUri(ConfigReader.get("BASE_API_URL"))
                .when()
                .get(path);

        checkForAndLogHeaders(response.getHeaders());
        assertTrue(isAtLeastOneSecurityHeaderPresent);
    }

    private void checkForAndLogHeaders(Headers headers) {
        headers.forEach(header -> {
            String key = header.toString().toLowerCase().split("=")[0];
            String value = header.toString().toLowerCase().split("=")[1];

            if (securityHeaders.contains(key)) {
                if (key.equals("cache-control") && (value.equals("no-store") || value.equals("no-cache"))) {
                    System.out.println("The security header '" + key + "' is present with the value of " + value);
                    actualSecurityHeaders.add(key);
                } else {
                    System.out.println("The security header '" + key + "' is present");
                    actualSecurityHeaders.add(key);
                }
            }
        });
        isAtLeastOneSecurityHeaderPresent = actualSecurityHeaders.size() > 0;
    }

}
