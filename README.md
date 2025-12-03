# Bank Account Management System

A console-based bank account management system built with Java 21 that demonstrates clean code practices, exception handling, and unit testing.

## Features

- **Account Management**: Create and manage savings and checking accounts
- **Transaction Processing**: Deposit, withdrawal, and transfer operations
- **Exception Handling**: Robust error handling with custom exceptions
- **Statement Generation**: Detailed account statements with transaction history
- **Testing**: Comprehensive JUnit 5 test coverage
- **Git Integration**: Version control with feature branching and cherry-picking

## Project Structure

```
bank-account-management-system/
├── src/
│   ├── Main.java
│   ├── models/
│   │   ├── Account.java
│   │   ├── SavingsAccount.java
│   │   ├── CheckingAccount.java
│   │   ├── Customer.java
│   │   ├── RegularCustomer.java
│   │   ├── PremiumCustomer.java
│   │   └── Transaction.java
│   ├── exceptions/
│   │   ├── InvalidAmountException.java
│   │   ├── InsufficientFundsException.java
│   │   └── OverdraftExceededException.java
│   ├── services/
│   │   ├── AccountManager.java
│   │   ├── TransactionManager.java
│   │   └── StatementGenerator.java
│   └── utils/
│       └── ValidationUtils.java
├── src/test/java/
│   ├── AccountTest.java
│   ├── TransactionManagerTest.java
│   └── ExceptionTest.java
├── docs/
│   └── git-workflow.md
└── README.md
```

## Technology Stack

- Java 21 (LTS)
- JUnit 5 for testing
- Git for version control
- Maven-compatible structure (can be imported into IDEs)

## Setup Instructions

### Prerequisites
- Java 21 or higher
- Git

### Building and Running

1. Clone the repository:
```bash
git clone <repository-url>
cd bank-account-management-system
```

2. Compile the project:
```bash
javac -d out src/main/java/*.java src/main/java/models/*.java src/main/java/exceptions/*.java src/main/java/services/*.java src/main/java/utils/*.java
```

3. Run the application:
```bash
java -cp out Main
```

### Running Tests

To run the unit tests, you'll need JUnit 5 in your classpath:

```bash
# Compile test files
javac -cp "out:lib/junit-platform-console-standalone-1.9.2.jar" -d out \
  src/test/java/*.java

# Run tests
java -cp "out:lib/junit-platform-console-standalone-1.9.2.jar" \
  org.junit.platform.console.ConsoleLauncher --classpath "out" --select-package tests
```

## Git Workflow

This project demonstrates proper Git workflows:

1. **Feature Branching**: Each feature developed in isolated branches
2. **Cherry-picking**: Selective application of commits across branches
3. **Descriptive Commits**: Clear, meaningful commit messages

See [Git Workflow Documentation](docs/git-workflow.md) for detailed commands.

## Testing Coverage

The system includes comprehensive JUnit tests:

- **AccountTest**: Tests for account creation, deposits, and withdrawals
- **TransactionManagerTest**: Tests for all transaction operations
- **ExceptionTest**: Tests for exception handling scenarios

## Usage Examples

### Main Menu
```
BANK ACCOUNT MANAGEMENT SYSTEM

Main Menu:
---
1. Manage Accounts
2. Perform Transactions
3. Generate Account Statements
4. Run Tests
5. Exit

Enter your choice: _
```

### Error Handling
The system gracefully handles invalid inputs:
- Invalid account numbers
- Negative amounts
- Insufficient funds
- Overdraft limit exceeded

## Clean Code Practices

- **Meaningful Names**: Clear, descriptive variable and method names
- **Single Responsibility**: Each class and method has a single, well-defined purpose
- **Exception Handling**: Proper try-catch blocks and custom exceptions
- **Modular Design**: Separation of concerns across different service classes
- **Documentation**: JavaDoc comments for public methods

## Architecture

- **Models**: Account, Customer, and Transaction entities
- **Services**: Business logic for account management and transactions
- **Exceptions**: Custom exception classes for specific error scenarios
- **Utils**: Helper classes for validation and common operations

## Version Control Best Practices

- Feature branching for isolated development
- Regular commits with descriptive messages
- Cherry-picking for selective code integration
- Merge strategies for combining features

