package phase1;

import java.util.HashMap;

/**
 * A static class to manage the list of all types of login accounts.
 */
class LoginManager {
    /** A mapping of username to Login. */
    private static HashMap<String, Login> login_map = new HashMap<>();

    /** Add a login account. */
    static void addLogin(Login user) {
        // Username should be unique.
        login_map.putIfAbsent(user.getUsername(), user);

    }

    /** Get a Login account by its username. */
    static Login getLogin(String username) {
        return login_map.get(username);
    }


    /** Check whether the login account exists. */
    static boolean checkLoginExistence(String username) {
        Login l = login_map.get(username);
        return l != null;
    }

    /** Verify if both username and password are valid. */
    static boolean verifyLogin(String u, String p) {
        Login l = login_map.get(u);
        // Username exists.
        if (l != null) {
            // Check whether password is correct.
            return l.getPassword().equals(p);
            // Username doesn't exist.
        } else {
            return false;
        }
    }
}
