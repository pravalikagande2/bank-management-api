package com.bank.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Transaction {
    private int transactionId;
    private int accountId;
    private String transactionType;
    private BigDecimal amount;
    private boolean isFlagged;
    private String reasonForFlag;
    private Timestamp transactionTime;

    public Transaction() {}

    public Transaction(int accountId, String transactionType, BigDecimal amount) {
        this.accountId = accountId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.isFlagged = false;
    }

    // --- Getters and Setters ---
    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }
    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public boolean isFlagged() { return isFlagged; }
    public void setFlagged(boolean flagged) { isFlagged = flagged; }
    public String getReasonForFlag() { return reasonForFlag; }
    public void setReasonForFlag(String reasonForFlag) { this.reasonForFlag = reasonForFlag; }
    public Timestamp getTransactionTime() { return transactionTime; }
    public void setTransactionTime(Timestamp transactionTime) { this.transactionTime = transactionTime; }

    @Override
    public String toString() {
        return "Transaction{" + "transactionId=" + transactionId + ", accountId=" + accountId + ", transactionType='" + transactionType + '\'' + ", amount=" + amount + ", isFlagged=" + isFlagged + ", reasonForFlag='" + reasonForFlag + '\'' + ", transactionTime=" + transactionTime + '}';
    }
}
