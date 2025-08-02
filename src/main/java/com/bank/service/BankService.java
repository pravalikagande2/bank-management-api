// File: src/main/java/com/bank/service/BankService.java

package com.bank.service;

import com.bank.exception.InsufficientFundsException;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.repository.AccountRepository;
import com.bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

/**
 * The main service class for handling all banking business logic.
 * It uses Spring's dependency injection and transaction management.
 */
@Service
public class BankService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final FraudDetectionService fraudDetectionService;

    @Autowired
    public BankService(AccountRepository accountRepository, TransactionRepository transactionRepository, FraudDetectionService fraudDetectionService) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.fraudDetectionService = fraudDetectionService;
    }

    public Account createAccount(String customerName, String accountType, BigDecimal initialDeposit) {
        Account newAccount = new Account(customerName, accountType, initialDeposit);
        // The save() method handles both creating new entities and updating existing ones.
        return accountRepository.save(newAccount);
    }

    /**
     * The @Transactional annotation ensures that this entire method runs within a single database transaction.
     * If any part of it fails, all database changes will be automatically rolled back.
     */
    @Transactional
    public Account deposit(int accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalStateException("Account not found with ID " + accountId));

        Transaction transaction = new Transaction(accountId, "DEPOSIT", amount);
        transaction = fraudDetectionService.checkForFraud(account, transaction);

        account.setBalance(account.getBalance().add(amount));

        if (!transaction.isFlagged()) {
            updateAverageTransactionAmount(account, amount);
        }

        transactionRepository.save(transaction);
        return accountRepository.save(account);
    }

    @Transactional
    public Account withdraw(int accountId, BigDecimal amount) throws InsufficientFundsException {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalStateException("Account not found with ID " + accountId));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal. Current balance: " + account.getBalance());
        }

        Transaction transaction = new Transaction(accountId, "WITHDRAWAL", amount);
        transaction = fraudDetectionService.checkForFraud(account, transaction);

        account.setBalance(account.getBalance().subtract(amount));

        if (!transaction.isFlagged()) {
            updateAverageTransactionAmount(account, amount);
        }

        transactionRepository.save(transaction);
        return accountRepository.save(account);
    }

    @Transactional
    public void transfer(int fromAccountId, int toAccountId, BigDecimal amount) throws InsufficientFundsException {
        // Since withdraw and deposit are already @Transactional, calling them will propagate the transaction.
        // We perform the withdrawal first. If it fails (e.g., insufficient funds), an exception is thrown
        // and the transaction is rolled back, so the deposit never even happens.
        withdraw(fromAccountId, amount);
        deposit(toAccountId, amount);
    }

    public Optional<Account> getAccount(int accountId) {
        return accountRepository.findById(accountId);
    }

    public List<Transaction> getTransactionHistory(int accountId) {
        return transactionRepository.findByAccountIdOrderByTransactionTimeDesc(accountId);
    }

    private void updateAverageTransactionAmount(Account account, BigDecimal newAmount) {
        long transactionCount = transactionRepository.countByAccountIdAndIsFlagged(account.getAccountId(), false);

        BigDecimal currentTotal = account.getAvgTransactionAmount().multiply(new BigDecimal(transactionCount));
        BigDecimal newTotal = currentTotal.add(newAmount);
        BigDecimal newAverage = newTotal.divide(new BigDecimal(transactionCount + 1), 2, RoundingMode.HALF_UP);

        account.setAvgTransactionAmount(newAverage);
    }
}
