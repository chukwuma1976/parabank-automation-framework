package com.parabank.pages;

import java.time.ZoneOffset;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.parabank.config.ConfigReader;
import com.parabank.utils.DateConverterApi;

public class TransactionDetailsPage {
    private WebDriver driver;

    public TransactionDetailsPage(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateToTransaction(int transactionID) {
        driver.get(ConfigReader.get("BASE_UI_URL") + "/transaction.htm?id=" + transactionID);
    }

    public WebElement retrieveTransactionField(String field) {
        By transactionField = By.xpath("//b[contains(text(), '" + field + "')]/ancestor::tr");
        return driver.findElement(transactionField);
    }

    public boolean confirmFieldIsVisible(WebElement field, String value) {
        return field.getText().contains(value);
    }

    public String convertDataToString(Object data) {
        if (data instanceof String s)
            return s;
        if (data instanceof Integer i)
            return Integer.toString(i);
        if (data instanceof Double d)
            return Double.toString(d);
        throw new IllegalArgumentException("Unsupported data type: " + data.getClass().getSimpleName());
    }

    public String convertTimestampToString(long epochMillis) {
        return DateConverterApi.toFormattedDate(epochMillis, ZoneOffset.UTC);
    }

    public boolean confirmTransactionFieldVisible(String fieldName, Object value) {
        String convertedValue = convertDataToString(value);
        return retrieveTransactionField(fieldName).getText().contains(convertedValue);
    }

    public boolean confirmTransactionTimestampFieldVisible(String fieldName, long value) {
        String convertedValue = convertTimestampToString(value);
        return retrieveTransactionField(fieldName).getText().contains(convertedValue);
    }

}