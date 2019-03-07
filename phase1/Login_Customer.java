package phase1;
import java.util.ArrayList;

class Login_Customer extends Login {
    /*
    * A class that stores, sets, gets Login_Customer information including:
    * username
    * password
    * user's account(s)
    * their net total (The total of their debt account balances subtracted from the total of their asset account balances.)
    */

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
        this.primary.set(0,primary);
    }

    ArrayList<Account> getAccounts() {
        return accounts;
    }

    boolean hasAccount(Account account){
        for(Account a: this.accounts){
            if(a.equals(account)){
                return true;
            }

        }
        return false;
    }
}
