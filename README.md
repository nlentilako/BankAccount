# Bank Account Management System

## Project Overview

A comprehensive console-based banking application that demonstrates Object-Oriented Programming principles in Java. The system allows bank staff to manage customer accounts, process transactions, and view account and transaction history.
A comprehensive console-based banking application that demonstrates Object-Oriented Programming principles in Java. The system allows bank staff to manage customer accounts, process transactions, and view account and transaction history.
## Features
- View transaction history for specific accounts
- Create new bank accounts with customer details
- View all accounts with detailed information
- Process deposits and withdrawals
- View transaction history for specific accounts
- Support for different account types (Savings and Checking)
- Support for different customer types (Regular and Premium)
- Menu-driven interface for easy navigation
- **Abstraction**: Abstract classes and interfaces
## OOP Concepts Implemented

- **Encapsulation**: Private fields with public getters/setters
- **Inheritance**: Account and Customer hierarchies
- **Abstraction**: Abstract classes and interfaces
- **Polymorphism**: Method overriding
- **Composition**: Manager classes that contain collections of objects
- `CheckingAccount`: Inherits from Account, includes overdraft limit and monthly fee
## Classes Structure
- `Customer` (abstract): Base class for all customers
### Core Classes
- `Account` (abstract): Base class for all accounts
- `SavingsAccount`: Inherits from Account, includes interest rate and minimum balance
- `CheckingAccount`: Inherits from Account, includes overdraft limit and monthly fee

### Customer Classes
- `Customer` (abstract): Base class for all customers
- `RegularCustomer`: Standard banking services
- `PremiumCustomer`: Enhanced benefits with waived fees

### Transaction Classes
- `Transaction`: Records transaction details
- `Transactable` (interface): Defines transaction contract

### Management Classes
- `AccountManager`: Manages collection of accounts
- `TransactionManager`: Manages collection of transactions

### Main Application
- `Main`: Console application with menu system

## Setup Instructions

1. **Compile all Java files:**
   ```bash
   javac *.java
   ```

2. **Run the application:**
   ```bash
   java Main
   ```

