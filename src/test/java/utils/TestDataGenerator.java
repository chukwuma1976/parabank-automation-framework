package utils;

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

}
