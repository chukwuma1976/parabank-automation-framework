package com.parabank.base;

import java.io.ByteArrayInputStream;
import java.time.Duration;

import io.qameta.allure.Allure;
import utils.RegistrationApi;
import utils.TestDataGenerator;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.parabank.config.ConfigReader;
import com.parabank.model.AuthenticatedUser;
import com.parabank.model.Customer;

public class BaseUiTest {

    protected WebDriver driver;
    protected static final Logger log = LoggerFactory.getLogger(BaseUiTest.class);

    private long startTime;

    @BeforeMethod(alwaysRun = true)
    public void setUp(ITestResult testInfo) {

        boolean isCI = Boolean.parseBoolean(System.getenv("CI"));

        ChromeOptions options = new ChromeOptions();

        if (isCI) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
        } else {
            options.addArguments("--start-maximized");
        }

        driver = new ChromeDriver(options);

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

        // Logging
        startTime = System.currentTimeMillis();
        MDC.put("testName", testInfo.getMethod().getMethodName());
        log.info("========== START TEST ==========");

        // Allure metadata
        Allure.epic("UI Tests");
        Allure.feature(testInfo.getTestClass().getRealClass().getSimpleName());
        Allure.story(testInfo.getMethod().getMethodName());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult testInfo) {

        // Attach screenshot if test failed
        if (ITestResult.FAILURE == testInfo.getStatus() && driver != null) {
            try {
                byte[] screenshot = ((TakesScreenshot) driver)
                        .getScreenshotAs(OutputType.BYTES);

                Allure.addAttachment(
                        "Failure Screenshot",
                        "image/png",
                        new ByteArrayInputStream(screenshot),
                        ".png");
            } catch (Exception e) {
                log.error("Unable to capture screenshot", e);
            }
        }

        // Attach current URL
        if (driver != null) {
            Allure.addAttachment("Current URL", driver.getCurrentUrl());
        }

        // Logging
        long duration = System.currentTimeMillis() - startTime;
        log.info("========== END TEST ({} ms) ==========", duration);
        MDC.clear();

        // Attach duration
        Allure.addAttachment("Execution Time", duration + " ms");

        // Quit browser
        if (driver != null) {
            driver.quit();
        }
    }

    protected AuthenticatedUser loginAsNewUser() {
        Customer customer = TestDataGenerator.generateCustomer(); // unique username/password per call

        AuthenticatedUser authUser = RegistrationApi.registerAndAuthenticate(customer);

        String baseUiUrl = ConfigReader.get("BASE_UI_URL");
        java.net.URI uri = java.net.URI.create(baseUiUrl);

        driver.get(baseUiUrl + "/index.htm");
        driver.manage().deleteAllCookies();

        Cookie sessionCookie = new Cookie.Builder("JSESSIONID", authUser.getSessionId())
                .domain(uri.getHost())
                .path("/parabank")
                .isSecure(uri.getScheme().equals("https"))
                .build();

        driver.manage().addCookie(sessionCookie);
        driver.navigate().refresh();
        driver.get(baseUiUrl + "/overview.htm");

        if (driver.getPageSource().contains("Customer Login")) {
            throw new RuntimeException(
                    "Session cookie injection failed — still on login page for user: " + customer.getUsername());
        }

        return authUser;
    }
}