package utils;

import com.parabank.db.DatabaseTestContainer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DatabaseTestContainer.getJdbcUrl(),
                DatabaseTestContainer.getUsername(),
                DatabaseTestContainer.getPassword());
    }
}