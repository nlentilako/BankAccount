// Java
package com.bank.system;

import com.bank.system.manager.AccountManager;
import com.bank.system.manager.TransactionManager;
import com.bank.system.model.*;

import static com.bank.system.utils.ConsoleUtil.*;
import static com.bank.system.utils.ConsoleFormatter.*;

public class Main {
    private static final AccountManager accountManager = new AccountManager();
    private static final TransactionManager transactionManager = new TransactionManager();
    private static Customer customer;
    private static Account account;
    private static class CustomerData {
        private final String  name;
        private final int age;
        private final String contact;
        private final String address;

        public CustomerData(String name, int age, String contact, String address) {
            this.name = name;
            this.age = age;
            this.contact = contact;
            this.address = address;
        }
    }
    // a custom println method to displace text to the console

    public static void main(String[] args) {
        // Pre-populate with sample accounts for demonstration
        displayWelcomeMessage();
        initializeSampleData();
        boolean running = true;
        while (running) {
            displayMainMenu();

            int choice = getValidIntInput("Enter your choice: ", 1,5);
            running = processMenuChoice(choice);

        }

        shutdown();
        }
    private static boolean processMenuChoice(int choice) {
        switch (choice) {
            case 1 -> createAccount();
            case 2 -> viewAccounts();
            case 3 -> processTransaction();
            case 4 -> viewTransactionHistory();
            case 5 -> { return false; }

        }
        return true;
    }

    private static void createAccount() {
        print(" ");
        print("ACCOUNT CREATION");
        print(" ");

        CustomerData data = readCustomerDetails();
        customer = createCustomerFromData(data);

        print(" ");
        print("Account type:");
        print("1. Savings Account (Interest: 3.5% Min Balance: $500)");
        print("2. Checking Account (Overdraft: $1,000, Monthly Fee: $10)");

        int accountType = getValidIntInput("Select type (1-2): ", 1,2);
        double initialDeposit = getValidDoubleInput(
                "Enter initial deposit amount: $",
                v -> v > 0,
                "Value Must be greater than  zero."
        );

        if (accountType == 2) {
            account = new CheckingAccount(customer, initialDeposit);
        } else {
            account = new SavingsAccount(customer, initialDeposit);
        }

        if (accountManager.addAccount(account)) {
            displayAccountCreatedInfo(account, customer);
        } else {
            print("Failed to create account. Maximum account limit reached.");
        }
        print(" ");
        pressEnterToContinue();
    }

    private static void viewAccounts() {
        accountManager.viewAllAccounts();
    }
    private static void displayAccountCreatedInfo(Account account, Customer customer) {
            print(" ");
            print("✓ Account created successfully!");
            print("Account Number: " + account.getAccountNumber());
            print("Customer: " + customer.getName() + " (" + customer.getCustomerType() + ")");
            print("Account Type: " + account.getAccountType());
            printf("Initial Balance: $%.2f%n", account.getBalance());

            if (account instanceof SavingsAccount savings) {
                printf("Interest Rate: %.1f%%%n", savings.getInterestRate());
                printf("Minimum Balance: $%,.2f%n", savings.getMinimumBalance());
            } else if (account instanceof CheckingAccount checking) {
                printf("Overdraft Limit: $%,.2f%n", checking.getOverdraftLimit());
                if (customer instanceof PremiumCustomer) {
                    System.out.println("Monthly Fee: Waived (Premium Customer)");
                } else {
                    printf("Monthly Fee: $%,.2f%n", checking.getMonthlyFee());
                }
            }
            print("Status: " + account.getStatus());
        }

    private static void processTransaction() {
        print(" ");
        print("PROCESS TRANSACTION");
        printSubSeparator(60);
        print(" ");

        String accountNumber = readString("Enter Account Number: ",
                s -> !s.isEmpty(),
                "Account Number cannot be empty."
        );

        Account account = accountManager.findAccount(accountNumber);
        if (account == null) {
            print("Account not found.");
            pressEnterToContinue();
            return;
        }
        accountManager.displayAccountDetails(account);
        print(" ");
        print("Transaction type:");
        print("1. Deposit");
        print("2. Withdrawal");
        print(" ");

        int transactionType = getValidIntInput("Select type (1-2): ", 1,2);
        print(" ");

        String typeStr = (transactionType == 1) ? "DEPOSIT" : "WITHDRAWAL";

        double amount = getValidDoubleInput("Enter amount: $",
                v -> v > 0,
                "Amount must be greater than zero.");

        boolean success = false;
        double previousBalance = account.getBalance();

        success = account.processTransaction(amount, typeStr);

        if (!success) {
            handleFailedTransaction(transactionType, account);
            pressEnterToContinue();
            return;
        }

        // Create transaction record
        Transaction transaction = new Transaction(
                accountNumber,
                typeStr,
                amount,
                account.getBalance()
        );

        transactionManager.addTransaction(transaction);

        // Display transaction confirmation
        transaction.displayTransactionDetails(previousBalance);

        print(" ");

       boolean confirmed = readConfirmation("Confirm transaction?");
       handleTransactionConfirmation(confirmed, account, previousBalance);
       pressEnterToContinue();

    }
    private static void handleFailedTransaction(int transactionType, Account account) {
        if (transactionType == 1) {
            print("Deposit failed. Invalid amount.");
        } else {
            if (account instanceof SavingsAccount) {
                print("Withdrawal failed. Insufficient funds or would go below minimum balance.");
            } else {
                print("Withdrawal failed. Insufficient funds or exceeds overdraft limit.");
            }
        }
    }

