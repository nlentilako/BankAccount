package com.bank.system.model;

import static com.bank.system.utils.ConsoleUtil.printf;
import com.bank.system.exceptions.InsufficientFundsException;
import com.bank.system.exceptions.InvalidAmountException;

public class SavingsAccount extends Account  {
    private final double interestRate;
    private final double minimumBalance;

    public SavingsAccount(Customer customer, double initialBalance) {
        super(customer, initialBalance);
        this.interestRate = 3.5; // 3.5% annually
        this.minimumBalance = 50.0; // $50 minimum balance
    }

    @Override
    public void displayAccountDetails() {
        printf("%-8s | %-15s | %-9s | $%,-9.2f | %-8s%n",
                getAccountNumber(),
                getCustomer().getName(),
                getAccountType(),
                getBalance(),
                getStatus());
        printf("%-8s | Interest Rate: %.1f%% | Min Balance: $,%.2f%n",
                "",
                interestRate,
                minimumBalance);
    }

    @Override
    public String getAccountType() {
        return "Savings";
    }

    @Override
    public boolean withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new InvalidAmountException("Invalid amount. Amount must be greater than 0.");
        }

        // Check if withdrawal would bring balance below minimum
        if (getBalance() - amount < minimumBalance) {
            throw new InsufficientFundsException(
                String.format("Withdrawal failed. Insufficient funds. Current balance: $%.2f, Minimum balance: $%.2f", 
                             getBalance(), minimumBalance));
        }

        setBalance(getBalance() - amount);
        return true;
    }

    // Method to calculate interest
    public double calculateInterest() {
        return getBalance() * (interestRate / 100);
    }

    // Getters
    public double getInterestRate() {
        return interestRate;
    }

    public double getMinimumBalance() {
        return minimumBalance;
    }
    @Override
    public boolean processTransaction(double amount, String type) throws InvalidAmountException, InsufficientFundsException {
        if (type.equalsIgnoreCase("DEPOSIT")) {
            return deposit(amount);
        } else if (type.equalsIgnoreCase("WITHDRAWAL")) {
            return withdraw(amount);
        }
        return false;
    }
}