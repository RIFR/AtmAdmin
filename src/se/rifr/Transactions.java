package se.rifr;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Transactions implements java.io.Serializable{

    private Account account;
    private LocalDateTime time;
    private boolean deposit;  // deposit or withdraw
    private double money;

    public Transactions(Account account, LocalDateTime time, boolean deposit, double money) {
        this.account = account;
        this.time = time;
        this.deposit = deposit;
        this.money = money;
    }

    //public String getKey () {
    //    return account.getKey() + time.toString();
    //}

    public Account getAccount() {
        return account;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public boolean isDeposit() {
        return deposit;
    }

    public double getMoney() {
        return money;
    }

    public String toString() {
        return Str.padRight(account.getCustomer().getFullName(),30) +
              time.toLocalTime().toString().substring(0,8) + " " +
                (deposit ? "DEPOSIT   ":"WITHDRAW " + Str.padRight(Double.toString(money),8));
    }
}
