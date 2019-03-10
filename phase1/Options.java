package phase1;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 * A class to provide the available options to the Login user base on their login account type, bank account balance,
 * and type of owned accounts.
 */
class Options {
    /**
     * The login account of the current logged-in user.
     * It is set to null if a user is logout or the login is not valid at the first place.
     */
    private Login loginUser;

    /**
     * Storing all available options: description as keys, and their methods as values.
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
            options.put("Undo the most recent transaction on a user's account.", new Thread(this::undoPrompt));
            options.put("Change password.", new Thread(this::setPasswordPrompt));
            options.put("Logout", new Thread(this::logoutPrompt));
//            options.put("Logout", new Thread(() -> {this.loggedOut = true;}));
        } else if (loginUser instanceof Login_Customer) {
            options.put("Show summary of all account balances.", new Thread(this::viewBalancePrompt));
            options.put("View an account.", new Thread(this::viewAccountPrompt));
            options.put("See net worth.", new Thread(this::netTotalPrompt));
            options.put("Change password.", new Thread(this::setPasswordPrompt));
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
            System.err.printf("Serialized data saved. ");
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
        System.out.println("Command runs successfully.");
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


    private void netTotalPrompt() {
        double netTotal = ((Login_Customer) loginUser).netTotal();
        System.out.println("Your net total is " + netTotal + ".");
    }



    private void undoPrompt(){
        Scanner reader = new Scanner(System.in);
        boolean finished = false;
        while (!finished){
            System.out.println("Enter username: ");
            String username = reader.next();
            if (LoginManager.checkLoginExistence(username)) {
                Login customer = LoginManager.getLogin(username);
                System.out.println("Which account to undo: ");
                ArrayList<Account> accounts = ((Login_Customer) customer).getAccounts();
                int i = 1;
                for (Account a: accounts){
                    System.out.println("" + i + ". " + a);
                }
                int option = reader.nextInt();
                try{
                    Account account2undo = accounts.get(option);
                    account2undo.undoMostRecentTransaction();
                    finished = true;
                    System.out.println("Undo successful.");
                } catch(IndexOutOfBoundsException f){
                    System.out.println("invalid selection. try again?(y/n)");
                    String proceed = reader.next();
                    if (proceed.equals("n")) finished = true;
                }
            }
            else {
                System.out.println("User not found. Try again? (y/n)");
                String proceed = reader.next();
                if (proceed.equals("n")) finished = true;
            }
        }
    }

    private void setPasswordPrompt() {
        System.out.print("Please enter a new password: ");
        Scanner reader2 = new Scanner(System.in);
        String newPass = reader2.nextLine();
        loginUser.setPassword(newPass);
        System.out.println("Command runs successfully.");
    }

    private void viewBalancePrompt() {
        StringBuilder returnMessage = new StringBuilder();
        System.out.println("\n\u001B[1mAccount Type\t\tCreation Date\t\t\t\t\tBalance\u001B[0m");
        for(Account account:((Login_Customer)loginUser).getAccounts()){
            System.out.println(account);
        }
    }

    private void viewAccountPrompt() {
        System.out.println("\n\u001B[1m    Account Type\t\tCreation Date\t\t\t\t\tBalance\u001B[0m");
        HashMap<Integer, Account> option = new HashMap<>();
        int i = 1;
        for(Account account: ((Login_Customer)loginUser).getAccounts()){
            System.out.println("[" + i + "] " + account.toString());
            option.put(i, account);
            i += 1;
        }
        System.out.print("Please select the account you would like to work with: ");
        Scanner reader = new Scanner(System.in);
        int accountNumber = reader.nextInt();
        selectAccountPrompt(option.get(accountNumber));
    }

    private void selectAccountPrompt(Account account){
        System.out.println("[1] Show account creation date.");
        System.out.println("[2] Show account balance.");
        System.out.println("[3] Show most recent transaction.");
        System.out.println("Please enter the corresponding number: ");
        Scanner reader = new Scanner(System.in);
        int choice = reader.nextInt();
        switch(choice){
            case 1:
                System.out.println(account.dateOfCreation);
                break;
            case 2:
                System.out.println(account.getBalance());
                break;
            case 3:
                System.out.println("Type :" + account.mostRecentTransaction.get("Type"));
                System.out.println("Amount :" + account.mostRecentTransaction.get("Amount"));
                break;

        }
    }

}
