package com.parabank.ui;

import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.parabank.base.BaseUiTest;
import com.parabank.component.LeftNavComponent;
import com.parabank.config.ConfigReader;
import com.parabank.model.Customer;
import com.parabank.pages.LoginPage;

import utils.TestDataGenerator;

public class NavigationTest extends BaseUiTest {

    LeftNavComponent navComponent;

    @BeforeMethod
    public void setUp() {
        Customer customer = TestDataGenerator.getLoginCustomer();
        navComponent = new LeftNavComponent(driver);
        driver.get(ConfigReader.get("LANDING_PAGE_URL"));
        new LoginPage(driver).loginCustomer(customer);
    }

    @Test(groups = { "ui", "regression" })
    public void testNavigation() {
        SoftAssert softly = new SoftAssert();
        for (WebElement link : navComponent.getAllLinks()) {
            softly.assertTrue(link.isDisplayed());
        }
        softly.assertAll();
    }
}
