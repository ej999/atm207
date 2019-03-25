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

    static void createUser(String type, String username, String password) {
        if (isPresent(username)) {
            System.err.println("Username already exists. User account is not created.");
        } else {
            try {
                Class<?> clazz = Class.forName("ATM." + type);
                Constructor<?> cTor = clazz.getConstructor(String.class, String.class);
                User newUser = (User) cTor.newInstance(username, password);

                addUser(newUser);
                System.out.println("A User: \"" + newUser + "\", is successfully created.");
            } catch (NoSuchMethodException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                System.err.println("User type is not valid. Please retry.");
            }
        }
    }

    private static void addUser(User user) {
        // Username should be unique.
        if (user_map.containsKey(user))
            user_map.putIfAbsent(user.getUsername(), user);
    }

    static User getUser(String username) {
        return user_map.get(username);
    }


    static boolean isPresent(String username) {
        User l = user_map.get(username);
        return l != null;
    }

    static boolean auth(String username, String password) {
        User user = user_map.get(username);
        if (isPresent(username) && user.getPassword().equals(password)) {
            return true;
        } else {
            System.err.println("The login attempt failed. Please double-check your username and password.");
            return false;
        }
    }
}
