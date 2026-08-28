package com.parabank.pages;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AccountDetailsPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private By monthSelect;
    private By transactionTypeSelect;
    private By submitTransactionSelection;

    public AccountDetailsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.monthSelect = By.id("month");
        this.transactionTypeSelect = By.id("transactionType");
        this.submitTransactionSelection = By.cssSelector("input[value='Go']");
    }

    public boolean verifyAccountNumber(int accountNumber) {
        By acctNum = By.id("accountId");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(acctNum));
        return driver.findElement(acctNum).getText().contains(Integer.toString(accountNumber));
    }

    public boolean verifyAccountType(String accountType) {
        By acctType = By.id("accountType");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(acctType));
        return driver.findElement(acctType).getText().contains(accountType);
    }

    public boolean verifyBalance(double accountBalance) {
        By balance = By.id("balance");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(balance));
        return driver.findElement(balance).getText().contains(Double.toString(accountBalance));
    }

    public boolean verifyAvailableBalance(double availableBalance) {
        By availableBal = By.id("availableBalance");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(availableBal));
        return driver.findElement(availableBal).getText().contains(Double.toString(availableBalance));
    }

    public void selectMonth(String month) {
        handleSelect(monthSelect, month);
    }

    public void selectTransactionType(String transactionType) {
        List<String> transactionTypes = List.of("All", "Credit", "Debit");
        if (transactionTypes.contains(transactionType))
            handleSelect(transactionTypeSelect, transactionType);
        else
            throw new NoSuchElementException(transactionType + " is not a valid transaction type.");
    }

    public void submitTransactionSelection() {
        driver.findElement(submitTransactionSelection).submit();
    }

    private void handleSelect(By select, String option) {
        wait.until(ExpectedConditions.elementToBeClickable(select));
        Select selectMonth = new Select(driver.findElement(select));
        selectMonth.selectByValue(option);
    }
}
