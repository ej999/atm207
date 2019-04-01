package ATM;

import javafx.application.Application;

import java.util.Collections;
import java.util.HashMap;
import java.util.Observable;

/**
 * An ATM that allows customers and employees to conduct a range of financial transactions and operations.
 * It is displayed on GUI.
 */
public class ATM extends Observable {
    // Declare and instantiate all the ATM components.
    static final ManagersSerialization serialization;
    static final UserManager userManager;
    static final AccountManager accountManager;
    static final BanknoteManager banknoteManager;
    static final ETransferManager eTransferManager;
    static final TradingSystem tradingSystem;

    static {
        serialization = new ManagersSerialization();
        userManager = new UserManager();
        accountManager = new AccountManager();
        banknoteManager = new BanknoteManager();
        eTransferManager = new ETransferManager();
        tradingSystem = new TradingSystem();
    }

    public static void main(String[] args) {
        // Comment out the following to disable java.util.logging for debugging.
//        Logger.getLogger("").setLevel(Level.OFF);

        serialization.deserializeAll();
        createDemoData();

        // Java FX -> invoke start method
        new Thread(() -> Application.launch(ATMFrame.class)).start();


        for (Account a : accountManager.account_map.values()) {
            if (a instanceof GIC) {
                ((GIC) a).newDay();
            }
            if (a instanceof Saving) {
                ((Saving) a).newDay();
            }
            if (a instanceof Youth) {
                ((Youth) a).newDay();
            }
        }
    }

    private static void createDemoData() {
        // If the any of the following groups of objects is empty or deleted, then create a demo and save it .
        if (userManager.user_map.isEmpty() || accountManager.account_map.isEmpty() || banknoteManager.banknotes.isEmpty()) {
            if (userManager.user_map.isEmpty()) {
                userManager.createAccount(BankManager.class.getSimpleName(), "jen", "1234");
                userManager.createAccount(Teller.class.getSimpleName(), "pete", "1234");
                userManager.createAccount(Customer.class.getSimpleName(), "steve", "1234");
            }

            if (accountManager.account_map.isEmpty()) {
                accountManager.addAccount(CreditCard.class.getSimpleName(), Collections.singletonList("steve"));
                accountManager.addAccount(Youth.class.getSimpleName(), Collections.singletonList("steve"));
                accountManager.addAccount(Saving.class.getSimpleName(), Collections.singletonList("steve"));
                accountManager.addAccount(Chequing.class.getSimpleName(), Collections.singletonList("steve"));
                accountManager.addAccount(CreditLine.class.getSimpleName(), Collections.singletonList("steve"));
            }

            if (banknoteManager.banknotes.isEmpty()) {
                banknoteManager.banknotes = new HashMap<>();
                for (int d : banknoteManager.DENOMINATIONS) {
                    banknoteManager.banknotes.put(String.valueOf(d), 50);
                }
            }
        }
    }

}