package phase1;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 * A class to provide the available options to the Login user base on their login account type, bank account balance,
 * and type of owned accounts.
 */
class Options {
    /**
     * It is set to null if a user is logout or the login is not valid at the first place.
     */
    private Login loginUser;

    /**
     * Storing all associated option descriptions and their methods.
     */
    private LinkedHashMap<String, Thread> options;

    Options(Login loginUser) {
        this.loginUser = loginUser;
        this.options = new LinkedHashMap<>();

        // display and allow logged-in user to select option as long as the login is still valid.
        while (this.loginUser != null) {
            createOptions();
            displayOptions();
            selectOptions();

            // need to be clear and create again since a Thread cannot be restarted.
            options.clear();
        }
    }

    /**
     * Create available options for the logged-in user.
     */
    private void createOptions() {
        if (loginUser instanceof Login_Employee_BankManager) {
            options.put("Create a login for a user.", new Thread(this::createLoginPrompt));
            options.put("Create a bank account for a user.", new Thread(this::createAccountPrompt));
            options.put("Restock the ATM.", new Thread(this::restockPrompt));
            options.put("Under the most recent transaction on a user's account.", new Thread(this::createLoginPrompt));
            options.put("Logout", new Thread(this::logoutPrompt));
//            options.put("Logout", new Thread(() -> {this.loggedOut = true;}));
        } else if (loginUser instanceof Login_Customer) {
            //TODO move the Options-related methods from Login_customer to here.
            options.put("Show summary of all account balances.", new Thread(this::createLoginPrompt));
            options.put("View an account.", new Thread(this::createLoginPrompt));
            options.put("See net worth.", new Thread(this::netTotalPrompt));
            options.put("Change password.", new Thread(this::createLoginPrompt));
            options.put("Logout", new Thread(this::logoutPrompt));
        }
    }

    /**
     * Display available options for the logged-in user.
     */
    private boolean helped = false;

    private void displayOptions() {
        if (helped) {
            System.out.println("\nIs there anything we can help you?");
        } else {
            System.out.println("\nHow can we help you today?");
        }

        int i = 0;
        for (String description : options.keySet()) {
            System.out.println("[" + (i + 1) + "] " + description);
            i++;
        }
        helped = true;
    }

    /**
     * Allow logged-in user to select available options.
     */
    private void selectOptions() {
        Scanner test = new Scanner(System.in);
        System.out.print("Please enter the corresponding number: ");
        int selected = test.nextInt();
        int i = 1;
        for (String key : options.keySet()) {
            if (selected == i) {
                options.get(key).start();
                // Waits for this thread to die. Check join() method in Thread for more details.
                try {
                    options.get(key).join();
                } catch (InterruptedException e) {
                    System.err.print("Main thread Interrupted");
                }
                options.get(key).interrupt();
                options.get(key).stop();
            }
            i++;
        }
    }


    private void logoutPrompt() {
        System.out.println("Have a nice day, " + this.loginUser.getUsername() + "!");
        System.out.println("===========================================================\n");
        LoginManagerBackup backUp = new LoginManagerBackup();
        try {
            FileOutputStream fileOut =
                    new FileOutputStream("LoginManagerStorage.txt");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(backUp);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data saved.");
        } catch (IOException i) {
            i.printStackTrace();
        }
        this.loginUser = null;
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


    private void netTotalPrompt() {
        double netTotal = ((Login_Customer) loginUser).netTotal();
        System.out.println("Your net total is " + netTotal + ".");
    }


}
