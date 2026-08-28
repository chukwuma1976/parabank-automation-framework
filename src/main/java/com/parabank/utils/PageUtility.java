package com.parabank.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PageUtility {

    public static void waitForAjaxToComplete(WebDriverWait wait) {
        wait.until(driver -> {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            Object activeRequests = js.executeScript(
                    "return (typeof jQuery !== 'undefined') ? jQuery.active : 0");
            return ((Long) activeRequests) == 0;
        });
    }

}
