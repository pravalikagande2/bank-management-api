// File: src/main/java/com/bank/dao/AccountDAO.java

package com.bank.dao;

import com.bank.model.Account;

import java.sql.*;
import java.util.Optional;
import java.math.BigDecimal;

public class AccountDAO {

    public int createAccount(Account account) {
        String sql = "INSERT INTO Accounts (customerName, accountType, balance, avgTransactionAmount) VALUES (?, ?, ?, ?)";
        int generatedAccountId = -1;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, account.getCustomerName());
            pstmt.setString(2, account.getAccountType());
            pstmt.setBigDecimal(3, account.getBalance());
            pstmt.setBigDecimal(4, account.getAvgTransactionAmount());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedAccountId = generatedKeys.getInt(1);
                        account.setAccountId(generatedAccountId);
                        System.out.println("Successfully created new account with ID: " + generatedAccountId);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating account: " + e.getMessage());
            e.printStackTrace();
        }
        return generatedAccountId;
    }

    public Optional<Account> findAccountById(int accountId) {
        String sql = "SELECT * FROM Accounts WHERE accountId = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, accountId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Account account = new Account();
                    account.setAccountId(rs.getInt("accountId"));
                    account.setCustomerName(rs.getString("customerName"));
                    account.setAccountType(rs.getString("accountType"));
                    account.setBalance(rs.getBigDecimal("balance"));
                    account.setAvgTransactionAmount(rs.getBigDecimal("avgTransactionAmount"));
                    account.setCreatedAt(rs.getTimestamp("createdAt"));
                    return Optional.of(account);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding account by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean updateAccount(Account account) {
        String sql = "UPDATE Accounts SET balance = ?, avgTransactionAmount = ? WHERE accountId = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBigDecimal(1, account.getBalance());
            pstmt.setBigDecimal(2, account.getAvgTransactionAmount());
            pstmt.setInt(3, account.getAccountId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating account: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
