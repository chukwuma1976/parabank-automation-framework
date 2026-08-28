package com.parabank.ui;

import org.testng.annotations.Test;

import com.parabank.base.BaseUiTest;
import com.parabank.config.ConfigReader;
import com.parabank.model.Customer;
import com.parabank.pages.LandingPage;
import com.parabank.pages.LoginPage;

import utils.TestDataGenerator;

public class LoginTest extends BaseUiTest {

    @Test(groups = { "ui", "smoke", "regression" })
    public void login() {
        Customer customer = TestDataGenerator.getLoginCustomer();
        LandingPage landingPage = new LandingPage(getDriver());

        getDriver().get(ConfigReader.get("BASE_UI_URL") + "/overview.htm");
        new LoginPage(getDriver()).loginCustomer(customer);
        landingPage.confirmLandingPageURL();
        landingPage.confirmLandingPageTitle();
    }

}
