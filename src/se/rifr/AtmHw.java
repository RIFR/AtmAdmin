package se.rifr;

public class AtmHw {

    private String machineId;
    private double saldo;
    private String description;
    private boolean hasHwError;

    public AtmHw(String machineId, double saldo, String description, boolean hasHwError) {
        this.machineId = machineId;
        this.saldo = saldo;
        this.description = description;
        this.hasHwError = hasHwError;
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

    public boolean isHasHwError() {
        return hasHwError;
    }

    public void setHasHwError(boolean hasHwError) {
        this.hasHwError = hasHwError;
    }

    @Override
    public String toString() {
        return "ATM HW{" +
                "MachineId='" + machineId + '\'' +
                ", Saldo='" + saldo + '\'' +
                ", Description='" + description + '\'' +
                ", HasHwError='" + (hasHwError ? "TRUE":"FALSE") + '\'' +
                '}';
    }

    public static String toStringHeader() {
        String returnString;
        returnString  = Str.padRight("Machine Id",16);
        returnString += Str.padRight("Saldo",16);
        returnString += Str.padRight("Description",40);
        returnString += Str.padRight("HasHwError",10);
        return returnString;
    }

    public String toStringLine() {
        String returnString;
        returnString  = Str.padRight(getMachineId(),16);
        returnString += Str.padRight(Double.toString(getSaldo()),16);
        returnString += Str.padRight(getDescription(),40);
        returnString += Str.padRight((isHasHwError() ? "ERROR":"OK"),10);
        return returnString;
    }

}
