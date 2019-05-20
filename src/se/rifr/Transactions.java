package se.rifr;

import java.time.LocalDateTime;

public class Transactions implements java.io.Serializable{

    private Account account;
    private LocalDateTime time;
    private boolean deposit;  // deposit or withdraw
    private double amount;
    private AtmHw atmHw;

    public Transactions(Account account, LocalDateTime time, boolean deposit, double amount, AtmHw atmHw) {
        this.account = account;
        this.time = time;
        this.deposit = deposit;
        this.amount = amount;
        this.atmHw = atmHw;
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

    public AtmHw getAtmHw() { return atmHw; }

    @Override
    public String toString() {
        return Str.padRight(account.getCustomer().getFullName(),30) +
              time.toLocalTime().toString().substring(0,8) + " " +
                (deposit ? "DEPOSIT   ":"WITHDRAW " + Str.padRight(Double.toString(amount),8) + " " +
                        atmHw.getMachineId());
    }

    public static String toStringHeader() {
        String returnString;
        returnString  = Str.padRight("Firstname",16);
        returnString += Str.padRight("Lastname",16);
        returnString += Str.padRight("Barcode",14);
        returnString += Str.padRight("Deposit/Withdraw",40);
        returnString += Str.padRight("Amount",20);
        returnString += Str.padRight("Time",20);
        returnString += Str.padRight("Machine",20);
        returnString += "\r\n" + StdIO.ConsoleColors.BLUE + Str.pad('-',140)+ StdIO.ConsoleColors.RESET;
        return returnString;
    }

    public String toStringLine() {
        String returnString;
        returnString  = Str.padRight(getAccount().getCustomer().getFirstName(),16);
        returnString += Str.padRight(getAccount().getCustomer().getLastName(),16);
        returnString += Str.padRight(getAccount().getCustomer().getBarCode(),14);
        returnString += Str.padRight((isDeposit() ? "DEPOSIT":"WITHDRAW"),40);
        returnString += Str.padRight(Double.toString(getAmount()),20);
        returnString += Str.padRight(getTime().toString().substring(0,19),20);
        returnString += Str.padRight(getAtmHw().getMachineId(),20);
        return returnString;
    }

}
