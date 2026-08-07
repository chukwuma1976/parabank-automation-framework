package com.parabank.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.parabank.model.Customer;

public class UpdateProfilePage {
    private WebDriver driver;

    private By firstNameField = By.id("customer.firstName");
    private By lastNameField = By.id("customer.lastName");
    private By addressField = By.id("customer.address.street");
    private By cityField = By.id("customer.address.city");
    private By stateField = By.id("customer.address.state");
    private By zipCodeField = By.id("customer.address.zipCode");
    private By phoneNumberField = By.id("customer.phoneNumber");

    private WebDriverWait wait;

    public UpdateProfilePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void confirmPresenceOfValuesInInputFields(Customer customer) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField));

        assert (driver.findElement(firstNameField).getAttribute("value")).equals(customer.getFirstName());
        assert (driver.findElement(lastNameField).getAttribute("value")).equals(customer.getLastName());
        assert (driver.findElement(addressField).getAttribute("value")).equals(customer.getAddress());
        assert (driver.findElement(cityField).getAttribute("value")).equals(customer.getCity());
        assert (driver.findElement(stateField).getAttribute("value")).equals(customer.getState());
        assert (driver.findElement(zipCodeField).getAttribute("value")).equals(customer.getZipCode());
        assert (driver.findElement(phoneNumberField).getAttribute("value")).equals(customer.getPhoneNumber());
    }

}
