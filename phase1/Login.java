package phase1;

abstract class Login {
    // username should be unique.
    private String username;

    // password should not be empty.
    private String password;

    private final String loginType;

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
        password = p;
    }

    String getLoginType() {
        return loginType;
    }

    boolean verifyLogin(String u, String p) {
        return username.equals(u) && this.password.equals(p);
    }
}