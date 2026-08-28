package com.parabank.db;

import org.testng.annotations.BeforeClass;

public class DatabaseTestBase {

    @BeforeClass(alwaysRun = true)
    public void startDatabase() {
        try {
            DatabaseTestContainer.start();
        } catch (IllegalStateException e) {
            throw new org.testng.SkipException("Docker unavailable — skipping database tests: " + e.getMessage());
        }
    }

    // no explicit @AfterSuite stop() call needed —
    // Testcontainers registers a JVM shutdown hook and tears the container down
    // automatically
}