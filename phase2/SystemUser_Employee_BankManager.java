package phase2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;


//import java.util.Account;
//Import ATM


class SystemUser_Employee_BankManager extends SystemUser_Employee implements Serializable {


    SystemUser_Employee_BankManager(String username, String password) {
        super(username, password);
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
     * TODO: I think this method should be in LoginManager
     */

    void setMaxTransactions(Account_Student account, int transactionsAmount) {
        account.setMaxTransactions(transactionsAmount);
    }

    void setTransferLimit(Account_Student account, int transferLimitAmount) {
        account.setTransferLimit(transferLimitAmount);
    }

    /**
     * Only a bank manager can create and set the initial password for a user.
     * TODO: I think this method should be in LoginManager
     */
    void createLogin(String account_type, String username, String password) {
        switch (account_type) {
            case "Customer": {
                SystemUser_Customer newUser = new SystemUser_Customer(username, password);

                // Username should be unique.
                if (LoginManager.checkLoginExistence(username)) {
                    System.out.println("Username already exists. SystemUser account is not created.");
                } else {
                    LoginManager.addLogin(newUser);
                    System.out.println("A customer account with username, " + username + ", is successfully created.");
                }
            }
            case "Teller": {
                SystemUser_Employee_Teller newTeller = new SystemUser_Employee_Teller(username, password);

                // Username should be unique.
                if (LoginManager.checkLoginExistence(username)) {
                    System.out.println("Username already exists. SystemUser account is not created.");
                } else {
                    LoginManager.addLogin(newTeller);
                    System.out.println("A teller account with username, " + username + ", is successfully created.");
                }
            }
        }
    }

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
