package phase1;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 * A class to provide the available options to the Login user base on their login account type, bank account balance,
 * and type of owned accounts.
 */
class Options {
    /**
     * Storing all available options: description as keys, and their methods as values.
     */
    private final LinkedHashMap<String, Thread> options;
    /**
     * The login account of the current logged-in user.
     * It is set to null if a user is logout or the login is not valid at the first place.
     */
    private Login loginUser;
    /**
     * Display available options for the logged-in user.
     */
    private boolean helped = false;

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
            options.put("Create a login for a user", new Thread(this::createLoginPrompt));

            options.put("Create a bank account for a user", new Thread(this::createAccountPrompt));

            options.put("Restock the ATM", new Thread(this::restockPrompt));

            options.put("Undo the most recent transaction on a user's account", new Thread(this::undoPrompt));

            options.put("Change password", new Thread(this::setPasswordPrompt));

            options.put("Logout", new Thread(this::logoutPrompt));
        } else if (loginUser instanceof Login_Customer) {
            options.put("Show my account summary", new Thread(() -> System.out.println(loginUser)));

            //TODO
            options.put("Make a Payment/Transfer", new Thread(this::setPasswordPrompt));

            //TODO deposit money into their account by entering a cheque or cash into the machine
            // (This will be simulated by individual lines in an input file called deposits.txt.
            // You can decide the format of the file. This will increase their balance.)
            options.put("Cash/Cheque Deposit", new Thread(this::setPasswordPrompt));

            //TODO
            options.put("Cash Withdrawal", new Thread(this::setPasswordPrompt));

            //TODO
            options.put("Request Creating an Account", new Thread(this::setPasswordPrompt));

            options.put("Change Primary Account", new Thread(this::setPrimaryPrompt));

            options.put("Change Password", new Thread(this::setPasswordPrompt));

            options.put("Logout", new Thread(this::logoutPrompt));
        }
    }

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
                return;
            }
            i++;
        }
    }


    private void logoutPrompt() {
        //Every time the user logs out, the LoginManager's contents will be serialized and saved.
        LoginManagerBackup backUp = new LoginManagerBackup();
        try {
            FileOutputStream fileOut =
                    new FileOutputStream("LoginManagerStorage.txt");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(backUp);
            out.close();
            fileOut.close();
            System.err.print("Serialized data saved. ");
        } catch (IOException i) {
            i.printStackTrace();
        }

        System.out.println("Your account has been logged out. Thank you for choosing CSC207 Bank!");
        System.out.println("===========================================================\n");

        // Logout the current user by assigning the loginUser to null.
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
    }

    private void createAccountPrompt() {
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter username: ");
        String username = reader.next();
        if (LoginManager.checkLoginExistence(username)) {
            System.out.println("Enter account type : \n" +
                    "Chequing \nSaving \nCredit Card \nLine of Credit ");
            String account = reader.next();
            ((Login_Employee_BankManager) loginUser).addAccount(account, (Login_Customer) LoginManager.getLogin(username));
            System.out.println("Command runs successfully.");
        }
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
        ArrayList<Integer> restock = new ArrayList<>();
        restock.add(fives);
        restock.add(tens);
        restock.add(twenties);
        restock.add(fifties);
//        HashMap<Integer, Integer> restock = new HashMap<>();
//        restock.put(5, fives);
//        restock.put(10, tens);
//        restock.put(20, twenties);
//        restock.put(50, fifties);
        //addToBill(restock, );
        Cash.cashDeposit(restock);
    }


    private void undoPrompt() {
        Scanner reader = new Scanner(System.in);
        boolean finished = false;
        while (!finished) {
            System.out.println("Enter username: ");
            String username = reader.next();
            if (LoginManager.checkLoginExistence(username)) {
                Login customer = LoginManager.getLogin(username);
                System.out.println("Which account to undo: ");
                ArrayList<Account> accounts = ((Login_Customer) customer).getAccounts();
                int i = 1;
                for (Account a : accounts) {
                    System.out.println("" + i + ". " + a);
                }
                int option = reader.nextInt();
                try {
                    Account account2undo = accounts.get(option);
                    account2undo.undoMostRecentTransaction();
                    finished = true;
                    System.out.println("Undo successful.");
                } catch (IndexOutOfBoundsException f) {
                    System.out.println("invalid selection. try again?(y/n)");
                    String proceed = reader.next();
                    if (proceed.equals("n")) finished = true;
                }
            } else {
                System.out.println("User not found. Try again? (y/n)");
                String proceed = reader.next();
                if (proceed.equals("n")) finished = true;
            }
        }
    }

    private void setPasswordPrompt() {
        System.out.print("\nPlease enter a new password: ");
        Scanner reader2 = new Scanner(System.in);
        String newPass = reader2.nextLine();
        loginUser.setPassword(newPass);
    }

    private void setPrimaryPrompt() {
        System.out.println("\nA primary chequing account will be the default destination for deposits.");

        if (((Login_Customer) loginUser).hasMoreThanOneChequing()) {
            System.out.println("\n\u001B[1mAccount Type\t\t\tCreation Date\t\t\t\t\tBalance\t\tMost Recent Transaction" +
                    "\u001B[0m");
            int i = 1;
            for (Account a : ((Login_Customer) loginUser).getAccounts()) {
                if (a instanceof Account_Asset_Chequing) {
                    System.out.println("[" + i + "] " + a);
                }
                i++;
            }

            System.out.print("Please choose the account you would like to set as Primary by entering the corresponding number: ");
            Scanner reader = new Scanner(System.in);
            int selected = reader.nextInt();
            ((Login_Customer)loginUser).setPrimary(((Login_Customer)loginUser).getAccounts().get(selected-1));
        } else {
            System.out.println("Sorry, you can only change your primary account if you have more than one chequing " +
                    "account.\nYou are welcome to request creating a new chequing account at anytime.");
        }


    }

//    private void viewBalancePrompt() {
//        StringBuilder returnMessage = new StringBuilder();
//        System.out.println("\n\u001B[1mAccount Type\t\tCreation Date\t\t\t\t\tBalance\t\tMost Recent Transaction\u001B[0m");
//        for(Account account:((Login_Customer)loginUser).getAccounts()){
//            System.out.println(account);
//        }
//
//        double netTotal = ((Login_Customer) loginUser).netTotal();
//        System.out.println("\n\u001B[1mYour net total is \u001B[0m$" + netTotal);
//    }

//    private void viewAccountPrompt() {
//        System.out.println("\n\u001B[1m    Account Type\t\tCreation Date\t\t\t\t\tBalance\t\tMost Recent Transaction\u001B[0m");
//        HashMap<Integer, Account> option = new HashMap<>();
//        int i = 1;
//        for(Account account: ((Login_Customer)loginUser).getAccounts()){
//            System.out.println("[" + i + "] " + account.toString());
//            option.put(i, account);
//            i += 1;
//        }
//        System.out.print("Please select the account you would like to work with: ");
//        Scanner reader = new Scanner(System.in);
//        int accountNumber = reader.nextInt();
//        selectAccountPrompt(option.get(accountNumber));
//    }

//    private void selectAccountPrompt(Account account){
//        System.out.println("[1] Make a Payment.");
//        System.out.println("[2] Make a Transfer.");
//        System.out.println("Please enter the corresponding number: ");
//        Scanner reader = new Scanner(System.in);
//        int choice = reader.nextInt();
//        switch(choice){
//            case 1:
//                break;
//
//        }
//    }

}
