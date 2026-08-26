package com.parabank.security.ui;

import org.testng.annotations.Test;

import com.parabank.base.BaseUiTest;
import com.parabank.config.ConfigReader;
import com.parabank.model.Customer;
import com.parabank.pages.LandingPage;
import com.parabank.pages.LoginPage;

public class SqlInjection extends BaseUiTest {
    String sqlInjection = "999 OR 1 = 1";

    @Test(groups = { "ui", "security" })
    public void testSqlInjectionInUI() {
        LandingPage landingPage = new LandingPage(getDriver());

        getDriver().get(ConfigReader.get("BASE_UI_URL") + "/overview.htm");
        new LoginPage(getDriver()).loginCustomer(new Customer(sqlInjection, "anypassword"));
        landingPage.confirmYouHaveBeenBlocked();
    }
}
