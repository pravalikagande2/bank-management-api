// File: src/main/java/com/bank/model/Account.java

package com.bank.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Account {
    private int accountId;
    private String customerName;
    private String accountType;
    private BigDecimal balance;
    private BigDecimal avgTransactionAmount;
    private Timestamp createdAt;

    public Account() {}

    public Account(String customerName, String accountType, BigDecimal balance) {
        this.customerName = customerName;
        this.accountType = accountType;
        this.balance = balance;
        this.avgTransactionAmount = balance.compareTo(BigDecimal.ZERO) > 0 ? balance : BigDecimal.ZERO;
    }

    // --- Getters and Setters ---
    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public BigDecimal getAvgTransactionAmount() { return avgTransactionAmount; }
    public void setAvgTransactionAmount(BigDecimal avgTransactionAmount) { this.avgTransactionAmount = avgTransactionAmount; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Account{" + "accountId=" + accountId + ", customerName='" + customerName + '\'' + ", accountType='" + accountType + '\'' + ", balance=" + balance + ", avgTransactionAmount=" + avgTransactionAmount + ", createdAt=" + createdAt + '}';
    }
}
