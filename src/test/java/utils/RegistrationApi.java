package utils;

import io.restassured.filter.session.SessionFilter;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import com.parabank.model.Customer;

public class RegistrationApi {

    private static final String BASE_URL = "https://parabank.parasoft.com/parabank";

    public static Response registerUser(Customer customer, SessionFilter sessionFilter) {
        // Step 1: establish session
        given()
                .baseUri(BASE_URL)
                .filter(sessionFilter)
                .get("/register.htm");

        // Step 2: submit registration on the same session
        return given()
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
    }
}
