package com.parabank.ui;

import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;

import com.parabank.base.BaseUiTest;
import com.parabank.model.Account;
import com.parabank.model.AuthenticatedUser;
import com.parabank.model.Transaction;
import com.parabank.pages.AccountDetailsPage;
import com.parabank.pages.LandingPage;
import com.parabank.pages.TransactionDetailsPage;

import utils.AccountApi;

import java.util.List;

import org.testng.annotations.Test;
import com.parabank.config.ConfigReader;
import utils.BankActions;

public class TransactionsTest extends BaseUiTest {
    private LandingPage landingPage;
    private AccountDetailsPage accountDetailsPage;
    private TransactionDetailsPage transactionDetailsPage;

    List<Transaction> transactions;
    private SoftAssert softly;

    private double deposit = 100000;
    private double withdrawal = 5000;
    private double transferMoney = 50000;

    @BeforeMethod
    public void setup() {
        AuthenticatedUser authUser = loginAsNewUser(); // fresh user, browser already authenticated, first account
                                                       // already seeded with $10,000

        int customerId = AccountApi.getCustomerId(
                authUser.getCustomer().getUsername(),
                authUser.getCustomer().getPassword());

        Account fundedAccount = AccountApi.getFirstAccount(customerId); // the seeded account — used as the source for
                                                                        // new-account creation
        int accountId = fundedAccount.getId();

        // Perform transactions through the API
        BankActions.depositMoney(accountId, deposit);
        BankActions.withdrawMoney(accountId, withdrawal);
        Account newSavingsAccount = BankActions.createNewAccount(1, customerId, accountId);
        int savingsAccountId = newSavingsAccount.getId();
        BankActions.transferMoney(transferMoney, accountId, savingsAccountId);

        // Get all customer accounts
        transactions = BankActions.getTransactionList(accountId);
        System.out.println(transactions);

        landingPage = new LandingPage(getDriver());
        accountDetailsPage = new AccountDetailsPage(getDriver());
        transactionDetailsPage = new TransactionDetailsPage(getDriver());
        softly = new SoftAssert();
    }

    @Test(groups = { "ui", "regression" })
    public void checkTransactionDetails() {
        getDriver().get(ConfigReader.get("LANDING_PAGE_URL"));
        for (Transaction transaction : transactions) {
            transactionDetailsPage.navigateToTransaction(transaction.getId());

            // Confirm that the text fields from the API matches the ones in the UI
            softly.assertTrue(
                    transactionDetailsPage.confirmTransactionFieldVisible("Transaction ID", transaction.getId()));
            softly.assertTrue(
                    transactionDetailsPage.cconfirmTransactionTimestampFieldVisible("Date", transaction.getDate()));
            softly.assertTrue(
                    transactionDetailsPage.confirmTransactionFieldVisible("Description", transaction.getDescription()));
            softly.assertTrue(
                    transactionDetailsPage.confirmTransactionFieldVisible("Type", transaction.getType()));
            softly.assertTrue(
                    transactionDetailsPage.confirmTransactionFieldVisible("Amount", transaction.getAmount()));
        }
        softly.assertAll();
    }

}
