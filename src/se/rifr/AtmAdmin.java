package se.rifr;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.LocalDateTime.now;

public class AtmAdmin {
    
    private Map<String, User> userList = new HashMap<>();
    private Map<String, Account> accountList = new HashMap<>();
    private Map<String, Customer> customerList = new HashMap<>();
    private List<Transactions> transactionList = new ArrayList<>();

    String dirName = "C:\\Dev\\AtmAdmin\\";
    
    String userFile        = dirName + "userlist.ser";
    String customerFile    = dirName + "customerlist.ser";
    String accountFile     = dirName + "accountlist.ser";
    String transactionFile = dirName + "transactionlist.ser";
    
    
    public AtmAdmin() {
        /*
        User user = new User("Marcus","Gyllencreutz","Marcus","marcus.gyllencreutz@lexicon.se","hemligt","007");
        userList.put(user.getKey(),user);
        FileIO.writeObject(userList,"userlist.ser");

        Account classroom = new Account(1,"Java","Java class");
        classroomList.put(classroom.getKey(), classroom);

        FileIO.writeObject(classroomList,"classroomlist.ser");
        Customer student = new Customer("Nisse",
                "Ghandi",
                "nisse",
                "nisse@ghandi.se","123456789",classroomList.get("1"));

        studentList.put(student.getKey(),student);
        FileIO.writeObject(studentList,"studentlist.ser");
*/
        LoadReloadData();

        //if (userList == null) {
        //    User user = new User("Super","User","SuperUser","admin@mybank.se","Super","007");
        //    userList.put(user.getKey(),user);
        //}
    }

    private void LoadReloadData () {
        
        userList        = FileIO.readObject(userFile);
        customerList    = FileIO.readObject(customerFile);
        accountList     = FileIO.readObject(accountFile);
        transactionList = FileIO.readObject(transactionFile);
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
            System.out.println(e);
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
                case "99":
                    listUsers(); System.out.println("");
                    listAccounts();System.out.println("");
                    listCustomers();System.out.println("");
                    listTransactions();System.out.println("");
                    break;
            }
        } while (!answer.equals("q"));

        if (accountList != null)
            FileIO.writeObject(accountList, accountFile);
        if (customerList != null)
            FileIO.writeObject(customerList, customerFile);
        if (transactionList != null)
            FileIO.writeObject(transactionList, transactionFile);
        if (userList != null)
            FileIO.writeObject(userList, userFile);
    }

    private String menu() {
        try {
            String answer;

            StdIO.clearScreen();
            StdIO.writeLine("Menu");
            StdIO.writeLine("");
            StdIO.writeLine("0. Reload");
            StdIO.writeLine("1. Maintain Customer");
            StdIO.writeLine("2. List Customer");
            StdIO.writeLine("3. Scan Transaction");
            StdIO.writeLine("4. List Transactions");
            StdIO.writeLine("5. Maintain users");
            StdIO.writeLine("6. List users");
            StdIO.writeLine("7. Maintain Accounts");
            StdIO.writeLine("8. List Accounts");
            StdIO.writeLine("");
            StdIO.writeLine("q. Exit");
            StdIO.writeLine("");
            StdIO.write("Select : ");
            answer = StdIO.readLine();
            return answer;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    private void scanTransaction() {
        try {
            StdIO.clearScreen();
            StdIO.writeLine("Scan Transaction");

            StdIO.writeLine("");
            StdIO.write("Barcode : ");
            String barcode = StdIO.readLine();
            StdIO.write("Deposit : ");
            boolean deposit = StdIO.readYesOrNo();
            StdIO.write("Money   : ");
            double money = Double.valueOf(StdIO.readLine());

            String fullName = registerTransaction(barcode,deposit,money);
            if (!fullName.isEmpty()) {
                StdIO.writeLine(fullName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String registerTransaction(String accountId, boolean deposit, double money) {
        System.out.println(accountId + (deposit ? " + ":" - ") +money);
        try {
            String returnStr = "";

            if (accountList.containsKey(accountId)) {
                Account account = accountList.get(accountId);

                if (!(deposit && account.getSaldo() < money)) {

                    Transactions transactions = new Transactions(account, LocalDateTime.now(), deposit,money);
                    transactionList.add(transactions);

                    if (deposit)
                        account.setSaldo(account.getSaldo() - money);
                    else
                        account.setSaldo(account.getSaldo() + money);

                    returnStr = account.getCustomer().getFullName() + " current saldo " +account.getSaldo();

                    FileIO.writeObject(transactionList, transactionFile);
                    return returnStr;

                }
            }
            return "Account with "+accountId+" not found";
        } catch (Exception e) {
            e.printStackTrace();
            return "EXCEPTION: " +e;
        }
    }

    //public String listTransactions() {
    //    return transactionList.stream().collect();
    //}

    private void listCustomers() {
        System.out.println(Customer.toStringHeader());
        customerList.forEach((k,v) -> System.out.println(v.toStringLine()));
    }
    private void maintainCustomer() {
        try {
            StdIO.clearScreen();
            //listCustomers();
            StdIO.writeLine("");
            StdIO.writeLine("Add a customer");
            StdIO.writeLine("");
            StdIO.write("First name : ");
            String firstName = StdIO.readLine();
            StdIO.write("Last name : ");
            String lastName = StdIO.readLine();
            StdIO.write("User name : ");
            String userName = StdIO.readLine();
            StdIO.write("email : ");
            String email = StdIO.readLine();
            StdIO.write("Barcode : ");
            String barcode = StdIO.readLine();
            Customer customer = new Customer(firstName, lastName, userName, email, barcode);
            customerList.put(customer.getKey(), customer);
            FileIO.writeObject(customerList,customerFile);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private void listTransactions() {
        transactionList.stream().forEach(item -> System.out.println());
    }

    private  void listUsers() {
        userList.forEach((k,v)-> System.out.println(v));
    }

    private void maintainUsers() {
        try {
            StdIO.clearScreen();
            listUsers();
            StdIO.writeLine("");
            StdIO.writeLine("Add a user");
            StdIO.writeLine("");
            StdIO.write("First name : ");
            String firstName = StdIO.readLine();
            StdIO.write("Last name : ");
            String lastName = StdIO.readLine();
            StdIO.write("User name : ");
            String userName = StdIO.readLine();
            StdIO.write("email : ");
            String email = StdIO.readLine();
            StdIO.write("Password : ");
            String password = StdIO.readLine();
            StdIO.write("Barcode : ");
            String barcode = StdIO.readLine();
            User user = new User(firstName,lastName,userName,email,password,barcode);
            userList.put(user.getKey(),user);
            FileIO.writeObject(userList,userFile);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void listAccounts() {
        accountList.forEach((k,v)-> System.out.println(v));
    }

    private void maintainAccounts() {
        try {
            Customer customer;

            StdIO.clearScreen();
            //listAccounts();

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
            StdIO.write("Account     : ");
            String accountID = StdIO.readLine().trim();
            StdIO.write("Saldo       : ");
            double saldo = Double.valueOf(StdIO.readLine());
            StdIO.write("Description : ");
            String description = StdIO.readLine();

            Account account = new Account(customer, accountID,saldo,description);
            accountList.put(account.getKey(), account);

            FileIO.writeObject(accountList,accountFile);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
