package models;

import exceptions.InsufficientFundsException;
import exceptions.InvalidAmountException;

public class SavingsAccount extends Account {
    private static final double MINIMUM_BALANCE = 100.0;
    private static final double WITHDRAWAL_FEE = 2.0;
    
    public SavingsAccount(String accountNumber, double initialBalance, Customer customer) {
        super(accountNumber, initialBalance, customer);
    }
    
    @Override
    public boolean withdraw(double amount) throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than 0");
        }
        
        double totalAmount = amount + WITHDRAWAL_FEE;
        if (balance - totalAmount < MINIMUM_BALANCE) {
            throw new InsufficientFundsException(
                String.format("Insufficient funds. Current balance: $%.2f, Requested: $%.2f, Min required: $%.2f", 
                            balance, totalAmount, MINIMUM_BALANCE));
        }
        
        balance -= totalAmount;
        return true;
    }
    
    @Override
    public void deposit(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be greater than 0");
        }
        balance += amount;
    }
    
    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }
}