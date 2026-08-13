package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.parabank.model.Account;
import com.parabank.model.AuthenticatedUser;
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

    public static Customer generateCustomer() {
        String username = "UN" + System.currentTimeMillis();
        String password = "PW" + System.currentTimeMillis();

        Customer customer = new Customer(
                "Calvin",
                "Ellis",
                "1600 Pensylvania Ave",
                "Washington",
                "DC",
                "20010",
                "1-800-123-4567",
                "123-45-6789",
                username,
                password);

        return customer;
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

    public static Customer getCustomerData(AuthenticatedUser authUser) {
        return authUser.getCustomer();
    }

    public static Account getCustomerBaseAccount(AuthenticatedUser authUser) {
        String username = authUser.getCustomer().getUsername();
        String password = authUser.getCustomer().getPassword();

        int customerId = AccountApi.getCustomerId(username, password);
        return AccountApi.getFirstAccount(customerId);
    }

}
