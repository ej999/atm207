package ATM;

import java.util.HashMap;

/**
 * A static class to manage the list of all types of login accounts.
 */
final class LoginManager {
    /**
     * A mapping of username to SystemUser.
     */
    static HashMap<String, SystemUser> login_map = new HashMap<>();

    private LoginManager() {
    }

    static void addLogin(SystemUser user) {
        // Username should be unique.
        login_map.putIfAbsent(user.getUsername(), user);

    }

    static SystemUser getLogin(String username) {
        return login_map.get(username);
    }


    static boolean checkLoginExistence(String username) {
        SystemUser l = login_map.get(username);

        return l != null;
    }

    /**
     * Verify if both username and password are valid.
     * Return SystemUser user if valid, otherwise return null.
     */
    static SystemUser verifyLogin(String u, String p) {
        SystemUser l = login_map.get(u);
        // Username exists.
        if (l != null && l.getPassword().equals(p)) {
            return getLogin(u);

        } else {
            System.out.println("The login attempt failed. Please double-check your username and password.\n");
            return null;
        }
    }
}
