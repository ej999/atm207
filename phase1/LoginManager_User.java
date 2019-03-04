package phase1;

import java.util.HashMap;
import java.util.Map;

/**
 * A class to manage users.
 */

class LoginManager_User implements LoginManager {
    private static HashMap<String, Login_User> user_map;

    LoginManager_User() {
        user_map = new HashMap<>();
    }

    static void addLogin(Login_User user) {
        user_map.put(user.getUsername(), user);
    }

    static boolean checkUser(String username, String password) {
        for (Map.Entry<String, Login_User> userEntry : user_map.entrySet()) {
            if (userEntry.getKey().equals(username) & userEntry.getValue().getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}
