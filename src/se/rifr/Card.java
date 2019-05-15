package se.rifr;

import java.time.LocalDate;

public class Card implements java.io.Serializable {

    private String  cardId;
    private String  pinCode;
    private LocalDate validThrou;
    private Account account;

    public Card(String cardId, String pinCode, LocalDate validThrou, Account account) {
        this.cardId = cardId;
        this.pinCode = pinCode;
        this.validThrou = validThrou;
        this.account = account;
    }

    public String getKey() {
        return cardId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public LocalDate getValidThrou() {
        return validThrou;
    }

    public void setValidThrou(LocalDate validThrou) {
        this.validThrou = validThrou;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Card{" +
                "CardId='" + cardId + '\'' +
                "Pincode='" + pinCode + '\'' +
                "ValidThrou='" + validThrou + '\'' +
                "Saldo='" + getAccount().getSaldo() + '\'' +
                "Name='" + getAccount().getCustomer().getFullName() + '\'' +
                '}';
    }

    public static String toStringHeader() {
        String returnString;
        returnString  = Str.padRight("Card Id",16);
        returnString += Str.padRight("Pincode",16);
        returnString += Str.padRight("ValidThrou",14);
        returnString += Str.padRight("Name",40);
        returnString += Str.padRight("Saldo",20);
        return returnString;
    }

    public String toStringLine() {
        String returnString;
        returnString  = Str.padRight(getCardId(),16);
        returnString += Str.padRight(getPinCode(),16);
        returnString += Str.padRight(getValidThrou().toString(),14);
        returnString += Str.padRight(getAccount().getCustomer().getFullName(),40);
        returnString += Str.padRight(Double.toString(getAccount().getSaldo()),20);
        return returnString;
    }

}
