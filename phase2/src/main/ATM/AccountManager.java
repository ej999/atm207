package ATM;

import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * A static class that manage all Bank Accounts.
 * <p>
 * To implement new type of Account, create a class that extends any Account- class.
 * No change of code in AccountManagers needed.
 */
final class AccountManager {
    private static final ATMSystem myATM = new ATMSystem();
    // List of simple name of Account types.
    static List<String> TYPES_OF_ACCOUNTS;
    // A mapping of id to the Bank Account
    static HashMap<String, Account> account_map = new HashMap<>();

    static {
        // By using reflections, all Account types are automatically added to the List even when we implement a new type.
        String packageName = AccountManager.class.getPackage().getName();
        Set<Class<? extends Account>> subType = new Reflections(packageName).getSubTypesOf(Account.class);

        List<String> types_of_accounts = new ArrayList<>();
        for (Class<? extends Account> type : subType) {
            // Check if the subclass is abstract.
            if (!Modifier.isAbstract(type.getModifiers())) {
                types_of_accounts.add(type.getSimpleName());
            }
        }
        TYPES_OF_ACCOUNTS = types_of_accounts;
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

            System.out.println("A User: \"" + newAccount + "\", is successfully created");
            return newAccount;
        } catch (Exception e) {
            System.err.println("Invalid account type. Please try again");
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
                    + User.getUsername());
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
