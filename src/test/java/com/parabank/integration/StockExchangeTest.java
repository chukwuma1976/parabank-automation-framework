package com.parabank.integration;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.parabank.config.ConfigReader;

import io.restassured.response.Response;
import utils.BankActions;
import utils.DataControl;
import utils.TestDataGenerator;
import static io.restassured.RestAssured.given;

public class StockExchangeTest {
    private String baseURL = ConfigReader.get("BASE_API_URL");
    private int customerId = TestDataGenerator.getCustomerId();
    private int accountId = TestDataGenerator.getAccountId();
    private String stockName = "stock";
    private int numberOfShares = 100;
    private int priceOfShares = 15;
    private int newPriceOfShares = 20;

    private double initialBalance;

    @BeforeMethod
    public void setUp() {
        DataControl.resetData();
        initialBalance = BankActions.getBalance(accountId);
    }

    @Test(groups = { "ui", "regression" })
    public void testStockExchange() {

        // Purchase 100 shares of stock at $15/share
        String stockPurchasePath = "customers/" + customerId + "/buyPosition";
        Response stockPurchase = given()
                .baseUri(baseURL)
                .headers("Accept", "application/json")
                .queryParam("accountId", accountId)
                .queryParam("name", stockName)
                .queryParam("symbol", stockName)
                .queryParam("shares", numberOfShares)
                .queryParam("pricePerShare", priceOfShares)
                .when()
                .post(stockPurchasePath)
                .then()
                .statusCode(200)
                .extract().response();

        int moneyPaidForStock = numberOfShares * priceOfShares;
        double afterStockPurchaseBalance = BankActions.getBalance(accountId);
        int changeInBalance = (int) (initialBalance - afterStockPurchaseBalance);

        assert (changeInBalance == moneyPaidForStock);

        int positionId = stockPurchase.jsonPath().get("[1].positionId");
        System.out.println(positionId);

        // Sell 100 shares of stock at $20/share
        String stockSalePath = "customers/" + customerId + "/sellPosition";
        given()
                .baseUri(baseURL)
                .headers("Accept", "application/json")
                .queryParam("accountId", accountId)
                .queryParam("positionId", positionId)
                .queryParam("shares", numberOfShares)
                .queryParam("pricePerShare", newPriceOfShares)
                .when()
                .post(stockSalePath)
                .then()
                .statusCode(200)
                .extract().response();

        int moneyEarnedFromStock = numberOfShares * newPriceOfShares;
        double afterStockSaleBalance = BankActions.getBalance(accountId);
        int nextChangeInBalance = (int) (afterStockSaleBalance - afterStockPurchaseBalance);

        assert (nextChangeInBalance == moneyEarnedFromStock);
    }

}
