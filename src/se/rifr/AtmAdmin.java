package se.rifr;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class AtmAdmin {

    private Map<String, User>         userList = new HashMap<>();
    private Map<String, Account>   accountList = new HashMap<>();
    private Map<String, Customer> customerList = new HashMap<>();
    private List<Transactions> transactionList = new ArrayList<>();
    private Map<String, Card>         cardList = new HashMap<>();
    private Map<String, AtmHw>       atmHwList = new HashMap<>();

    String dirName = "C:\\Dev\\AtmAdmin\\";

    String userFile        = dirName + "userlist.ser";
    String customerFile    = dirName + "customerlist.ser";
    String accountFile     = dirName + "accountlist.ser";
    String transactionFile = dirName + "transactionlist.ser";
    String cardFile        = dirName + "cardlist.ser";
    String atmHwFile       = dirName + "atmhwlist.ser";

    
    public AtmAdmin() {

        LoadReloadData();

        //if (userList == null) {
        User user = new User("Super","User","007","admin@mybank.se","SuperUser","SuperUser");
        userList.put(user.getKey(),user);
        //FileIO.writeObject(userList, userFile);
        //}
    }


    private void LoadReloadData () {

        try{
            Map<String, User> tempUserList = FileIO.readObject(userFile);
            if (tempUserList != null)
                userList = tempUserList;

            Map<String, Customer> tempCustomerList = FileIO.readObject(customerFile);
            if (tempCustomerList != null)
                customerList = tempCustomerList;

            Map<String, Account> tempAccountList = FileIO.readObject(accountFile);
            if (tempAccountList != null)
                accountList = tempAccountList;

            List<Transactions> tempTransactionList= FileIO.readObject(transactionFile);
            if (tempTransactionList != null)
                transactionList = tempTransactionList;

            Map<String, Card> tempCardList = FileIO.readObject(cardFile);
            if (tempCardList != null)
                cardList = tempCardList;

            Map<String, AtmHw> tempAtmHwList = FileIO.readObject(atmHwFile);
            if (tempAtmHwList != null)
                atmHwList = tempAtmHwList;

        } catch (Exception e) {
            e.printStackTrace();
            StdIO.ErrorReport("Exception -" + e.toString());
        }
    }

    private boolean login() {

        try {
            int noLogins = 0;
            while (noLogins < 3) {
                StdIO.clearScreen();
                StdIO.writeLine("");
                StdIO.writeLine("Logon");
                StdIO.writeLine("");
                StdIO.write("Username : ");
                String userName = StdIO.readLine();
                StdIO.write("Password : ");
                String password = StdIO.readLine();
                noLogins++;
                if (!checkLogin(userName,password).isEmpty()) {
                    return true;
                }
            }
            //System.exit(0);
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            StdIO.ErrorReport("Exception -" + e.toString());
        }
        return false;
    }

    public String checkLogin(String username, String password) {
        String returnStr = "";

        if (userList.containsKey(username) && userList.get(username).getPassword().equals(password))
            returnStr = userList.get(username).getUserName();
        return returnStr;
    }

    public String checkCode(String cardId, String pincode) {
        String returnStr = "";

        if (cardList.containsKey(cardId) && cardList.get(cardId).getPinCode().equals(pincode))
            returnStr = cardList.get(cardId).getValidThrou().toString();
        return returnStr;
    }

    public String checkCard(String cardId) {
        String returnStr = "";

        if (cardList.containsKey(cardId) && !cardList.get(cardId).isWithDrawn() &&
                cardList.get(cardId).getValidThrou().compareTo(LocalDateTime.now().toLocalDate()) >= 0 )
            returnStr = cardList.get(cardId).getValidThrou().toString();
        return returnStr;
    }

    public String checkSaldo(String cardId, double WithdrawAmount) {
        String returnStr = "";

        if (cardList.containsKey(cardId) && cardList.get(cardId).getAccount().getSaldo() >= WithdrawAmount )
            returnStr = cardList.get(cardId).getValidThrou().toString();
        return returnStr;
    }

    public String checkMachine(String atmHwId) {
        String returnStr = "";

        if (atmHwList.containsKey(atmHwId) && !atmHwList.get(atmHwId).hasHwError() )
            returnStr = Double.toString(atmHwList.get(atmHwId).getSaldo());
        return returnStr;
    }

    public String checkMachineSaldo(String atmHwId, boolean deposit, double amount) {
        String returnStr = "";

        if (atmHwList.containsKey(atmHwId) && !atmHwList.get(atmHwId).hasHwError() &&
                atmHwList.get(atmHwId).getMaxWithdraw()>= amount &&
                (deposit || atmHwList.get(atmHwId).getSaldo() >= amount) )
            returnStr = Double.toString(atmHwList.get(atmHwId).getSaldo());
        return returnStr;
    }

    public Account getAccount(String cardId) {
        return cardList.get(cardId).getAccount();
    }

    public double getSaldo(String atmHwId) {
        return atmHwList.get(atmHwId).getSaldo();
    }

    public void start() {

        if (!login()) return;

        String answer = "";
        do {
            answer =menu();
            try{
            switch (answer) {
                case "0":
                    LoadReloadData();
                    break;
                case "1":
                    maintainCustomer();
                    break;
                case "2":
                    listCustomers();
                    break;
                case "3":
                    scanTransaction();
                    break;
                case "4":
                    listTransactions();
                    break;
                case "5":
                    maintainUsers();
                    break;
                case "6":
                    listUsers();
                    break;
                case "7":
                    maintainAccounts();
                    break;
                case "8":
                    listAccounts();
                    break;
                case "9":
                    copyCustomerDataToUser();
                    break;
                case "10":
                    maintainCards();
                    break;
                case "11":
                    listCards();
                    break;
                case "12":
                    maintainAtmMachines();
                    break;
                case "13":
                    listAtmMachines();
                    break;
                case "14":
                    deleteUser();
                    break;
                case "15":
                    excludeOrOpenCard(true);
                    break;
                case "16":
                    excludeOrOpenCard(false);
                    break;
                case "88":

                    Customer customer = new Customer("Kalle", "Anka", "189901011111", "kalle.anka@ankeborg.com", "KALLEANKA");
                    customerList.put(customer.getKey(),customer);

                    User user = new User(customer.getFirstName(),customer.getLastName(),customer.getBarCode(),customer.getEmail(),customer.getUserName(),"KalleAnkaÄrBäst");
                    userList.put(user.getKey(),user);

                    Account account = new Account(customer, "12345678901234567890", 50000.0, "Kalles head account");
                    accountList.put(account.getKey(), account);

                    Card card = new Card("000000000000001","12345",LocalDateTime.now().plusYears(2).toLocalDate(),account,20000.0);
                    cardList.put(card.getKey(), card);

                    AtmHw atmHw1 = new AtmHw("AtmTest001",Double.valueOf(200000.0),"Test ATM","",5000.0);
                    atmHwList.put(atmHw1.getKey(),atmHw1);

                    AtmHw atmHw2 = new AtmHw("AtmTest002",Double.valueOf(2000.0),"Test ATM","",5000.0);
                    atmHwList.put(atmHw2.getKey(),atmHw2);

                    AtmHw atmHw3 = new AtmHw("AtmTest003",Double.valueOf(200000.0),"Test ATM","CARD-READER 4",5000.0);
                    atmHwList.put(atmHw3.getKey(),atmHw3);

                    System.out.println(checkCard(card.getCardId()));
                    break;
                case "89":
                    System.out.println(registerTransaction(getAccount("12345678901234567890"), false, 500.0));
                    break;
                case "90":
                    System.out.println(registerTransaction(getAccount("12345678901234567890"), true, 2000.0));
                    break;
                case "99":
                    listUsers(); System.out.println("");
                    listAccounts();System.out.println("");
                    listCustomers();System.out.println("");
                    listTransactions();System.out.println("");
                    listCards();System.out.println("");
                    listAtmMachines();System.out.println("");
                    break;
            }
            } catch (Exception e) {
                e.printStackTrace();
                StdIO.ErrorReport("Exception -" + e.toString());
            }
        } while (!answer.equalsIgnoreCase("q"));

        if (accountList != null)
            FileIO.writeObject(accountList, accountFile);
        if (customerList != null)
            FileIO.writeObject(customerList, customerFile);
        if (transactionList != null)
            FileIO.writeObject(transactionList, transactionFile);
        if (userList != null)
            FileIO.writeObject(userList, userFile);
        if (cardList != null)
            FileIO.writeObject(cardList,cardFile);
        if (atmHwList != null)
            FileIO.writeObject(atmHwList,atmHwFile);
    }

    private String menu() {
        try {
            String answer;

            StdIO.clearScreen();
            StdIO.writeLine("");
            StdIO.writeLine("Menu");
            StdIO.writeLine("");
            StdIO.writeLine(" 0. Reload");
            StdIO.writeLine(" 1. Maintain Customer");
            StdIO.writeLine(" 2. List Customer");
            StdIO.writeLine(" 3. Scan Transaction");
            StdIO.writeLine(" 4. List Transactions");
            StdIO.writeLine(" 5. Maintain users");
            StdIO.writeLine(" 6. List users");
            StdIO.writeLine(" 7. Maintain Accounts");
            StdIO.writeLine(" 8. List Accounts");
            StdIO.writeLine(" 9. Create user from customer");
            StdIO.writeLine("10. Maintain cards");
            StdIO.writeLine("11. List cards");
            StdIO.writeLine("12. Maintain ATM machines");
            StdIO.writeLine("13. List ATM machines");
            StdIO.writeLine("14. Delete user");
            StdIO.writeLine("15. Exclude Card ");
            StdIO.writeLine("16. Open Card ");
            StdIO.writeLine("");
            StdIO.writeLine("q. Exit");
            StdIO.writeLine("");
            StdIO.write("Select : ");
            answer = StdIO.readLine();
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
            StdIO.ErrorReport("Exception -" + e.toString());
            return null;
        }
    }

    private void scanTransaction() {
        Card card;
        try {
            StdIO.clearScreen();
            StdIO.writeLine("Scan Transaction");

            StdIO.write("AtmHw ID      : ");
            String atmHwId = StdIO.readLine();
            if (!atmHwList.containsKey(atmHwId)){
                StdIO.ErrorReport("Unknown ATM mashine "+atmHwId);
                return;
            }

            if (atmHwList.get(atmHwId).hasHwError()){
                StdIO.ErrorReport("ATM mashine HW problem: "+atmHwId + " "+atmHwList.get(atmHwId).getHwErrorCode());
                return;
            }

            StdIO.write("Card ID       : ");
            String cardId = StdIO.readLine();
            if (!cardList.containsKey(cardId)){
                StdIO.ErrorReport("Unknown Card " +cardId);
                return;
            }
            card = cardList.get(cardId);

            if (!Str.readAcceptedValue("Pin code      : ",card.getPinCode(),3)){
                StdIO.ErrorReport("Unknown Pin Code, max number of tries reached ");
                return;
            };

            if (checkCard(card.getCardId()).isEmpty()) {
                StdIO.ErrorReport("The card is not accepted ");
                return;
            }

            StdIO.write("Deposit (y/n) : ");
            boolean deposit = StdIO.readYesOrNo();

            StdIO.write("Amount        : ");
            double amount = Double.valueOf(StdIO.readLine());

            if (checkMachineSaldo(atmHwId,deposit,amount).isEmpty()) {
                StdIO.ErrorReport("The ATM HW rejected your request ");
                return;
            }

            String fullName = registerTransaction(card.getAccount(),deposit,amount);
            if (!fullName.isEmpty()) {
                StdIO.writeLine(fullName);

                registerAtmTransaction(atmHwId,deposit,amount);

            }

        } catch (Exception e) {
            e.printStackTrace();
            StdIO.ErrorReport("Exception -" + e.toString());
        }
    }

    public String registerAtmTransaction(String atmHwId, boolean deposit, double amount) {

        try {
            if (atmHwList.containsKey(atmHwId)) {
                AtmHw atmHw = atmHwList.get(atmHwId);

                atmHw.changeSaldo(amount,deposit);
                atmHwList.put(atmHw.getKey(),atmHw);
                FileIO.writeObject(atmHwList, atmHwFile);

                return atmHw.getMachineId();
            } else
                StdIO.ErrorReport("UNKNOWN AtmHwId: " +atmHwId);

        } catch (Exception e) {
            e.printStackTrace();
            StdIO.ErrorReport("Exception -" + e.toString());
       }
        return ""; //Not OK
    }

    public String registerTransaction(Account account, boolean deposit, double amount) {
        //System.out.println(accountId + (deposit ? " + ":" - ") +money);
        String returnStr = "";
        try {

            if (!(deposit && account.getSaldo() < amount)) {

                Transactions transactions = new Transactions(account, LocalDateTime.now(), deposit,amount);

                transactionList.add(transactions);
                FileIO.writeObject(transactionList, transactionFile);

                account.changeSaldo(amount,deposit);

                returnStr = account.getCustomer().getFullName() + " current saldo " +account.getSaldo();

                accountList.put(account.getKey(),account);
                FileIO.writeObject(accountList, accountFile);

                return returnStr;

            }

        } catch (Exception e) {
            e.printStackTrace();
            StdIO.ErrorReport("Exception -" + e.toString());
            return "EXCEPTION: " +e;
        }

        return returnStr;
    }

    //private String TransactionToString (Transactions trans) {
    //    return trans.toString();
    //}
    public String listAllTransactions() {
        String str = "";
        for (Transactions x : transactionList){
            str += x.toString() +"\r\n";
        }
        return str;
    }

    private void listCustomers() {
        System.out.println(Customer.toStringHeader());
        if (customerList != null)
            customerList.forEach((k,v) -> System.out.println(v.toStringLine()));
    }
    private void maintainCustomer() {
        try {
            StdIO.clearScreen();

            StdIO.writeLine("");
            StdIO.writeLine("Add/update customer");
            StdIO.writeLine("");

            StdIO.write("First name: ");
            String firstName = StdIO.readLine();
            StdIO.write("Last name : ");
            String lastName = StdIO.readLine();
            StdIO.write("Barcode   : ");
            String barcode = StdIO.readLine();
            StdIO.write("email     : ");
            String email = StdIO.readLine();
            StdIO.write("User name : ");
            String userName = StdIO.readLine();

            Customer customer = new Customer(firstName, lastName, barcode, email, userName);

            customerList.put(customer.getKey(), customer);

            FileIO.writeObject(customerList, customerFile);

        } catch (Exception e) {
            e.printStackTrace();
            StdIO.ErrorReport("Exception -" + e.toString());
        }
    }

    private void listTransactions() {
        System.out.println(Transactions.toStringHeader());
        if (transactionList != null)
            transactionList.stream().forEach(item -> System.out.println(item.toStringLine()));
    }

    private void listUsers() {
        System.out.println(User.toStringHeader());
        if (userList != null)
            userList.forEach((k,v) -> System.out.println(v.toStringLine()));
    }

    private void maintainUsers() {
        try {
            StdIO.clearScreen();

            StdIO.writeLine("");
            StdIO.writeLine("Add a user");
            StdIO.writeLine("");
            StdIO.write("First name: ");
            String firstName = StdIO.readLine();
            StdIO.write("Last name : ");
            String lastName = StdIO.readLine();
            StdIO.write("Barcode   : ");
            String barcode = StdIO.readLine();
            StdIO.write("email     : ");
            String email = StdIO.readLine();
            StdIO.write("User name : ");
            String userName = StdIO.readLine();
            StdIO.write("Password  : ");
            String password = StdIO.readLine();
            User user = new User(firstName,lastName,barcode,email,userName,password);

            userList.put(user.getKey(),user);

            FileIO.writeObject(userList,userFile);

        } catch (Exception e) {
            e.printStackTrace();
            StdIO.ErrorReport("Exception -" + e.toString());
        }
    }

    private void copyCustomerDataToUser () {

        try {

            Customer customer;
            StdIO.write("Barcode   : ");
            String barcode = StdIO.readLine().trim();

            if (customerList.containsKey(barcode))
                customer = customerList.get(barcode);
            else {
                StdIO.ErrorReport("Unknown Customer " +barcode);
                return;
            }

            String userName = customer.getUserName();
            while (userList.containsKey(userName)){
                StdIO.ErrorReport("User Name " + customer.getUserName() + " already exists, enter new");
                userName = StdIO.readLine().trim();
            }

            customer.setUserName(userName);
            customerList.put(customer.getKey(),customer);
            FileIO.writeObject(customerList,customerFile);

            StdIO.write("Password   : ");
            String password = StdIO.readLine().trim();

            User user = new User(customer.getFirstName(),customer.getLastName(),customer.getBarCode(),customer.getEmail(),userName,password);
            userList.put(user.getKey(),user);
            FileIO.writeObject(userList,userFile);

        } catch (Exception e) {
            e.printStackTrace();
            StdIO.ErrorReport("Exception -" + e.toString());
        }
    }
    private void deleteUser() {
        try {
            StdIO.write("User Name : ");
            String userName = StdIO.readLine();
            if (userList.containsKey(userName)) {
                User user = userList.get(userName);
                StdIO.writeLine("Confirm deletion for "
                        +user.getFirstName()+" " +user.getLastName() + " "+user.getBarcode()+ "(y/n)");
                if (StdIO.readYesOrNo()) {
                    userList.remove(userName);
                    FileIO.writeObject(userList,userFile);
                }
            } else
                StdIO.ErrorReport("Unknown User Name");

        } catch (Exception e) {
            e.printStackTrace();
            StdIO.ErrorReport("Exception -" + e.toString());
        }
    }


    private void listAccounts() {
        System.out.println(Account.toStringHeader());
        if (accountList != null)
            accountList.forEach((k,v) -> System.out.println(v.toStringLine()));
     }

    private void maintainAccounts() {
        try {
            Customer customer;

            StdIO.clearScreen();

            StdIO.writeLine("");
            StdIO.writeLine("Add an Account");

            StdIO.writeLine("");
            StdIO.writeLine("Customer barcode");
            String barcode = StdIO.readLine();
            if (customerList.containsKey(barcode))
                customer = customerList.get(barcode);
            else {
                System.out.println("Unknown customer " +barcode);
                return;
            }

            StdIO.writeLine("");
            StdIO.write("Account Id  : ");
            String accountID = StdIO.readLine().trim();
            StdIO.write("Saldo       : ");
            double saldo = Double.valueOf(StdIO.readLine());
            StdIO.write("Description : ");
            String description = StdIO.readLine();

            Account account = new Account(customer, accountID, saldo, description);
            accountList.put(account.getKey(), account);

            FileIO.writeObject(accountList, accountFile);

        } catch (Exception e) {
            e.printStackTrace();
            StdIO.ErrorReport("Exception -" + e.toString());
        }
    }

    private void listCards() {
        System.out.println(Card.toStringHeader());
        if (cardList != null)
            cardList.forEach((k,v) -> System.out.println(v.toStringLine()));
    }

    private void maintainCards() {
        try {
            StdIO.clearScreen();

            StdIO.writeLine("");
            StdIO.writeLine("Maintain Cards");
            StdIO.writeLine("");
            StdIO.write("Card Id     : ");
            String cardId = StdIO.readLine();
            StdIO.write("Pincode     : ");
            String pincode = StdIO.readLine();

            LocalDate validThrou = LocalDateTime.now().plusYears(5).toLocalDate();
            StdIO.writeLine("ValidThrou "+validThrou.toString());

            StdIO.write("Account Id  : ");
            String accountID = StdIO.readLine().trim();

            StdIO.write("Max per week: ");
            String maxPerWeek = StdIO.readLine().trim();

            if (accountList.containsKey(accountID)) {
                Card card = new Card(cardId,pincode,validThrou,accountList.get(accountID),Double.valueOf(maxPerWeek));

                cardList.put(card.getKey(),card);

                FileIO.writeObject(cardList,cardFile);
            } else {
                StdIO.ErrorReport("Unknown Account Id " +accountID);
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            StdIO.ErrorReport("Exception -" + e.toString());
        }
    }

    private void excludeOrOpenCard (boolean exclude) {

        try {
            StdIO.write("Card Id     : ");
            String cardId = StdIO.readLine();

            if (!cardList.containsKey(cardId)) {
                StdIO.ErrorReport("Unknown Card " +cardId);
                return;
            }

            Card card = cardList.get(cardId);

            card.setWithDrawn(exclude);
            cardList.put(card.getKey(),card);

            FileIO.writeObject(cardList,cardFile);

        } catch (Exception e) {
            e.printStackTrace();
            StdIO.ErrorReport("Exception -" + e.toString());
        }
    }

    private void listAtmMachines() {
        System.out.println(AtmHw.toStringHeader());
        if (atmHwList != null)
            atmHwList.forEach((k,v) -> System.out.println(v.toStringLine()));
    }

    private void maintainAtmMachines() {
        try {
            StdIO.clearScreen();

            StdIO.writeLine("");
            StdIO.writeLine("Maintain an ATM mashine");
            StdIO.writeLine("");
            StdIO.write("MachineId       : ");
            String mashineId = StdIO.readLine();
            StdIO.write("Saldo           : ");
            String saldo = StdIO.readLine();
            StdIO.write("Description     : ");
            String description = StdIO.readLine();
            StdIO.write("HwErrorCode     : ");
            String hwErrorCode = StdIO.readLine();
            StdIO.write("Max withdraw    : ");
            String maxWithdraw = StdIO.readLine();

            AtmHw atmHw = new AtmHw(mashineId,Double.valueOf(saldo),description,hwErrorCode,Double.valueOf(maxWithdraw));

            atmHwList.put(atmHw.getKey(),atmHw);

            FileIO.writeObject(atmHwList,atmHwFile);

        } catch (Exception e) {
            e.printStackTrace();
            StdIO.ErrorReport("Exception -" + e.toString());
        }
    }
}
