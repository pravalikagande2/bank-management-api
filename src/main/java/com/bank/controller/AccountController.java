// File: src/main/java/com/bank/controller/AccountController.java

package com.bank.controller;

import com.bank.dto.AccountCreateRequest;
import com.bank.dto.TransactionRequest;
import com.bank.exception.InsufficientFundsException;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for handling all account and transaction related API endpoints.
 * @RestController combines @Controller and @ResponseBody, telling Spring to return JSON.
 * @RequestMapping defines a base URL for all endpoints in this class.
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final BankService bankService;

    @Autowired
    public AccountController(BankService bankService) {
        this.bankService = bankService;
    }

    /**
     * Endpoint to create a new bank account.
     * Handles POST requests to /api/accounts
     * @param request The request body containing account details.
     * @return The newly created account object with a 201 CREATED status.
     */
    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody AccountCreateRequest request) {
        Account newAccount = bankService.createAccount(
                request.getCustomerName(),
                request.getAccountType(),
                request.getInitialDeposit()
        );
        return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
    }

    /**
     * Endpoint to get account details by ID.
     * Handles GET requests to /api/accounts/{accountId}
     * @param accountId The ID of the account, passed in the URL path.
     * @return The account object if found (200 OK), or a 404 NOT FOUND status.
     */
    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccount(@PathVariable int accountId) {
        Optional<Account> account = bankService.getAccount(accountId);
        return account.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint to get the transaction history for an account.
     * Handles GET requests to /api/accounts/{accountId}/transactions
     */
    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable int accountId) {
        List<Transaction> history = bankService.getTransactionHistory(accountId);
        return ResponseEntity.ok(history);
    }

    /**
     * Endpoint to deposit funds into an account.
     * Handles POST requests to /api/accounts/{accountId}/deposit
     */
    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<Account> deposit(@PathVariable int accountId, @RequestBody TransactionRequest request) {
        Account updatedAccount = bankService.deposit(accountId, request.getAmount());
        return ResponseEntity.ok(updatedAccount);
    }

    /**
     * Endpoint to withdraw funds from an account.
     * Handles POST requests to /api/accounts/{accountId}/withdraw
     */
    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<?> withdraw(@PathVariable int accountId, @RequestBody TransactionRequest request) {
        try {
            Account updatedAccount = bankService.withdraw(accountId, request.getAmount());
            return ResponseEntity.ok(updatedAccount);
        } catch (InsufficientFundsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint to transfer funds between accounts.
     * Handles POST requests to /api/accounts/transfer
     */
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransactionRequest request) {
        try {
            bankService.transfer(request.getFromAccountId(), request.getToAccountId(), request.getAmount());
            return ResponseEntity.ok().body("Transfer successful!");
        } catch (InsufficientFundsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
