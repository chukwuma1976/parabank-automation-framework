package com.parabank.db;

import org.testcontainers.containers.PostgreSQLContainer;

public class DatabaseTestContainer {

    @SuppressWarnings({ "resource", "deprecation" })
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("parabank_test")
            .withUsername("postgres")
            .withPassword("postgres")
            .withInitScript("db/schema.sql"); // runs automatically on container startup

    public static void start() {
        if (!postgres.isRunning()) {
            postgres.start();
        }
    }

    public static String getJdbcUrl() {
        return postgres.getJdbcUrl();
    }

    public static String getUsername() {
        return postgres.getUsername();
    }

    public static String getPassword() {
        return postgres.getPassword();
    }
}