package phase1;
import java.util.ArrayList;


class Login_User implements Login {
    /*
    * A class that stores, sets, gets Login_User information including:
    * username
    * password
    * user's account(s)
    * their net total (The total of their debt account balances subtracted from the total of their asset account balances.)
    */
    private String password;

    // can not be empty
    private String username;

    // username should be unique
    private final ArrayList<Account> accounts;
    private ArrayList<Account> primary;

    Login_User(String username, String password){
        this.username = username;
        this.password = password;
        this.accounts = new ArrayList<>();
        this.primary = new ArrayList<>();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String u) {
        this.username = u;
    }

    @Override
    public boolean verifyLogin(String u, String p) {
        return this.username.equals(u) && this.password.equals(p);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }


    void addAccount(Account account){
        this.accounts.add(account);
        if(this.primary.isEmpty()){
            this.primary.add(account);
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
