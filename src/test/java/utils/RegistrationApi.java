package utils;

import com.parabank.config.ConfigReader;
import com.parabank.model.AuthenticatedUser;
import com.parabank.model.Customer;
import io.restassured.filter.session.SessionFilter;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class RegistrationApi {

    private static final String BASE_URL = ConfigReader.get("BASE_UI_URL"); // note that this is not the api URL

    public static AuthenticatedUser registerAndAuthenticate(Customer customer) {
        SessionFilter sessionFilter = new SessionFilter();

        // Step 1: establish session (server binds registration state to this session)
        given()
                .baseUri(BASE_URL)
                .filter(sessionFilter)
                .get("/register.htm");

        // Step 2: submit registration on the same session
        Response response = given()
                .baseUri(BASE_URL)
                .filter(sessionFilter)
                .contentType("application/x-www-form-urlencoded")
                .header("Referer", BASE_URL + "/register.htm")
                .formParam("customer.firstName", customer.getFirstName())
                .formParam("customer.lastName", customer.getLastName())
                .formParam("customer.address.street", customer.getAddress())
                .formParam("customer.address.city", customer.getCity())
                .formParam("customer.address.state", customer.getState())
                .formParam("customer.address.zipCode", customer.getZipCode())
                .formParam("customer.phoneNumber", customer.getPhoneNumber())
                .formParam("customer.ssn", customer.getSsn())
                .formParam("customer.username", customer.getUsername())
                .formParam("customer.password", customer.getPassword())
                .formParam("repeatedPassword", customer.getPassword())
                .when()
                .redirects().follow(false)
                .post("/register.htm");

        String sessionId = sessionFilter.getSessionId();
        String body = response.getBody().asString();

        if (sessionId == null || !body.contains("Your account was created successfully")) {
            throw new RuntimeException(
                    "Registration failed. Status: " + response.getStatusCode() + ", Body: " + body);
        }

        AccountApi.seedFirstAccountWithMoney(customer, 10000);
        return new AuthenticatedUser(customer, sessionId);
    }
}