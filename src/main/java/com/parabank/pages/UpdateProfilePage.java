package com.parabank.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
    private By updateProfileButton = By.cssSelector("input[value='Update Profile']");
    private By updatedProfileMessage = By.id("updateProfileResult");

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

    private void clearFields() {
        List<By> fields = List.of(firstNameField, lastNameField, addressField, cityField, stateField, zipCodeField,
                phoneNumberField);
        for (By field : fields) {
            driver.findElement(field).clear();
        }
    }

    public void updateProfile(Customer customer) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField));
        clearFields();

        driver.findElement(firstNameField).sendKeys(customer.getFirstName());
        driver.findElement(lastNameField).sendKeys(customer.getLastName());
        driver.findElement(addressField).sendKeys(customer.getAddress());
        driver.findElement(cityField).sendKeys(customer.getCity());
        driver.findElement(stateField).sendKeys(customer.getState());
        driver.findElement(zipCodeField).sendKeys(customer.getZipCode());
        driver.findElement(phoneNumberField).sendKeys(customer.getPhoneNumber());
    }

    public void submitUpdatedProfile() {
        driver.findElement(updateProfileButton).submit();
    }

    public void confirmSuccessMessage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(updatedProfileMessage));
        assert (driver.findElement(updatedProfileMessage).isDisplayed());
    }

}
