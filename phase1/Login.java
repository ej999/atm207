package phase1;

import java.util.ArrayList;

/**
 * Login account, with username, password, login type, available options on an ATM.
 */
abstract class Login {
    private String username;
    private String password;
    private final String loginType;
    private ArrayList availableOption;


    Login(String username, String password, String loginType) {
        this.username = username;
        this.password = password;
        this.loginType = loginType;
        this.availableOption = new ArrayList();
    }

    String getUsername() {
        return username;
    }

    void setUsername(String u) {
        username = u;
    }

    String getPassword() {
        return password;
    }

    void setPassword(String p) {
        // password should not be empty.
        //TODO inform password is not changed if it is empty
        password = p.equals("") ? password : p;
    }

    String getLoginType() {
        return loginType;
    }
    abstract void displayOptions();
}