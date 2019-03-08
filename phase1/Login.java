package phase1;

/**
 * Login account, with username, password, login type on an ATM.
 */
abstract class Login {
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
        //TODO inform password is not changed if it is empty
        password = p.equals("") ? password : p;
    }

    String getLoginType() {
        return loginType;
    }
}