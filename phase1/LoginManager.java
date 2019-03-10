package phase1;

import java.util.HashMap;

/**
 * A static class to manage the list of all types of login accounts.
 */
final class LoginManager {
    /**
     * A mapping of username to Login.
     */
    static HashMap<String, Login> login_map = new HashMap<>();

    private LoginManager() {
    }

    /**
     * Add a login account.
     */
    static void addLogin(Login user) {
        // Username should be unique.
        login_map.putIfAbsent(user.getUsername(), user);

    }

    /**
     * Get a Login account by its username.
     */
    static Login getLogin(String username) {
        return login_map.get(username);
    }


    /**
     * Check whether the login account exists.
     */
    static boolean checkLoginExistence(String username) {
        Login l = login_map.get(username);

        return l != null;
    }

    /**
     * Verify if both username and password are valid.
     * Return Login user if valid, otherwise return null.
     */
    static Login verifyLogin(String u, String p) {
        Login l = login_map.get(u);
        // Username exists.
        if (l != null && l.getPassword().equals(p)) {
            return getLogin(u);

        } else {
            System.out.println("The login attempt failed. Please double-check your username and password.\n");
            return null;
        }
    }
}
