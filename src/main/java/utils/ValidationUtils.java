package utils;

public class ValidationUtils {
    
    /**
     * Validates if the given amount is positive
     * @param amount the amount to validate
     * @return true if amount is positive, false otherwise
     */
    public static boolean isValidAmount(double amount) {
        return amount > 0;
    }
    
    /**
     * Validates if the given account number format is correct
     * @param accountNumber the account number to validate
     * @return true if format is valid, false otherwise
     */
    public static boolean isValidAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return false;
        }
        
        // Simple validation: should start with "ACC" followed by digits
        return accountNumber.matches("^ACC\\d+$");
    }
    
    /**
     * Validates if the given customer name is not null or empty
     * @param name the customer name to validate
     * @return true if name is valid, false otherwise
     */
    public static boolean isValidCustomerName(String name) {
        return name != null && !name.trim().isEmpty();
    }
    
    /**
     * Validates if the given customer ID is not null or empty
     * @param customerId the customer ID to validate
     * @return true if customer ID is valid, false otherwise
     */
    public static boolean isValidCustomerId(String customerId) {
        return customerId != null && !customerId.trim().isEmpty();
    }
    
    /**
     * Validates if the given transaction type is valid
     * @param transactionType the transaction type to validate
     * @return true if transaction type is valid, false otherwise
     */
    public static boolean isValidTransactionType(String transactionType) {
        return transactionType != null && 
               (transactionType.equals("DEPOSIT") || 
                transactionType.equals("WITHDRAWAL") || 
                transactionType.equals("TRANSFER_IN") || 
                transactionType.equals("TRANSFER_OUT"));
    }
}