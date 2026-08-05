package api;

import com.intuit.karate.junit5.Karate;

public class CustomersTestRunner {
    @Karate.Test
    Karate testCustomers() {
        return Karate.run("customers.feature").relativeTo(getClass());
    }
}
