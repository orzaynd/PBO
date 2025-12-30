package dev.oryzaa.projekpbo.web.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection manager.
 */
public final class DBConnection {

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("PostgreSQL Driver not found: " + e.getMessage());
        }
    }

    private DBConnection() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    /**
     * Get a new database connection.
     *
     * @return a database connection
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(AppConfig.DB_URL, AppConfig.DB_USER, AppConfig.DB_PASSWORD);
    }
}
