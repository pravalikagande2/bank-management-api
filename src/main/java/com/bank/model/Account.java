// File: src/main/java/com/bank/model/Account.java

package com.bank.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;

/**
 * Represents a customer's bank account as a JPA Entity.
 * The @Entity annotation marks this class as a database entity.
 * The @Table annotation specifies the table name in the database.
 */
@Entity
@Table(name = "accounts") // It's good practice to use plural table names
public class Account {

    /**
     * @Id marks this field as the primary key.
     * @GeneratedValue tells JPA that the database will automatically generate this value (e.g., AUTO_INCREMENT).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountId; // Use wrapper type Integer for entities

    @Column(nullable = false)
    private String customerName;

    private String accountType;

    @Column(nullable = false)
    private BigDecimal balance;

    private BigDecimal avgTransactionAmount;

    /**
     * @CreationTimestamp automatically sets this field to the current timestamp when the entity is first created.
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;

    // JPA requires a no-arg constructor
    public Account() {}

    // A convenient constructor for creating new instances
    public Account(String customerName, String accountType, BigDecimal balance) {
        this.customerName = customerName;
        this.accountType = accountType;
        this.balance = balance;
        this.avgTransactionAmount = balance.compareTo(BigDecimal.ZERO) > 0 ? balance : BigDecimal.ZERO;
    }


    // --- Getters and Setters ---

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getAvgTransactionAmount() {
        return avgTransactionAmount;
    }

    public void setAvgTransactionAmount(BigDecimal avgTransactionAmount) {
        this.avgTransactionAmount = avgTransactionAmount;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
