package com.parabank.ui;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.parabank.base.BaseUiTest;
import com.parabank.config.ConfigReader;
import com.parabank.model.Customer;
import com.parabank.pages.LoginPage;

public class InvalidLoginTest extends BaseUiTest {
    String validUsername = "john";
    String validPassword = "demo";

    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentials() {
        return new Object[][] {
                { validUsername, "wrongPassword" }, // wrong password, valid username
                { "unknownUser", validPassword }, // unknown username
                { "unknownUser", "wrongPassword" }, // unknown username and wrong password
                { "", validPassword }, // blank username
                { validUsername, "" }, // blank password
                { "", "" }, // both blank
        };
    }

    @Test(dataProvider = "invalidCredentials", groups = { "ui", "regression" })
    public void loginWithInvalidCredentials(String username, String password) {
        LoginPage loginPage = new LoginPage(driver);
        Customer customer = new Customer(username, password);

        driver.get(ConfigReader.get("LANDING_PAGE_URL"));

        loginPage.loginCustomer(customer);
        loginPage.verifyErrorMessageDisplayed();
    }
}