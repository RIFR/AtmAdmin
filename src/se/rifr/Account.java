package se.rifr;

public class Account implements java.io.Serializable{

    private Customer customer;
    private int accountId;
    private double saldo;
    private String description;

    public Account(Customer customer, int accountId, double saldo, String description) {
        this.customer = customer;
        this.accountId = accountId;
        this.saldo = saldo;
        this.description = description;
    }

    public String getKey() {
        return Integer.toString(accountId).trim();
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Account{" + Str.padRight(customer.getFullName(),30) +
                "accountId=" + Str.padLeft(Integer.toString(accountId).trim(),15) +
                ", saldo=" + Str.padLeft(Double.toString(saldo).trim(),10) +
                ", description='" + description + '\'' +
                '}';
    }
}
