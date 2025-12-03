package models;

import exceptions.InsufficientFundsException;
import exceptions.InvalidAmountException;
import exceptions.OverdraftExceededException;

public class CheckingAccount extends Account {
    private static final double OVERDRAFT_LIMIT = 500.0;
    
    public CheckingAccount(String accountNumber, double initialBalance, Customer customer) {
        super(accountNumber, initialBalance, customer);
    }
    
    @Override
    public boolean withdraw(double amount) throws InsufficientFundsException, InvalidAmountException, OverdraftExceededException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than 0");
        }
        
        double newBalance = balance - amount;
        if (newBalance < -OVERDRAFT_LIMIT) {
            throw new OverdraftExceededException(
                String.format("Overdraft limit exceeded. Current balance: $%.2f, Requested: $%.2f, Overdraft limit: $%.2f", 
                            balance, amount, OVERDRAFT_LIMIT));
        }
        
        balance = newBalance;
        return true;
    }
    
    @Override
    public void deposit(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be greater than 0");
        }
        balance += amount;
    }
    
    public double getOverdraftLimit() {
        return OVERDRAFT_LIMIT;
    }
    
    public double getMaxWithdrawalAmount() {
        return balance + OVERDRAFT_LIMIT;
    }
}