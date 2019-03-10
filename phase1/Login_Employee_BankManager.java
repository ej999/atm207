package phase1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


//import java.util.Account;
//Import ATM


class Login_Employee_BankManager extends Login_Employee implements Serializable {

    ATMFrame myATM;
    Login_Employee_BankManager(String username, String password) {
        super(username, password, "BankManager");
        myATM = new ATMFrame();
    }

    // Assume ATM stores bills as HashMap
    public void addToBill(HashMap<Integer, Integer> bills, HashMap<Integer, Integer> ATM) {
        for (Map.Entry<Integer, Integer> denom : bills.entrySet()) {
            ATM.replace(denom.getKey(), denom.getValue() + ATM.get(denom.getKey()));
        }
    }

    /**
     * Bank Manager has the ability to restock cash machine.
     *
     * @param cashList amount of denominations [fives, tens, twenties, fifties]
     */
    public void restockMachine(ArrayList<Integer> cashList) {
        Cash.cashDeposit(cashList);
    }

    /**
     * The manager has the ability to undo the most recent transaction on any asset or debt account,
     * except for paying bills.
     *
     * @param account account involved
     */
    public void undoMostRecentTransaction(Account account) {
        account.undoMostRecentTransaction();
    }

    /**
     * Only a bank manager can create and set the initial password for a user.
     */
    void createLogin(String username, String password) {
        Login_Customer newUser = new Login_Customer(username, password);

        // Username should be unique.
        if (LoginManager.checkLoginExistence(username)) {
            System.out.println("Username already exists. Login account is not created.");
        } else {
            LoginManager.addLogin(newUser);
            System.out.println("Login account is successfully created.");
        }

    }

    /**
     * Create an account for a Customer.
     */
    void addAccount(String accountType, Login_Customer username, double amount) {
        Account newAccount = null;

        if (accountType == null) {
            System.out.println("Invalid account type. Account is not created.");
        } else {
            switch (accountType) {
                case "Chequing": {
                    newAccount = new Account_Asset_Chequing(amount, username);
                    break;
                }
                case "Saving": {
                    newAccount = new Account_Asset_Saving(amount, username);
                    myATM.addObserver((Account_Asset_Saving)newAccount);
                    break;
                }
                case "CreditCard": {
                    newAccount = new Account_Debt_CreditCard(amount, username);
                    break;
                }
                case "LineOfCredit": {
                    newAccount = new Account_Debt_LineOfCredit(amount, username);
                    break;
                }
                default:
                    System.out.println("Invalid account type. Account is not created.");
            }
        }

        if (newAccount != null) {
            username.addAccount(newAccount);
            System.out.println("Command runs successfully.");
        }

    }

    /**
     * Create an account for a Customer. Amount is not initialized here.
     */
    void addAccount(String accountType, Login_Customer username) {
        this.addAccount(accountType, username, 0);
    }

}
