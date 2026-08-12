package com.parabank.ui;

import org.testng.annotations.Test;

import com.parabank.base.BaseUiTest;
import com.parabank.component.LeftNavComponent;
import com.parabank.config.ConfigReader;
import com.parabank.model.Customer;
import com.parabank.pages.LandingPage;
import com.parabank.pages.LoginPage;

import utils.TestDataGenerator;

public class LogoutTest extends BaseUiTest {

    @Test(groups = { "ui", "smoke" })
    public void login() {
        Customer customer = TestDataGenerator.getLoginCustomer();
        LandingPage landingPage = new LandingPage(driver);
        LoginPage loginPage = new LoginPage(driver);

        driver.get(ConfigReader.get("LANDING_PAGE_URL"));
        loginPage.loginCustomer(customer);
        landingPage.confirmLandingPageURL();
        landingPage.confirmLandingPageTitle();

        new LeftNavComponent(driver).clickLogOut();

        loginPage.confirmLoginPageURL();

    }

}