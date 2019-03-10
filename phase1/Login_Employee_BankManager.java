package phase1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;


//import java.util.Account;
//Import ATM


class Login_Employee_BankManager extends Login_Employee implements Serializable {

    private final ATMTime myATM;

    Login_Employee_BankManager(String username, String password) {
        super(username, password, "BankManager");
        myATM = new ATMTime();
    }

    /**
     * Bank Manager has the ability to restock cash machine.
     *
     * @param cashList amount of denominations [fives, tens, twenties, fifties]
     */
    void restockMachine(ArrayList<Integer> cashList) {
        Cash.cashDeposit(cashList);
    }

    /**
     * The manager has the ability to undo the most recent transaction on any asset or debt account,
     * except for paying bills.
     *
     * @param account account involved
     */
    void undoMostRecentTransaction(Account account) {
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
            System.out.println("A Login account with username, " + username + ", is successfully created.");
        }

    }

    /**
     * Create an account for a Customer.
     */
    void addAccount(String accountType, Login_Customer username, @SuppressWarnings("SameParameterValue") double amount) {
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
                    myATM.addObserver((Account_Asset_Saving) newAccount);
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
            System.out.println("A " + accountType + " account with $" + amount + " balance is successfully created for " + username.getUsername() + ". ");
        }

    }

    /**
     * Create an account for a Customer. Amount is not initialized here.
     */
    void addAccount(String accountType, Login_Customer username) {
        this.addAccount(accountType, username, 0);
    }

    /**
     * Allow BankManger to read alerts.
     */
    void readAlerts() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("phase1/alerts.txt"));
            String alert = reader.readLine();
            while (alert != null) {
                System.out.println(alert);
                alert = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
