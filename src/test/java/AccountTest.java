import models.*;
import exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {
    private RegularCustomer customer;
    private PremiumCustomer premiumCustomer;
    
    @BeforeEach
    void setUp() {
        customer = new RegularCustomer("John Smith", "CUST001");
        premiumCustomer = new PremiumCustomer("Jane Doe", "CUST002");
    }
    
    @Test
    void testSavingsAccountDeposit() throws InvalidAmountException {
        SavingsAccount account = new SavingsAccount("ACC001", 1000.0, customer);
        double initialBalance = account.getBalance();
        
        account.deposit(500.0);
        
        assertEquals(initialBalance + 500.0, account.getBalance(), 0.01);
    }
    
    @Test
    void testSavingsAccountDepositInvalidAmount() {
        SavingsAccount account = new SavingsAccount("ACC001", 1000.0, customer);
        double initialBalance = account.getBalance();
        
        assertThrows(InvalidAmountException.class, () -> {
            account.deposit(-100.0);
        });
        
        assertThrows(InvalidAmountException.class, () -> {
            account.deposit(0.0);
        });
        
        assertEquals(initialBalance, account.getBalance(), 0.01); // Balance should remain unchanged
    }
    
    @Test
    void testSavingsAccountWithdraw() throws InsufficientFundsException, InvalidAmountException, OverdraftExceededException {
        SavingsAccount account = new SavingsAccount("ACC001", 1000.0, customer);
        double initialBalance = account.getBalance();
        
        boolean success = account.withdraw(200.0);
        
        assertTrue(success);
        assertEquals(initialBalance - 200.0 - 2.0, account.getBalance(), 0.01); // 2.0 is withdrawal fee
    }
    
    @Test
    void testSavingsAccountWithdrawInvalidAmount() {
        SavingsAccount account = new SavingsAccount("ACC001", 1000.0, customer);
        double initialBalance = account.getBalance();
        
        assertThrows(InvalidAmountException.class, () -> {
            account.withdraw(-50.0);
        });
        
        assertThrows(InvalidAmountException.class, () -> {
            account.withdraw(0.0);
        });
        
        assertEquals(initialBalance, account.getBalance(), 0.01); // Balance should remain unchanged
    }
    
    @Test
    void testSavingsAccountWithdrawInsufficientFunds() {
        SavingsAccount account = new SavingsAccount("ACC001", 100.0, customer); // Just above minimum balance
        
        assertThrows(InsufficientFundsException.class, () -> {
            account.withdraw(50.0); // This would bring balance below minimum
        });
    }
    
    @Test
    void testCheckingAccountDeposit() throws InvalidAmountException {
        CheckingAccount account = new CheckingAccount("ACC002", 1000.0, premiumCustomer);
        double initialBalance = account.getBalance();
        
        account.deposit(300.0);
        
        assertEquals(initialBalance + 300.0, account.getBalance(), 0.01);
    }
    
    @Test
    void testCheckingAccountDepositInvalidAmount() {
        CheckingAccount account = new CheckingAccount("ACC002", 1000.0, premiumCustomer);
        double initialBalance = account.getBalance();
        
        assertThrows(InvalidAmountException.class, () -> {
            account.deposit(-200.0);
        });
        
        assertThrows(InvalidAmountException.class, () -> {
            account.deposit(0.0);
        });
        
        assertEquals(initialBalance, account.getBalance(), 0.01); // Balance should remain unchanged
    }
    
    @Test
    void testCheckingAccountWithdrawWithinOverdraft() throws InsufficientFundsException, InvalidAmountException, OverdraftExceededException {
        CheckingAccount account = new CheckingAccount("ACC002", 200.0, premiumCustomer);
        double initialBalance = account.getBalance();
        
        boolean success = account.withdraw(600.0); // Within $500 overdraft limit
        
        assertTrue(success);
        assertEquals(initialBalance - 600.0, account.getBalance(), 0.01);
    }
    
    @Test
    void testCheckingAccountWithdrawExceedsOverdraft() {
        CheckingAccount account = new CheckingAccount("ACC002", 200.0, premiumCustomer);
        
        assertThrows(OverdraftExceededException.class, () -> {
            account.withdraw(800.0); // Exceeds $500 overdraft limit
        });
    }
    
    @Test
    void testSavingsAccountMinimumBalance() {
        SavingsAccount account = new SavingsAccount("ACC001", 1000.0, customer);
        assertEquals(100.0, account.getMinimumBalance());
    }
    
    @Test
    void testCheckingAccountOverdraftLimit() {
        CheckingAccount account = new CheckingAccount("ACC002", 1000.0, premiumCustomer);
        assertEquals(500.0, account.getOverdraftLimit());
    }
    
    @Test
    void testCustomerInterestRates() {
        assertEquals(0.02, customer.getInterestRate());
        assertEquals(0.035, premiumCustomer.getInterestRate());
    }
}