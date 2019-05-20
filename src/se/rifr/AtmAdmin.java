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
                if (loginOk(userName,password)) {
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

    public boolean loginOk(String username, String password) {
        return (userList.containsKey(username) && userList.get(username).getPassword().equals(password));
    }

    public boolean cardCodeOk(String cardId, String pincode) {
        return cardList.containsKey(cardId) && cardList.get(cardId).getPinCode().equals(pincode);
    }

    public boolean cardOk(Card card) {
        return (!card.isWithDrawn() && card.getValidThrou().compareTo(LocalDateTime.now().toLocalDate()) >= 0);
    }

    public boolean cardOk(String cardId) {
        return (cardList.containsKey(cardId) && cardOk(cardList.get(cardId)));
    }

    public boolean cardSaldoOk(Card card, double WithdrawAmount) {
        return (card.getAccount().getSaldo() >= WithdrawAmount ) &&
                (WithdrawsLastWeek (card) + WithdrawAmount) <= card.getMaxPerWeek();
    }

    public boolean cardSaldoOk(String cardId, double WithdrawAmount) {
        return (cardList.containsKey(cardId)) && cardSaldoOk (cardList.get(cardId),WithdrawAmount);
    }

    public double WithdrawsLastWeek (Card card){
        double discount = 0.0d;
        LocalDateTime oneWeek = LocalDateTime.now().minusWeeks(1);
        for (Transactions x : transactionList) {
            if (x.getAccount().getKey() == card.getAccount().getKey() && (x.getTime().compareTo(oneWeek) > 0)) {
                discount += (x.isDeposit() ? -x.getAmount():x.getAmount());
            }
        }
        //System.out.println("WithdrawsLastWeek " + card.getCardId() + " " +discount);
        return discount;
    }

    public boolean atmMachineOk(AtmHw atmHw) {
        return (!atmHw.hasHwError());
    }

    public boolean atmMachineOk(String atmHwId) {
        return (atmHwList.containsKey(atmHwId))&& atmMachineOk(atmHwList.get(atmHwId));
    }

    public boolean atmMachinSaldoOk(AtmHw atwhw, boolean deposit, double amount) {
        return (!atwhw.hasHwError() && atwhw.getMaxWithdraw()>= amount &&
                (deposit || atwhw.getSaldo() >= amount ));
    }

    public boolean atmMachinSaldoOk(String atmHwId, boolean deposit, double amount) {
        return (atmHwList.containsKey(atmHwId)) && atmMachinSaldoOk (atmHwList.get(atmHwId),deposit,amount);
    }

    public Account getAccount(String cardId) {
        return cardList.get(cardId).getAccount();
    }

    public AtmHw getAtmHw(String atmHwId) {
        return atmHwList.get(atmHwId);
    }

    public double getAtmSaldo(String atmHwId) {
        return atmHwList.get(atmHwId).getSaldo();
    }

    public double getSaldo(String cardId) {
        return cardList.get(cardId).getAccount().getSaldo();
    }

    public Card getCard(String cardId) {
        return cardList.get(cardId);
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

                    break;
                case "89":
                    System.out.println(registerTransaction(getAtmHw("AtmTest001"),getAccount("000000000000001"), false, 500.0));
                    break;
                case "90":
                    System.out.println(registerTransaction(getAtmHw("AtmTest001"),getAccount("000000000000001"), true, 2000.0));
                    break;
                case "91":
                    System.out.println(WithdrawsLastWeek(getCard ("000000000000001")));
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

            if (!atmMachineOk(atmHwId)){
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

            if (!cardOk(card.getCardId())) {
                StdIO.ErrorReport("The card is not accepted ");
                return;
            }

            StdIO.write("Deposit (y/n) : ");
            boolean deposit = StdIO.readYesOrNo();

            StdIO.write("Amount        : ");
            double amount = Double.valueOf(StdIO.readLine());

            if (!atmMachinSaldoOk(atmHwId,deposit,amount)) {
                StdIO.ErrorReport("The ATM HW rejected your request ");
                return;
            }

            String res = registerFullTransaction(atmHwId,cardId,deposit,amount);
            System.out.println(res);

        } catch (Exception e) {
            e.printStackTrace();
            StdIO.ErrorReport("Exception -" + e.toString());
        }
    }

    public String registerFullTransaction(String atmHwId, String CardId, boolean deposit, double amount) {

        AtmHw   atmhw   = atmHwList.get(atmHwId);
        Card    card    = cardList.get(CardId);
        Account account = accountList.get(card.getAccount().getAccountId());

        if (!atmMachineOk(atmhw)) return "";
        if (!atmMachinSaldoOk(atmhw,deposit,amount)) return "";
        if (!cardOk(card)) return "";
        if (!cardSaldoOk(card,amount)) return "";


        String res = registerTransaction(atmhw, account,deposit,amount);
        if (res.isEmpty()) return "";
        else {
            card.setAccount(account);
            cardList.put(card.getKey(),card);
            FileIO.writeObject(cardList, cardFile);

            registerAtmTransaction(atmhw,deposit,amount);

        }

        return res;
    }

    public void registerAtmTransaction(AtmHw atmHw, boolean deposit, double amount) {

        try {
            atmHw.changeSaldo(amount,deposit);
            atmHwList.put(atmHw.getKey(),atmHw);
            FileIO.writeObject(atmHwList, atmHwFile);

        } catch (Exception e) {
            e.printStackTrace();
            StdIO.ErrorReport("Exception -" + e.toString());
       }
    }

    public String registerTransaction(AtmHw atmHw, Account account, boolean deposit, double amount) {

        String returnStr = "";
        try {

            if (!(deposit && account.getSaldo() < amount)) {

                Transactions transactions = new Transactions(account, LocalDateTime.now(), deposit,amount,atmHw);

                transactionList.add(transactions);
                FileIO.writeObject(transactionList, transactionFile);

                System.out.println("Before " +account.getSaldo() +" "+ deposit);
                account.changeSaldo(amount,deposit);
                System.out.println("After " +account.getSaldo() +" amount"+ amount);

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

            updateAccountdWithUpdatedCustomer (customer); // update if exists in accounts

        } catch (Exception e) {
            e.printStackTrace();
            StdIO.ErrorReport("Exception -" + e.toString());
        }
    }

    private void updateAccountdWithUpdatedCustomer (Customer customer) {
        for (Account x : accountList.values()) {
            if (x.getCustomer().getKey() == customer.getKey()){

                x.setCustomer(customer);
                accountList.put(x.getKey(),x);

                updateCardWithUpdatedAccount(x);
            }
        }
        FileIO.writeObject(accountList, accountFile);
    }

    private void updateCardWithUpdatedAccount (Account account) {
        for (Card x : cardList.values()) {
            if (x.getKey() == account.getKey()){

                x.setAccount(account);
                cardList.put(x.getKey(),x);
            }
        }
        FileIO.writeObject(cardList, cardFile);
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
        Account account;
        double saldo;
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
            StdIO.write("Description : ");
            String description = StdIO.readLine();

            if (accountList.containsKey(accountID)) {
                account = accountList.get(accountID);
                account.setDescription(description);
                if (account.getCustomer().getBarCode() != customer.getBarCode()) {
                    StdIO.ErrorReport("The customer can not be changed");
                    return;
                } else
                    account.setCustomer(customer);

            } else {
                StdIO.write("Saldo       : ");
                saldo = Double.valueOf(StdIO.readLine());

                account = new Account(customer, accountID, saldo, description);
            }

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
