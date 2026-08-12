package utils;

import com.parabank.config.ConfigReader;
import com.parabank.model.Transaction;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

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

    public static JsonPath createNewAccount(int accountListIndex) {
        String baseURL = ConfigReader.get("BASE_API_URL");
        Response response = given()
                .baseUri(baseURL)
                .queryParam("customerId", TestDataGenerator.getCustomerId())
                .queryParam("newAccountType", accountListIndex)
                .queryParam("fromAccountId", TestDataGenerator.getAccountId())
                .accept(ContentType.JSON)
                .when()
                .post("/createAccount")
                .then()
                .statusCode(200)
                .extract().response();

        return response.jsonPath();
    }

    public static void depositMoney(double money) {
        given()
                .baseUri(baseURL)
                .queryParam("accountId", TestDataGenerator.getAccountId())
                .queryParam("amount", money)
                .when()
                .post("/deposit")
                .then()
                .statusCode(200)
                .extract().response();
    }

    public static void withdrawMoney(double money) {
        given()
                .baseUri(baseURL)
                .queryParam("accountId", TestDataGenerator.getAccountId())
                .queryParam("amount", money)
                .when()
                .post("/withdraw")
                .then()
                .statusCode(200)
                .extract().response();
    }

    public static void transferMoney(double money, int fromAccountId, int toAccountId) {
        given()
                .baseUri(baseURL)
                .queryParam("fromAccountId", fromAccountId)
                .queryParam("toAccountId", toAccountId)
                .queryParam("amount", money)
                .when()
                .post("/transfer")
                .then()
                .statusCode(200)
                .extract().response();
    }

    public static List<Transaction> getTransactionList(int accountId) {
        Response response = given()
                .baseUri(baseURL)
                .accept(ContentType.JSON)
                .when()
                .get("/accounts/" + accountId + "/transactions")
                .then()
                .statusCode(200)
                .extract().response();

        List<Transaction> transactionList = response.as(new TypeRef<List<Transaction>>() {
        });
        ArrayList<Transaction> transactions = new ArrayList<>(transactionList);

        return transactions;
    }

}
