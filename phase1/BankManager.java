package phase1;
import java.util.HashMap;
import java.util.Map;
import java.util Account;
import phase1.User;
import phase1.UserManager;
//Import ATM


public class BankManager extends Employees {
    String username;
    String password;

    public BankManager(String username, String password){
        this.username = username;
        this.password = password;

    }

    //Assume ATM stores bills as HashMap
    public void addToTill(HashMap<Integer, Integer> bills, HashMap<Integer, Integer> ATM){
        for (Map.Entry<Integer, Integer> denom : bills.entrySet()) {
            ATM.replace(denom.getKey(), denom.getValue() + ATM.get(denom.getKey()));
        }
    }

    public void createUser(String username, String password, String user_type){
        if (user_type.equals("Customer")) {
            User newUser = new User(username, password);
            UserManager.addUser(newUser);
        }
        else if (user_type.equals("Bank Manager")){
            BankManager newManager = new BankManager(username, password);
        }
    }

    //Add functionality to add starting balance
    public void addAccount(Account account, User user){
        user.addAccount(Account);
    }

}
