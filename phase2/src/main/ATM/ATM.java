package ATM;

import javafx.application.Application;

import java.util.*;

/**
 * An ATM that allows customers and employees to conduct a range of financial transactions and operations.
 * It is displayed on GUI.
 */
//TODO: A User: "Customer NewUser! has a net total of ", is successfully created
// TODO: 2019-03-29 check if there's static class 
// TODO: 2019-03-29 controller class -> all -manager class should be an object variable inside a controller
// TODO: 2019-03-29 consider adding custom exception
// TODO: 2019-03-29 single responsibility: delegating chunky methods into inner class
// TODO: 2019-03-29 make Options obsolete
// TODO: 2019-03-30 display as Joint- if it's joint account.
public class ATM extends Observable {
    // Declare all its components as static variables.
    static ManagersSerialization serialization;
    static UserManager userManager;
    static AccountManager accountManager;
    static BanknoteManager banknoteManager;
    static ETransferManager eTransferManager;
    static TradingSystem tradingsystem;

    static {
        ATM.serialization = new ManagersSerialization();
        ATM.userManager = new UserManager();
        ATM.accountManager = new AccountManager();
        ATM.banknoteManager = new BanknoteManager();
        ATM.eTransferManager = new ETransferManager();
        ATM.tradingsystem = new TradingSystem();
    }

    public static void main(String[] args) {
        // Comment out the following to disable java.util.logging for debugging.
//        Logger.getLogger("").setLevel(Level.OFF);

        // Java FX -> invoke start method
        new Thread(() -> Application.launch(ATMFrame.class)).start();

        serialization.deserializeAll();
        createDemoData();

        for (Account a : accountManager.account_map.values()) {
            if (a instanceof GIC) {
                ((GIC) a).newDay();
            }
            if (a instanceof Saving) {
                ((Saving) a).newDay();

            }
        }


        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        int now = (calendar.get(Calendar.MONTH));

        // The ATM should displays User interface all the time, until it is being shut down.
        //noinspection InfiniteLoopStatement
        while (true) {
            // Constantly checking if now is the start of the month.
//            now = isNewMonth(now);
//
//            // A login session.
//            User user = authPrompt();
//            new Options(user);
        }
    }

    private static void createDemoData() {
        // If the any of the following groups of objects is empty or deleted, then create a demo and save it .
        if (userManager.user_map.isEmpty() || accountManager.account_map.isEmpty() || ATM.banknoteManager.banknotes.isEmpty()) {
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

            if (ATM.banknoteManager.banknotes.isEmpty()) {
                ATM.banknoteManager.banknotes = new HashMap<>();
                for (int d : ATM.banknoteManager.DENOMINATIONS) {
                    ATM.banknoteManager.banknotes.put(String.valueOf(d), 50);
                }
            }
        }
    }

//    private int isNewMonth(int past) {
//        Date today = new Date();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(today);
//        int now = (calendar.get(Calendar.MONTH));
//
//        setChanged();
//        notifyObservers(!(past == now));
//        return now;
//    }

    // It will return the User if the login is valid; otherwise, it'll continuity asking user to retry.
    private static User authPrompt() {
        System.out.println("Welcome to CSC207 Banking Service!");

        Scanner reader = new Scanner(System.in);

        String username = null;
        User user;
        boolean authResult = false;
        while (!authResult) {
            System.out.print("Please enter your username: ");
            username = reader.next();
            System.out.print("Please enter your password: ");
            String password = reader.next();

            authResult = userManager.auth(username, password);
        }
        user = userManager.getUser(username);

        System.out.println("\nUser success. Hi " + user.getUsername() + "!");
        return user;
    }


}