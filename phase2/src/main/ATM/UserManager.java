package ATM;

import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * A class that manage all User.
 * <p>
 * To implement new type of User, create a class that extends any User- class.
 * No change of code in UserManager needed.
 */
final class UserManager {
    // List of the simple names of User types.
    final Collection<String> USER_TYPE_NAMES;

    // A mapping of Username to the User.
    Map<String, User> user_map = new HashMap<>();

    UserManager() {
        // By using Reflections, all the actual (non-abstract) User types are automatically added to the List even when we implement a new one.
        String packageName = UserManager.class.getPackage().getName();
        Set<Class<? extends User>> subType = new Reflections(packageName).getSubTypesOf(User.class);

        Collection<String> types_of_users = new ArrayList<>();
        for (Class<? extends User> type : subType) {
            // Check if the subclass is non-abstract.
            if (!Modifier.isAbstract(type.getModifiers())) {
                types_of_users.add(type.getSimpleName());
            }
        }
        USER_TYPE_NAMES = types_of_users;
    }


    /**
     * @param <T> a generic T type of User
     * @return HashMap of all T type of Users.
     */
    <T extends User> Map<String, T> getSubType_map() {
        Map<String, T> t_map = new HashMap<>();
        for (String username : user_map.keySet()) {
            User user = getUser(username);
            if (user instanceof Customer) {
                @SuppressWarnings("unchecked")
                T customer = (T) user;
                t_map.put(username, customer);
            }
        }
        return t_map;
    }

    /**
     * @param typeSimpleName the simple name of the subclass of Account represented by this Class object, for example, CreditCard, CreditLine, Saving.
     */
    boolean createAccount(String typeSimpleName, String username, String password) {
        if (isPresent(username)) {
            System.err.println("Username already exists. Please try again");
            return false;
        } else {
            try {
                // Creating a new instance by getting the proper constructor
                String className = UserManager.class.getPackage().getName() + "." + typeSimpleName;
                Class<?> clazz = Class.forName(className);
                Constructor<?> cTor = clazz.getConstructor(String.class, String.class);
                User newUser = (User) cTor.newInstance(username, password);

                user_map.putIfAbsent(newUser.getUsername(), newUser);
                System.out.println("A " + typeSimpleName + " is successfully created: \"" + newUser + "\"");
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
        return isPresent(username) && user.getPassword().equals(password);
    }
}
