package phase1;

import java.util.HashMap;
import java.util.Map;

//import java.util.Account;
//Import ATM


class Login_Employee_BankManager extends Login_Employee {
    private String username;
    private String password;

    Login_Employee_BankManager(String username, String password){
        this.username = username;
        this.password = password;

    }

    public void setPassword(String new_pass){
        this.password = new_pass;
    }

    public void setUsername(String new_user){
        this.password = new_user;
    }

    @Override
    public boolean verifyLogin(String u, String p) {
        return this.username.equals(u) && this.password.equals(p);
    }

    //Assume ATM stores bills as HashMap
    public void addToTill(HashMap<Integer, Integer> bills, HashMap<Integer, Integer> ATM){
        for (Map.Entry<Integer, Integer> denom : bills.entrySet()) {
            ATM.replace(denom.getKey(), denom.getValue() + ATM.get(denom.getKey()));
        }
    }

    public void createUser(String username, String password, String user_type){
        if (user_type.equals("Customer")) {
            Login_Customer newUser = new Login_Customer(username, password);
            LoginManager_Customer.addLogin(newUser);
        }
        else if (user_type.equals("Bank LoginManager")){
            Login_Employee_BankManager newManager = new Login_Employee_BankManager(username, password);
        }
    }

    //Add functionality to add starting balance
    public void addAccount(Account account, Login_Customer user){
        user.addAccount(account);
    }

}
