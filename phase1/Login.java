package phase1;

import java.io.Serializable;

/**
 * Login account, with username, password, login type on an ATM.
 */
abstract class Login implements Serializable {
    private final String loginType;
    private final String username;
    private String password;


    Login(String username, String password, String loginType) {
        this.username = username;
        this.password = password;
        this.loginType = loginType;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

    void setPassword(String p) {
        // password should not be empty.
        if (p.equals("")) {
            System.out.println("Password should not be empty. Your password has not been changed");
        } else {
            password = p;
            System.out.println("Password is set successfully.");
        }
    }

    String getLoginType() {
        return loginType;
    }
}