package com.bank.service;

import com.bank.dao.TransactionDAO;
import com.bank.model.Account;
import com.bank.model.Transaction;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

public class FraudDetectionService {
    private static final int FREQUENCY_TRANSACTION_LIMIT = 10;
    private static final long FREQUENCY_WINDOW_MINUTES = 5;
    private static final BigDecimal AMOUNT_MULTIPLIER_LIMIT = new BigDecimal("5.0");

    private final TransactionDAO transactionDAO;

    public FraudDetectionService() {
        this.transactionDAO = new TransactionDAO();
    }

    public Transaction checkForFraud(Account account, Transaction newTransaction) {
        if (isTransactionFrequencyTooHigh(account.getAccountId())) {
            newTransaction.setFlagged(true);
            newTransaction.setReasonForFlag("High transaction frequency detected.");
            return newTransaction;
        }

        if (isTransactionAmountAnomalous(account, newTransaction.getAmount())) {
            newTransaction.setFlagged(true);
            newTransaction.setReasonForFlag("Transaction amount is significantly higher than average.");
            return newTransaction;
        }

        return newTransaction;
    }

    private boolean isTransactionFrequencyTooHigh(int accountId) {
        Timestamp windowStart = Timestamp.from(Instant.now().minusSeconds(FREQUENCY_WINDOW_MINUTES * 60));
        List<Transaction> recentTransactions = transactionDAO.findTransactionsByAccountIdSince(accountId, windowStart);
        System.out.println("Found " + recentTransactions.size() + " transactions in the last " + FREQUENCY_WINDOW_MINUTES + " minutes.");
        return recentTransactions.size() >= FREQUENCY_TRANSACTION_LIMIT;
    }

    private boolean isTransactionAmountAnomalous(Account account, BigDecimal amount) {
        BigDecimal average = account.getAvgTransactionAmount();
        if (average == null || average.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        BigDecimal limit = average.multiply(AMOUNT_MULTIPLIER_LIMIT);
        System.out.println("Checking amount " + amount + " against limit " + limit + " (avg: " + average + ")");
        return amount.compareTo(limit) > 0;
    }
}
