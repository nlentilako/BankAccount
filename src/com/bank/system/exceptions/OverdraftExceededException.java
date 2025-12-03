package com.bank.system.exceptions;

public class OverdraftExceededException extends Exception {
    public OverdraftExceededException(String message) {
        super(message);
    }
    
    public OverdraftExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}