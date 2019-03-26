package ATM;

import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * A static class that manage all existing bank accounts.
 */
final class AccountManager {
    private static final ATMSystem myATM = new ATMSystem();
    // A list of existing bank accounts
    static HashMap<String, Account> account_map = new HashMap<>();

    // By using reflections, all Account types are automatically added to the List even when we implement a new type.
    private static List<Class<? extends Account>> TYPES_OF_ACCOUNTS() {
        Set<Class<? extends Account>> subType = new Reflections("ATM").getSubTypesOf(Account.class);

        List<Class<? extends Account>> types_of_accounts = new ArrayList<>();

        // Check if the subclass is abstract.
        for (Class<? extends Account> type : subType) {
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

    //TODO GIC has unique parameter
    private static Account createAccount(String type, Customer owner, double initialAmount) {
        try {
            // Creating a new instance by getting the proper constructor; instead of using switch cases.
            Class<?> clazz = Class.forName(type);

            // The constructor has to be declared public, otherwise ...........
            Constructor<?> cTor = clazz.getConstructor(String.class, double.class, Customer.class);

            String id = idGenerator();
            Account newAccount = (Account) cTor.newInstance(id, initialAmount, owner);
            account_map.put(newAccount.getId(), newAccount);
            if (newAccount instanceof Saving) {
                myATM.addObserver((Saving) newAccount);
            }

            System.out.println("A User: \"" + newAccount + "\", is successfully created.");
            return newAccount;
        } catch (Exception e) {
            System.err.println("Invalid account type. Please try again.");
            return null;
        }
    }

    static Account getAccount(String id) {
        return account_map.get(id);
    }

    static List<Account> getListOfAccounts(String username) {
        ArrayList<Account> accountsOwned = new ArrayList<>();
        for (String key : account_map.keySet()) {
            if (account_map.get(key).getOwners().contains(username)) {
                accountsOwned.add(account_map.get(key));
            }
        }
        return accountsOwned;
    }

    static boolean isPresent(String id) {
        Account account = account_map.get(id);
        return account != null;
    }

    static void addAccount(String accountType, Customer User, double amount) {
        Account account = createAccount(accountType, User, amount);
        if (account != null) {
            User.addAccount(account);
            System.out.println("A " + accountType + " account with $" + amount + " balance is successfully created for "
                    + User.getUsername() + ". ");
        }
    }

    static void addAccount(String accountType, Customer User) {
        addAccount(accountType, User, 0);
    }

    private static String idGenerator() {
        boolean validId = false;
        String id = null;
        while (!validId) {
            id = String.valueOf((int) ((Math.random() * 9000000) + 1000000));
            validId = true;
            for (String key : account_map.keySet()) {
                if (account_map.get(key).getId().equals(id)) {
                    validId = false;
                }
            }
        }
        return id;
    }
}
