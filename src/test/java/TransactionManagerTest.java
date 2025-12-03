import models.*;
import services.*;
import exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionManagerTest {
    private AccountManager accountManager;
    private TransactionManager transactionManager;
    private RegularCustomer customer;
    private PremiumCustomer premiumCustomer;
    
    @BeforeEach
    void setUp() {
        accountManager = new AccountManager();
        transactionManager = new TransactionManager(accountManager);
        customer = new RegularCustomer("John Smith", "CUST001");
        premiumCustomer = new PremiumCustomer("Jane Doe", "CUST002");
    }
    
    @Test
    void testDepositUpdatesBalance() throws InvalidAmountException {
        SavingsAccount account = new SavingsAccount("ACC001", 1000.0, customer);
        accountManager.addAccount(account);
        
        double initialBalance = account.getBalance();
        transactionManager.deposit("ACC001", 500.0);
        
        assertEquals(initialBalance + 500.0, account.getBalance(), 0.01);
        assertEquals(1, account.getTransactions().size());
        assertEquals("DEPOSIT", account.getTransactions().get(0).getType());
    }
    
    @Test
    void testDepositInvalidAccount() {
        assertThrows(InvalidAmountException.class, () -> {
            transactionManager.deposit("ACC999", 500.0);
        });
    }
    
    @Test
    void testDepositInvalidAmount() throws InvalidAmountException {
        SavingsAccount account = new SavingsAccount("ACC001", 1000.0, customer);
        accountManager.addAccount(account);
        
        double initialBalance = account.getBalance();
        
        assertThrows(InvalidAmountException.class, () -> {
            transactionManager.deposit("ACC001", -100.0);
        });
        
        assertThrows(InvalidAmountException.class, () -> {
            transactionManager.deposit("ACC001", 0.0);
        });
        
        assertEquals(initialBalance, account.getBalance(), 0.01); // Balance should remain unchanged
        assertEquals(0, account.getTransactions().size()); // No transactions should be recorded
    }
    
    @Test
    void testWithdrawUpdatesBalance() throws InsufficientFundsException, InvalidAmountException, OverdraftExceededException {
        SavingsAccount account = new SavingsAccount("ACC001", 1000.0, customer);
        accountManager.addAccount(account);
        
        double initialBalance = account.getBalance();
        transactionManager.withdraw("ACC001", 200.0);
        
        assertEquals(initialBalance - 200.0 - 2.0, account.getBalance(), 0.01); // 2.0 is withdrawal fee
        assertEquals(1, account.getTransactions().size());
        assertEquals("WITHDRAWAL", account.getTransactions().get(0).getType());
    }
    
    @Test
    void testWithdrawInvalidAccount() {
        assertThrows(InvalidAmountException.class, () -> {
            transactionManager.withdraw("ACC999", 200.0);
        });
    }
    
    @Test
    void testWithdrawInvalidAmount() throws InsufficientFundsException, InvalidAmountException, OverdraftExceededException {
        SavingsAccount account = new SavingsAccount("ACC001", 1000.0, customer);
        accountManager.addAccount(account);
        
        double initialBalance = account.getBalance();
        
        assertThrows(InvalidAmountException.class, () -> {
            transactionManager.withdraw("ACC001", -50.0);
        });
        
        assertThrows(InvalidAmountException.class, () -> {
            transactionManager.withdraw("ACC001", 0.0);
        });
        
        assertEquals(initialBalance, account.getBalance(), 0.01); // Balance should remain unchanged
        assertEquals(0, account.getTransactions().size()); // No transactions should be recorded
    }
    
    @Test
    void testWithdrawInsufficientFunds() throws InsufficientFundsException, InvalidAmountException, OverdraftExceededException {
        SavingsAccount account = new SavingsAccount("ACC001", 150.0, customer); // Just above minimum balance
        accountManager.addAccount(account);
        
        assertThrows(InsufficientFundsException.class, () -> {
            transactionManager.withdraw("ACC001", 100.0); // This would bring balance below minimum
        });
    }
    
    @Test
    void testTransferBetweenAccounts() throws InsufficientFundsException, InvalidAmountException, OverdraftExceededException {
        SavingsAccount fromAccount = new SavingsAccount("ACC001", 1000.0, customer);
        CheckingAccount toAccount = new CheckingAccount("ACC002", 500.0, premiumCustomer);
        
        accountManager.addAccount(fromAccount);
        accountManager.addAccount(toAccount);
        
        double fromInitialBalance = fromAccount.getBalance();
        double toInitialBalance = toAccount.getBalance();
        
        transactionManager.transfer("ACC001", "ACC002", 300.0);
        
        // Check balances after transfer
        assertEquals(fromInitialBalance - 300.0, fromAccount.getBalance(), 0.01);
        assertEquals(toInitialBalance + 300.0, toAccount.getBalance(), 0.01);
        
        // Check transaction records
        assertEquals(1, fromAccount.getTransactions().size());
        assertEquals(1, toAccount.getTransactions().size());
        assertEquals("TRANSFER_OUT", fromAccount.getTransactions().get(0).getType());
        assertEquals("TRANSFER_IN", toAccount.getTransactions().get(0).getType());
    }
    
    @Test
    void testTransferInvalidSourceAccount() throws InsufficientFundsException, InvalidAmountException, OverdraftExceededException {
        CheckingAccount toAccount = new CheckingAccount("ACC002", 500.0, premiumCustomer);
        accountManager.addAccount(toAccount);
        
        assertThrows(InvalidAmountException.class, () -> {
            transactionManager.transfer("ACC999", "ACC002", 100.0);
        });
    }
    
    @Test
    void testTransferInvalidDestinationAccount() throws InsufficientFundsException, InvalidAmountException, OverdraftExceededException {
        SavingsAccount fromAccount = new SavingsAccount("ACC001", 1000.0, customer);
        accountManager.addAccount(fromAccount);
        
        assertThrows(InvalidAmountException.class, () -> {
            transactionManager.transfer("ACC001", "ACC999", 100.0);
        });
    }
    
    @Test
    void testTransferInvalidAmount() throws InsufficientFundsException, InvalidAmountException, OverdraftExceededException {
        SavingsAccount fromAccount = new SavingsAccount("ACC001", 1000.0, customer);
        CheckingAccount toAccount = new CheckingAccount("ACC002", 500.0, premiumCustomer);
        
        accountManager.addAccount(fromAccount);
        accountManager.addAccount(toAccount);
        
        double fromInitialBalance = fromAccount.getBalance();
        double toInitialBalance = toAccount.getBalance();
        
        assertThrows(InvalidAmountException.class, () -> {
            transactionManager.transfer("ACC001", "ACC002", -100.0);
        });
        
        assertThrows(InvalidAmountException.class, () -> {
            transactionManager.transfer("ACC001", "ACC002", 0.0);
        });
        
        // Balances should remain unchanged
        assertEquals(fromInitialBalance, fromAccount.getBalance(), 0.01);
        assertEquals(toInitialBalance, toAccount.getBalance(), 0.01);
        
        // No transactions should be recorded
        assertEquals(0, fromAccount.getTransactions().size());
        assertEquals(0, toAccount.getTransactions().size());
    }
    
    @Test
    void testGetTransactionsForAccount() throws InvalidAmountException, InsufficientFundsException, OverdraftExceededException {
        SavingsAccount account = new SavingsAccount("ACC001", 1000.0, customer);
        accountManager.addAccount(account);
        
        transactionManager.deposit("ACC001", 200.0);
        transactionManager.withdraw("ACC001", 100.0);
        
        assertEquals(2, transactionManager.getTransactionsForAccount("ACC001").size());
        assertEquals(0, transactionManager.getTransactionsForAccount("ACC999").size());
    }
}