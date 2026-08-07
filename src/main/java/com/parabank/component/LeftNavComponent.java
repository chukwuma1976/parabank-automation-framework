package com.parabank.component;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LeftNavComponent {
    private WebDriver driver;
    private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

    private By leftNavPanel = By.id("leftPanel");
    private List<String> links = List.of("Open New Account", "Accounts Overview", "Transfer Funds", "Bill Pay",
            "Find Transactions",
            "Update Contact Info", "Request Loan", "Log Out");

    public LeftNavComponent(WebDriver driver) {
        this.driver = driver;
    }

    public void clickOpenNewAccount() {
        clickLink("Open New Account");
    }

    public void clickAccountsOverview() {
        clickLink("Accounts Overview");
    }

    public void clickTransferFunds() {
        clickLink("Transfer Funds");
    }

    public void clickBillPay() {
        clickLink("Bill Pay");
    }

    public void clickFindTransactions() {
        clickLink("Find Transactions");
    }

    public void clickUpdateContactInfo() {
        clickLink("Update Contact Info");
    }

    public void clickRequestLoan() {
        clickLink("Request Loan");
    }

    public void clickLogOut() {
        clickLink("Log Out");
    }

    public List<By> getAllLinks() {
        return links.stream().map(By::linkText).toList();
    }

    private void clickLink(String linkText) {
        WebElement link = driver.findElement(leftNavPanel).findElement(By.linkText(linkText));
        wait.until(ExpectedConditions.elementToBeClickable(link));
        link.click();
    }
}
