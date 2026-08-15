package com.parabank.db;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.Assert;
import utils.DatabaseConnector;
import utils.DatabaseSeeder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.math.BigDecimal;

public class AccountBalanceTest extends DatabaseTestBase {

    @BeforeMethod
    public void resetData() throws Exception {
        DatabaseSeeder.reseed();
    }

    @Test(groups = { "db", "regression" })
    public void checkingAccountHasExpectedBalance() throws Exception {
        try (Connection conn = DatabaseConnector.getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT balance FROM accounts WHERE customer_id = ? AND type = ?")) {

            stmt.setInt(1, 1);
            stmt.setString(2, "CHECKING");

            try (ResultSet rs = stmt.executeQuery()) {
                Assert.assertTrue(rs.next(), "Expected a checking account row for customer 1");
                BigDecimal balance = rs.getBigDecimal("balance");
                Assert.assertEquals(balance, new BigDecimal("15000.00"));
            }
        }
    }

    @Test(groups = { "db", "regression" })
    public void withdrawalReducesBalanceCorrectly() throws Exception {
        try (Connection conn = DatabaseConnector.getConnection()) {
            conn.setAutoCommit(false); // demonstrates transaction control — worth knowing for interviews too

            try (PreparedStatement withdraw = conn.prepareStatement(
                    "UPDATE accounts SET balance = balance - ? WHERE id = ?")) {
                withdraw.setBigDecimal(1, new BigDecimal("500.00"));
                withdraw.setInt(2, 1);
                withdraw.executeUpdate();
            }

            conn.commit();

            try (PreparedStatement check = conn.prepareStatement(
                    "SELECT balance FROM accounts WHERE id = ?")) {
                check.setInt(1, 1);
                try (ResultSet rs = check.executeQuery()) {
                    rs.next();
                    Assert.assertEquals(rs.getBigDecimal("balance"), new BigDecimal("14500.00"));
                }
            }
        }
    }
}