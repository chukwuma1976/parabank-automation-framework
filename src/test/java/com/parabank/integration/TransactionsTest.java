package com.parabank.integration;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.parabank.base.BaseApiTest;
import com.parabank.config.ConfigReader;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import utils.DataControl;
import utils.TestDataGenerator;

public class TransactionsTest extends BaseApiTest {
    private String baseURL = ConfigReader.get("BASE_API_URL");
    private int customerId = TestDataGenerator.getCustomerId();
    private int accountId = TestDataGenerator.getAccountId();

    private double initialBalance;

    @BeforeMethod
    public void setUp() {
        DataControl.resetData();
        initialBalance = getBalance(accountId);
    }

    @Test(groups = { "ui", "regression" })
    public void testBillPay() {
        int amountToPay = 1200;
        String requestPayload = TestDataGenerator.getPayee();

        Response billPayment = given()
                .baseUri(baseURL)
                .headers("Accept", "application/json")
                .queryParam("accountId", accountId)
                .queryParam("amount", amountToPay)
                .contentType(ContentType.JSON)
                .body(requestPayload)
                .when()
                .post("/billpay")
                .then()
                .statusCode(200)
                .extract().response();

        assert (billPayment.jsonPath().getInt("accountId") == accountId);
        assert (billPayment.jsonPath().getDouble("amount") == amountToPay);

        Double finalBalance = getBalance(accountId);
        int difference = (int) Math.abs(initialBalance - finalBalance);

        assert (difference == amountToPay);
    }

    @Test(groups = { "ui", "regression" })
    public void testDeposit() {
        int amountToDeposit = 100000;
        String expectedResponse = "Successfully deposited $" + amountToDeposit + " to account #" + accountId;

        Response depositStatement = given()
                .baseUri(baseURL)
                .queryParam("accountId", accountId)
                .queryParam("amount", amountToDeposit)
                .when()
                .post("/deposit")
                .then()
                .statusCode(200)
                .extract().response();

        assert (depositStatement.asString()).equals(expectedResponse);
        Double finalBalance = getBalance(accountId);
        int difference = (int) Math.abs(initialBalance - finalBalance);

        assert (difference == amountToDeposit);
    }

    @Test(groups = { "ui", "regression" })
    public void testWithdrawal() {
        int amountToWithdraw = 2000;
        String expectedResponse = "Successfully withdrew $" + amountToWithdraw + " from account #" + accountId;

        Response withdrawalStatement = given()
                .baseUri(baseURL)
                .queryParam("accountId", accountId)
                .queryParam("amount", amountToWithdraw)
                .when()
                .post("/withdraw")
                .then()
                .statusCode(200)
                .extract().response();

        assert (withdrawalStatement.asString()).equals(expectedResponse);
        Double finalBalance = getBalance(accountId);
        int difference = (int) Math.abs(initialBalance - finalBalance);

        assert (difference == amountToWithdraw);
    }

    @Test(groups = { "ui", "regression" })
    public void testBankTransfer() {
        Response savingsAccount = given()
                .baseUri(baseURL)
                .headers("Accept", "application/json")
                .queryParam("customerId", customerId)
                .queryParam("newAccountType", 1)
                .queryParam("fromAccountId", accountId)
                .when()
                .post("/createAccount")
                .then()
                .statusCode(200)
                .extract().response();

        assert (savingsAccount.jsonPath().getInt("customerId") == customerId);
        assert (savingsAccount.jsonPath().get("type")).equals("SAVINGS");

        double initialSavingsBalance = savingsAccount.jsonPath().getDouble("balance");
        int savingsAccountId = savingsAccount.jsonPath().getInt("id");

        int amountToTransfer = 2000;
        String expectedResponse = "Successfully transferred $" + amountToTransfer + " from account #" + accountId
                + " to account #" + savingsAccountId;

        Response transferStatement = given()
                .baseUri(baseURL)
                .queryParam("fromAccountId", accountId)
                .queryParam("toAccountId", savingsAccountId)
                .queryParam("amount", amountToTransfer)
                .when()
                .post("/transfer")
                .then()
                .statusCode(200)
                .extract().response();
        transferStatement.prettyPrint();
        assert (transferStatement.asString()).equals(expectedResponse);

        // double finalCheckingBalance = getBalance(accountId);
        // int checkingAcctDiff = (int) Math.abs(initialBalance - finalCheckingBalance);
        // assert (checkingAcctDiff == amountToTransfer);

        // double finalSavingsBalance = getBalance(savingsAccountId);
        // int savingsAccountDiff = (int) Math.abs(initialSavingsBalance -
        // finalSavingsBalance);
        // assert (savingsAccountDiff == amountToTransfer);

    }

    private double getBalance(int accountId) {
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

/*
 * Integration Test plans:
 * 
 * Test transfer
 * 
 * create account/or use existing account -> check initial balance -> create
 * another account -> transfer money between accounts -> check balances
 * 
 * Negative scenarios
 * 
 * Test above but overdraft
 * 
 */