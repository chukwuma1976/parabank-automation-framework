package com.parabank.pages;

import org.openqa.selenium.WebDriver;

public class LandingPage {

    private WebDriver driver;

    public LandingPage(WebDriver driver) {
        this.driver = driver;
    }

    public void confirmLandingPageURL() {
        assert (driver.getCurrentUrl()).contains("overview.htm");
    }

    public void confirmLandingPageTitle() {
        assert (driver.getTitle()).contains("ParaBank | Accounts Overview");
    }
}
