package phase1;
import java.util.ArrayList;


class User {
    /* A class that stores, sets, gets User information including:
    * username
    * password
    * user's account(s)
    * their net total (The total of their debt account balances subtracted from the total of their asset account balances.)
    */
    private String password;
//    can not be empty
    private final String username;
//    username should be unique
    private final ArrayList<Account> accounts;

    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.accounts = new ArrayList<>();
    }

    public void setPassword(String password){
        this.password = password;
    }
    public String getUsername(){
        return this.username;
    }
    public void addAccount(Account account){
        this.accounts.add(account);
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }
}
