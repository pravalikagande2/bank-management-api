// File: src/main/java/com/bank/model/Transaction.java

        package com.bank.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;

/**
 * Represents a single bank transaction as a JPA Entity.
 */
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionId;

    @Column(nullable = false)
    private Integer accountId;

    @Column(nullable = false)
    private String transactionType;

    @Column(nullable = false)
    private BigDecimal amount;

    private boolean isFlagged = false;

    private String reasonForFlag;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp transactionTime;

    // JPA requires a no-arg constructor
    public Transaction() {}

    public Transaction(Integer accountId, String transactionType, BigDecimal amount) {
        this.accountId = accountId;
        this.transactionType = transactionType;
        this.amount = amount;
    }

    // --- Getters and Setters ---

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }



    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    public String getReasonForFlag() {
        return reasonForFlag;
    }

    public void setReasonForFlag(String reasonForFlag) {
        this.reasonForFlag = reasonForFlag;
    }

    public Timestamp getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Timestamp transactionTime) {
        this.transactionTime = transactionTime;
    }
}
