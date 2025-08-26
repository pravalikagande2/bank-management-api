// File: src/main/java/com/bank/Main.java

package com.bank;

import com.bank.dao.DatabaseConnector;
import com.bank.exception.InsufficientFundsException;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.service.BankService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final BankService bankService = new BankService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        DatabaseConnector.initializeDatabase();

        while (true) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    deposit();
                    break;
                case 3:
                    withdraw();
                    break;
                case 4:
                    transfer();
                    break;
                case 5:
                    viewAccount();
                    break;
                case 6:
                    viewTransactionHistory();
                    break;
                case 7:
                    runFraudScenarios();
                    break;
                case 8:
                    System.out.println("Thank you for using the Bank Management System. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--- Bank Management System ---");
        System.out.println("1. Create New Account");
        System.out.println("2. Deposit Funds");
        System.out.println("3. Withdraw Funds");
        System.out.println("4. Transfer Funds");
        System.out.println("5. View Account Details");
        System.out.println("6. View Transaction History");
        System.out.println("7. Run Fraud Detection Scenarios");
        System.out.println("8. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void createAccount() {
        System.out.print("Enter Customer Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Account Type (e.g., Savings, Checking): ");
        String type = scanner.nextLine();
        System.out.print("Enter Initial Deposit Amount: ");
        BigDecimal initialDeposit = scanner.nextBigDecimal();
        scanner.nextLine();

        Account account = bankService.createAccount(name, type, initialDeposit);
        System.out.println("Account created successfully! Your new Account ID is: " + account.getAccountId());
    }

    private static void deposit() {
        System.out.print("Enter Account ID: ");
        int accountId = scanner.nextInt();
        System.out.print("Enter Deposit Amount: ");
        BigDecimal amount = scanner.nextBigDecimal();
        scanner.nextLine();

        bankService.deposit(accountId, amount);
    }

    private static void withdraw() {
        System.out.print("Enter Account ID: ");
        int accountId = scanner.nextInt();
        System.out.print("Enter Withdrawal Amount: ");
        BigDecimal amount = scanner.nextBigDecimal();
        scanner.nextLine();

        try {
            bankService.withdraw(accountId, amount);
        } catch (InsufficientFundsException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void transfer() {
        System.out.print("Enter Source Account ID: ");
        int fromId = scanner.nextInt();
        System.out.print("Enter Destination Account ID: ");
        int toId = scanner.nextInt();
        System.out.print("Enter Transfer Amount: ");
        BigDecimal amount = scanner.nextBigDecimal();
        scanner.nextLine();

        try {
            bankService.transfer(fromId, toId, amount);
        } catch (InsufficientFundsException e) {
            System.err.println("Error during transfer: " + e.getMessage());
        }
    }

    private static void viewAccount() {
        System.out.print("Enter Account ID: ");
        int accountId = scanner.nextInt();
        scanner.nextLine();

        Optional<Account> accountOpt = bankService.getAccount(accountId);
        if (accountOpt.isPresent()) {
            System.out.println("Account Details: " + accountOpt.get());
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void viewTransactionHistory() {
        System.out.print("Enter Account ID: ");
        int accountId = scanner.nextInt();
        scanner.nextLine();

        List<Transaction> transactions = bankService.getTransactionHistory(accountId);
        if (transactions.isEmpty()) {
            System.out.println("No transactions found for this account.");
        } else {
            System.out.println("--- Transaction History for Account " + accountId + " ---");
            transactions.forEach(System.out::println);
        }
    }

    private static void runFraudScenarios() {
        System.out.println("\n--- Running Fraud Detection Scenarios ---");
        System.out.println("\n[SCENARIO 1: High Frequency]");
        Account fraudAccount1 = bankService.createAccount("Fraudster One", "Checking", new BigDecimal("1000.00"));
        int accountId1 = fraudAccount1.getAccountId();
        System.out.println("Created new account for frequency test: ID " + accountId1);
        System.out.println("Performing 11 small deposits quickly...");
        for (int i = 0; i < 11; i++) {
            System.out.print("Transaction " + (i + 1) + ": ");
            bankService.deposit(accountId1, new BigDecimal("1.00"));
        }
        System.out.println("Check the transaction history for flagged transactions.");

        System.out.println("\n[SCENARIO 2: Anomalous Amount]");
        Account fraudAccount2 = bankService.createAccount("Fraudster Two", "Savings", new BigDecimal("100.00"));
        int accountId2 = fraudAccount2.getAccountId();
        System.out.println("Created new account for amount test: ID " + accountId2);
        bankService.deposit(accountId2, new BigDecimal("150.00"));
        System.out.println("Performing a very large deposit that breaks the pattern...");
        bankService.deposit(accountId2, new BigDecimal("5000.00"));
        System.out.println("Check the transaction history for flagged transactions.");
    }
}
