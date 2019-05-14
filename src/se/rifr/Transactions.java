package se.rifr;

import java.time.LocalDateTime;

public class Transactions implements java.io.Serializable{

    private Account account;
    private LocalDateTime time;
    private boolean deposit;  // deposit or withdraw
    private double amount;

    public Transactions(Account account, LocalDateTime time, boolean deposit, double amount) {
        this.account = account;
        this.time = time;
        this.deposit = deposit;
        this.amount = amount;
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

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return Str.padRight(account.getCustomer().getFullName(),30) +
              time.toLocalTime().toString().substring(0,8) + " " +
                (deposit ? "DEPOSIT   ":"WITHDRAW " + Str.padRight(Double.toString(amount),8));
    }

    public static String toStringHeader() {
        String returnString;
        returnString  = Str.padRight("Firstname",16);
        returnString += Str.padRight("Lastname",16);
        returnString += Str.padRight("Barcode",14);
        returnString += Str.padRight("Deposit/Withdraw",40);
        returnString += Str.padRight("Amount",20);
        returnString += Str.padRight("Time",20);
        return returnString;
    }

    public String toStringLine() {
        String returnString;
        returnString  = Str.padRight(getAccount().getCustomer().getFirstName(),16);
        returnString += Str.padRight(getAccount().getCustomer().getLastName(),16);
        returnString += Str.padRight(getAccount().getCustomer().getBarCode(),14);
        returnString += Str.padRight((isDeposit() ? "DEPOSIT":"WITHDRAW"),40);
        returnString += Str.padRight(Double.toString(getAmount()) + " (" + Double.toString(getAccount().getSaldo()) + ")",20);
        returnString += Str.padRight(getTime().toString().substring(0,19),20);
        return returnString;
    }

}
