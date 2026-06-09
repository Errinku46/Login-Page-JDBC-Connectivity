package com.login.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class to manage MySQL database connections via JDBC.
 * Update DB_URL, DB_USER, and DB_PASSWORD to match your environment.
 */
public class DBConnection {

    private static final String DB_URL      = "jdbc:mysql://localhost:3306/login_db?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "05rinku2002";  

    private static Connection connection = null;

    // Private constructor – utility class
    private DBConnection() {}

    /**
     * Returns a singleton Connection object.
     * Re-opens the connection if it was closed.
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found. Add mysql-connector-j JAR to classpath.", e);
        }

        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        }
        return connection;
    }

    /** Closes the singleton connection safely. */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
