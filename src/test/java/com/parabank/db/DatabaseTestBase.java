package com.parabank.db;

import org.testng.annotations.BeforeSuite;

public class DatabaseTestBase {

    @BeforeSuite(alwaysRun = true)
    public void startDatabase() {
        DatabaseTestContainer.start();
    }

    // no explicit @AfterSuite stop() call needed —
    // Testcontainers registers a JVM shutdown hook and tears the container down
    // automatically
}