    private static void handleTransactionConfirmation(boolean confirmed, Account account, double previousBalance) {
            if (confirmed) {
                print(" ");
                print("✓ Transaction completed successfully!");
            } else {
                // Rollback transaction
                account.setBalance(previousBalance); // works for deposit or withdrawal
                print(" ");
                print("Transaction cancelled.");
            }
    }
    public static void displayWelcomeMessage() {
        print("\nWelcome to the Bank Account Management System!");
        print("Please select an option from the menu below:");
    }

    private static Customer createCustomerFromData(CustomerData data) {

        print(" ");
        print("Customer type:");
        print("1. Regular Customer (Standard banking services)");
        print("2. Premium Customer (Enhanced benefits, min balance $10,000)");

        int customerType = getValidIntInput("Select type (1-2): ", 1, 2);

        if (customerType == 2) {
            return new PremiumCustomer(data.name, data.age, data.contact, data.address);
        } else {
            return new RegularCustomer(data.name, data.age, data.contact, data.address);
        }
    }

    private static CustomerData readCustomerDetails() {
        String name = readString("Enter Customer Name: ",
                s -> !s.isEmpty(),
                "Name cannot be empty.");

        int age = getValidIntInput("Enter customer age: ", 1, 150);

        String contact = readString("Enter customer contact: ",
                s -> !s.isEmpty(),
                "Contact cannot be empty.");

        String address = readString("Enter customer address: ",
                s -> !s.isEmpty(),
                "Address cannot be empty.");

        return new CustomerData(name, age, contact, address);
    }

    private static void viewTransactionHistory() {
        print(" ");
        print(underline("VIEW TRANSACTION HISTORY", '-'));
//        printSeparator();
        print(" ");

        String accountNumber = readString("Enter Account Number: ", s -> !s.isEmpty(), "Account Number cannot be empty.");
        Account account = accountManager.findAccount(accountNumber);
        transactionManager.viewTransactionsByAccount(accountNumber, account);
    }

    private static void initializeSampleData() {
        // Create sample customers
        Customer customer1 = new RegularCustomer("John Smith", 35, "+1-555-0101", "456 Elm Street, Metropolis");
        Customer customer2 = new RegularCustomer("Sarah Johnson", 28, "+1-555-0102", "789 Oak Avenue, Metropolis");
        Customer customer3 = new PremiumCustomer("Michael Chen", 42, "+1-555-0103", "321 Pine Road, Metropolis");
        Customer customer4 = new RegularCustomer("Emily Brown", 31, "+1-555-0104", "654 Maple Drive, Metropolis");
        Customer customer5 = new PremiumCustomer("David Wilson", 48, "+1-555-0105", "987 Cedar Lane, Metropolis");

        // Create sample accounts
        Account account1 = new SavingsAccount(customer1, 5250.00);
        Account account2 = new CheckingAccount(customer2, 3450.00);
        Account account3 = new SavingsAccount(customer3, 15750.00);
        Account account4 = new CheckingAccount(customer4, 890.00);
        Account account5 = new SavingsAccount(customer5, 25300.00);

        // Add accounts to the manager
        accountManager.addAccount(account1);
        accountManager.addAccount(account2);
        accountManager.addAccount(account3);
        accountManager.addAccount(account4);
        accountManager.addAccount(account5);



    }

    private static void displayMainMenu() {

        printHeader("BANK ACCOUNT MANAGEMENT SYSTEM - MAIN MENU");
        print("BANK ACCOUNT MANAGEMENT - MAIN MENU");
        print(" ");
        print("1. Create Account");
        print("2. View Accounts");
        print("3. Process Transaction");
        print("4. View Transaction History");
        print("5. Exit");
       print("");

  }

    private static void shutdown() {
        System.out.println("\nThank you for using Bank Account Management System!");
        System.out.println("Goodbye!");

    }
    public static void seedTransuctions(){
        Transaction transaction1 = new Transaction("ACC005", "DEPOSIT", 345.00, account.getBalance());
        Transaction transaction2 = new Transaction("ACC005", "DEPOSIT", 120.00, account.getBalance());
        Transaction transaction3 = new Transaction("ACC005", "DEPOSIT", 75.50, account.getBalance());
        Transaction transaction4 = new Transaction("ACC005", "DEPOSIT", 200.00, account.getBalance());
        Transaction transaction5 = new Transaction("ACC005", "WITHDRAWAL", 50.00, account.getBalance());
        transactionManager.addTransaction(transaction1);
        transactionManager.addTransaction(transaction2);
        transactionManager.addTransaction(transaction3);
        transactionManager.addTransaction(transaction4);
        transactionManager.addTransaction(transaction5);
    }

}





