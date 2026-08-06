package api;

import com.intuit.karate.junit5.Karate;

public class BankOperationsTestRunner {
    @Karate.Test
    Karate testBankOperations() {
        return Karate.run("bank-operations.feature").relativeTo(getClass());
    }
}
