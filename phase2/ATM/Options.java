package ATM;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 * A class to provide the available options to the SystemUser user base on their login account type, bank account balance,
 * and type of owned accounts.
 */
class Options {
    /**
     * Storing all available options: description as keys, and their methods as values.
     */
    private final LinkedHashMap<String, Thread> options;

    /**
     * The login account of the current logged-in user.
     */
    private SystemUser systemUser;

    private boolean helped = false;

    Options(SystemUser systemUser) {
        this.systemUser = systemUser;
        this.options = new LinkedHashMap<>();

        // create, display. and allow logged-in user to select option.
        while (this.systemUser != null) {
            createOptions();
            displayOptions();
            selectOptions();

            // Since a Thread cannot be restarted, options has to be recreated every time after a user selects an option.
            options.clear();
        }
    }

    private void createOptions() {
        if (systemUser instanceof SystemUser_Employee_BankManager) {
            options.put("Read alerts", new Thread(this::readAlertPrompt));

            options.put("Create a login for a user", new Thread(this::createLoginPrompt));

            options.put("Create a bank account for a user", new Thread(this::createAccountPrompt));

            options.put("Restock the ATM", new Thread(this::restockPrompt));

            options.put("Undo the most recent transaction on a user's account", new Thread(this::undoPrompt));

            options.put("Change password", new Thread(this::setPasswordPrompt));

            options.put("Load custom bank data", new Thread(this::loadCustomPrompt));

            options.put("Clear all bank data", new Thread(this::clearDataPrompt));

            options.put("Logout", new Thread(this::logoutPrompt));

        } else if (systemUser instanceof SystemUser_Employee_Teller) {
            options.put("Read alerts", new Thread(this::readAlertPrompt));

            options.put("Create a bank account for a user", new Thread(this::createAccountPrompt));

            options.put("Change password", new Thread(this::setPasswordPrompt));

            options.put("Undo the most recent transaction on a user's account", new Thread(this::undoPrompt));

            options.put("Logout", new Thread(this::logoutPrompt));


        } else if (systemUser instanceof SystemUser_Customer) {

            options.put("Show my account summary", new Thread(() -> System.out.println(systemUser)));

            options.put("Pay a Bill", new Thread(this::payBillPrompt));

            options.put("Make a Transfer between my Accounts", new Thread(this::transferBetweenAccountsPrompt));

            options.put("Make a Transfer to another User", new Thread(this::transferToAnotherUserPrompt));

            options.put("Cash/Cheque Deposit", new Thread(this::depositPrompt));

            options.put("Cash Withdrawal", new Thread(this::withdrawalPrompt));

            options.put("Request Creating an Account", new Thread(this::requestAccountPrompt));

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
        System.out.println("\nThe option [" + selected + "] is not valid. Please double-checked the number you entered.");
    }

    /**
     * Gets username and password from input and tells BankManager to create the customer.
     */
    private void createLoginPrompt() {
        Scanner reader = new Scanner(System.in);
        System.out.print("Creating SystemUser... Enter user type (Customer, Teller):");
        String type = reader.next();
        System.out.print(" Enter username: ");
        String username = reader.next();
        System.out.print("Enter password: ");
        String password = reader.next();
        ((SystemUser_Employee_BankManager) systemUser).createLogin(type, username, password);
    }

    private String selectAccountTypePrompt() {
        Scanner reader = new Scanner(System.in);
        System.out.println("\n[1] Chequing");
        System.out.println("[2] Saving");
        System.out.println("[3] Credit Card");
        System.out.println("[4] Line of Credit");

        System.out.print("Please enter account type by number [1-4]: ");
        int account = reader.nextInt();
        switch (account) {
            case 1: {
                return "Chequing";
            }
            case 2: {
                return "Saving";
            }
            case 3: {
                return "CreditCard";
            }
            case 4: {
                return "LineOfCredit";
            }
            default:
                return null;
        }
    }

    /**
     * Gets username and tells BankManager to create the specified account for the customer
     */
    private void createAccountPrompt() {
        Scanner reader = new Scanner(System.in);
        System.out.print("Please enter username: ");
        String username = reader.next();
        if (LoginManager.checkLoginExistence(username)) {
            String accountType = selectAccountTypePrompt();
            ((SystemUser_Employee_BankManager) systemUser).addAccount(accountType, (SystemUser_Customer) LoginManager.getLogin(username));
        } else {
            System.out.println("The username does not exist. No account has been created.");
        }
    }

    /**
     * Restocks the bank machine based on input.
     * Only the BankManager is able to access this.
     */
    private void restockPrompt() {
        Scanner reader = new Scanner(System.in);
        System.out.print("Enter amount of 5 dollar bills: ");
        int fives = reader.nextInt();
        System.out.print("Enter amount of 10 dollar bills: ");
        int tens = reader.nextInt();
        System.out.print("Enter amount of 20 dollar bills: ");
        int twenties = reader.nextInt();
        System.out.print("Enter amount of 50 dollar bills: ");
        int fifties = reader.nextInt();

        ArrayList<Integer> restock = new ArrayList<>();
        restock.add(fives);
        restock.add(tens);
        restock.add(twenties);
        restock.add(fifties);

        ((SystemUser_Employee_BankManager) systemUser).restockMachine(restock);
        System.out.println(fives + " 5-dollar-bill, " + tens + " 10-dollar-bill, " + twenties + " 20-dollar-bill, "
                + fifties + " 50-dollar-bill are successfully restocked. ");
    }

    private void logoutPrompt() {
        //Every time the user logs out, the LoginManager's contents will be serialized and saved.
        LoginManagerBackup backUp = new LoginManagerBackup();
        try {
            FileOutputStream fileOut = new FileOutputStream("phase1/LoginManagerStorage.txt");
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

        // Logout the current user by assigning the systemUser to null.
        this.systemUser = null;
    }

    private void loadCustomPrompt() {
        System.out.println("Please enter the name of the file you want to load from (don't include its extension.)" +
                " Note that it must be stored in the phase1 folder");
        Scanner reader1 = new Scanner(System.in);
        String answer = reader1.nextLine();
        LoginManagerBackup custom_loader = new LoginManagerBackup();
        HashMap<String, SystemUser> custom_map = custom_loader.loadCustom(answer);
        LoginManager.login_map = custom_map;
    }

    private void clearDataPrompt() {
        System.out.print("USE WITH CAUTION: all data including all login and bank account will be deleted! (Y/N): ");
        Scanner reader2 = new Scanner(System.in);
        String answer = reader2.nextLine();
        if (answer.equals("Y")) {
            LoginManagerBackup deleter = new LoginManagerBackup();
            deleter.deleteBackup();
            System.out.println("Data has been cleared. Please restart the program.");
            System.exit(0);
        }
    }

    private Account selectAccountPrompt(SystemUser_Customer customer) {
        return selectAccountPrompt(customer, "no_exclusion");
    }

    private Account selectAccountPrompt(SystemUser_Customer customer, String exclusion) {
        Scanner reader = new Scanner(System.in);

        System.out.println();
        ArrayList<Account> accounts = customer.getAccounts();
        int i = 1;
        for (Account a : accounts) {
            if (!a.getClass().getName().contains(exclusion)) {
                System.out.println("[" + i + "] " + a);
                i++;
            }
        }

        int option = -99;
        while (option > accounts.size() || option < 0) {
            System.out.print("Please select an account: ");
            option = reader.nextInt();
        }
        return accounts.get(option - 1);
    }

    /**
     * Gets customer by username and displays all their accounts
     * Select an account from input and tell that account to undo the last transaction
     * only BankManager can access this
     */
    private void undoPrompt() {
        Scanner reader = new Scanner(System.in);
        boolean finished = false;
        while (!finished) {
            System.out.print("Enter username: ");
            String username = reader.next();
            if (LoginManager.checkLoginExistence(username)) {
                Account account2undo = selectAccountPrompt((SystemUser_Customer) LoginManager.getLogin(username));
                ((SystemUser_Employee_BankManager) systemUser).undoMostRecentTransaction(account2undo);
                finished = true;
                System.out.println("Undo successful.");
            } else {
                System.out.print("User not found. Try again? (Y/N)");
                String proceed = reader.next().toUpperCase().trim();
                if (proceed.equals("N")) finished = true;
            }
        }
    }


    private void setPasswordPrompt() {
        System.out.print("\nPlease enter a new password: ");
        Scanner reader = new Scanner(System.in);
        String newPass = reader.nextLine();
        systemUser.setPassword(newPass);
    }

    /**
     * checks if the customer has more than one account and gets choice of primary account from input
     * tells the LoginCustomer to set chosen account as primary
     */
    private void setPrimaryPrompt() {
        System.out.println("\nA primary chequing account will be the default destination for deposits.");

        if (((SystemUser_Customer) systemUser).hasMoreThanOneChequing()) {
            System.out.println("\n\u001B[1mAccount Type\t\t\tCreation Date\t\t\t\t\tBalance\t\tMost Recent Transaction" +
                    "\u001B[0m");
            int i = 1;
            for (Account a : ((SystemUser_Customer) systemUser).getAccounts()) {
                if (a instanceof Account_Asset_Chequing) {
                    System.out.println("[" + i + "] " + a);
                }
                i++;
            }

            System.out.print("Please choose the account you would like to set as Primary by entering the corresponding number: ");
            Scanner reader = new Scanner(System.in);
            int selected = reader.nextInt();
            ((SystemUser_Customer) systemUser).setPrimary(((SystemUser_Customer) systemUser).getAccounts().get(selected - 1));
        } else {
            System.out.println("Sorry, you can only change your primary account if you have more than one chequing " +
                    "account.\nHowever, you are welcome to request creating a new chequing account on main menu.");
        }


    }

    private void requestAccountPrompt() {
        String accountType = selectAccountTypePrompt();

        try {
            ((SystemUser_Customer) systemUser).requestAccount(accountType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void withdrawalPrompt() {
        Scanner reader = new Scanner(System.in);
        System.out.print("Please enter an amount: ");
        double amount = reader.nextDouble();
        double actualAmount = amount - amount % 5;

        Account account = selectAccountPrompt((SystemUser_Customer) systemUser);

        account.withdraw(actualAmount);


    }

    private void depositPrompt() {
        Account primary = ((SystemUser_Customer) systemUser).getPrimary();

        Scanner reader = new Scanner(System.in);
        System.out.println("Please make sure to ready your cash/cheque in deposit.txt");
        System.out.print("Enter any key to proceed... ");
        reader.next();

        try {
            primary.depositMoney();
        } catch (IOException e) {
            // do nothing?
        }
    }

    private void payBillPrompt() {
        Account account = selectAccountPrompt((SystemUser_Customer) systemUser, "CreditCard");

        Scanner reader = new Scanner(System.in);
        System.out.print("Please enter the amount you would like to pay: ");
        double amount = reader.nextDouble();
        System.out.print("Please enter the non-user account you would like to pay: ");
        String payee = reader.next();


        try {
            if (((Account_Transferable) account).payBill(amount, payee)) {
                System.out.println("Bill has been paid.");
            } else {
                System.out.println("Payment is unsuccessful.");
            }
        } catch (IOException e) {
            // do nothing?
        }
    }

    private void transferBetweenAccountsPrompt() {
        System.out.println("Now select the account you would like to transfer FROM: ");
        Account from = selectAccountPrompt((SystemUser_Customer) systemUser, "CreditCard");

        System.out.println("Now select the account you would like to transfer TO: ");
        Account to = selectAccountPrompt((SystemUser_Customer) systemUser);

        Scanner reader = new Scanner(System.in);
        System.out.print("Please enter the amount you would like to transfer: ");
        double amount = reader.nextDouble();

        if (((Account_Transferable) from).transferBetweenAccounts(amount, to)) {
            System.out.println("Transfer is successful.");
        } else {
            System.out.println("Transfer is unsuccessful.");
        }
    }

    private void transferToAnotherUserPrompt() {
        Scanner reader = new Scanner(System.in);

        Account from = selectAccountPrompt((SystemUser_Customer) systemUser, "CreditCard");

        System.out.print("Please enter username you would like to transfer to: ");
        String username = reader.next();

        System.out.print("Please enter the amount you would like to transfer: ");
        double amount = reader.nextDouble();

        if (LoginManager.checkLoginExistence(username)) {
            SystemUser_Customer user = (SystemUser_Customer) LoginManager.getLogin(username);
            ((Account_Transferable) from).transferToAnotherUser(amount, user, user.getPrimary());
            System.out.println("Transfer is successful.");
        } else {
            System.out.println("The username does not exist. Transfer is cancelled.");
        }
    }

    private void readAlertPrompt() {
        ((SystemUser_Employee_BankManager) systemUser).readAlerts();
    }
}
