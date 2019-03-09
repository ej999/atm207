package phase1;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.io.FileWriter;


//import java.util.Account;
//Import ATM


class Login_Employee_BankManager extends Login_Employee implements Serializable {

    Login_Employee_BankManager(String username, String password) {
        super(username, password, "BankManager");
    }

    // Assume ATM stores bills as HashMap
    public void addToBill(HashMap<Integer, Integer> bills, HashMap<Integer, Integer> ATM) {
        for (Map.Entry<Integer, Integer> denom : bills.entrySet()) {
            ATM.replace(denom.getKey(), denom.getValue() + ATM.get(denom.getKey()));
        }
    }

    /**
     * Only a bank manager can create and set the initial password for a user.
     */
    public void createLogin(String username, String password) {

        Login_Customer newUser = new Login_Customer(username, password);
        LoginManager.addLogin(newUser);

    }

    /**
     * Create an account for a Customer.
     */
    void addAccount(String accountType, Login_Customer username, double amount) {
        switch (accountType) {
            case "Chequing": {
                Account_Asset_Chequing newAccount = new Account_Asset_Chequing(amount, username);
                username.addAccount(newAccount);
                break;
            }
            case "Saving": {
                Account_Asset_Saving newAccount = new Account_Asset_Saving(amount, username);
                username.addAccount(newAccount);
                break;
            }
            case "CreditCard": {
                Account_Debt_CreditCard newAccount = new Account_Debt_CreditCard(amount, username);
                username.addAccount(newAccount);
                break;
            }
            case "LineOfCredit": {
                Account_Debt_LineOfCredit newAccount = new Account_Debt_LineOfCredit(amount, username);
                username.addAccount(newAccount);
                break;
            }
            default:
                System.out.println("Invalid account type");
                break;
        }
    }

    /**
     * Create an account for a Customer. Amount is not initialized here.
     */
    void addAccount(String accountType, Login_Customer username) {
        this.addAccount(accountType, username, 0);
    }

}
