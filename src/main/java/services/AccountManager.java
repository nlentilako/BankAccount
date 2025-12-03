package services;

import models.*;

import java.util.HashMap;
import java.util.Map;

public class AccountManager {
    private Map<String, Account> accounts;
    private int nextAccountId;
    
    public AccountManager() {
        this.accounts = new HashMap<>();
        this.nextAccountId = 1;
    }
    
    public String generateAccountNumber() {
        String accountNumber = String.format("ACC%03d", nextAccountId);
        nextAccountId++;
        return accountNumber;
    }
    
    public boolean addAccount(Account account) {
        if (account != null && !accounts.containsKey(account.getAccountNumber())) {
            accounts.put(account.getAccountNumber(), account);
            return true;
        }
        return false;
    }
    
    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }
    
    public boolean accountExists(String accountNumber) {
        return accounts.containsKey(accountNumber);
    }
    
    public Map<String, Account> getAllAccounts() {
        return new HashMap<>(accounts);
    }
    
    public boolean removeAccount(String accountNumber) {
        return accounts.remove(accountNumber) != null;
    }
    
    public int getTotalAccounts() {
        return accounts.size();
    }
}