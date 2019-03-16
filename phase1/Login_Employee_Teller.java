package phase1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

public class Login_Employee_Teller extends Login_Employee implements Serializable {

    private final ATMTime myATM;

    Login_Employee_Teller(String username, String password) {
        super(username, password, "BankTeller");
        myATM = new ATMTime();

    }

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

//    void undoMostRecentTransaction(Account account) {
//        account.undoMostRecentTransaction();
//    }
//
//    /**
//     * Create an account for a Customer. Amount is not initialized here.
//     */
//    void addAccount(String accountType, Login_Customer username) {
//        this.addAccount(accountType, username, 0);
//    }
//
//    /**
//     * Allow BankManger to read alerts.
//     */
//    void readAlerts() {
//        BufferedReader reader;
//        try {
//            reader = new BufferedReader(new FileReader("phase1/alerts.txt"));
//            String alert = reader.readLine();
//            while (alert != null) {
//                System.out.println(alert);
//                alert = reader.readLine();
//            }
//            reader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
