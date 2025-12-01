package com.bank.system.utils;

import com.bank.system.exceptions.InvalidAmountException;

public class ValidationUtils {
    
    /**
     * Validates that an amount is positive and greater than zero
     * @param amount the amount to validate
     * @throws InvalidAmountException if amount is invalid
     */
    public static void validateAmount(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Invalid amount. Amount must be greater than 0.");
        }
    }
    
    /**
     * Validates that an account number is not null or empty
     * @param accountNumber the account number to validate
     * @throws InvalidAmountException if account number is invalid
     */
    public static void validateAccountNumber(String accountNumber) throws InvalidAmountException {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new InvalidAmountException("Account number cannot be null or empty.");
        }
    }
    
    /**
     * Validates that a string is not null or empty
     * @param value the string to validate
     * @param fieldName the name of the field for error messages
     * @throws InvalidAmountException if string is invalid
     */
    public static void validateString(String value, String fieldName) throws InvalidAmountException {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidAmountException(fieldName + " cannot be null or empty.");
        }
    }
}