package com.parabank.ui;

import org.testng.annotations.Test;

import com.parabank.base.BaseUiTest;
import com.parabank.config.ConfigReader;
import com.parabank.model.Customer;
import com.parabank.pages.LandingPage;
import com.parabank.pages.LoginPage;

public class LoginTest extends BaseUiTest {

    @Test(groups = { "ui", "smoke" })
    public void login() {
        Customer customer = new Customer("john", "demo");
        LandingPage landingPage = new LandingPage(driver);

        driver.get(ConfigReader.get("LANDING_PAGE_URL"));
        new LoginPage(driver).loginCustomer(customer);
        landingPage.confirmLandingPageURL();
        landingPage.confirmLandingPageTitle();
    }

}
