// File: src/main/java/com/bank/dto/TransactionRequest.java

package com.bank.dto;

import java.math.BigDecimal;

/**
 * DTO for handling deposits, withdrawals, and transfers.
 */
public class TransactionRequest {
    private BigDecimal amount;
    // For transfers
    private Integer fromAccountId;
    private Integer toAccountId;


    // Getters and Setters
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public Integer getFromAccountId() { return fromAccountId; }
    public void setFromAccountId(Integer fromAccountId) { this.fromAccountId = fromAccountId; }
    public Integer getToAccountId() { return toAccountId; }
    public void setToAccountId(Integer toAccountId) { this.toAccountId = toAccountId; }
}
