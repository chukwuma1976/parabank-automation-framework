package com.parabank.ui;

import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;

import com.parabank.base.BaseUiTest;
import com.parabank.model.Account;
import com.parabank.model.AuthenticatedUser;
import com.parabank.model.Transaction;
import com.parabank.pages.FindTransactionsPage;
import com.parabank.pages.TransactionDetailsPage;

import utils.AccountApi;

import java.util.List;

import org.testng.annotations.Test;
import com.parabank.config.ConfigReader;
import utils.BankActions;

public class TransactionsTest extends BaseUiTest {

    private TransactionDetailsPage transactionDetailsPage;
    private FindTransactionsPage findTransactionsPage;

    List<Transaction> transactions;
    private SoftAssert softly;

    private double deposit = 100000;
    private double withdrawal = 5000;
    private double transferMoney = 50000;

    int customerId;
    int accountId;

    @BeforeMethod
    public void setup() {
        AuthenticatedUser authUser = loginAsNewUser(); // fresh user, browser already authenticated, first account
                                                       // already seeded with $10,000

        customerId = AccountApi.getCustomerId(
                authUser.getCustomer().getUsername(),
                authUser.getCustomer().getPassword());

        Account fundedAccount = AccountApi.getFirstAccount(customerId); // the seeded account — used as the source for
                                                                        // new-account creation
        accountId = fundedAccount.getId();

        // Perform transactions through the API
        BankActions.depositMoney(accountId, deposit);
        BankActions.withdrawMoney(accountId, withdrawal);
        Account newSavingsAccount = BankActions.createNewAccount(1, customerId, accountId);
        int savingsAccountId = newSavingsAccount.getId();
        BankActions.transferMoney(transferMoney, accountId, savingsAccountId);

        // Get all customer accounts
        transactions = BankActions.getTransactionList(accountId);

        transactionDetailsPage = new TransactionDetailsPage(getDriver());
        findTransactionsPage = new FindTransactionsPage(getDriver());
        softly = new SoftAssert();
    }

    @Test(groups = { "ui", "regression" })
    public void checkTransactionDetails() {
        getDriver().get(ConfigReader.get("BASE_UI_URL") + "/overview.htm");
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

    @Test(groups = { "ui", "regression" })
    public void findTransactionsById() {
        transactions.forEach(transaction -> {
            findTransactionsPage.goToFindTransactions();
            findTransactionsPage.findByTransactionId(transaction.getId());
            softly.assertTrue(findTransactionsPage.getNumberOfTransactionsInTable() == 1);
        });
        softly.assertAll();
    }

    @Test(groups = { "ui", "regression" })
    public void findTransactionsByDate() {
        findTransactionsPage.goToFindTransactions();
        long dateMillis = transactions.get(0).getDate();
        findTransactionsPage.findByTransactionDate(dateMillis);
        softly.assertTrue(findTransactionsPage.getNumberOfTransactionsInTable() == transactions.size());
        softly.assertAll();
    }

    @Test(groups = { "ui", "regression" })
    public void findTransactionsByDateRange() {
        findTransactionsPage.goToFindTransactions();
        long dateMillis = transactions.get(0).getDate();
        findTransactionsPage.findByDateRange(dateMillis, dateMillis);
        softly.assertTrue(findTransactionsPage.getNumberOfTransactionsInTable() == transactions.size());
        softly.assertAll();
    }

    @Test(groups = { "ui", "regression" })
    public void findTransactionsByAmount() {
        transactions.forEach(transaction -> {
            findTransactionsPage.goToFindTransactions();
            findTransactionsPage.findByAmount(transaction.getAmount());
            softly.assertTrue(findTransactionsPage.getNumberOfTransactionsInTable() == 1);
        });
        softly.assertAll();
    }

    @Test(groups = { "ui", "regression" })
    public void confirmEmptySearchInputTriggerErrorMessages() {
        findTransactionsPage.goToFindTransactions();
        findTransactionsPage.getErrorsFromEmptyInputMap().forEach((name, button) -> {
            findTransactionsPage.submitById(button);
            softly.assertTrue(findTransactionsPage.confirmErrorMessageIsVisible(name));
        });
        softly.assertAll();
    }

    @Test(groups = { "ui", "regression" })
    public void confirmInvalidSearchInputTriggerErrorMessages() {
        findTransactionsPage.goToFindTransactions();
        findTransactionsPage.getErrorsFromInvalidInputMap().forEach((field, button) -> {
            findTransactionsPage.fillById(field, "----------");
            findTransactionsPage.submitById(button);
        });
        findTransactionsPage.getDisplayedErrors().forEach(error -> {
            softly.assertTrue(findTransactionsPage.confirmErrorMessageIsVisible(error));
        });
        softly.assertAll();
    }

    @Test(groups = { "ui", "regression" })
    public void confirmInvalidSearchIdInputTriggerFatalErrorMessage() {
        findTransactionsPage.goToFindTransactions();
        findTransactionsPage.findByTransactionId(0000000000);
        softly.assertTrue(findTransactionsPage.confirmInternalErrorMessage());
        softly.assertAll();
    }

}
