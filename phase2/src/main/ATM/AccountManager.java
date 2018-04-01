package ATM;

import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

import static ATM.ATM.userManager;

/**
 * A class that manage all Bank Accounts.
 * <p>
 * To implement new type of Account, create a class that extends any Account- class.
 * No change of code in AccountManagers needed.
 */
final class AccountManager {
    // List of the simple names of Account types.
    final Collection<String> TYPES_OF_ACCOUNTS;

    // A mapping of id to the bank account
    HashMap<String, Account> account_map = new HashMap<>();

    AccountManager() {
        // By using reflections, all Account types are automatically added to the List even when we implement a new type.
        String packageName = AccountManager.class.getPackage().getName();
        Set<Class<? extends Account>> subType = new Reflections(packageName).getSubTypesOf(Account.class);

        Collection<String> types_of_accounts = new ArrayList<>();
        for (Class<? extends Account> type : subType) {
            // Check if the subclass is abstract.
            if (!Modifier.isAbstract(type.getModifiers())) {
                types_of_accounts.add(type.getSimpleName());
            }
        }
        TYPES_OF_ACCOUNTS = types_of_accounts;
    }

    /**
     * @param typeSimpleName the simple name of the subclass of Account represented by this Class object, for example, CreditCard, CreditLine, Saving
     * @param ownersUsername list of username of owners
     */
    private Account createAccount(String typeSimpleName, List<String> ownersUsername) {
        try {
            // Creating a new instance by getting the proper constructor
            String className = AccountManager.class.getPackage().getName() + "." + typeSimpleName;
            Class<?> clazz = Class.forName(className);
            // The constructor has to be declared public in order to get its constructor.
            Constructor<?> cTor = clazz.getConstructor(String.class, List.class);

            String id = idGenerator();
            Account newAccount = (Account) cTor.newInstance(id, ownersUsername);
            account_map.put(newAccount.getId(), newAccount);
//            if (newAccount instanceof Saving) {
//                addObserver((Saving) newAccount);
//            }

            System.out.println("A " + typeSimpleName + " is successfully created: \"" + newAccount + "\"");
            return newAccount;
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            System.err.println("Invalid account type. Please try again");
            return null;
        }
    }

    Account getAccount(String id) {
        if (id != null) {
            return account_map.get(id);
        } else {
            System.err.println("Cannot find the corresponding account by ID");
            return null;
        }
    }

    List<Account> getListOfAccounts(String username) {
        ArrayList<Account> accountsOwned = new ArrayList<>();
        for (String key : account_map.keySet()) {
            if (account_map.get(key).getOwnersUsername().contains(username)) {
                accountsOwned.add(account_map.get(key));
            }
        }
        return accountsOwned;
    }

    void addAccount(String accountType, List<String> ownersUsername) {
        Account account = createAccount(accountType, ownersUsername);
        if (account != null) {
            for (String username : ownersUsername) {
                Customer owner = (Customer) userManager.getUser(username);
                owner.addAccount(account);
                System.out.println("A " + accountType + " account is successfully created for " + username);
            }
        }
    }

    // Overloading method for GIC
    void addGICAccount(double rate, int months, List<String> ownersUsername) {
        String id = idGenerator();

        Account account = new GIC(id, rate, months, ownersUsername);
        account_map.put(account.getId(), account);
        System.out.println("A GIC is successfully created: \"" + account + "\"");

    }


    private String idGenerator() {
        boolean validId = false;
        String id = null;
        while (!validId) {
            id = String.valueOf((int) ((Math.random() * 9000000) + 1000000));
            validId = true;
            for (String key : account_map.keySet()) {
                if (account_map.get(key).getId() != null) {
                    if (account_map.get(key).getId().equals(id)) {
                        validId = false;
                    }
                }
            }
        }
        return id;
    }

//    public Collection<Account> allAccounts() {
//        return this.account_map.values();
//
//    }
}
