package utils;

import static io.restassured.RestAssured.given;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.parabank.config.ConfigReader;

public class DataControl {
    private static String baseURL = ConfigReader.get("BASE_API_URL");

    public static void resetData() {
        given().baseUri(baseURL)
                .when().post("/cleanDB")
                .then().statusCode(204);
    }

    public static void initializeData() {
        given().baseUri(baseURL)
                .when().post("/initializeDB")
                .then().statusCode(204);
    }

    public static void waitForAjaxToComplete(WebDriverWait wait) {

        wait.until(driver -> {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            Object activeRequests = js.executeScript(
                    "return (typeof jQuery !== 'undefined') ? jQuery.active : 0");
            return ((Long) activeRequests) == 0;
        });
    }

}
