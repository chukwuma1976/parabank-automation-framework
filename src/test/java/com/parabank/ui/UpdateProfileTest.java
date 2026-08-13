package com.parabank.ui;

import org.testng.annotations.Test;
import com.parabank.base.BaseUiTest;
import com.parabank.component.LeftNavComponent;
import com.parabank.model.AuthenticatedUser;
import com.parabank.model.Customer;
import com.parabank.pages.UpdateProfilePage;

import utils.TestDataGenerator;

public class UpdateProfileTest extends BaseUiTest {
    @Test(groups = { "api", "regression" })
    public void registerUser() {

        AuthenticatedUser authUser = loginAsNewUser();
        Customer customer = TestDataGenerator.getCustomerData(authUser);

        LeftNavComponent navComponent = new LeftNavComponent(driver);
        UpdateProfilePage updateProfilePage = new UpdateProfilePage(driver);

        navComponent.clickUpdateContactInfo();
        updateProfilePage.confirmPresenceOfValuesInInputFields(customer);

        // now update customer address, city, state
        customer.setAddress("15000 Clark Avenue");
        customer.setCity("Metropolis");
        customer.setState("Delaware");
        customer.setZipCode("19901");

        updateProfilePage.updateProfile(customer);
        updateProfilePage.confirmPresenceOfValuesInInputFields(customer);
        updateProfilePage.submitUpdatedProfile();

    }

}
