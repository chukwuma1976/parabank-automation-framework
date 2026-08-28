package com.parabank.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.parabank.model.Customer;

public class RegistrationPage {
    private WebDriver driver;
    private By registrationLink;
    private By firstNameField = By.id("customer.firstName");
    private By lastNameField = By.id("customer.lastName");
    private By addressField = By.id("customer.address.street");
    private By cityField = By.id("customer.address.city");
    private By stateField = By.id("customer.address.state");
    private By zipCodeField = By.id("customer.address.zipCode");
    private By phoneNumberField = By.id("customer.phoneNumber");
    private By ssnField = By.id("customer.ssn");
    private By usernameField = By.id("customer.username");
    private By passwordField = By.id("customer.password");
    private By confirmPasswordField = By.id("repeatedPassword");
    private By registerButton = By.cssSelector("input[value='Register']");

    private WebDriverWait wait;

    public RegistrationPage(WebDriver driver) {
        this.driver = driver;
        this.registrationLink = By.linkText("Register");
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void clickRegistrationLink() {
        wait.until(ExpectedConditions.elementToBeClickable(registrationLink));
        driver.findElement(registrationLink).click();
    }

    public void fillRegistrationForm(Customer customer) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField));
        driver.findElement(firstNameField).sendKeys(customer.getFirstName());
        driver.findElement(lastNameField).sendKeys(customer.getLastName());
        driver.findElement(addressField).sendKeys(customer.getAddress());
        driver.findElement(cityField).sendKeys(customer.getCity());
        driver.findElement(stateField).sendKeys(customer.getState());
        driver.findElement(zipCodeField).sendKeys(customer.getZipCode());
        driver.findElement(phoneNumberField).sendKeys(customer.getPhoneNumber());
        driver.findElement(ssnField).sendKeys(customer.getSsn());
        driver.findElement(usernameField).sendKeys(customer.getUsername());
        driver.findElement(passwordField).sendKeys(customer.getPassword());
        driver.findElement(confirmPasswordField).sendKeys(customer.getPassword());
    }

    public void clickRegisterButton() {
        wait.until(ExpectedConditions.elementToBeClickable(registerButton));
        driver.findElement(registerButton).click();
    }

    public void dismissModalIfPresent() {
        try {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            // Modal not present, do nothing
        }
    }
}
