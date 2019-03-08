package phase1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A customer's login account, with username, password, list of their accounts, primary account, and net total.
 */
class Login_Customer extends Login {
    private final ArrayList<Account> accounts;
    private Account primary;

    Login_Customer(String username, String password){
        super(username, password, "Customer");
        this.accounts = new ArrayList<>();
    }

    void addAccount(Account account){
        this.accounts.add(account);
        // If a user has only one checking account, it will be the default destination for any deposits.
        if(primary == null && account instanceof Account_Asset_Chequing){
            primary = account;
        }
    }

    void setPrimary(Account primary) {
        this.primary = primary;
    }

    ArrayList<Account> getAccounts() {
        return accounts;
    }

    boolean hasAccount(Account account){
        for(Account a : this.accounts){
            if(a.equals(account)){
                return true;
            }

        }
        return false;
    }

    // The total of their debt account balances subtracted from the total of their asset account balances.
    double netTotal() {
        double sum = 0;
        for (Account a : this.accounts) {
            if (a instanceof Account_Asset) {
                sum += a.getBalance();
            } else {
                sum -= a.getBalance();
            }
        }
        return sum;
    }
// add a line of request in the alert.text
    void requestHelp(String s) throws IOException {
        String content = s + "\n";
        File file = new File("alert.txt");
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(content);
        bw.close(); // Be sure to close BufferedWriter

    }

// add a line of string in alert that request for new account
    void requestAccount(String accountType) throws IOException {
         String n = "Requesting to create " + accountType + " Account." + "\n";
         requestHelp(n);
    }
    void displayOptions(){
        System.out.println("1. Show summary of all account balances");
        System.out.println("2. Show most recent transaction on any account");
        System.out.println("3. Show date of creation of an account");
        System.out.println("4. See net worth.");
    }

    void selectOption(int o){
        switch(o) {
            case 1:
                StringBuilder returnMessage = new StringBuilder();
                for(Account account:accounts){
                    System.out.println(account.getClass() + ": " + String.valueOf(account.getBalance()));
            }

        }
    }
}
