// File: src/main/java/com/bank/exception/InsufficientFundsException.java

package com.bank.exception;

/**
 * A custom exception thrown when an account has insufficient funds for a withdrawal or transfer.
 */
public class InsufficientFundsException extends Exception {

    public InsufficientFundsException(String message) {
        super(message);
    }
}
