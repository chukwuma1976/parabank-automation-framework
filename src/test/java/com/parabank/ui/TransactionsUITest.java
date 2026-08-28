package com.parabank.ui;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;

import com.parabank.base.BaseUiTest;
import com.parabank.model.Account;
import com.parabank.model.AuthenticatedUser;
import com.parabank.model.Customer;
import com.parabank.model.Transaction;
import com.parabank.pages.FindTransactionsPage;
import com.parabank.pages.TransactionDetailsPage;

import utils.AccountApi;

import java.util.List;

import org.testng.annotations.Test;
import com.parabank.config.ConfigReader;
import utils.BankActions;
import utils.RegistrationApi;
import utils.TestDataGenerator;

public class TransactionsUITest extends BaseUiTest {

    private TransactionDetailsPage transactionDetailsPage;
    private FindTransactionsPage findTransactionsPage;

    List<Transaction> transactions;
    private SoftAssert softly;

    private double deposit = 100000;
    private double withdrawal = 5000;
    private double transferMoney = 50000;

    int customerId;
    int accountId;

    private AuthenticatedUser authUser;

    @BeforeClass
    public void setupClassData() {
        Customer customer = TestDataGenerator.generateCustomer();
        authUser = RegistrationApi.registerAndAuthenticate(customer); // pure API call — no driver needed

        int customerId = AccountApi.getCustomerId(customer.getUsername(), customer.getPassword());
        Account fundedAccount = AccountApi.getFirstAccount(customerId);
        accountId = fundedAccount.getId();

        // Perform transactions through the API
        BankActions.depositMoney(accountId, deposit);
        BankActions.withdrawMoney(accountId, withdrawal);
        Account newSavingsAccount = BankActions.createNewAccount(1, customerId, accountId);
        BankActions.transferMoney(transferMoney, accountId, newSavingsAccount.getId());

        // Get all customer accounts
        transactions = BankActions.getTransactionList(accountId);
    }

    @BeforeMethod
    public void setupDriver() {
        injectSessionCookie(authUser);
    }

    @BeforeMethod
    public void setup() {
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
                    transactionDetailsPage.confirmTransactionTimestampFieldVisible("Date", transaction.getDate()));
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
            System.out.println(transaction);
            System.out.println("Find transactions by Id: " + findTransactionsPage.getNumberOfTransactionsInTable());
            softly.assertTrue(findTransactionsPage.getNumberOfTransactionsInTable() == 1);
        });
        softly.assertAll();
    }

    @Test(groups = { "ui", "regression" })
    public void findTransactionsByDate() {
        findTransactionsPage.goToFindTransactions();
        long dateMillis = transactions.get(0).getDate();
        findTransactionsPage.findByTransactionDate(dateMillis);
        System.out.println("transaction size" + transactions.size());
        System.out.println("Find transactions by Date: " + findTransactionsPage.getNumberOfTransactionsInTable());
        softly.assertTrue(findTransactionsPage.getNumberOfTransactionsInTable() == transactions.size());
        softly.assertAll();
    }

    @Test(groups = { "ui", "regression" })
    public void findTransactionsByDateRange() {
        findTransactionsPage.goToFindTransactions();
        long dateMillis = transactions.get(0).getDate();
        findTransactionsPage.findByDateRange(dateMillis, dateMillis);
        System.out.println("transaction size" + transactions.size());
        System.out.println("Find transactions by Date range: " + findTransactionsPage.getNumberOfTransactionsInTable());
        softly.assertTrue(findTransactionsPage.getNumberOfTransactionsInTable() == transactions.size());
        softly.assertAll();
    }

    @Test(groups = { "ui", "regression" })
    public void findTransactionsByAmount() {
        transactions.forEach(transaction -> {
            findTransactionsPage.goToFindTransactions();
            findTransactionsPage.findByAmount(transaction.getAmount());
            System.out.println(transaction);
            System.out.println("Find transactions by amount: " + findTransactionsPage.getNumberOfTransactionsInTable());
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
    public void confirmInvalidSearchIdInputReturnsNoTransactions() {
        findTransactionsPage.goToFindTransactions();
        findTransactionsPage.findByTransactionId(-99999999);
        softly.assertTrue(findTransactionsPage.doesNotContainTransactionItems());
        softly.assertAll();
    }

}
