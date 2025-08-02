// File: src/main/java/com/bank/service/FraudDetectionService.java

package com.bank.service;

import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Service responsible for detecting potentially fraudulent transactions.
 * The @Service annotation marks this as a Spring service bean.
 */
@Service
public class FraudDetectionService {

    // --- Configuration for Fraud Rules ---
    private static final int FREQUENCY_TRANSACTION_LIMIT = 10;
    private static final long FREQUENCY_WINDOW_MINUTES = 5;
    private static final BigDecimal AMOUNT_MULTIPLIER_LIMIT = new BigDecimal("5.0");

    private final TransactionRepository transactionRepository;

    /**
     * Using constructor injection to get the TransactionRepository bean from Spring.
     * This is the recommended way to inject dependencies.
     * @param transactionRepository The repository for transaction data access.
     */
    @Autowired
    public FraudDetectionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Checks a transaction against a set of fraud rules.
     */
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

    private boolean isTransactionFrequencyTooHigh(Integer accountId) {
        Timestamp windowStart = Timestamp.from(Instant.now().minusSeconds(FREQUENCY_WINDOW_MINUTES * 60));

        // Use the new repository method. It's much cleaner!
        List<Transaction> recentTransactions = transactionRepository.findByAccountIdAndTransactionTimeGreaterThanEqual(accountId, windowStart);

        System.out.println("Found " + recentTransactions.size() + " transactions in the last " + FREQUENCY_WINDOW_MINUTES + " minutes for account " + accountId);
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
