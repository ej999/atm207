package phase1;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 * A class to provide the available options to the Login user base on their login account type, bank account balance,
 * and type of owned accounts.
 */
class Options {
    private Login loginUser;

    /**
     * storing all associated option descriptions and their methods.
     */
    private LinkedHashMap<String, Thread> options;

    Options(Login loginUser) {
        this.loginUser = loginUser;
        this.options = new LinkedHashMap<>();
        createOptions();

        //noinspection InfiniteLoopStatement
        while (true) {
            displayOptions();
            selectOptions();

        }
    }

    private void createOptions() {
        if (loginUser instanceof Login_Employee_BankManager) {
            options.put("Create a login for a user.", new Thread(this::createLoginPrompt));
            options.put("Create a bank account for a user.", new Thread(this::createAccountPrompt));
            options.put("Restock the ATM.", new Thread(this::restockPrompt));
            options.put("Under the most recent transaction on a user's account.", new Thread(this::createLoginPrompt));
            options.put("Logout", new Thread(() -> {System.exit(0);}));
        } else if (loginUser instanceof Login_Customer) {
            //TODO move the Options-related methods from Login_customer to here.
            options.put("Show summary of all account balances.", new Thread(this::createLoginPrompt));
            options.put("View an account.", new Thread(this::createLoginPrompt));
//            options.put("See net worth.", new Thread(((Login_Customer) loginUser).netTotal()));
            options.put("Change password.", new Thread(this::createLoginPrompt));
        }
    }

    private boolean helped = false;
    private void displayOptions() {
        if (helped) {
            System.out.println("Is there anything we can help you?");
        } else {
            System.out.println("How can we help you today?");
        }

        int i = 0;
        for (String description : options.keySet()) {
            System.out.println("[" + (i+1) + "] " + description);
            i++;
        }
        helped = true;
    }

    private void selectOptions() {
        Scanner test = new Scanner(System.in);
        System.out.print("Please enter the corresponding number: ");
        int selected = test.nextInt();
        int i = 1;
        for (String key : options.keySet()) {
            if (selected == i) {
                options.get(key).start();
                try {
                    options.get(key).join();
                } catch (InterruptedException e) {
                    System.err.print("Main thread Interrupted");
                }
            }
            i++;
        }
    }

    private void createLoginPrompt() {
        Scanner reader = new Scanner(System.in);
        System.out.print("Creating Login...");
        System.out.print("Enter username: ");
        String username = reader.next();
        System.out.print("Enter password: ");
        String password = reader.next();
        ((Login_Employee_BankManager) loginUser).createLogin(username, password);
        System.out.println("Command runs successfully.");
        System.out.println();
    }

    private void createAccountPrompt() {
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter username: ");
        String username = reader.next();
        if (LoginManager.checkLoginExistence(username)) {
            System.out.println("Enter account type : \n" +
                    "Chequing \n Saving \n Credit Card \n Line of Credit ");
        }
        String account = reader.next();
        ((Login_Employee_BankManager) loginUser).addAccount(account, (Login_Customer) LoginManager.getLogin(username));
        System.out.println("Command runs successfully.");
        System.out.println();
    }

    private void restockPrompt() {
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter amount of 5 dollar bills: ");
        int fives = reader.nextInt();
        System.out.println("Enter amount of 10 dollar bills: ");
        int tens = reader.nextInt();
        System.out.println("Enter amount of 20 dollar bills: ");
        int twenties = reader.nextInt();
        System.out.println("Enter amount of 50 dollar bills: ");
        int fifties = reader.nextInt();
        HashMap<Integer, Integer> restock = new HashMap<>();
        restock.put(5, fives);
        restock.put(10, tens);
        restock.put(20, twenties);
        restock.put(50, fifties);
        //addToBill(restock, ); TODO: figure out how to access Cash: need to know where cash is stored
    }
}
