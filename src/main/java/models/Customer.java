package models;

public abstract class Customer {
    protected String name;
    protected String customerId;
    
    public Customer(String name, String customerId) {
        this.name = name;
        this.customerId = customerId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public abstract double getInterestRate();
    
    @Override
    public String toString() {
        return String.format("Customer: %s (ID: %s)", name, customerId);
    }
}