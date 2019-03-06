package phase1;

import java.util.HashMap;
import java.util.Map;

/**
 * A class to manage users.
 */

class LoginManager_Customer implements LoginManager {
    private static HashMap<String, Login_Customer> user_map;

    LoginManager_Customer() {
        user_map = new HashMap<>();
    }

    static void addLogin(Login_Customer user) {
        user_map.put(user.getUsername(), user);
    }

    static boolean checkUser(String username, String password) {
        for (Map.Entry<String, Login_Customer> userEntry : user_map.entrySet()) {
            if (userEntry.getKey().equals(username) & userEntry.getValue().getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}
