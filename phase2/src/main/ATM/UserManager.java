package ATM;

import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * A static class that manage all existing user accounts.
 */
final class UserManager {
    // A mapping of username to the existing User.
    static HashMap<String, User> account_map = new HashMap<>();

    // By using reflections, all User types are automatically added to the List even when we implement a new type.
    private static List<Class<? extends User>> TYPES_OF_ACCOUNTS() {
        Set<Class<? extends User>> subType = new Reflections("ATM").getSubTypesOf(User.class);

        List<Class<? extends User>> types_of_accounts = new ArrayList<>();

        // Check if the subclass is abstract.
        for (Class<? extends User> type : subType) {
            if (!Modifier.isAbstract(type.getModifiers())) {
                types_of_accounts.add(type);
            }
        }

        return types_of_accounts;
    }

    // Return as List of Strings; not List of Classes.
    static List<String> getTypesOfAccounts() {
        List<String> types = new ArrayList<>();

        for (Class type : TYPES_OF_ACCOUNTS()) {
            types.add(type.getSimpleName());
        }

        return types;
    }

    static boolean createAccount(String type, String username, String password) {
        if (isPresent(username)) {
            System.err.println("Username already exists. Please try again.");
            return false;
        } else {
            try {
                // Creating a new instance by getting the proper constructor; instead of using switch cases.
                Class<?> clazz = Class.forName(type);
                Constructor<?> cTor = clazz.getConstructor(String.class, String.class);
                User newUser = (User) cTor.newInstance(username, password);

                account_map.putIfAbsent(newUser.getUsername(), newUser);
                System.out.println("A User: \"" + newUser + "\", is successfully created.");
                return true;
            } catch (NoClassDefFoundError | NoSuchMethodException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                System.err.println("Invalid user type. Please try again.");
                return false;
            }
        }
    }

    static User getAccount(String username) {
        return account_map.get(username);
    }


    static boolean isPresent(String username) {
        User user = account_map.get(username);
        return user != null;
    }

    static boolean auth(String username, String password) {
        User user = account_map.get(username);
        if (isPresent(username) && user.getPassword().equals(password)) {
            return true;
        } else {
            System.err.println("The login attempt failed. Please double-check your username and password.");
            return false;
        }
    }
}
