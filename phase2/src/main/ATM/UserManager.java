package ATM;

import java.util.HashMap;

/**
 * A static class to manage the list of all types of login accounts.
 */
final class UserManager {
    // A mapping of username to SystemUser.
    static HashMap<String, SystemUser> user_map = new HashMap<>();

    private UserManager() {}

    static void addLogin(SystemUser user) {
        // Username should be unique.
        user_map.putIfAbsent(user.getUsername(), user);
    }

    // Note that only a Bank Manager can create and set the initial password for a user.
    static void createLogin(String account_type, String username, String password) {
        switch (account_type) {
            case "Customer": {
                SystemUser_Customer newUser = new SystemUser_Customer(username, password);

                // Username should be unique.
                if (UserManager.checkLoginExistence(username)) {
                    System.out.println("Username already exists. SystemUser account is not created.");
                } else {
                    UserManager.addLogin(newUser);
                    System.out.println("A customer account with username, " + username + ", is successfully created.");
                }
            }
            case "Teller": {
                SystemUser_Employee_Teller newTeller = new SystemUser_Employee_Teller(username, password);

                // Username should be unique.
                if (UserManager.checkLoginExistence(username)) {
                    System.out.println("Username already exists. SystemUser account is not created.");
                } else {
                    UserManager.addLogin(newTeller);
                    System.out.println("A teller account with username, " + username + ", is successfully created.");
                }
            }
            case "BankManager": {
                SystemUser_Employee_BankManager newManager = new SystemUser_Employee_BankManager(username, password);

                // Username should be unique.
                if (UserManager.checkLoginExistence(username)) {
                    System.out.println("Username already exists. SystemUser account is not created.");
                } else {
                    UserManager.addLogin(newManager);
                    System.out.println("A teller account with username, " + username + ", is successfully created.");
                }
            }
        }
    }

    static SystemUser getLogin(String username) {
        return user_map.get(username);
    }


    static boolean checkLoginExistence(String username) {
        SystemUser l = user_map.get(username);

        return l != null;
    }

    /**
     * Verify if both username and password are valid.
     * Return SystemUser user if valid, otherwise return null.
     */
    static SystemUser verifyLogin(String u, String p) {
        SystemUser l = user_map.get(u);
        // Username exists.
        if (l != null && l.getPassword().equals(p)) {
            return getLogin(u);

        } else {
            System.out.println("The login attempt failed. Please double-check your username and password.\n");
            return null;
        }
    }
}
