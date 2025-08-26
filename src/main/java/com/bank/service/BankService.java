package com.bank.service;

import com.bank.dao.AccountDAO;
import com.bank.dao.TransactionDAO;
import com.bank.exception.InsufficientFundsException;
import com.bank.model.Account;
import com.bank.model.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.List;

public class BankService {
    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;
    private final FraudDetectionService fraudDetectionService;

    public BankService() {
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
        this.fraudDetectionService = new FraudDetectionService();
    }

    public Account createAccount(String customerName, String accountType, BigDecimal initialDeposit) {
        Account newAccount = new Account(customerName, accountType, initialDeposit);
        int accountId = accountDAO.createAccount(newAccount);
        newAccount.setAccountId(accountId);
        return newAccount;
    }

    public Optional<Account> deposit(int accountId, BigDecimal amount) {
        Optional<Account> accountOpt = accountDAO.findAccountById(accountId);
        if (accountOpt.isEmpty()) {
            System.err.println("Deposit failed: Account not found with ID " + accountId);
            return Optional.empty();
        }

        Account account = accountOpt.get();
        Transaction transaction = new Transaction(accountId, "DEPOSIT", amount);
        transaction = fraudDetectionService.checkForFraud(account, transaction);

        account.setBalance(account.getBalance().add(amount));

        if (!transaction.isFlagged()) {
            updateAverageTransactionAmount(account, amount);
        }

        accountDAO.updateAccount(account);
        transactionDAO.createTransaction(transaction);

        System.out.println("Deposit successful for account " + accountId);
        return Optional.of(account);
    }

    public Optional<Account> withdraw(int accountId, BigDecimal amount) throws InsufficientFundsException {
        Optional<Account> accountOpt = accountDAO.findAccountById(accountId);
        if (accountOpt.isEmpty()) {
            System.err.println("Withdrawal failed: Account not found with ID " + accountId);
            return Optional.empty();
        }

        Account account = accountOpt.get();
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal. Current balance: " + account.getBalance());
        }

        Transaction transaction = new Transaction(accountId, "WITHDRAWAL", amount);
        transaction = fraudDetectionService.checkForFraud(account, transaction);

        account.setBalance(account.getBalance().subtract(amount));

        if (!transaction.isFlagged()) {
            updateAverageTransactionAmount(account, amount);
        }

        accountDAO.updateAccount(account);
        transactionDAO.createTransaction(transaction);

        System.out.println("Withdrawal successful for account " + accountId);
        return Optional.of(account);
    }

    public void transfer(int fromAccountId, int toAccountId, BigDecimal amount) throws InsufficientFundsException {
        Optional<Account> fromAccountOpt = accountDAO.findAccountById(fromAccountId);
        Optional<Account> toAccountOpt = accountDAO.findAccountById(toAccountId);

        if (fromAccountOpt.isEmpty() || toAccountOpt.isEmpty()) {
            System.err.println("Transfer failed: One or both accounts not found.");
            return;
        }

        withdraw(fromAccountId, amount);
        deposit(toAccountId, amount);

        System.out.println("Transfer of " + amount + " from account " + fromAccountId + " to " + toAccountId + " successful.");
    }

    public Optional<Account> getAccount(int accountId) {
        return accountDAO.findAccountById(accountId);
    }

    public List<Transaction> getTransactionHistory(int accountId) {
        return transactionDAO.findTransactionsByAccountId(accountId);
    }

    private void updateAverageTransactionAmount(Account account, BigDecimal newAmount) {
        long transactionCount = transactionDAO.findTransactionsByAccountId(account.getAccountId())
                .stream()
                .filter(t -> !t.isFlagged())
                .count();

        BigDecimal currentTotal = account.getAvgTransactionAmount().multiply(new BigDecimal(transactionCount));
        BigDecimal newTotal = currentTotal.add(newAmount);
        BigDecimal newAverage = newTotal.divide(new BigDecimal(transactionCount + 1), 2, RoundingMode.HALF_UP);

        account.setAvgTransactionAmount(newAverage);
    }
}
