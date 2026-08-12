package com.parabank.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LandingPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public LandingPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void confirmLandingPageURL() {
        assert (driver.getCurrentUrl()).contains("overview.htm");
    }

    public void confirmLandingPageTitle() {
        assert (driver.getTitle()).contains("ParaBank | Accounts Overview");
    }

    public void verifyAccountNumber(int accountNumber) {
        By acctNum = By.xpath("//a[contains(@href, " + accountNumber + ")]");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(acctNum));
        assert (driver.findElement(acctNum)).isDisplayed();
    }

    public void gotoAccountNumber(int accountNumber) {
        By acctNum = By.xpath("//a[contains(@href, " + accountNumber + ")]");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(acctNum));
        driver.findElement(acctNum).click();
    }
}
