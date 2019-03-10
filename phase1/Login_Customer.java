package phase1;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A customer's login account, with username, password, list of their accounts, primary chequing account, and net total.
 */
class Login_Customer extends Login implements Serializable {
    private final ArrayList<Account> accounts;
    private Account primary;


    Login_Customer(String username, String password) {
        super(username, password, "Customer");
        this.accounts = new ArrayList<>();
    }

    /**
     * By default, first chequing account added is the primary account.
     *
     * @param account to be added
     */
    void addAccount(Account account) {
        this.accounts.add(account);
        // If a user has only one checking account, it will be the default destination for any deposits.
        if (primary == null && account instanceof Account_Asset_Chequing) {
            primary = account;
        }
    }

    void setPrimary(Account primary) {
        this.primary = primary;
    }

    ArrayList<Account> getAccounts() {
        return accounts;
    }

    boolean hasAccount(Account account) {
        for (Account a : this.accounts) {
            if (a.equals(account)) {
                return true;
            }

        }
        return false;
    }

    boolean hasMoreThanOneChequing() {
        int i = 0;
        for (Account a : this.accounts) {
            if (a instanceof Account_Asset_Chequing) {
                i++;
            }
        }
        return i > 1;
    }

    // The total of their debt account balances subtracted from the total of their asset account balances.
    private double netTotal() {
        double sum = 0;
        for (Account a : this.accounts) {
//            if (a instanceof Account_Asset_Saving || a instanceof  Account_Asset_Chequing) {
            sum += a.getBalance();
//            }
//            } else if (a instanceof Account_Debt) {
//                sum -= a.getBalance();
//                System.err.println(a);
//            }
        }
        return sum;
    }

    // add a line of request in the alert.text
    void requestHelp(String s) throws IOException {
        String content = s + "\n";
        File file = new File("alerts.txt");
        FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(content);
        bw.close(); // Be sure to close BufferedWriter

    }

    // add a line of string in alert that request for new account
    void requestAccount(String accountType) throws IOException {
        String n = "Requesting to create " + accountType + " Account." + "\n";
        requestHelp(n);
    }


    void selectAccount(Account account) {
        System.out.println("1. Show account creation date.");
        System.out.println("2. Show account balance.");
        System.out.println("3. Show most recent transaction.");
        Scanner reader = new Scanner(System.in);
        int choice = reader.nextInt();
        switch (choice) {
            case 1:
                System.out.println(account.dateOfCreation);
                break;
            case 2:
                System.out.println(account.getBalance());
                break;
            case 3:
                System.out.println("Type :" + account.mostRecentTransaction.get("Type"));
                System.out.println("Amount :" + account.mostRecentTransaction.get("Amount"));
                break;

        }
    }

    @Override
    public String toString() {
        StringBuilder returnMessage = new StringBuilder();
        returnMessage.append("\n\u001B[1mPrimary\t\tAccount Type\t\tCreation Date\t\t\t\t\tBalance\t\tMost Recent Transaction" +
                "\u001B[0m");
        for (Account account : getAccounts()) {
            if (account == primary) {
                returnMessage.append("\nX\t\t\t" + account);
            } else {
                returnMessage.append("\n\t\t\t" + account);
            }

        }

        returnMessage.append("\n\n\u001B[1mYour net total is \u001B[0m$" + netTotal());

        return returnMessage.toString();
    }
}
