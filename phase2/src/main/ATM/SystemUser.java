package ATM;

import java.io.Serializable;

/**
 * SystemUser with username and password.
 */
abstract class SystemUser implements Serializable {
    private final String username;
    private String password;
    //TODO: any login user (e.g. employees) should have accounts, not just customers


    SystemUser(String username, String password) {
        this.username = username;
        this.password = password;
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
}