package models;

import java.util.ArrayList;
import java.util.List;
import exceptions.InsufficientFundsException;
import exceptions.InvalidAmountException;
import exceptions.OverdraftExceededException;

public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected Customer customer;
    protected List<Transaction> transactions;
    
    public Account(String accountNumber, double initialBalance, Customer customer) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.customer = customer;
        this.transactions = new ArrayList<>();
    }
    
    // Abstract methods to be implemented by subclasses
    public abstract boolean withdraw(double amount) throws InsufficientFundsException, InvalidAmountException, OverdraftExceededException;
    public abstract void deposit(double amount) throws InvalidAmountException;
    
    // Common methods
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }
    
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }
    
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    @Override
    public String toString() {
        return String.format("Account Number: %s, Balance: $%.2f, Customer: %s", 
                           accountNumber, balance, customer.getName());
    }
}