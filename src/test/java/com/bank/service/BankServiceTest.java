// File: src/test/java/com/bank/service/BankServiceTest.java

package com.bank.service;

import com.bank.exception.InsufficientFundsException;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.repository.AccountRepository;
import com.bank.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private FraudDetectionService fraudDetectionService;

    @InjectMocks
    private BankService bankService;

    private Account fromAccount;
    private Account toAccount;


    @BeforeEach
    void setUp() {
        // Setup a "from" account for most tests
        fromAccount = new Account("Alice", "Savings", new BigDecimal("1000.00"));
        fromAccount.setAccountId(1);

        // Setup a "to" account specifically for the transfer test
        toAccount = new Account("Bob", "Checking", new BigDecimal("500.00"));
        toAccount.setAccountId(2);
    }

    @Test
    void whenDeposit_thenBalanceShouldIncrease() {
        // --- Arrange ---
        BigDecimal depositAmount = new BigDecimal("200.00");
        when(accountRepository.findById(1)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(fromAccount);
        when(fraudDetectionService.checkForFraud(any(Account.class), any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(1));

        // --- Act ---
        Account updatedAccount = bankService.deposit(1, depositAmount);

        // --- Assert ---
        assertEquals(new BigDecimal("1200.00"), updatedAccount.getBalance());
        verify(accountRepository, times(1)).save(any(Account.class));
        verify(transactionRepository, times(1)).save(any());
    }

    @Test
    void whenWithdrawSufficientFunds_thenBalanceShouldDecrease() throws InsufficientFundsException {
        // --- Arrange ---
        BigDecimal withdrawAmount = new BigDecimal("300.00");
        when(accountRepository.findById(1)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(fromAccount);
        when(fraudDetectionService.checkForFraud(any(Account.class), any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(1));

        // --- Act ---
        Account updatedAccount = bankService.withdraw(1, withdrawAmount);

        // --- Assert ---
        assertEquals(new BigDecimal("700.00"), updatedAccount.getBalance());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void whenWithdrawInsufficientFunds_thenThrowException() {
        // --- Arrange ---
        BigDecimal withdrawAmount = new BigDecimal("2000.00");
        when(accountRepository.findById(1)).thenReturn(Optional.of(fromAccount));

        // --- Act & Assert ---
        assertThrows(InsufficientFundsException.class, () -> {
            bankService.withdraw(1, withdrawAmount);
        });
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void whenTransfer_thenBalancesShouldUpdateCorrectly() throws InsufficientFundsException {
        // --- Arrange ---
        BigDecimal transferAmount = new BigDecimal("200.00");
        // Mock the repository to return the correct account for each ID
        when(accountRepository.findById(1)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(2)).thenReturn(Optional.of(toAccount));

        // Mock the fraud service to return the transaction unmodified
        when(fraudDetectionService.checkForFraud(any(Account.class), any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(1));

        // --- Act ---
        bankService.transfer(1, 2, transferAmount);

        // --- Assert ---
        // Use an ArgumentCaptor to capture the Account objects passed to the save method
        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        // Verify that save was called twice (once for each account) and capture the arguments
        verify(accountRepository, times(2)).save(accountCaptor.capture());

        // Get the captured accounts
        List<Account> savedAccounts = accountCaptor.getAllValues();
        Account savedFromAccount = savedAccounts.get(0);
        Account savedToAccount = savedAccounts.get(1);

        // Check if the final balances are correct
        assertEquals(new BigDecimal("800.00"), savedFromAccount.getBalance()); // 1000 - 200
        assertEquals(new BigDecimal("700.00"), savedToAccount.getBalance()); // 500 + 200
    }
}
