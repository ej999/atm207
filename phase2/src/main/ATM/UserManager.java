package ATM;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * A static class to manage all existing user accounts.
 */
final class UserManager {
    // A mapping of username to User.
    static HashMap<String, User> user_map = new HashMap<>();

    static final List<Class> typesOfUsers = Arrays.asList(
            User_Customer.class,
            User_Employee_BankManager.class,
            User_Employee_Teller.class
    );

    UserManager() {
    }

    // Return as String, not Class.
    public static List<String> getTypesOfUsers() {
        List<String> types = new ArrayList<>();

        for (Class type : typesOfUsers) {
            types.add(type.getSimpleName());
        }

        return types;
    }

    static void addUser(User user) {
        // Username should be unique.
        user_map.putIfAbsent(user.getUsername(), user);
    }

    // Note that only a Bank Manager can create and set the initial password for a user.
    static void createUser(String type, String username, String password) {
        if (UserManager.checkLoginExistence(username)) {
            System.out.println("Username already exists. User account is not created.");
        } else {
            try {
                Class<?> clazz = Class.forName("ATM." + type);
                Constructor<?> cTor = clazz.getConstructor(String.class, String.class);
                User newUser = (User) cTor.newInstance(username, password);

                UserManager.addUser(newUser);
                System.out.println("A User: \"" + newUser + "\", is successfully created.");
            } catch (NoSuchMethodException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                System.err.println("User type is not valid. Please retry.");
            }
        }
    }

    static User getUser(String username) {
        return user_map.get(username);
    }


    static boolean checkLoginExistence(String username) {
        User l = user_map.get(username);

        return l != null;
    }

    // Verify if both username and password are valid.
    static User authentication(String u, String p) {
        User l = user_map.get(u);
        // Username exists.
        if (l != null && l.getPassword().equals(p)) {
            return getUser(u);

        } else {
            System.out.println("The login attempt failed. Please double-check your username and password.\n");
            return null;
        }
    }
}
