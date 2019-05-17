package se.rifr;

public class AtmHw implements java.io.Serializable {

    private String machineId;
    private double saldo;
    private String description;
    private String hwErrorCode;
    private double maxWithdraw;

    public AtmHw(String machineId, double saldo, String description, String hwErrorCode, double maxWithdraw) {

        this.machineId = machineId;
        this.saldo = saldo;
        this.description = description;
        this.hwErrorCode = hwErrorCode;
        this.maxWithdraw = maxWithdraw;
    }

    public String getKey() {
        return machineId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
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

    public String getHwErrorCode() {
        return hwErrorCode;
    }

    public void setHwErrorCode(String hwErrorCode) {
        this.hwErrorCode = hwErrorCode;
    }

    public double getMaxWithdraw() {
        return maxWithdraw;
    }

    public void setMaxWithdraw(double maxWithdraw) {
        this.maxWithdraw = maxWithdraw;
    }

    public boolean hasHwError() {
        return !(hwErrorCode.isEmpty() || hwErrorCode.equals("OK"));
    }

    public void changeSaldo(double amount, boolean deposit ) {
        saldo += (deposit ? amount : -amount);
    }

    @Override
    public String toString() {
        return "ATM HW{" +
                "MachineId='" + machineId + '\'' +
                "Saldo='" + saldo + '\'' +
                "Description='" + description + '\'' +
                "HwError='" + hwErrorCode + '\'' +
                "MaxWithdraw='" + maxWithdraw + '\'' +
                '}';
    }

    public static String toStringHeader() {
        String returnString;
        returnString  = Str.padRight("Machine Id",16);
        returnString += Str.padRight("Saldo",16);
        returnString += Str.padRight("HwErrorCode",14);
        returnString += Str.padRight("Description",40);
        returnString += Str.padRight("MaxWithdraw",20);
        returnString += "\r\n" + StdIO.ConsoleColors.BLUE + Str.pad('-',140)+ StdIO.ConsoleColors.RESET;
        return returnString;
    }

    public String toStringLine() {
        String returnString;
        returnString  = Str.padRight(getMachineId(),16);
        returnString += Str.padRight(Double.toString(getSaldo()),16);
        returnString += Str.padRight(hwErrorCode,14);
        returnString += Str.padRight(getDescription(),40);
        returnString += Str.padRight(Double.toString(getMaxWithdraw()),20);

        return returnString;
    }

}
