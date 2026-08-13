package utils;

import com.parabank.config.ConfigReader;
import com.parabank.model.Account;
import com.parabank.model.Customer;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import java.util.List;

public class AccountApi {

    private static final String BASE_URL = ConfigReader.get("BASE_API_URL");

    public static int getCustomerId(String username, String password) {
        Response response = given()
                .baseUri(BASE_URL)
                .header("Accept", "application/json")
                .when()
                .get("login/" + username + "/" + password);

        if (response.getStatusCode() != 200) {
            throw new RuntimeException("Login lookup failed. Status: " + response.getStatusCode() + ", Body: "
                    + response.getBody().asString());
        }

        return response.jsonPath().getInt("id");
    }

    public static List<Account> getAccounts(int customerId) {
        Response response = given()
                .baseUri(BASE_URL)
                .header("Accept", "application/json")
                .when()
                .get("/customers/" + customerId + "/accounts");

        return List.of(response.as(Account[].class));
    }

    public static Account getFirstAccount(int customerId) {
        return getAccounts(customerId).get(0);
    }

    public static Account seedFirstAccountWithMoney(int customerId, double money) {
        Account baseAccount = getFirstAccount(customerId);
        BankActions.depositMoney(baseAccount.getId(), money);
        return getFirstAccount(customerId);
    }

    public static Account seedFirstAccountWithMoney(Customer customer, double money) {
        int customerId = getCustomerId(customer.getUsername(), customer.getPassword());
        Account baseAccount = getFirstAccount(customerId);
        BankActions.depositMoney(baseAccount.getId(), money);
        return getFirstAccount(customerId);
    }

}