package com.parabank.pages;

import java.time.Duration;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.parabank.config.ConfigReader;
import com.parabank.utils.DateConverterApi;
import com.parabank.utils.PageUtility;

public class FindTransactionsPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By transactionIdInput = By.id("transactionId");
    private By submitByIdInput = By.id("findById");

    private By transactionDateInput = By.id("transactionDate");
    private By submitByDate = By.id("findByDate");

    private By fromDateInput = By.id("fromDate");
    private By toDateInput = By.id("toDate");
    private By submitByDateRange = By.id("findByDateRange");

    private By amountInput = By.id("amount");
    private By submitByAmount = By.id("findByAmount");

    private By transactionTable = By.id("transactionBody");

    private By errorContainer = By.id("errorContainer");

    public FindTransactionsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void goToFindTransactions() {
        driver.get(ConfigReader.get("BASE_UI_URL") + "/findtrans.htm");
    }

    public void fillById(By field, String value) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(field));
        driver.findElement(field).sendKeys(value);
    }

    public void submitById(By button) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(button));
        driver.findElement(button).click();
        PageUtility.waitForAjaxToComplete(wait); // don't move on until the search's AJAX call has actually resolved
    }

    public void findByTransactionId(int id) {
        fillById(transactionIdInput, Integer.toString(id));
        submitById(submitByIdInput);
    }

    public void findByTransactionDate(long millis) {
        fillById(transactionDateInput, DateConverterApi.toFormattedDate(millis, ZoneOffset.UTC));
        submitById(submitByDate);
    }

    public void findByDateRange(long datefrom, long dateto) {
        fillById(fromDateInput, DateConverterApi.toFormattedDate(datefrom, ZoneOffset.UTC));
        fillById(toDateInput, DateConverterApi.toFormattedDate(dateto, ZoneOffset.UTC));
        submitById(submitByDateRange);
    }

    public void findByAmount(double money) {
        fillById(amountInput, Double.toString(money));
        submitById(submitByAmount);
    }

    public int getNumberOfTransactionsInTable() {
        wait.until(ExpectedConditions.presenceOfElementLocated(transactionTable));
        WebElement table = driver.findElement(transactionTable);
        return table.findElements(By.tagName("tr")).size();
    }

    public boolean doesNotContainTransactionItems() {
        return getNumberOfTransactionsInTable() == 0;
    }

    public Map<String, By> getErrorsFromEmptyInputMap() {
        Map<String, By> errorMap = new HashMap<>();
        errorMap.put("transactionIdError", submitByIdInput);
        errorMap.put("transactionDateError", submitByDate);
        errorMap.put("dateRangeError", submitByDateRange);
        errorMap.put("amountError", submitByAmount);

        return errorMap;
    }

    public List<String> getDisplayedErrors() {
        return List.of("transactionDateError", "dateRangeError", "amountError");
    }

    public Map<By, By> getErrorsFromInvalidInputMap() {
        Map<By, By> invalidMap = new HashMap<>();
        // invalidMap.put(transactionIdInput, submitByIdInput);
        invalidMap.put(transactionDateInput, submitByDate);
        invalidMap.put(fromDateInput, submitByDateRange);
        invalidMap.put(amountInput, submitByAmount);

        return invalidMap;
    }

    public boolean confirmErrorMessageIsVisible(String messageId) {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id(messageId)));
        return driver.findElement(By.id(messageId)).isDisplayed();
    }

    public boolean confirmInternalErrorMessage() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(errorContainer));
        return driver.findElement(errorContainer).isDisplayed();
    }

}
