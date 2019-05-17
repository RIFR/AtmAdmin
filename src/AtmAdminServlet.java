import se.rifr.AtmAdmin;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AtmAdminServlet extends HttpServlet {

    private String title = "ATM Transactions";
    AtmAdmin atmAdmin = new AtmAdmin();

    String AtmHwId  = "AtmTest001";
    String CardId   = "";
    boolean deposit = false;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String pos = request.getParameter("pos");

        String message = "";
        if (pos == null )
            message = cardLoginScr(); //loginScr();
        else {
            System.out.println(pos);
            switch (pos) {
                case "m":
                    message = inputDepositWithdrawMeny ();
                    break;
                case "0":
                    AtmHwId = request.getParameter("atmid");
                    if (atmAdmin.checkMachine(AtmHwId).isEmpty())
                        message = cardLoginScr();
                    else
                        message = cardLogin(request.getParameter("cardid"),request.getParameter("pincode"));
                    break;
                case "1":
                    deposit = (request.getParameter("deposit").equals("1"));
                    message = depositWithdrawAmountMenu();

                    break;
                case "2":
                    String opt = request.getParameter("option");
                    message  = "<form action = \"AtmAdminServlet\" method = \"GET\">\n";
                    if (opt.equals("8"))
                        message += "    <input type = \"hidden\" name = \"pos\" value = \"3\">\n";
                    else {
                        message += "    <input type = \"hidden\" name = \"pos\" value = \"4\">\n";
                        message += "    <input type = \"hidden\" name = \"amount\" value = \""+getAmount(opt)+"\">\n";
                    }
                    message += "</form>";
                    break;
                case "3":
                    message = getAmountMenu();
                    break;
                case "4":
                    message  = "<form action = \"AtmAdminServlet\" method = \"GET\">\n";
                    message += "    <input type = \"hidden\" name = \"pos\" value = \"9\">\n";
                    message += "    <h1> "+atmAdmin.registerTransaction(atmAdmin.getAccount(CardId),deposit,Double.valueOf(request.getParameter("amount")))+" </h1>\n";
                    message += "    <br />";
                    message += "    <input type = \"submit\" value = \"Submit\" />\n";
                    message += "</form>";
                    break;
                case "9":
                    message = cardLoginScr(); //loginScr();
                    break;
            }
        }

        // Set response content type
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String docType =
                "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";

        out.println(docType +
                "<html>\n" +
                "<head><title>" + title + "</title></head>\n" +
                "<body bgcolor = \"#f0f0f0\">\n" +
                "<h1 align = \"center\">" + title + "</h1>\n" +
                message+
                "</body>" +
                "</html>"
        );
    }

    private String cardLoginScr() {
        String returnStr;
        returnStr  = "<form action = \"AtmAdminServlet\" method = \"GET\">\n";
        returnStr += "    <input type = \"hidden\" name = \"pos\" value = \"0\">\n";
        returnStr += "    ATM No : <input type = \"text\" name = \"atmid\">\n";
        returnStr += "    <br />";
        returnStr += "    Card No : <input type = \"text\" name = \"cardid\">\n";
        returnStr += "    <br />";
        returnStr += "    Pin Code:  <input type = \"password\" name = \"pincode\">\n";
        returnStr += "    <br />";
        returnStr += "    <input type = \"submit\" value = \"Submit\" />\n";
        returnStr += "</form>";
        title = "Login";
        return returnStr;
    }

    private String cardLogin(String cardid, String pincode) {
        String returnStr;
        String uname = atmAdmin.checkCode(cardid, pincode);
        if (uname.isEmpty() || atmAdmin.checkCode(cardid, pincode).isEmpty()) {
            returnStr = cardLoginScr();
        } else
            CardId = cardid;

            returnStr  = "<form action = \"AtmAdminServlet\" method = \"GET\">\n";
            returnStr += "    <input type = \"hidden\" name = \"pos\" value = \"m\">\n";
            returnStr += "    <input type = \"hidden\" name = \"cardno\" value = \""+cardid+"\">\n";
            returnStr += "    <h1>Hi Valid Throu "+uname+" </h1>\n";
            returnStr += "    <br />";
            returnStr += "    <input type = \"submit\" value = \"Submit\" />\n";
            returnStr += "</form>";
            title = "Logged in";

        return returnStr;

    }

    /*private String loginScr() {
        String returnStr;
        returnStr  = "<form action = \"AtmAdminServlet\" method = \"GET\">\n";
        returnStr += "    <input type = \"hidden\" name = \"pos\" value = \"0\">\n";
        returnStr += "    Username: <input type = \"text\" name = \"username\">\n";
        returnStr += "    <br />";
        returnStr += "    Password:  <input type = \"password\" name = \"password\">\n";
        returnStr += "    <br />";
        returnStr += "    <input type = \"submit\" value = \"Submit\" />\n";
        returnStr += "</form>";
        title = "Login";
        return returnStr;
    }*/


    /*private String login(String username, String password) {
        String returnStr;
        String uname = atmAdmin.checkLogin(username,password);
        if (uname.isEmpty()) {
            returnStr = loginScr();
        } else {
            returnStr  = "<form action = \"AtmAdminServlet\" method = \"GET\">\n";
            returnStr += "    <input type = \"hidden\" name = \"pos\" value = \"m\">\n";
            returnStr += "    <input type = \"hidden\" name = \"username\" value = \""+uname+"\">\n";
            returnStr += "    <h1>Hi "+uname+" you're in!</h1>\n";
            returnStr += "    <br />";
            returnStr += "    <input type = \"submit\" value = \"Submit\" />\n";
            returnStr += "</form>";
            title = "Logged in";
        }
        return returnStr;

    }*/

    private String inputDepositWithdrawMeny () {
        String option = "";
        String returnStr;
        returnStr  = "<form action = \"AtmAdminServlet\" method = \"GET\">\n";
        returnStr += "    <input type = \"hidden\" name = \"pos\" value = \"1\">\n";
        returnStr += "    <h1> 1.Deposit  </h1>\n";
        returnStr += "    <h1> 2.Withdraw </h1>\n";
        returnStr += "    Options: <input type = \"text\" name = \"deposit\" value = \""+option+"\">\n";
        returnStr += "    <br />";
        returnStr += "    <input type = \"submit\" value = \"Submit\" />\n";
        returnStr += "</form>";
        title = "MENY Deposit or Withdraw ";

        return returnStr;

    }

     private String depositWithdrawAmountMenu() {
        String option = "";
        String returnStr;
        returnStr  = "<form action = \"AtmAdminServlet\" method = \"GET\">\n";
        returnStr += "    <input type = \"hidden\" name = \"pos\" value = \"2\">\n";
        returnStr += "    <h1> 1. 100          5.  700  </h1>\n";
        returnStr += "    <h1> 2. 200          6. 1000  </h1>\n";
        returnStr += "    <h1> 3. 300          7. 1500  </h1>\n";
        returnStr += "    <h1> 4. 500          8. Other </h1>\n";
        returnStr += "    Options: <input type = \"text\" name = \"option\" value = \""+option+"\">\n";
        returnStr += "    <br />";
        returnStr += "    <input type = \"submit\" value = \"Submit\" />\n";
        returnStr += "</form>";
        title = "MENU set amount ";

        return returnStr;
    }

    private int getAmount (String option) {
        switch (option) {
            case "1":
                return 100;
            case "2":
                return 200;
            case "3":
                return 300;
            case "4":
                return 500;
            case "5":
                return 700;
            case "6":
                return 1000;
            case "7":
                return 1500;
        }
        return 0;
    }

    private String getAmountMenu() {
        String amount = "";
        String returnStr;
        returnStr  = "<form action = \"AtmAdminServlet\" method = \"GET\">\n";
        returnStr += "    <input type = \"hidden\" name = \"pos\" value = \"4\">\n";
        returnStr += "    Options: <input type = \"text\" name = \"amount\" value = \""+amount+"\">\n";
        returnStr += "    <br />";
        returnStr += "    <input type = \"submit\" value = \"Submit\" />\n";
        returnStr += "</form>";
        title = "MENU set amount manually ";

        return returnStr;

    }

}
