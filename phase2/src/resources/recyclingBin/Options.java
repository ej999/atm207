//package ATM;
//
//import java.io.IOException;
//import java.time.DateTimeException;
//import java.time.LocalDate;
//import java.util.*;
//
//import static ATM.ATM.*;
//
///**
// * A class to provide the available options to the User base on their login account type, bank account balance,
// * and type of owned accounts.
// */
//class Options {
//    /**
//     * Storing all available options: description as keys, and their methods as values.
//     */
//    private final LinkedHashMap<String, Thread> options;
//
//    private User current_user;
//
//    private boolean helped = false;
//
//    Options(User current_user) {
//        this.current_user = current_user;
//        this.options = new LinkedHashMap<>();
//
//        // Create, display. and allow user to select option.
//        while (this.current_user != null) {
//            createOptions();
//            displayOptions();
//            selectOptions();
//
//            // Since a Thread cannot be restarted, options has to be recreated every time after a user selects an option.
//            options.clear();
//
//            serialization.serializeAll();
//        }
//    }
//
//    private void createOptions() {
//        if (current_user instanceof BankManager) {
//            options.put("Read alerts", new Thread(this::readAlertPrompt));
//
//            options.put("Create a user", new Thread(this::createUserPrompt));
//
//            options.put("Create a bank account for a customer", new Thread(this::createAccountPrompt));
//
//            options.put("Restock the ATM", new Thread(this::restockPrompt));
//
//            options.put("Undo the most recent transaction on a customer", new Thread(this::undoPrompt));
//
//            options.put("Change password", new Thread(this::setPasswordPrompt));
//
////            options.put("Clear all bank data", new Thread(this::clearDataPrompt));
//
//            options.put("Logout", new Thread(this::logoutPrompt));
//
//        } else if (current_user instanceof Teller) {
//            options.put("Read alerts", new Thread(this::readAlertPrompt));
//
//            options.put("Create a bank account for a user", new Thread(this::createAccountPrompt));
//
//            options.put("Change password", new Thread(this::setPasswordPrompt));
//
//            options.put("Undo the most recent transaction on a user's account", new Thread(this::undoPrompt));
//
//            options.put("Logout", new Thread(this::logoutPrompt));
//
//
//        } else if (current_user instanceof Customer) {
//
//            options.put("Show my account summary", new Thread(() -> System.out.println(current_user)));
//
//            options.put("Pay a Bill", new Thread(this::payBillPrompt));
//
//            options.put("Make a Transfer between my Accounts", new Thread(this::transferBetweenAccountsPrompt));
//
//            options.put("Make a Transfer to another User", new Thread(this::transferToAnotherUserPrompt));
//
//            options.put("Banknote/Cheque Deposit", new Thread(this::depositPrompt));
//
//            options.put("Banknote Withdrawal", new Thread(this::withdrawalPrompt));
//
//            options.put("Request Creating an Account", new Thread(this::requestAccountPrompt));
//
//            options.put("Change Primary Account", new Thread(this::setPrimaryPrompt));
//
//            options.put("Change Password", new Thread(this::setPasswordPrompt));
//
//            options.put("Add sell offer", new Thread(this::addSellOffer));
//
//            options.put("Add buy offer", new Thread(this::addBuyOffer));
//
//            options.put("See offers", new Thread(this::seeOffers));
//
//            options.put("Deposit item to inventory", new Thread(this::addToInventory));
//
//            options.put("View inventory", new Thread(this::viewInventoryPrompt));
//
//            options.put("ETransfers", new Thread(this::eTransferPrompt));
//
//
//            options.put("Logout", new Thread(this::logoutPrompt));
//
//        }
//    }
//
//    private void displayOptions() {
//        if (helped) {
//            System.out.println("\nIs there anything we can help you?");
//        } else {
//            System.out.println("\nHow can we help you today?");
//        }
//
//        int i = 0;
//        for (String description : options.keySet()) {
//            System.out.println("[" + (i + 1) + "] " + description);
//            i++;
//        }
//        helped = true;
//    }
//
//    private void selectOptions() {
//        Scanner test = new Scanner(System.in);
//        System.out.print("Please enter the corresponding number: ");
//
//        int selected;
//        try {
//            selected = test.nextInt();
//
//            int i = 1;
//            for (String key : options.keySet()) {
//                if (selected == i) {
//                    options.get(key).start();
//                    // Waits for this thread to die. Check isNotJoint() method in Thread for more details.
//                    try {
//                        options.get(key).join();
//                    } catch (InterruptedException e) {
//                        System.err.print("Main thread Interrupted");
//                    }
//                    return;
//                }
//                i++;
//            }
//            System.err.println("The option [" + selected + "] is not valid. Please double-checked the number you entered");
//        } catch (InputMismatchException e) {
//            System.err.println("The option is not valid. Please double-checked the number you entered");
//        }
//
//
//    }
//
//    /**
//     * Gets username and password from input and tells Bank Manager to create the customer.
//     */
//    private void createUserPrompt() {
//        Scanner reader = new Scanner(System.in);
//        System.out.print("Creating User... Enter user type " + userManager.USER_TYPE_NAMES + ": ");
//        String typeSimpleName = reader.next();
//        System.out.print("Enter username: ");
//        String username = reader.next();
//        System.out.print("Enter password: ");
//        String password = reader.next();
//        boolean created = userManager.createAccount(typeSimpleName, username, password);
//
//        if (created && typeSimpleName.equals(Customer.class.getName())) {
//            setDobPrompt(username);
//        }
//    }
//
//    private void setDobPrompt(String username) {
//        Scanner reader = new Scanner(System.in);
//        System.out.print("Would you like to set the Date of Birth as well? (Y/N): ");
//        String confirm = reader.next().toUpperCase();
//
//        if (confirm.equals("Y")) {
//            System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
//            try {
//                String dob = LocalDate.parse(reader.next()).toString();
//                ((Customer) userManager.getUser(username)).setDob(dob);
//                System.out.println("Date of Birth for " + username + " is set to " + dob);
//            } catch (DateTimeException e) {
//                System.err.println("Are you sure you born on a day that doesn't exist?");
//            }
//        }
//    }
//
//    private String selectAccountTypePrompt() {
//        Scanner reader = new Scanner(System.in);
//        System.out.print("Enter account type " + accountManager.TYPES_OF_ACCOUNTS + ": ");
//        return Options.class.getPackage().getName() + "." + reader.next();
//    }
//
//    /**
//     * Bank Manager creates an account for the Customer.
//     */
//    private void createAccountPrompt() {
//        Scanner reader = new Scanner(System.in);
//        System.out.print("Please enter username: ");
//        String username = reader.next();
//        if (userManager.isPresent(username) && userManager.isCustomer(username)) {
//            String accountType = selectAccountTypePrompt();
//            accountManager.addAccount(accountType, Collections.singletonList(username));
//        } else {
//            System.err.println("Invalid customer. Please try again");
//        }
//    }
//
//    /**
//     * Restocks the bank machine based on input.
//     * Only the Bank Manager is able to access this.
//     */
//    private void restockPrompt() {
//        Map<Integer, Integer> restock = selectBillsPrompt();
//
//        ((BankManager) current_user).restockMachine(restock);
//        System.out.println("are successfully restocked. ");
//    }
//
//    private Map<Integer, Integer> selectBillsPrompt() {
//        Scanner reader = new Scanner(System.in);
//
//        StringBuilder print = new StringBuilder();
//        Map<Integer, Integer> selectedBills = new HashMap<>();
//        for (Integer d : banknoteManager.DENOMINATIONS) {
//            System.out.print("Enter amount of $" + d + " dollar bill: ");
//            int amount = reader.nextInt();
//
//            selectedBills.put(d, amount);
//            print.append(amount).append(" of $").append(d).append("-bill, ");
//        }
//        System.out.println(print);
//        return selectedBills;
//    }
//
//    private void logoutPrompt() {
//        System.out.println("Your account has been logged out. Thank you for choosing CSC-Bank!");
//        System.out.println("===========================================================\n");
//
//        // Logout the current user.
//        this.current_user = null;
//    }
//
////    private void clearDataPrompt() {
////        System.out.print("WARNING: Committing a fraud with value exceeding one million dollars might result in 14 year jail sentence! (Y/N): ");
////        Scanner reader2 = new Scanner(System.in);
////        String response = reader2.nextLine();
////        if (response.equals("Y")) {
////            ManagersSerialization.deleteDatabase();
////            System.out.println("Data has been cleared. Good Luck!");
////            System.exit(0);
////        }
////    }
//
//    private Account selectAccountPrompt(Customer customer) {
//        return selectAccountPrompt(customer, "no_exclusion");
//    }
//
//    private Account selectAccountPrompt(Customer customer, String exclusion) {
//        Scanner reader = new Scanner(System.in);
//
//        System.out.println();
//        List<String> accounts = customer.getAccountIDs();
//        int i = 1;
//        for (String a : accounts) {
//            if (!Objects.requireNonNull(accountManager.getAccount(a)).getClass().getName().contains(exclusion)) {
//                System.out.println("[" + i + "] " + accountManager.getAccount(a));
//                i++;
//            }
//        }
//
//        int option = -99;
//        while (option > accounts.size() || option < 0) {
//            System.out.print("Please select an account: ");
//            option = reader.nextInt();
//        }
//        return accountManager.getAccount(accounts.get(option));
//    }
//
//    /**
//     * Gets customer by username and displays all their accounts
//     * Select an account from input and tell that account to undo the last transaction
//     * only Bank Manager can access this
//     */
//    private void undoPrompt() {
//        Scanner reader = new Scanner(System.in);
//        boolean finished = false;
//        while (!finished) {
//            System.out.print("Enter username: ");
//            String username = reader.next();
//            if (userManager.isPresent(username)) {
//                Account account2undo = selectAccountPrompt((Customer) userManager.getUser(username));
//                account2undo.undoTransactions(1);
//                System.out.println("Undo successful");
//                finished = true;
//            } else {
//                System.err.print("User not found. Try again? (Y/N)");
//                String proceed = reader.next().toUpperCase().trim();
//                if (proceed.equals("N")) finished = true;
//            }
//        }
//    }
//
//
//    private void setPasswordPrompt() {
//        System.out.print("\nPlease enter a new password: ");
//        Scanner reader = new Scanner(System.in);
//        String newPass = reader.nextLine();
//        current_user.setPassword(newPass);
//    }
//
//    /**
//     * checks if the customer has more than one account and gets choice of primary account from input
//     * tells the LoginCustomer to set chosen account as primary
//     */
//    private void setPrimaryPrompt() {
//        System.out.println("\nA primary chequing account will be the default destination for deposits");
//
//        if (((Customer) current_user).hasMoreThanOneChequing()) {
//            System.out.println("\n\u001B[1mAccount Type\t\t\tCreation Date\t\t\t\t\tBalance\t\tMost Recent Transaction" +
//                    "\u001B[0m");
//            int i = 1;
//            for (String a : ((Customer) current_user).getAccountIDs()) {
//                if (accountManager.getAccount(a) instanceof Chequing) {
//                    System.out.println("[" + i + "] " + accountManager.getAccount(a));
//                }
//                i++;
//            }
//
//            System.out.print("Please choose the account you would like to set as Primary by entering the corresponding number: ");
//            Scanner reader = new Scanner(System.in);
//            int selected = reader.nextInt();
//            ((Customer) current_user).setPrimaryAccount(accountManager.getAccount(((Customer) current_user).getAccountIDs().get(selected - 1)));
//        } else {
//            System.err.println("Sorry, you can only change your primary account if you have more than one chequing " +
//                    "account.\nHowever, you are welcome to request creating a new chequing account on main menu");
//        }
//
//
//    }
//
//    private void requestAccountPrompt() {
//        String accountType = selectAccountTypePrompt();
//
//        try {
//            ((Customer) current_user).requestAccount(accountType);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void withdrawalPrompt() {
//        Scanner reader = new Scanner(System.in);
//        System.out.print("Please enter an amount: ");
//
//        double amount = reader.nextDouble();
//        double actualAmount = amount - amount % 5;
//
//        Account account = selectAccountPrompt((Customer) current_user);
//
//        account.withdraw(actualAmount);
//    }
//
//    private void depositPrompt() {
//        if (((Customer) current_user).hasPrimary()) {
//            Chequing primary = (Chequing) accountManager.getAccount(((Customer) current_user).getPrimaryAccount());
//            Scanner reader = new Scanner(System.in);
//
//            System.out.print("Are you depositing [1] banknote or [2] cheque? ");
//            int option = 0;
//            while (option > 3 || option < 1) {
//                option = reader.nextInt();
//            }
//
//            if (option == 1) {
//                Map<Integer, Integer> depositedBills = selectBillsPrompt();
//                assert primary != null;
//                primary.depositBill(depositedBills);
//
//            } else {
//                System.out.print("How much would you like to deposit? ");
//                double amount = Double.valueOf(reader.next());
//                assert primary != null;
//                primary.deposit(amount);
//            }
//        } else {
//            System.err.println("Deposit cannot be made since you have no primary accounts. Request a new account in the main menu.");
//        }
//
//    }
//
//    private void payBillPrompt() {
//        Account account = selectAccountPrompt((Customer) current_user, "CreditCard");
//
//        Scanner reader = new Scanner(System.in);
//        System.out.print("Please enter the amount you would like to pay: ");
//        double amount = reader.nextDouble();
//        System.out.print("Please enter the non-user account you would like to pay: ");
//        String payee = reader.next();
//
//
//        try {
//            if (((AccountTransferable) account).payBill(amount, payee)) {
//                System.out.println("Bill has been paid");
//            } else {
//                System.err.println("Payment is unsuccessful");
//            }
//        } catch (IOException e) {
//            // do nothing?
//        }
//    }
//
//    private void transferBetweenAccountsPrompt() {
//        System.out.println("Now select the account you would like to transfer FROM: ");
//        Account from = selectAccountPrompt((Customer) current_user, "CreditCard");
//
//        System.out.println("Now select the account you would like to transfer TO: ");
//        Account to = selectAccountPrompt((Customer) current_user);
//
//        Scanner reader = new Scanner(System.in);
//        System.out.print("Please enter the amount you would like to transfer: ");
//        double amount = reader.nextDouble();
//
//        if (((AccountTransferable) from).transferBetweenAccounts(amount, to)) {
//            System.out.println("Transfer is successful");
//        } else {
//            System.err.println("Transfer is unsuccessful");
//        }
//    }
//
//    private void transferToAnotherUserPrompt() {
//        Scanner reader = new Scanner(System.in);
//
//        Account from = selectAccountPrompt((Customer) current_user, "CreditCard");
//
//        System.out.print("Please enter username you would like to transfer to: ");
//        String username = reader.next();
//
//        System.out.print("Please enter the amount you would like to transfer: ");
//        double amount = reader.nextDouble();
//
//        if (userManager.isPresent(username)) {
//            Customer user = (Customer) userManager.getUser(username);
//            ((AccountTransferable) from).transferToAnotherUser(amount, username, accountManager.getAccount(user.getPrimaryAccount()));
//            System.out.println("Transfer is successful");
//        } else {
//            System.err.println("The username does not exist. Transfer is cancelled");
//        }
//    }
//
//    private void readAlertPrompt() {
//        ((BankManager) current_user).readAlerts();
//    }
//
//    //WIP
//    private void addSellOffer() {
//        Scanner reader = new Scanner(System.in);
//        System.out.println("What item you like to sell?");
//        String item = reader.nextLine();
//        System.out.println("How much do you have? (in integer unit)");
//        int quantity = reader.nextInt();
//        System.out.println("How much are you selling it for? (in dollars)");
//        int price = reader.nextInt();
//        TradeOffer tradeoffer = new TradeOffer(quantity, price, (Customer) current_user);
//        tradingsystem.addSellOffer(item, tradeoffer);
//    }
//
//    private void addBuyOffer() {
//        Scanner reader = new Scanner(System.in);
//        System.out.println("What item you like to buy?");
//        String item = reader.nextLine();
//        System.out.println("How much do you want? (in integer unit)");
//        int quantity = reader.nextInt();
//        System.out.println("How much are you buying it for? (in dollars)");
//        int price = reader.nextInt();
//        TradeOffer tradeoffer = new TradeOffer(quantity, price, (Customer) current_user);
//        tradingsystem.addBuyOffer(item, tradeoffer);
//    }
//
//    private void seeOffers() {
//        Scanner reader = new Scanner(System.in);
//        System.out.println("Would you like to see sell offers of buy offers? (S/B)");
//        boolean choice_bool = false;
//        String choice = reader.nextLine();
//        if (choice.equals("S")) {
//            choice_bool = true;
//        }
//        System.out.println("Which item would you like to see offers for?");
//        String item = reader.nextLine();
//        ArrayList<String> sell_offers = tradingsystem.seeOffers(item, choice_bool);
//        System.out.println(sell_offers);
//    }
//
//    private void addToInventory() {
//        Scanner reader = new Scanner(System.in);
//        System.out.println("What item would you like to deposit?");
//        String item = reader.nextLine();
//        System.out.println("How much of it would you like to add? (in integer unit)");
//        int amount = reader.nextInt();
//        Customer current_customer = (Customer) current_user;
//        current_customer.getInventory().depositItem(item, amount);
//    }
//
////    private void removeFromInventory() {
////        Scanner reader = new Scanner(System.in);
////        System.out.println("What item would you like to withdraw?");
////        String item = reader.nextLine();
////        System.out.println("How much of it would you like to remove? (in integer unit)");
////        int amount = reader.nextInt();
////        Customer current_customer = (Customer) current_user;
////        current_customer.getInventory().withdrawItem(item, amount);
////    }
//
//    private void viewInventoryPrompt() {
//        Customer current_customer = (Customer) current_user;
//        ArrayList<String> inventory = current_customer.getInventory().viewInventory();
//        System.out.println(inventory);
//    }
//
//    private void eTransferPrompt() {
//        Scanner reader = new Scanner(System.in);
//        System.out.println("[1] Make an eTransfer");
//        System.out.println("[2] Accept incoming eTransfers");
//        System.out.println("[3] View requests");
//        System.out.println("[4] Make a request");
//        System.out.println("[5] Return to main menu");
//        int choice = reader.nextInt();
//        switch (choice) {
//            case 1:
//                makeETransferPrompt();
//                break;
//            case 2:
//                acceptTransfersPrompt();
//                break;
//            case 3:
//                viewRequestsPrompt();
//                break;
//            case 4:
//                makeRequestPrompt();
//                break;
//            case 5:
//                break;
//            default:
//                System.out.println("Invalid choice");
//        }
//        System.out.println("press any key to return");
//        reader.next();
//    }
//
//    private void makeETransferPrompt() {
//        Scanner reader = new Scanner(System.in);
//        System.out.println("Select account you would like to transfer from");
//        Account senderAccount = selectAccountPrompt((Customer) current_user, "CreditCard");
//        System.out.println("Enter recipient username");
//        String user = reader.next();
//        if (!userManager.isCustomer(user)) {
//            System.out.println("invalid username");
//            return;
//        }
//        System.out.println("Enter amount you want to send: ");
//        double amount = reader.nextDouble();
//        reader.nextLine();
//        while (amount > senderAccount.getBalance() || amount <= 0.0) {
//            System.out.println("You have entered an amount exceeding your balance or an invalid amount. Try again");
//            amount = reader.nextDouble();
//            reader.nextLine();
//        }
//        System.out.println("Enter security question: ");
//        String q = reader.nextLine();
//        System.out.println("Enter response to question: ");
//        String a;
//        a = reader.nextLine();
//        if (a != null)
//            eTransferManager.send(current_user.getUsername(), senderAccount.getId(), user, q, a, amount);
//        System.out.println("sent eTransfer to: " + user);
//    }
//
//    private void acceptTransfersPrompt() {
//        ETransfer oldest = eTransferManager.getOldestTransfer(current_user.getUsername());
//        if (oldest == null) {
//            System.out.println("you have no incoming eTransfers :(");
//            return;
//        }
//        System.out.println("Choose an account to deposit into");
//        Account account = selectAccountPrompt((Customer) current_user);
//        System.out.println(oldest);
//        System.out.println("Security question: " + oldest.getQuestion() + "?");
//        System.out.println("Enter response: ");
//        Scanner reader = new Scanner(System.in);
//        String response = reader.nextLine();
//        int tries = 1;
//        while (!eTransferManager.validate(response, account, current_user.getUsername())) {
//            if (tries > 5) {
//                System.out.println("Exceeded maximum attempts");
//                return;
//            }
//            System.out.println("Incorrect response, try again (" + (5 - tries) + ") tries remaining");
//            response = reader.nextLine();
//            tries++;
//        }
//        System.out.println("Success! $" + oldest.getAmount() + " deposited into " + account.getClass().getSimpleName());
//    }
//
//    private void viewRequestsPrompt() {
//        HashMap<String, Double> requests = eTransferManager.readRequests(current_user.getUsername());
//        System.out.println("You have " + requests.size() + "requests: ");
//        int i = 1;
//        for (String s : requests.keySet()) {
//            System.out.println("" + i + ". " + s + " requested $" + requests.get(s));
//        }
//    }
//
//    private void makeRequestPrompt() {
//        Scanner reader = new Scanner(System.in);
//        System.out.println("Enter username you would like to send request to");
//        String user = reader.next();
//        if (!userManager.isCustomer(user)) {
//            System.out.println("invalid username");
//            return;
//        }
//        System.out.println("Enter amount you wish to request");
//        double amount = reader.nextDouble();
//        reader.nextLine();
//        eTransferManager.request(current_user.getUsername(), user, amount);
//    }
//
//}
//
//
