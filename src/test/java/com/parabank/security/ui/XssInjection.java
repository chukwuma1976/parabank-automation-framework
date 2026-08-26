package com.parabank.security.ui;

import org.testng.annotations.Test;

import com.parabank.base.BaseUiTest;
import com.parabank.config.ConfigReader;
import com.parabank.model.Customer;
import com.parabank.pages.LandingPage;
import com.parabank.pages.LoginPage;

public class XssInjection extends BaseUiTest {
    String XSSInjection = "<script>alert('XSS Injection Detected')</script>";

    @Test(groups = { "ui", "security" })
    public void testXssInjectionInUI() {
        LandingPage landingPage = new LandingPage(getDriver());

        getDriver().get(ConfigReader.get("BASE_UI_URL") + "/overview.htm");
        new LoginPage(getDriver()).loginCustomer(new Customer(XSSInjection, "anypassword"));
        landingPage.confirmPerformingSecurityVerification();
    }

}
