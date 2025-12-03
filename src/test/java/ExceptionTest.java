import models.*;
import services.*;
import exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExceptionTest {
    private AccountManager accountManager;
    private TransactionManager transactionManager;
    private RegularCustomer customer;
    
    @BeforeEach
    void setUp() {
        accountManager = new AccountManager();
        transactionManager = new TransactionManager(accountManager);
        customer = new RegularCustomer("John Smith", "CUST001");
    }
    
    @Test
    void testInvalidAmountExceptionMessage() {
        InvalidAmountException exception = new InvalidAmountException("Test error message");
        assertEquals("Test error message", exception.getMessage());
    }
    
    @Test
    void testInsufficientFundsExceptionMessage() {
        InsufficientFundsException exception = new InsufficientFundsException("Test error message");
        assertEquals("Test error message", exception.getMessage());
    }
    
    @Test
    void testOverdraftExceededExceptionMessage() {
        OverdraftExceededException exception = new OverdraftExceededException("Test error message");
        assertEquals("Test error message", exception.getMessage());
    }
    
    @Test
    void testSavingsAccountWithdrawBelowMinimumThrowsException() {
        SavingsAccount account = new SavingsAccount("ACC001", 150.0, customer); // Just above minimum balance
        accountManager.addAccount(account);
        
        // Attempt to withdraw an amount that would bring the balance below the minimum
        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class, () -> {
            transactionManager.withdraw("ACC001", 100.0); // This should fail because it would bring balance below $100 minimum
        });
        
        // Verify the exception message contains relevant information
        String message = exception.getMessage();
        assertTrue(message.contains("Insufficient funds"));
        assertTrue(message.contains("$150.00")); // Current balance
        assertTrue(message.contains("$100.00")); // Requested amount
        assertTrue(message.contains("$100.00")); // Minimum required
    }
    
    @Test
    void testCheckingAccountOverdraftExceedThrowsException() {
        CheckingAccount account = new CheckingAccount("ACC002", 200.0, customer);
        accountManager.addAccount(account);
        
        // Attempt to withdraw an amount that exceeds the overdraft limit
        OverdraftExceededException exception = assertThrows(OverdraftExceededException.class, () -> {
            transactionManager.withdraw("ACC002", 800.0); // This exceeds the $500 overdraft limit
        });
        
        // Verify the exception message contains relevant information
        String message = exception.getMessage();
        assertTrue(message.contains("Overdraft limit exceeded"));
        assertTrue(message.contains("$200.00")); // Current balance
        assertTrue(message.contains("$800.00")); // Requested amount
        assertTrue(message.contains("$500.00")); // Overdraft limit
    }
    
    @Test
    void testDepositNegativeAmountThrowsInvalidAmountException() throws InvalidAmountException {
        SavingsAccount account = new SavingsAccount("ACC001", 1000.0, customer);
        accountManager.addAccount(account);
        
        double initialBalance = account.getBalance();
        
        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> {
            transactionManager.deposit("ACC001", -100.0);
        });
        
        assertEquals("Deposit amount must be greater than 0", exception.getMessage());
        assertEquals(initialBalance, account.getBalance(), 0.01); // Balance should remain unchanged
    }
    
    @Test
    void testWithdrawNegativeAmountThrowsInvalidAmountException() throws InsufficientFundsException, OverdraftExceededException {
        SavingsAccount account = new SavingsAccount("ACC001", 1000.0, customer);
        accountManager.addAccount(account);
        
        double initialBalance = account.getBalance();
        
        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> {
            transactionManager.withdraw("ACC001", -50.0);
        });
        
        assertEquals("Withdrawal amount must be greater than 0", exception.getMessage());
        assertEquals(initialBalance, account.getBalance(), 0.01); // Balance should remain unchanged
    }
    
    @Test
    void testTransferNegativeAmountThrowsInvalidAmountException() throws InsufficientFundsException, OverdraftExceededException {
        SavingsAccount fromAccount = new SavingsAccount("ACC001", 1000.0, customer);
        CheckingAccount toAccount = new CheckingAccount("ACC002", 500.0, customer);
        
        accountManager.addAccount(fromAccount);
        accountManager.addAccount(toAccount);
        
        double fromInitialBalance = fromAccount.getBalance();
        double toInitialBalance = toAccount.getBalance();
        
        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> {
            transactionManager.transfer("ACC001", "ACC002", -100.0);
        });
        
        assertEquals("Transfer amount must be greater than 0", exception.getMessage());
        assertEquals(fromInitialBalance, fromAccount.getBalance(), 0.01); // Balances should remain unchanged
        assertEquals(toInitialBalance, toAccount.getBalance(), 0.01);
    }
    
    @Test
    void testInvalidAccountNumberThrowsException() throws InvalidAmountException {
        // Try to deposit to a non-existent account
        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> {
            transactionManager.deposit("ACC999", 500.0);
        });
        
        assertEquals("Account not found: ACC999", exception.getMessage());
    }
    
    @Test
    void testInvalidSourceAccountInTransferThrowsException() throws InvalidAmountException, InsufficientFundsException, OverdraftExceededException {
        CheckingAccount toAccount = new CheckingAccount("ACC002", 500.0, customer);
        accountManager.addAccount(toAccount);
        
        // Try to transfer from a non-existent account
        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> {
            transactionManager.transfer("ACC999", "ACC002", 100.0);
        });
        
        assertEquals("Source account not found: ACC999", exception.getMessage());
    }
    
    @Test
    void testInvalidDestinationAccountInTransferThrowsException() throws InvalidAmountException, InsufficientFundsException, OverdraftExceededException {
        SavingsAccount fromAccount = new SavingsAccount("ACC001", 1000.0, customer);
        accountManager.addAccount(fromAccount);
        
        // Try to transfer to a non-existent account
        InvalidAmountException exception = assertThrows(InvalidAmountException.class, () -> {
            transactionManager.transfer("ACC001", "ACC999", 100.0);
        });
        
        assertEquals("Destination account not found: ACC999", exception.getMessage());
    }
}