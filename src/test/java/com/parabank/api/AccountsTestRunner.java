package com.parabank.api;

import com.intuit.karate.junit5.Karate;

public class AccountsTestRunner {
    @Karate.Test
    Karate testAccounts() {
        return Karate.run("accounts.feature").relativeTo(getClass());
    }
}
