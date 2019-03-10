package phase1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


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
     * Bank Manager has the ability to restock cash machine.
     * @param cashList amount of denominations [fives, tens, twenties, fifties]
     */
    public void restockMachine(ArrayList<Integer> cashList) {
        Cash.cashDeposit(cashList);
    }

    /**
     * The manager has the ability to undo the most recent transaction on any asset or debt account,
     * except for paying bills.
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
            System.out.println("Existed username. Login account is not created.");
        } else {
            LoginManager.addLogin(newUser);
            System.out.println("Command runs successfully.");
        }

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
