package com.parabank.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.parabank.model.Customer;

public class LoginPage {
    private WebDriver driver;

    private By loginPanel = By.id("loginPanel");
    private By usernameInput = By.name("username");
    private By passwordInput = By.name("password");
    private By loginButton = By.cssSelector("input[value=\"Log In\"]");

    private WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void loginCustomer(Customer customer) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginPanel));
        WebElement loginForm = driver.findElement(loginPanel);
        loginForm.findElement(usernameInput).sendKeys(customer.getUsername());
        loginForm.findElement(passwordInput).sendKeys(customer.getPassword());
        loginForm.findElement(loginButton).submit();
    }

    public void verifyErrorMessageDisplayed() {
        By errorMessageLocator = By.cssSelector("#rightPanel .error");
        wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessageLocator));
        assert (driver.findElement(errorMessageLocator)).isDisplayed();
    }

    public void confirmLoginPageURL() {
        assert (driver.getCurrentUrl()).contains("index.htm");
    }
}
