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
 * A class that manage all User.
 * <p>
 * To implement new type of User, create a class that extends any User- class.
 * No change of code in UserManager needed.
 */
final class UserManager {
    // List of the simple names of User types.
    final List<String> USER_TYPE_NAMES;

    // A mapping of Username to the User.
    HashMap<String, User> user_map = new HashMap<>();

    UserManager() {
        // By using Reflections, all the actual (non-abstract) User types are automatically added to the List even when we implement a new one.
        String packageName = UserManager.class.getPackage().getName();
        Set<Class<? extends User>> subType = new Reflections(packageName).getSubTypesOf(User.class);

        List<String> types_of_users = new ArrayList<>();
        for (Class<? extends User> type : subType) {
            // Check if the subclass is non-abstract.
            if (!Modifier.isAbstract(type.getModifiers())) {
                types_of_users.add(type.getSimpleName());
            }
        }
        USER_TYPE_NAMES = types_of_users;
    }

    boolean createAccount(String type, String username, String password) {
        if (isPresent(username)) {
            System.err.println("Username already exists. Please try again");
            return false;
        } else {
            try {
                // Creating a new instance by getting the proper constructor; instead of using switch cases.
                Class<?> clazz = Class.forName(type);
                Constructor<?> cTor = clazz.getConstructor(String.class, String.class);
                User newUser = (User) cTor.newInstance(username, password);

                user_map.putIfAbsent(newUser.getUsername(), newUser);
                System.out.println("A User: \"" + newUser + "\", is successfully created");
                return true;
            } catch (NoClassDefFoundError | NoSuchMethodException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                System.err.println("Invalid user type. Please try again");
                return false;
            }
        }
    }

    User getUser(String username) {
        return user_map.get(username);
    }


    boolean isPresent(String username) {
        return user_map.get(username) != null;
    }

    boolean isCustomer(String username) {
        return isPresent(username) && user_map.get(username) instanceof Customer;
    }

    boolean auth(String username, String password) {
        User user = user_map.get(username);
        if (isPresent(username) && user.getPassword().equals(password)) {
            return true;
        } else {
            System.err.println("Wrong username or password. Please try again");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.err.println("Ouch!");
            }
            return false;
        }
    }
}
