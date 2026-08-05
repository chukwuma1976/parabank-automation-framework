package test.base;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.Filter;

import org.slf4j.MDC;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseApiTest {

    protected static final Logger log = LoggerFactory.getLogger(BaseApiTest.class);
    private long startTime;

    @BeforeClass
    public static void setup() {
        // Attach Allure filter globally for reporting
        Filter allureFilter = new AllureRestAssured();
        RestAssured.filters(allureFilter);
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeEach(ITestResult testInfo) {
        startTime = System.currentTimeMillis();
        MDC.put("testName", testInfo.getMethod().getMethodName());
        log.info("========== START TEST ==========");
    }

    @AfterMethod(alwaysRun = true)
    public void afterEach(ITestResult testInfo) {
        long duration = System.currentTimeMillis() - startTime;
        log.info("========== END TEST ({} ms) ==========", duration);
        MDC.clear();
    }
}