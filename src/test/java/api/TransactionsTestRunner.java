package api;

import com.intuit.karate.junit5.Karate;

public class TransactionsTestRunner {
    @Karate.Test
    Karate testTransactions() {
        return Karate.run("transactions.feature").relativeTo(getClass());
    }
}
