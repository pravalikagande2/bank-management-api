// File: src/main/java/com/bank/dao/TransactionDAO.java

package com.bank.dao;

import com.bank.model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public int createTransaction(Transaction transaction) {
        String sql = "INSERT INTO Transactions (accountId, transactionType, amount, isFlagged, reasonForFlag) VALUES (?, ?, ?, ?, ?)";
        int generatedTransactionId = -1;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, transaction.getAccountId());
            pstmt.setString(2, transaction.getTransactionType());
            pstmt.setBigDecimal(3, transaction.getAmount());
            pstmt.setBoolean(4, transaction.isFlagged());
            pstmt.setString(5, transaction.getReasonForFlag());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedTransactionId = generatedKeys.getInt(1);
                        transaction.setTransactionId(generatedTransactionId);
                        System.out.println("Successfully created transaction with ID: " + generatedTransactionId);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating transaction: " + e.getMessage());
            e.printStackTrace();
        }
        return generatedTransactionId;
    }

    public List<Transaction> findTransactionsByAccountId(int accountId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM Transactions WHERE accountId = ? ORDER BY transactionTime DESC";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, accountId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = new Transaction();
                    transaction.setTransactionId(rs.getInt("transactionId"));
                    transaction.setAccountId(rs.getInt("accountId"));
                    transaction.setTransactionType(rs.getString("transactionType"));
                    transaction.setAmount(rs.getBigDecimal("amount"));
                    transaction.setFlagged(rs.getBoolean("isFlagged"));
                    transaction.setReasonForFlag(rs.getString("reasonForFlag"));
                    transaction.setTransactionTime(rs.getTimestamp("transactionTime"));
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding transactions by account ID: " + e.getMessage());
            e.printStackTrace();
        }
        return transactions;
    }

    public List<Transaction> findTransactionsByAccountIdSince(int accountId, Timestamp windowStart) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM Transactions WHERE accountId = ? AND transactionTime >= ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, accountId);
            pstmt.setTimestamp(2, windowStart);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = new Transaction();
                    transaction.setTransactionId(rs.getInt("transactionId"));
                    transaction.setAccountId(rs.getInt("accountId"));
                    transaction.setTransactionType(rs.getString("transactionType"));
                    transaction.setAmount(rs.getBigDecimal("amount"));
                    transaction.setFlagged(rs.getBoolean("isFlagged"));
                    transaction.setReasonForFlag(rs.getString("reasonForFlag"));
                    transaction.setTransactionTime(rs.getTimestamp("transactionTime"));
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding recent transactions: " + e.getMessage());
            e.printStackTrace();
        }
        return transactions;
    }
}
