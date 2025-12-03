package models;

public class PremiumCustomer extends Customer {
    private static final double INTEREST_RATE = 0.035; // 3.5% annual interest
    
    public PremiumCustomer(String name, String customerId) {
        super(name, customerId);
    }
    
    @Override
    public double getInterestRate() {
        return INTEREST_RATE;
    }
}