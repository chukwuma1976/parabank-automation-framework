package api;

import com.intuit.karate.junit5.Karate;

public class LoansTestRunner {
    @Karate.Test
    Karate testLoans() {
        return Karate.run("loans.feature").relativeTo(getClass());
    }
}
