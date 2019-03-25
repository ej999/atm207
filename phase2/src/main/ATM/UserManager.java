package ATM;

import java.util.HashMap;

/**
 * A static class to manage the list of all types of login accounts.
 */
final class UserManager {
    // A mapping of username to User.
    static HashMap<String, User> user_map = new HashMap<>();

    private UserManager() {}

    static void addLogin(User user) {
        // Username should be unique.
        user_map.putIfAbsent(user.getUsername(), user);
    }

    // Note that only a Bank Manager can create and set the initial password for a user.
    static void createLogin(String account_type, String username, String password) {
        switch (account_type) {
            case "Customer": {
                User_Customer newUser = new User_Customer(username, password);

                // Username should be unique.
                if (UserManager.checkLoginExistence(username)) {
                    System.out.println("Username already exists. User account is not created.");
                } else {
                    UserManager.addLogin(newUser);
                    System.out.println("A customer account with username, " + username + ", is successfully created.");
                }
            }
            case "Teller": {
                User_Employee_Teller newTeller = new User_Employee_Teller(username, password);

                // Username should be unique.
                if (UserManager.checkLoginExistence(username)) {
                    System.out.println("Username already exists. User account is not created.");
                } else {
                    UserManager.addLogin(newTeller);
                    System.out.println("A teller account with username, " + username + ", is successfully created.");
                }
            }
            case "BankManager": {
                User_Employee_BankManager newManager = new User_Employee_BankManager(username, password);

                // Username should be unique.
                if (UserManager.checkLoginExistence(username)) {
                    System.out.println("Username already exists. User account is not created.");
                } else {
                    UserManager.addLogin(newManager);
                    System.out.println("A teller account with username, " + username + ", is successfully created.");
                }
            }
        }
    }

    static User getLogin(String username) {
        return user_map.get(username);
    }


    static boolean checkLoginExistence(String username) {
        User l = user_map.get(username);

        return l != null;
    }

    /**
     * Verify if both username and password are valid.
     * Return User user if valid, otherwise return null.
     */
    static User verifyLogin(String u, String p) {
        User l = user_map.get(u);
        // Username exists.
        if (l != null && l.getPassword().equals(p)) {
            return getLogin(u);

        } else {
            System.out.println("The login attempt failed. Please double-check your username and password.\n");
            return null;
        }
    }
}
