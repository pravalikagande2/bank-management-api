// File: src/main/java/com/bank/dto/AccountCreateRequest.java

package com.bank.dto;

import java.math.BigDecimal;

/**
 * DTO for handling the creation of a new account.
 * It contains only the fields necessary for the request.
 */
public class AccountCreateRequest {
    private String customerName;
    private String accountType;
    private BigDecimal initialDeposit;

    // Getters and Setters
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    public BigDecimal getInitialDeposit() { return initialDeposit; }
    public void setInitialDeposit(BigDecimal initialDeposit) { this.initialDeposit = initialDeposit; }
}
