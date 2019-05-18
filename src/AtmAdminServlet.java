import se.rifr.AtmAdmin;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AtmAdminServlet extends HttpServlet {

    private String title = "ATM Transactions";
    AtmAdmin atmAdmin = new AtmAdmin();

    String atmHwId  = "AtmTest001";
    String cardId   = "";
    String pinCode  = "";
    boolean deposit = false;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String pos = request.getParameter("pos");

        String message = "";
        if (pos == null)
            message = cardLoginScr(); //loginScr();
        else {
            System.out.println(pos);
            switch (pos) {
                case "m":
                    message = cardLoginScr(); //loginScr();
                    cardId = "";
                    break;
                case "0": // from cardLoginScr(), param atmid, cardid & pincode
                    atmHwId = request.getParameter("atmid");
                    if (atmAdmin.checkMachine(atmHwId).isEmpty())
                        message = cardLoginScr();
                    else
                        message = cardLogin(request.getParameter("cardid"), request.getParameter("pincode"));
                    break;
                case "1": // from cardLogin, param option (1=deposit, 2=withdraw, 3=view saldo)
                    String opt1 = request.getParameter("option");
                    if (opt1.equals("3")) {
                        message = InfoMenu(String.valueOf(atmAdmin.getAccount(cardId).getSaldo()), "0",
                                "    <input type = \"hidden\" name = \"atmid\" value = \"" + atmHwId + "\">\n" +
                                        "    <input type = \"hidden\" name = \"cardid\" value = \"" + cardId + "\">\n" +
                                        "    <input type = \"hidden\" name = \"pincode\" value = \"" + pinCode + "\">\n");
                    } else {
                        deposit = (opt1.equals("1"));
                        message = depositWithdrawAmountMenu();
                    }
                    break;
                case "2": // from depositWithdrawAmountMenu, param option (1-7 hardcoded amount, 8 enter any amount)
                    String opt2 = request.getParameter("option");
                    if (opt2.equals("8"))
                        message = getAmountMenu();
                    else {
                        if (!deposit && atmAdmin.checkSaldo(cardId, Double.valueOf(getAmount(opt2))).isEmpty())
                            message = InfoMenu("Saldo " + String.valueOf(atmAdmin.getAccount(cardId).getSaldo()), "0",
                                    "    <input type = \"hidden\" name = \"atmid\" value = \"" + atmHwId + "\">\n" +
                                            "    <input type = \"hidden\" name = \"cardid\" value = \"" + cardId + "\">\n" +
                                            "    <input type = \"hidden\" name = \"pincode\" value = \"" + pinCode + "\">\n");
                        else {
                            if (!atmAdmin.registerTransaction(atmAdmin.getAccount(cardId), deposit, Double.valueOf(getAmount(opt2))).isEmpty())
                                atmAdmin.registerAtmTransaction(atmHwId, deposit, Double.valueOf(getAmount(opt2)));

                            message = InfoMenu("Thanks for using this ATM machine " +
                                    atmAdmin.getAccount(cardId).getCustomer().getFullName(), "m", "");
                        }
                    }

                    break;
                case "3":
                    String amount = request.getParameter("amount");
                    if (!deposit && atmAdmin.checkSaldo(cardId, Double.valueOf(amount)).isEmpty())
                        message = InfoMenu("Saldo " + String.valueOf(atmAdmin.getAccount(cardId).getSaldo()), "0",
                                "    <input type = \"hidden\" name = \"atmid\" value = \"" + atmHwId + "\">\n" +
                                        "    <input type = \"hidden\" name = \"cardid\" value = \"" + cardId + "\">\n" +
                                        "    <input type = \"hidden\" name = \"pincode\" value = \"" + pinCode + "\">\n");
                    else if (atmAdmin.checkMachineSaldo(atmHwId, deposit, Double.valueOf(amount)).isEmpty()) {
                        message = InfoMenu("ATM Saldo " + String.valueOf(atmAdmin.getSaldo(atmHwId)), "0",
                                "    <input type = \"hidden\" name = \"atmid\" value = \"" + atmHwId + "\">\n" +
                                        "    <input type = \"hidden\" name = \"cardid\" value = \"" + cardId + "\">\n" +
                                        "    <input type = \"hidden\" name = \"pincode\" value = \"" + pinCode + "\">\n");
                    } else {
                        String res = atmAdmin.registerTransaction(atmAdmin.getAccount(cardId), deposit, Double.valueOf(amount));
                        if (res.isEmpty())
                            message += cardLoginScr();
                        else {
                            atmAdmin.registerAtmTransaction(atmHwId, deposit, Integer.valueOf(amount));
                            message += InfoMenu("Thanks for using this ATM machine " +
                                    atmAdmin.getAccount(cardId).getCustomer().getFullName(), "m", "");
                        }

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
                    message +
                    "</body>" +
                    "</html>"
            );
        }
    }

        private String cardLoginScr () {

            String returnStr;
            returnStr = "<form action = \"AtmAdminServlet\" method = \"GET\">\n";
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

        private String cardLogin (String cardid, String pincode){

            if (atmAdmin.checkCode(cardid, pincode).isEmpty() || atmAdmin.checkCard(cardid).isEmpty()) {

                return cardLoginScr();
            }

            cardId = cardid; //set global variable
            pinCode = pincode;

            return inputDepositWithdrawMeny();

        }

        private String inputDepositWithdrawMeny () {

            String option = "";
            String returnStr;
            returnStr = "<form action = \"AtmAdminServlet\" method = \"GET\">\n";
            returnStr += "    <input type = \"hidden\" name = \"pos\" value = \"1\">\n";
            returnStr += "    <h1> 1.Deposit       </h1>\n";
            returnStr += "    <h1> 2.Withdraw      </h1>\n";
            returnStr += "    <h1> 3.Present Saldo </h1>\n";
            returnStr += "    Options: <input type = \"text\" name = \"option\" value = \"" + option + "\">\n";
            returnStr += "    <br />";
            returnStr += "    <input type = \"submit\" value = \"Submit\" />\n";
            returnStr += "</form>";
            title = "MENY Deposit or Withdraw ";

            return returnStr;

        }

        private String depositWithdrawAmountMenu () {

            String option = "";
            String returnStr;
            returnStr = "<form action = \"AtmAdminServlet\" method = \"GET\">\n";
            returnStr += "    <input type = \"hidden\" name = \"pos\" value = \"2\">\n";
            returnStr += "    <h1> 1. 100           5.  700  </h1>\n";
            returnStr += "    <h1> 2. 200           6. 1000  </h1>\n";
            returnStr += "    <h1> 3. 300           7. 1500  </h1>\n";
            returnStr += "    <h1> 4. 500           8. Other </h1>\n";
            returnStr += "    Options: <input type = \"text\" name = \"option\" value = \"" + option + "\">\n";
            returnStr += "    <br />";
            returnStr += "    <input type = \"submit\" value = \"Submit\" />\n";
            returnStr += "</form>";
            title = "MENU set amount ";

            return returnStr;
        }

        private int getAmount (String option){
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
                default:
                    return 0;
            }
        }

        private String getAmountMenu () {

            String amount = "";
            String returnStr;
            returnStr = "<form action = \"AtmAdminServlet\" method = \"GET\">\n";
            returnStr += "    <input type = \"hidden\" name = \"pos\" value = \"3\">\n";
            returnStr += "    Amount: <input type = \"text\" name = \"amount\" value = \"" + amount + "\">\n";
            returnStr += "    <br />";
            returnStr += "    <input type = \"submit\" value = \"Submit\" />\n";
            returnStr += "</form>";
            title = "MENU set amount manually ";

            return returnStr;

        }
        private String InfoMenu (String infoLine1, String pos, String hiddenLine1){

            String returnStr;
            returnStr = "<form action = \"AtmAdminServlet\" method = \"GET\">\n";
            //returnStr += "    <input type = \"hidden\" name = \"pos\" value = \"m\">\n";
            returnStr += "    <input type = \"hidden\" name = \"pos\" value = \"" + pos + "\">\n";
            if (!hiddenLine1.isEmpty())
                returnStr += hiddenLine1;
            returnStr += "    <h1> " + infoLine1 + " </h1>\n";
            returnStr += "    <br />";
            returnStr += "    <input type = \"submit\" value = \"Submit\" />\n";
            returnStr += "</form>";
            title = "Info Menu ";

            return returnStr;

        }
    }
