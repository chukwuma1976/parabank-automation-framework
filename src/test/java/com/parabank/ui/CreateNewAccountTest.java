package com.parabank.ui;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.parabank.base.BaseUiTest;
import com.parabank.config.ConfigReader;
import com.parabank.model.Customer;
import com.parabank.pages.AccountDetailsPage;
import com.parabank.pages.LandingPage;
import com.parabank.pages.LoginPage;

import io.restassured.path.json.JsonPath;
import utils.BankActions;
import utils.DataControl;
import utils.TestDataGenerator;

public class CreateNewAccountTest extends BaseUiTest {
    // String baseURL = ConfigReader.get("BASE_API_URL");
    int customerId = TestDataGenerator.getCustomerId();
    int accountId = TestDataGenerator.getAccountId();
    List<JsonPath> createdAccounts = new ArrayList<>();
    SoftAssert softly = new SoftAssert();

    LoginPage loginPage;
    LandingPage landingPage;
    AccountDetailsPage accountDetailsPage;

    @BeforeSuite
    public void reset() {
        DataControl.resetData();
        createdAccounts = createAPIAccounts();
    }

    @BeforeMethod
    public void addAccounts() {

        Customer customer = TestDataGenerator.getLoginCustomer();
        loginPage = new LoginPage(driver);
        landingPage = new LandingPage(driver);
        accountDetailsPage = new AccountDetailsPage(driver);

        driver.get(ConfigReader.get("LANDING_PAGE_URL"));
        loginPage.loginCustomer(customer);
    }

    @DataProvider(name = "newAccounts")
    public Object[][] newAccounts() {
        JsonPath checking = createdAccounts.get(0);
        JsonPath savings = createdAccounts.get(1);
        JsonPath loan = createdAccounts.get(2);
        return new Object[][] {
                { checking.get("type"), checking.get("id") },
                { savings.get("type"), savings.get("id") },
                { loan.get("type"), loan.get("id") }
        };
    }

    @Test(dataProvider = "newAccounts", groups = { "ui", "regression" })
    public void createAccounts(String accountType, int accountId) {
        System.out.println(accountType + " : " + accountId);
        landingPage.verifyAccountNumber(accountId);
        landingPage.gotoAccountNumber(accountId);

        double balance = BankActions.getBalance(accountId);
        softly.assertTrue(accountDetailsPage.verifyAccountNumber(accountId));
        softly.assertTrue(accountDetailsPage.verifyAccountType(accountType));
        softly.assertTrue(accountDetailsPage.verifyBalance(balance));
        softly.assertTrue(accountDetailsPage.verifyAvailableBalance(balance));
        softly.assertAll();
    }

    private List<JsonPath> createAPIAccounts() {
        List<JsonPath> accounts = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            accounts.add(BankActions.createNewAccount(i));
        }
        return accounts;
    }

}
