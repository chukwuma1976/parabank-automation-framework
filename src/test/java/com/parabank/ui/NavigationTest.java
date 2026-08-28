package com.parabank.ui;

import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.parabank.base.BaseUiTest;
import com.parabank.component.LeftNavComponent;
import com.parabank.config.ConfigReader;

public class NavigationTest extends BaseUiTest {

    LeftNavComponent navComponent;

    @BeforeMethod
    public void setUp() {
        navComponent = new LeftNavComponent(getDriver());

        loginAsNewUser();
        getDriver().get(ConfigReader.get("BASE_UI_URL") + "/overview.htm");
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
