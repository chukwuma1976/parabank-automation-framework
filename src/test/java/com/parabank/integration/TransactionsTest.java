package com.parabank.integration;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.parabank.base.BaseApiTest;
import com.parabank.config.ConfigReader;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

import utils.DataControl;
import utils.TestDataGenerator;
import utils.BankActions;

public class TransactionsTest extends BaseApiTest {
        private String baseURL = ConfigReader.get("BASE_API_URL");
        private int customerId = TestDataGenerator.getCustomerId();
        private int accountId = TestDataGenerator.getAccountId();

        private double initialBalance;

        @BeforeMethod
        public void setUp() {
                DataControl.resetData();
                initialBalance = BankActions.getBalance(accountId);
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

                assertEquals(billPayment.jsonPath().getInt("accountId"), accountId);
                assertEquals(billPayment.jsonPath().getDouble("amount"), amountToPay);

                double finalBalance = BankActions.getBalance(accountId);
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

                assertEquals(depositStatement.asString(), expectedResponse);
                double finalBalance = BankActions.getBalance(accountId);
                int difference = (int) Math.abs(initialBalance - finalBalance);

                assertEquals(difference, amountToDeposit);
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

                assertEquals(withdrawalStatement.asString(), expectedResponse);
                double finalBalance = BankActions.getBalance(accountId);
                int difference = (int) Math.abs(initialBalance - finalBalance);

                assertEquals(difference, amountToWithdraw);
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

                assertEquals(savingsAccount.jsonPath().getInt("customerId"), customerId);
                assertEquals(savingsAccount.jsonPath().get("type"), "SAVINGS");

                int savingsAccountId = savingsAccount.jsonPath().getInt("id");
                /*
                 * Get initial balance after the transfer. $90 is automatically added in an
                 * operation after creating a new account.
                 * Therefore the initial balance will be $90 NOT $0 for a newly created account,
                 * This $90 is withdrawn from the origin account so the new balce for that
                 * account need to be retrieved.
                 */
                double initialSavingsBalance = BankActions.getBalance(savingsAccountId); // initial balance with $90
                                                                                         // minimum
                                                                                         // balance
                double newInitialCheckingBalance = BankActions.getBalance(accountId); // new intial account balance for
                                                                                      // origin
                                                                                      // account

                int amountToTransfer = 2000;
                String expectedResponse = "Successfully transferred $" + amountToTransfer + " from account #"
                                + accountId
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
   
                assertEquals(transferStatement.asString(), expectedResponse);

                double finalCheckingBalance = BankActions.getBalance(accountId);
                int checkingAcctDiff = (int) Math.abs(newInitialCheckingBalance - finalCheckingBalance);
                assertEquals(checkingAcctDiff, amountToTransfer);

                double finalSavingsBalance = BankActions.getBalance(savingsAccountId);
                int savingsAccountDiff = (int) Math.abs(initialSavingsBalance -
                                finalSavingsBalance);
                assertEquals(savingsAccountDiff, amountToTransfer);

        }

}
