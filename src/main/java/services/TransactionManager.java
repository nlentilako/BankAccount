package services;

import models.*;
import exceptions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionManager {
    private List<Transaction> allTransactions;
    private int nextTransactionId;
    private AccountManager accountManager;
    
    public TransactionManager(AccountManager accountManager) {
        this.accountManager = accountManager;
        this.allTransactions = new ArrayList<>();
        this.nextTransactionId = 1;
    }
    
    public String generateTransactionId() {
        return String.format("TXN%03d", nextTransactionId++);
    }
    
    public boolean deposit(String accountNumber, double amount) throws InvalidAmountException, InsufficientFundsException, OverdraftExceededException {
        Account account = accountManager.getAccount(accountNumber);
        if (account == null) {
            throw new InvalidAmountException("Account not found: " + accountNumber);
        }
        
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be greater than 0");
        }
        
        double previousBalance = account.getBalance();
        account.deposit(amount);
        double newBalance = account.getBalance();
        
        // Create and record the transaction
        String transactionId = generateTransactionId();
        Transaction transaction = new Transaction(transactionId, accountNumber, "DEPOSIT", amount, newBalance);
        allTransactions.add(transaction);
        account.addTransaction(transaction);
        
        return true;
    }
    
    public boolean withdraw(String accountNumber, double amount) throws InsufficientFundsException, InvalidAmountException, OverdraftExceededException {
        Account account = accountManager.getAccount(accountNumber);
        if (account == null) {
            throw new InvalidAmountException("Account not found: " + accountNumber);
        }
        
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than 0");
        }
        
        double previousBalance = account.getBalance();
        boolean success = account.withdraw(amount);
        double newBalance = account.getBalance();
        
        if (success) {
            // Create and record the transaction
            String transactionId = generateTransactionId();
            Transaction transaction = new Transaction(transactionId, accountNumber, "WITHDRAWAL", amount, newBalance);
            allTransactions.add(transaction);
            account.addTransaction(transaction);
        }
        
        return success;
    }
    
    public boolean transfer(String fromAccountNumber, String toAccountNumber, double amount) 
            throws InsufficientFundsException, InvalidAmountException, OverdraftExceededException {
        Account fromAccount = accountManager.getAccount(fromAccountNumber);
        Account toAccount = accountManager.getAccount(toAccountNumber);
        
        if (fromAccount == null) {
            throw new InvalidAmountException("Source account not found: " + fromAccountNumber);
        }
        
        if (toAccount == null) {
            throw new InvalidAmountException("Destination account not found: " + toAccountNumber);
        }
        
        if (amount <= 0) {
            throw new InvalidAmountException("Transfer amount must be greater than 0");
        }
        
        // Perform withdrawal from source account
        double previousFromBalance = fromAccount.getBalance();
        fromAccount.withdraw(amount);
        double newFromBalance = fromAccount.getBalance();
        
        // Perform deposit to destination account
        double previousToBalance = toAccount.getBalance();
        toAccount.deposit(amount);
        double newToBalance = toAccount.getBalance();
        
        // Record withdrawal transaction
        String withdrawalTransactionId = generateTransactionId();
        Transaction withdrawalTransaction = new Transaction(withdrawalTransactionId, fromAccountNumber, "TRANSFER_OUT", amount, newFromBalance);
        allTransactions.add(withdrawalTransaction);
        fromAccount.addTransaction(withdrawalTransaction);
        
        // Record deposit transaction
        String depositTransactionId = generateTransactionId();
        Transaction depositTransaction = new Transaction(depositTransactionId, toAccountNumber, "TRANSFER_IN", amount, newToBalance);
        allTransactions.add(depositTransaction);
        toAccount.addTransaction(depositTransaction);
        
        return true;
    }
    
    public List<Transaction> getTransactionsForAccount(String accountNumber) {
        return allTransactions.stream()
                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .collect(Collectors.toList());
    }
    
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(allTransactions);
    }
    
    public int getTotalTransactions() {
        return allTransactions.size();
    }
}