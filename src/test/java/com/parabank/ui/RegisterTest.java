package com.parabank.ui;

import org.testng.annotations.Test;

import com.parabank.base.BaseUiTest;
import com.parabank.component.LeftNavComponent;
import com.parabank.config.ConfigReader;
import com.parabank.model.Customer;
import com.parabank.pages.RegistrationPage;
import com.parabank.pages.UpdateProfilePage;

public class RegisterTest extends BaseUiTest {

    @Test(groups = { "ui", "regression" })
    public void registerUser() {
        RegistrationPage registrationPage = new RegistrationPage(driver);
        LeftNavComponent leftNavComponent = new LeftNavComponent(driver);
        UpdateProfilePage updateProfilePage = new UpdateProfilePage(driver);
        String username = "UN" + System.currentTimeMillis();
        String password = "PW" + System.currentTimeMillis();

        Customer customer = new Customer(
                "Calvin",
                "Ellis",
                "1600 Pennsylvania Avenue",
                "Washington",
                "DC",
                "12345",
                "1234-567-8910",
                "111-111-1111",
                username,
                password);

        System.out.println("LANDING_PAGE_URL: " + ConfigReader.get("LANDING_PAGE_URL"));
        driver.get(ConfigReader.get("LANDING_PAGE_URL"));
        registrationPage.clickRegistrationLink();
        registrationPage.fillRegistrationForm(customer);
        registrationPage.clickRegisterButton();
        registrationPage.dismissModalIfPresent();
        leftNavComponent.clickUpdateContactInfo();
        updateProfilePage.confirmPresenceOfValuesInInputFields(customer);
    }
}