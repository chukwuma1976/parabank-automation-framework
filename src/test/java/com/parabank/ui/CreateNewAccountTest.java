package com.parabank.ui;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.parabank.base.BaseUiTest;
import com.parabank.config.ConfigReader;
import com.parabank.model.Account;
import com.parabank.model.AuthenticatedUser;
import com.parabank.pages.AccountDetailsPage;
import com.parabank.pages.LandingPage;

import utils.AccountApi;
import utils.BankActions;
import utils.TestDataGenerator;

public class CreateNewAccountTest extends BaseUiTest {

    private LandingPage landingPage;
    private AccountDetailsPage accountDetailsPage;
    private List<Account> createdAccounts;
    private SoftAssert softly;

    @BeforeMethod
    public void setup() {
        AuthenticatedUser authUser = loginAsNewUser(); // fresh user, browser already authenticated, first account
                                                       // already seeded with $10,000

        int customerId = AccountApi.getCustomerId(
                authUser.getCustomer().getUsername(),
                authUser.getCustomer().getPassword());

        Account fundedAccount = AccountApi.getFirstAccount(customerId); // the seeded account — used as the source for
                                                                        // new-account creation

        createdAccounts = createAccountsForCustomer(customerId, fundedAccount.getId());

        landingPage = new LandingPage(getDriver());
        accountDetailsPage = new AccountDetailsPage(getDriver());
        softly = new SoftAssert();
    }

    @Test(groups = { "ui", "regression" })
    public void createAccounts() {
        for (Account account : createdAccounts) {
            getDriver().get(ConfigReader.get("BASE_UI_URL") + "/overview.htm");

            String accountType = account.getType();
            int accountId = account.getId();
            landingPage.verifyAccountNumber(accountId);
            landingPage.gotoAccountNumber(accountId);

            double balance = BankActions.getBalance(accountId);
            softly.assertTrue(accountDetailsPage.verifyAccountNumber(accountId));
            softly.assertTrue(accountDetailsPage.verifyAccountType(accountType));
            softly.assertTrue(accountDetailsPage.verifyBalance(balance));
            softly.assertTrue(accountDetailsPage.verifyAvailableBalance(balance));
        }
        softly.assertAll();
    }

    private List<Account> createAccountsForCustomer(int customerId, int fromAccountId) {
        List<Account> accounts = new ArrayList<>();
        for (int i = 0; i < TestDataGenerator.getAccountTypes().size(); i++) {
            accounts.add(BankActions.createNewAccount(i, customerId, fromAccountId));
        }
        return accounts;
    }
}