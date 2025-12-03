package models;

public class RegularCustomer extends Customer {
    private static final double INTEREST_RATE = 0.02; // 2% annual interest
    
    public RegularCustomer(String name, String customerId) {
        super(name, customerId);
    }
    
    @Override
    public double getInterestRate() {
        return INTEREST_RATE;
    }
}