package ATM;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

/**
 * A class to provide the available options to the User user base on their login account type, bank account balance,
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
    private User user;

    private boolean helped = false;

    Options(User user) {
        this.user = user;
        this.options = new LinkedHashMap<>();

        // create, display. and allow logged-in user to select option.
        while (this.user != null) {
            createOptions();
            displayOptions();
            selectOptions();

            // Since a Thread cannot be restarted, options has to be recreated every time after a user selects an option.
            options.clear();
        }
    }

    private void createOptions() {
        if (user instanceof User_Employee_BankManager) {
            options.put("Read alerts", new Thread(this::readAlertPrompt));

            options.put("Create a user", new Thread(this::createUserPrompt));

            options.put("Create a bank account for a user", new Thread(this::createAccountPrompt));

            options.put("Restock the ATM", new Thread(this::restockPrompt));

            options.put("Undo the most recent transaction on a user's account", new Thread(this::undoPrompt));

            options.put("Change password", new Thread(this::setPasswordPrompt));

//            options.put("Load custom bank data", new Thread(this::loadCustomPrompt));

            options.put("Clear all bank data", new Thread(this::clearDataPrompt));

            options.put("Logout", new Thread(this::logoutPrompt));

        } else if (user instanceof User_Employee_Teller) {
            options.put("Read alerts", new Thread(this::readAlertPrompt));

            options.put("Create a bank account for a user", new Thread(this::createAccountPrompt));

            options.put("Change password", new Thread(this::setPasswordPrompt));

            options.put("Undo the most recent transaction on a user's account", new Thread(this::undoPrompt));

            options.put("Logout", new Thread(this::logoutPrompt));


        } else if (user instanceof User_Customer) {

            options.put("Show my account summary", new Thread(() -> System.out.println(user)));

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
        System.err.println("\nThe option [" + selected + "] is not valid. Please double-checked the number you entered.");
    }

    /**
     * Gets username and password from input and tells Bank Manager to create the customer.
     */
    private void createUserPrompt() {
        Scanner reader = new Scanner(System.in);
        System.out.print("Creating User... Enter user type (" + UserManager.getTypesOfAccounts() + "): ");
        String type = "ATM." + reader.next();
        System.out.print("Enter username: ");
        String username = reader.next();
        System.out.print("Enter password: ");
        String password = reader.next();
        boolean created = UserManager.createAccount(type, username, password);

        if (created && type.equals(User_Customer.class.getName())) {
            setDobPrompt(username);
        }
    }

    private void setDobPrompt(String username) {
        Scanner reader = new Scanner(System.in);
        System.out.print("Would you like to set the Date of Birth as well? (Y/N): ");
        String confirm = reader.next().toUpperCase();

        if (confirm.equals("Y")) {
            System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
            try {
                String dob = LocalDate.parse(reader.next()).toString();
                ((User_Customer) UserManager.getAccount(username)).setDob(dob);
                System.out.println("Date of Birth for " + username + " is set to " + dob + ".");
            } catch (DateTimeException e) {
                System.err.println("Are you sure you born on a day that doesn't exist?");
            }
        }
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
                return "CHEQUING";
            }
            case 2: {
                return "SAVINGS";
            }
            case 3: {
                return "CREDITCARD";
            }
            case 4: {
                return "LINEOFCREDIT";
            }
            case 5: {
                return "STUDENT";
            }
            default:
                return null;
        }
    }

    /**
     * Gets username and tells Bank Manager to create the specified account for the customer
     */
    private void createAccountPrompt() {
        Scanner reader = new Scanner(System.in);
        System.out.print("Please enter username: ");
        String username = reader.next();
        if (UserManager.isPresent(username)) {
            String accountType = selectAccountTypePrompt();
            AccountManager.addAccount(accountType, (User_Customer) UserManager.getAccount(username));
        } else {
            System.err.println("The username does not exist. No account has been created.");
        }
    }

    /**
     * Restocks the bank machine based on input.
     * Only the Bank Manager is able to access this.
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

        ((User_Employee_BankManager) user).restockMachine(restock);
        System.out.println(fives + " 5-dollar-bill, " + tens + " 10-dollar-bill, " + twenties + " 20-dollar-bill, "
                + fifties + " 50-dollar-bill are successfully restocked. ");
    }

    private void logoutPrompt() {
        //Every time the user logs out, the UserManager's contents will be serialized and saved to FireBase database.
        new UserManagerSerialization().serialize();

        System.out.println("Your account has been logged out. Thank you for choosing CSC207 Bank!");
        System.out.println("===========================================================\n");

        // Logout the current user by assigning the user to null.
        this.user = null;
    }

//    private void loadCustomPrompt() {
//        System.out.println("Please enter the name of the file you want to load from (don't include its extension.)" +
//                " Note that it must be stored in the phase1 folder");
//        Scanner reader1 = new Scanner(System.in);
//        String answer = reader1.nextLine();
//        UserManagerSerialization custom_loader = new UserManagerSerialization();
//        HashMap<String, User> custom_map = custom_loader.loadCustom(answer);
//        UserManager.account_map = custom_map;
//    }

    private void clearDataPrompt() {
        System.out.print("WARNING: Committing a fraud with value exceeding one million dollars might result in 14 year jail sentence! (Y/N): ");
        Scanner reader2 = new Scanner(System.in);
        String answer = reader2.nextLine();
        if (answer.equals("Y")) {
            new UserManagerSerialization().deleteDatabase();
            System.out.println("Data has been cleared. Good Luck!");
            System.exit(0);
        }
    }

    private Account selectAccountPrompt(User_Customer customer) {
        return selectAccountPrompt(customer, "no_exclusion");
    }

    private Account selectAccountPrompt(User_Customer customer, String exclusion) {
        Scanner reader = new Scanner(System.in);

        System.out.println();
        List<String> accounts = customer.getAccounts();
        int i = 1;
        for (String a : accounts) {
            if (!AccountManager.getAccount(a).getClass().getName().contains(exclusion)) {
                System.out.println("[" + i + "] " + AccountManager.getAccount(a));
                i++;
            }
        }

        int option = -99;
        while (option > accounts.size() || option < 0) {
            System.out.print("Please select an account: ");
            option = reader.nextInt();
        }
        return AccountManager.getAccount(accounts.get(option - 1));
    }

    /**
     * Gets customer by username and displays all their accounts
     * Select an account from input and tell that account to undo the last transaction
     * only Bank Manager can access this
     * TODO: Now the bank manager can undo n transactions.
     */
    private void undoPrompt() {
        Scanner reader = new Scanner(System.in);
        boolean finished = false;
        while (!finished) {
            System.out.print("Enter username: ");
            String username = reader.next();
            if (UserManager.isPresent(username)) {
                Account account2undo = selectAccountPrompt((User_Customer) UserManager.getUser(username));
                ((User_Employee_BankManager) user).undoTransactions(account2undo, 1);
                finished = true;
                System.out.println("Undo successful.");
                finished = true;
            } else {
                System.err.print("User not found. Try again? (Y/N)");
                String proceed = reader.next().toUpperCase().trim();
                if (proceed.equals("N")) finished = true;
            }
        }
    }


    private void setPasswordPrompt() {
        System.out.print("\nPlease enter a new password: ");
        Scanner reader = new Scanner(System.in);
        String newPass = reader.nextLine();
        user.setPassword(newPass);
    }

    /**
     * checks if the customer has more than one account and gets choice of primary account from input
     * tells the LoginCustomer to set chosen account as primary
     */
    private void setPrimaryPrompt() {
        System.out.println("\nA primary chequing account will be the default destination for deposits.");

        if (((User_Customer) user).hasMoreThanOneChequing()) {
            System.out.println("\n\u001B[1mAccount Type\t\t\tCreation Date\t\t\t\t\tBalance\t\tMost Recent Transaction" +
                    "\u001B[0m");
            int i = 1;
            for (String a : ((User_Customer) user).getAccounts()) {
                if (AccountManager.getAccount(a) instanceof Account_Asset_Chequing) {
                    System.out.println("[" + i + "] " + AccountManager.getAccount(a));
                }
                i++;
            }

            System.out.print("Please choose the account you would like to set as Primary by entering the corresponding number: ");
            Scanner reader = new Scanner(System.in);
            int selected = reader.nextInt();
            ((User_Customer) user).setPrimary(AccountManager.getAccount(((User_Customer) user).getAccounts().get(selected - 1)));
        } else {
            System.err.println("Sorry, you can only change your primary account if you have more than one chequing " +
                    "account.\nHowever, you are welcome to request creating a new chequing account on main menu.");
        }


    }

    private void requestAccountPrompt() {
        String accountType = selectAccountTypePrompt();

        try {
            ((User_Customer) user).requestAccount(accountType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void withdrawalPrompt() {
        Scanner reader = new Scanner(System.in);
        System.out.print("Please enter an amount: ");
        double amount = reader.nextDouble();
        double actualAmount = amount - amount % 5;

        Account account = selectAccountPrompt((User_Customer) user);

        account.withdraw(actualAmount);


    }

    private void depositPrompt() {
        Account primary = ((User_Customer) user).getPrimary();

        Scanner reader = new Scanner(System.in);
//        System.out.println("Please make sure to ready your cash/cheque in deposit.txt");
//        System.out.print("Enter any key to proceed... ");
//        reader.next();

        System.out.println("How much would you like to deposit?");
        double amount = Double.valueOf(reader.next());
        primary.deposit(amount);
    }

    private void payBillPrompt() {
        Account account = selectAccountPrompt((User_Customer) user, "CreditCard");

        Scanner reader = new Scanner(System.in);
        System.out.print("Please enter the amount you would like to pay: ");
        double amount = reader.nextDouble();
        System.out.print("Please enter the non-user account you would like to pay: ");
        String payee = reader.next();


        try {
            if (((Account_Transferable) account).payBill(amount, payee)) {
                System.out.println("Bill has been paid.");
            } else {
                System.err.println("Payment is unsuccessful.");
            }
        } catch (IOException e) {
            // do nothing?
        }
    }

    private void transferBetweenAccountsPrompt() {
        System.out.println("Now select the account you would like to transfer FROM: ");
        Account from = selectAccountPrompt((User_Customer) user, "CreditCard");

        System.out.println("Now select the account you would like to transfer TO: ");
        Account to = selectAccountPrompt((User_Customer) user);

        Scanner reader = new Scanner(System.in);
        System.out.print("Please enter the amount you would like to transfer: ");
        double amount = reader.nextDouble();

        if (((Account_Transferable) from).transferBetweenAccounts(amount, to)) {
            System.out.println("Transfer is successful.");
        } else {
            System.err.println("Transfer is unsuccessful.");
        }
    }

    private void transferToAnotherUserPrompt() {
        Scanner reader = new Scanner(System.in);

        Account from = selectAccountPrompt((User_Customer) user, "CreditCard");

        System.out.print("Please enter username you would like to transfer to: ");
        String username = reader.next();

        System.out.print("Please enter the amount you would like to transfer: ");
        double amount = reader.nextDouble();

        if (UserManager.isPresent(username)) {
            User_Customer user = (User_Customer) UserManager.getAccount(username);
            ((Account_Transferable) from).transferToAnotherUser(amount, user, user.getPrimary());
            System.out.println("Transfer is successful.");
        } else {
            System.err.println("The username does not exist. Transfer is cancelled.");
        }
    }

    private void readAlertPrompt() {
        ((User_Employee_BankManager) user).readAlerts();
    }
//WIP
//    private void tradePrompt() {
//        Scanner reader = new Scanner(System.in);
//        System.out.println("Would you like to: 1) Put up a trade offer 2) Remove a trade");
//        int choice = reader.nextInt();
//
//    }
//
//    private void makeTrade(){
//        System.out.println("Would you like to BUY or SELL?");
//        Scanner reader = new Scanner(System.in);
//        String choice = reader.nextLine();
//        System.out.println("What item would you like to sell?");
//
//    }
}


