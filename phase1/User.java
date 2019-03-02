import java.util.ArrayList;

public class User {
    // A class to create new users with username and password,
    private String password;
    private String username;
    private ArrayList<Account> accounts;

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
