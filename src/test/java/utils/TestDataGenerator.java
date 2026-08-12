package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.parabank.model.Customer;

public class TestDataGenerator {
    private static final int customerId = 12212;
    private static final int accountId = 13344;
    private static final String payee = """
            {
              "name": "Test Rental Properties",
              "address": {
                "street": "Selenium Drive",
                "city": "Test City",
                "state": "Storage State",
                "zipCode": "10101"
              },
              "phoneNumber": "111-110-1111",
              "accountNumber": 0
            }
                            """;

    public static int getCustomerId() {
        return customerId;
    }

    public static int getAccountId() {
        return accountId;
    }

    public static String getPayee() {
        return payee;
    }

    public static List<String> getAccountTypes() {
        return new ArrayList<>(List.of("CHECKING", "SAVINGS", "LOAN"));
    }

    public static Customer getLoginCustomer() {
        return new Customer("john", "demo");
    }

    public static String getTodaysDateFormatted() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        return today.format(formatter);
    }

    public static String getCurrentMonthName() {
        LocalDate today = LocalDate.now();
        return today.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }

    public static String getAnyDaysFromNowFormatted(int days) {
        LocalDate today = LocalDate.now().plusDays(days);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        return today.format(formatter);
    }

}
