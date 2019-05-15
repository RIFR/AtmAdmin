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
            if (tempAccountList != null)
                cardList = tempCardList;

            Map<String, AtmHw> tempAtmHwList = FileIO.readObject(atmHwFile);
            if (tempAccountList != null)
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
                case "88":

                    Customer customer = new Customer("Kalle", "Anka", "189901011111", "kalle.anka@ankeborg.com", "KALLE ANKA");
                    customerList.put(customer.getKey(),customer);

                    Account account = new Account(customer, "0123456789012345", 50000.0, "Test account");
                    accountList.put(account.getKey(), account);

                    User user = new User("Super","User","007","admin@mybank.se","SuperUser","SuperUser");
                    userList.put(user.getKey(),user);

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
        try {
            StdIO.clearScreen();
            StdIO.writeLine("Scan Transaction");

            StdIO.writeLine("");
            StdIO.write("Account No    : ");
            String accountId = StdIO.readLine();
            if (!accountList.containsKey(accountId)) {
                StdIO.write("Barcode : ");
                String barcode = StdIO.readLine();
                for (Account x : accountList.values()){
                    if (x.getCustomer().getBarCode().equalsIgnoreCase(barcode))
                        accountId = x.getKey();
                }
            }

            StdIO.write("Deposit (y/n) : ");
            boolean deposit = StdIO.readYesOrNo();
            StdIO.write("Amount        : ");
            double money = Double.valueOf(StdIO.readLine());

            String fullName = registerTransaction(accountId,deposit,money);
            if (!fullName.isEmpty()) {
                StdIO.writeLine(fullName);
            }

        } catch (Exception e) {
            e.printStackTrace();
            StdIO.ErrorReport("Exception -" + e.toString());
        }
    }

    public String registerTransaction(String accountId, boolean deposit, double amount) {
        //System.out.println(accountId + (deposit ? " + ":" - ") +money);
        try {
            String returnStr = "";

            if (accountList.containsKey(accountId)) {
                Account account = accountList.get(accountId);

                if (!(deposit && account.getSaldo() < amount)) {

                    Transactions transactions = new Transactions(account, LocalDateTime.now(), deposit,amount);

                    transactionList.add(transactions);
                    FileIO.writeObject(transactionList, transactionFile);

                    if (deposit)
                        account.setSaldo(account.getSaldo() + amount);
                    else
                        account.setSaldo(account.getSaldo() - amount);

                    returnStr = account.getCustomer().getFullName() + " current saldo " +account.getSaldo();

                    accountList.put(account.getKey(),account);
                    FileIO.writeObject(accountList, accountFile);

                    return returnStr;

                }
            }
            return "Account with "+accountId+" not found";

        } catch (Exception e) {
            e.printStackTrace();
            StdIO.ErrorReport("Exception -" + e.toString());
            return "EXCEPTION: " +e;
        }
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
            StdIO.write("Card Id   : ");
            String cardId = StdIO.readLine();
            StdIO.write("Pincode   : ");
            String pincode = StdIO.readLine();

            LocalDate validThrou = LocalDateTime.now().plusYears(5).toLocalDate();
            StdIO.writeLine("ValidThrou "+validThrou.toString());

            StdIO.write("Account Id  : ");
            String accountID = StdIO.readLine().trim();
            if (accountList.containsKey(accountID)) {
                Card card = new Card(cardId,pincode,validThrou,accountList.get(accountID));

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
            StdIO.write("HasHwError (y/n): ");
            boolean hasHwError = StdIO.readYesOrNo();

            AtmHw atmHw = new AtmHw(mashineId,Double.valueOf(saldo),description,hasHwError);

            atmHwList.put(atmHw.getKey(),atmHw);

            FileIO.writeObject(atmHwList,atmHwFile);

        } catch (Exception e) {
            e.printStackTrace();
            StdIO.ErrorReport("Exception -" + e.toString());
        }
    }
}
