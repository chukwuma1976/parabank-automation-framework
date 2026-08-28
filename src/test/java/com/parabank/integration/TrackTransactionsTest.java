package com.parabank.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.parabank.base.BaseApiTest;
import com.parabank.model.Account;
import com.parabank.model.Customer;
import com.parabank.model.Transaction;

import utils.AccountApi;
import utils.BankActions;
import utils.RegistrationApi;
import utils.TestDataGenerator;

public class TrackTransactionsTest extends BaseApiTest {
    int accountId;
    int customerId;

    double deposit = 100000;
    double withdrawal = 5000;
    double transferMoney = 50000;

    @BeforeMethod
    public void setUp() {
        Customer customer = RegistrationApi.registerAndAuthenticate(TestDataGenerator.generateCustomer()).getCustomer();
        customerId = AccountApi.getCustomerId(customer.getUsername(), customer.getPassword());
        accountId = AccountApi.getFirstAccount(customerId).getId();
    }

    @Test(groups = { "api", "regression" })
    public void testMultipleTransactions() {
        // Get initial balance
        double initialBalance = BankActions.getBalance(accountId);

        // Deposit some money -- transaction
        BankActions.depositMoney(accountId, deposit);

        // Get new balance and document that the balance has increased accordingly
        double balanceAfterDeposit = BankActions.getBalance(accountId);
        assertEquals(deposit, diff(balanceAfterDeposit, initialBalance));

        // Withdraw some money -- transaction
        BankActions.withdrawMoney(accountId, withdrawal);

        // Get new balance and document that the balance has decreased accordingly
        double balanceAfterWithdrawal = BankActions.getBalance(accountId);
        assertEquals(withdrawal, diff(balanceAfterDeposit, balanceAfterWithdrawal));

        // Create a savings account since now you have some real money
        Account newSavingsAccount = BankActions.createNewAccount(1, customerId, accountId);
        int savingsAccountId = newSavingsAccount.getId();

        /*
         * Document the new balance since creating an account requires a minimum balance
         * which is AUTOMATICALLY transferred into the new account and counts as another
         * transcation
         */
        double minimumReqAmtInSavings = BankActions.getBalance(savingsAccountId);
        double newAmountinCheckingAfterCreatingSavings = BankActions.getBalance(accountId);
        assertEquals(minimumReqAmtInSavings,
                diff(balanceAfterWithdrawal, newAmountinCheckingAfterCreatingSavings));

        // transfer some money into the savings account -- transaction
        BankActions.transferMoney(transferMoney, accountId, savingsAccountId);

        // Get the new balance for the original checking account and new savings account
        // and document correctness
        double checkingAfterTransfer = BankActions.getBalance(accountId);
        double savingsAfterTransfer = BankActions.getBalance(savingsAccountId);
        assertEquals(transferMoney, diff(newAmountinCheckingAfterCreatingSavings, checkingAfterTransfer));
        assertEquals(transferMoney, diff(savingsAfterTransfer, minimumReqAmtInSavings));

        // Assert that there are at least 4 new transactions
        List<Transaction> transactions = BankActions.getTransactionList(accountId);
        assertTrue(transactions.size() >= 4);

        // With the transaction list assert that transaction exists based on amounts
        List<Double> transactionAmounts = List.of(deposit, withdrawal, minimumReqAmtInSavings, transferMoney);
        transactionAmounts.forEach(amount -> {
            assertTrue(transactions.stream().anyMatch(transaction -> transaction.getAmount() == amount));
        });

        // assert that each transaction ID is unique
        List<Integer> transactionIDs = transactions.stream().map(transaction -> transaction.getId()).toList();
        Set<Integer> transactionIDSet = new HashSet<>(transactionIDs);
        assertEquals(transactionIDs.size(), transactionIDSet.size());

    }

    private double diff(double num1, double num2) {
        return Math.round(Math.abs(num1 - num2));
    }
}
