package phase1;

import java.io.Serializable;

/**
 * Login account, with username, password, login type on an ATM.
 */
abstract class Login implements Serializable {
    private final String loginType;
    private String username;
    private String password;


    Login(String username, String password, String loginType) {
        this.username = username;
        this.password = password;
        this.loginType = loginType;
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
        if(p.equals("")) {
            System.out.println("Your password has not been changed");
        }
        password = p.equals("") ? password : p;
    }

    String getLoginType() {
        return loginType;
    }
}