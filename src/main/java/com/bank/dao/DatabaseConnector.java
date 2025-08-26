// File: src/main/java/com/bank/dao/DatabaseConnector.java

package com.bank.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {

    private static final String URL = "jdbc:mysql://localhost:3306/bank_system?serverTimezone=UTC";

    // !!! IMPORTANT: Change this to your actual MySQL username and password !!!
    private static final String USER = "root";       // <-- CHANGE THIS
    private static final String PASSWORD = "123456"; // <-- CHANGE THIS

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Make sure it's in your classpath.");
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initializeDatabase() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            // SQL to create the Accounts table (Full Version)
            String createAccountsTableSql = "CREATE TABLE IF NOT EXISTS Accounts (" +
                    "accountId INT PRIMARY KEY AUTO_INCREMENT," +
                    "customerName VARCHAR(255) NOT NULL," +
                    "accountType VARCHAR(50)," +
                    "balance DECIMAL(15, 2) NOT NULL," +
                    "avgTransactionAmount DECIMAL(15, 2) DEFAULT 0.00," +
                    "createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ");";

            // SQL to create the Transactions table (Full Version)
            String createTransactionsTableSql = "CREATE TABLE IF NOT EXISTS Transactions (" +
                    "transactionId INT PRIMARY KEY AUTO_INCREMENT," +
                    "accountId INT," +
                    "transactionType VARCHAR(50) NOT NULL," +
                    "amount DECIMAL(15, 2) NOT NULL," +
                    "isFlagged BOOLEAN DEFAULT FALSE," +
                    "reasonForFlag VARCHAR(255)," +
                    "transactionTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (accountId) REFERENCES Accounts(accountId)" +
                    ");";

            statement.execute(createAccountsTableSql);
            statement.execute(createTransactionsTableSql);

            System.out.println("Database connection successful. Tables are ready.");

        } catch (SQLException e) {
            System.err.println("Error initializing the database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
