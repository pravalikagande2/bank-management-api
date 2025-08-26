Bank Management System (CLI)
This project is a command-line based Bank Management System built with Java. It handles standard banking operations like account creation, deposits, and withdrawals, and features a built-in fraud detection module to identify suspicious transactions based on unusual patterns.

Features
Core Banking
Account Management: Create new customer accounts.

View Details: Check account balance and information.

Transactions: Deposit, withdraw, and transfer funds between accounts.

History: View a complete transaction history for any account.

Fraud Detection Module
The system automatically checks every transaction for the following patterns:

High Transaction Frequency: Flags accounts with too many transactions in a short time frame (e.g., more than 10 transactions in 5 minutes) using a Sliding Window algorithm.

Anomalous Transaction Amount: Flags transactions that are significantly larger than the user's historical average.

Technology Stack
Language: Java

Database: MySQL

Connectivity: JDBC (Java Database Connectivity)

Build Tool: Maven

Database Schema
You will need two tables in your MySQL database.

Accounts Table

CREATE TABLE Accounts (
accountId INT PRIMARY KEY AUTO_INCREMENT,
customerName VARCHAR(255) NOT NULL,
accountType VARCHAR(50),
balance DECIMAL(15, 2) NOT NULL,
avgTransactionAmount DECIMAL(15, 2) DEFAULT 0.00,
createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

Transactions Table

CREATE TABLE Transactions (
transactionId INT PRIMARY KEY AUTO_INCREMENT,
accountId INT,
transactionType VARCHAR(50) NOT NULL,
amount DECIMAL(15, 2) NOT NULL,
isFlagged BOOLEAN DEFAULT FALSE,
reasonForFlag VARCHAR(255),
transactionTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (accountId) REFERENCES Accounts(accountId)
);

How to Set Up and Run
Clone the Repository:

git clone <your-repository-url>

Set Up the Database:

Make sure you have a MySQL server running.

Connect to your MySQL server and run the following command:

CREATE DATABASE bank_system;

Configure Database Connection:

Open the project in your IDE (like IntelliJ).

Navigate to the file: src/main/java/com/bank/dao/DatabaseConnector.java.

Update the USER and PASSWORD fields with your own MySQL credentials.

Run the Application:

Find the Main.java file in src/main/java/com/bank/.

Right-click on the file and select "Run 'Main.main()'".

The application will start, and you can interact with it through the menu in your IDE's console.
