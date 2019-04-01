package ATM;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

import static ATM.ATM.*;

/**
 * A GUI for customer options.
 */
class CustomerOptionsGUI extends OptionsGUI {

    CustomerOptionsGUI(Stage mainWindow, Scene welcomeScreen, User user) {
        super(mainWindow, welcomeScreen, user);
    }

    @Override
    public Scene createOptionsScreen() {
        addOptionText("Show my account summary");
        addOptionText("Show transaction history");
        addOptionText("Pay a Bill");
        addOptionText("Make a Transfer between my Accounts");
        addOptionText("Make a Transfer to another User");
        addOptionText("Banknote/Cheque Deposit");
        addOptionText("Banknote Withdrawal");
        addOptionText("Request Creating an Account");
        addOptionText("Investing in GIC");
        addOptionText("Request Making Preexisting Account Joint");
        addOptionText("Add an item for Sale");
        addOptionText("Request an item for Sale");
        addOptionText("See Offers");
        addOptionText("Add to Inventory");
        addOptionText("Remove from Inventory");
        addOptionText("View Inventory");
        addOptionText("eTransfers");
        addOptionText("Change Primary Account");
        addOptionText("Change Password");
        addOptionText("Logout");
        addOptions();

        getOption(0).setOnAction(event -> showAccountSummaryScreen());
        getOption(1).setOnAction(event -> showTransactionHistory());
        getOption(2).setOnAction(event -> payBillScreen());
        getOption(3).setOnAction(event -> makeTransferBetweenScreen());
        getOption(4).setOnAction(event -> makeTransferAnotherScreen());
        getOption(5).setOnAction(event -> depositScreen());
        getOption(6).setOnAction(event -> withdrawalScreen());
        getOption(7).setOnAction(event -> requestAccountScreen());
        getOption(8).setOnAction(event -> InvestGICScreen());
        getOption(9).setOnAction(event -> accountToJointScreen());
        getOption(10).setOnAction(event -> addSellOfferScreen());
        getOption(11).setOnAction(event -> addBuyOfferScreen());
        getOption(12).setOnAction(event -> seeOffersScreen());
        getOption(13).setOnAction(event -> addToInventoryScreen());
        getOption(14).setOnAction(event -> removeFromInventoryScreen());
        getOption(15).setOnAction(event -> viewInventoryScreen());
        getOption(16).setOnAction(event -> eTransferPromptScreen());
        getOption(17).setOnAction(event -> changePrimaryScreen());
        getOption(18).setOnAction(event -> changePasswordScreen());
        getOption(19).setOnAction(event -> logoutHandler());

//        getOption(15).setOnAction(event -> InvestGICScreen);

        return generateOptionsScreen();
    }

    private void showAccountSummaryScreen() {
        String netTotal = "Your net total is $" + ((Customer) user).getNetTotal();
        showAlert(Alert.AlertType.INFORMATION, window, "NetTotal", netTotal, false);

        // Start with the columns
        TableColumn<AccountSummary, String> primCol = new TableColumn<>("PRIMARY");
        primCol.setMinWidth(50);
        primCol.setCellValueFactory(new PropertyValueFactory<>("isPrimary"));

        TableColumn<AccountSummary, String> typeCol = new TableColumn<>("TYPE");
        typeCol.setMinWidth(150);
        typeCol.setCellValueFactory(new PropertyValueFactory<>("accountType"));

        TableColumn<AccountSummary, String> idCol = new TableColumn<>("ID");
        idCol.setMinWidth(150);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<AccountSummary, String> dateCol = new TableColumn<>("DATE CREATED");
        dateCol.setMinWidth(150);
        dateCol.setCellValueFactory(new PropertyValueFactory<>("creationDate"));

        TableColumn<AccountSummary, Double> balCol = new TableColumn<>("BALANCE");
        balCol.setMinWidth(100);
        balCol.setCellValueFactory(new PropertyValueFactory<>("balance"));

        TableColumn<AccountSummary, List<String>> ownerCol = new TableColumn<>("ACCOUNT HOLDERS");
        ownerCol.setMinWidth(400);
        ownerCol.setCellValueFactory(new PropertyValueFactory<>("owners"));

        TableColumn<AccountSummary, String> recCol = new TableColumn<>("MOST RECENT TRANSACTION");
        recCol.setMinWidth(300);
        recCol.setCellValueFactory(new PropertyValueFactory<>("mostRecent"));

        TableView<AccountSummary> table = new TableView<>();
        table.setItems(getSummary());
        table.getColumns().addAll(Arrays.asList(primCol, typeCol, idCol, dateCol, balCol, ownerCol, recCol));

        Button goBack = new Button("Go Back");
        goBack.setOnAction(event -> window.setScene(optionsScreen));
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(goBack);

        VBox vBox = new VBox();
        vBox.getChildren().add(table);
        vBox.getChildren().add(hbBtn);

        window.setScene(new Scene(vBox, 1000, 400));
    }

    private ObservableList<AccountSummary> getSummary() {
        ObservableList<AccountSummary> summaries = FXCollections.observableArrayList();
        List<Account> accounts = accountManager.getListOfAccounts(user.getUsername());
        for (Account a : accounts) {

            AccountSummary sum;
            Transaction mostRecent = a.getMostRecentTransaction();
            String recent = (mostRecent == null) ? "N/A" : mostRecent.toString();

            if (a.getId().equals(((Customer) user).getPrimaryAccount())) {
                sum = new AccountSummary("X", a.getClass().getSimpleName(), a.getDateCreatedReadable(),
                        a.getBalance(), recent, a.getId(), a.getOwnersUsername());
            } else {
                sum = new AccountSummary("", a.getClass().getSimpleName(), a.getDateCreatedReadable(),
                        a.getBalance(), recent, a.getId(), a.getOwnersUsername());
            }
            summaries.add(sum);
        }
        return summaries;
    }

    private void payBillScreen() {
        GridPane gridPane = createFormPane();
        Label chooseLbl = new Label("Choose account:");
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getSelectionModel().selectFirst();

        // Add user's accounts as entries to ComboBox
        List<Account> accounts = accountManager.getListOfAccounts(user.getUsername());
        for (Account a : accounts) {
            String accountName = a.getClass().getName();
            if (!accountName.equals(CustomerOptionsGUI.class.getPackage().getName() + ".CreditCard")) {
                String choice = accountName + " " + a.getId();
                choiceBox.getItems().add(choice);
            }
        }

        System.out.println(accounts);

        Label amountLbl = new Label("Amount:");
        TextField amountInput = new TextField(); // assume user enters a number

        Label name = new Label("Non-user account name:");
        TextField nameInput = new TextField();

        Button cancel = new Button("Cancel");
        Button pay = new Button("Pay");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(cancel);
        hbBtn.getChildren().add(pay);

        gridPane.add(chooseLbl, 0, 0);
        gridPane.add(choiceBox, 1, 0);
        gridPane.add(amountLbl, 0, 1);
        gridPane.add(amountInput, 1, 1);
        gridPane.add(name, 0, 2);
        gridPane.add(nameInput, 1, 2);
        gridPane.add(hbBtn, 1, 3);

        cancel.setOnAction(event -> window.setScene(optionsScreen));
        pay.setOnAction(event -> {
            // [type, id] -> id -> account with id <id>
            String[] aInfo = choiceBox.getValue().split("\\s+");
            String aID = aInfo[1];
            Account account = accountManager.getAccount(aID);
            double amount = Double.valueOf(amountInput.getText());
            String payee = nameInput.getText();
            try {
                assert account != null;
                if (((AccountTransferable) account).payBill(amount, payee)) {
                    showAlert(Alert.AlertType.INFORMATION, window, "Success", "Bill has been paid", true);
                } else {
                    showAlert(Alert.AlertType.ERROR, window, "Error", "Payment is unsuccessful", true);
                }
            } catch (IOException e) {
                // do nothing?
            }
            window.setScene(optionsScreen);
        });

        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        window.setScene(scene);
    }

    private void makeTransferBetweenScreen() {
        GridPane gridPane = createFormPane();
        Label chooseLbl = new Label("Choose account:");
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        ChoiceBox<String> otherChoiceBox = new ChoiceBox<>();

        // Add user's accounts as entries to ComboBox
        List<Account> accounts = accountManager.getListOfAccounts(user.getUsername());
        for (Account a : accounts) {
            String accountName = a.getClass().getName();
            if (!accountName.equals(CustomerOptionsGUI.class.getPackage().getName() + ".CreditCard")) {
                String choice = accountName + " " + a.getId();
                choiceBox.getItems().add(choice);
                otherChoiceBox.getItems().add(choice);
            } else {
                String choice = accountName + " " + a.getId();
                otherChoiceBox.getItems().add(choice);
            }
        }
        choiceBox.getSelectionModel().selectFirst();
        otherChoiceBox.getSelectionModel().selectFirst();

        Label amountLbl = new Label("Amount:");
        TextField amountInput = new TextField(); // assume user enters a number

        Label transferLbl = new Label("Transfer To:");

        Button cancel = new Button("Cancel");
        Button pay = new Button("Transfer");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(cancel);
        hbBtn.getChildren().add(pay);

        gridPane.add(chooseLbl, 0, 0);
        gridPane.add(choiceBox, 1, 0);
        gridPane.add(amountLbl, 0, 1);
        gridPane.add(amountInput, 1, 1);
        gridPane.add(transferLbl, 0, 2);
        gridPane.add(otherChoiceBox, 1, 2);
        gridPane.add(hbBtn, 1, 3);

        cancel.setOnAction(event -> window.setScene(optionsScreen));
        pay.setOnAction(event -> {
            // [type, id] -> id -> account with id <id>
            String[] aInfo = choiceBox.getValue().split("\\s+");
            String aID = aInfo[1];
            String[] oInfo = otherChoiceBox.getValue().split("\\s+");
            String oID = oInfo[1];
            Account account = accountManager.getAccount(aID);
            Account otherAccount = accountManager.getAccount(oID);
            double amount = Double.valueOf(amountInput.getText());
            assert account != null;
            if (((AccountTransferable) account).transferBetweenAccounts(amount, otherAccount)) {
                showAlert(Alert.AlertType.INFORMATION, window, "Success", "Transfer has been made", true);
            } else {
                showAlert(Alert.AlertType.ERROR, window, "Error", "Transfer is unsuccessful", true);
            }
            window.setScene(optionsScreen);
        });

        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        window.setScene(scene);
    }

    private void makeTransferAnotherScreen() {
        GridPane gridPane = createFormPane();
        Label chooseLbl = new Label("Choose account:");
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getSelectionModel().selectFirst();

        // Add user's accounts as entries to ComboBox
        List<Account> accounts = accountManager.getListOfAccounts(user.getUsername());
        for (Account a : accounts) {
            String accountName = a.getClass().getName();
            if (!accountName.equals(CustomerOptionsGUI.class.getPackage().getName() + ".CreditCard")) {
                String choice = accountName + " " + a.getId();
                choiceBox.getItems().add(choice);
            }
        }

        Label otherName = new Label("User to transfer to");
        TextField otherNameInput = new TextField();
        TextFields.bindAutoCompletion(otherNameInput, userManager.getSubType_map().keySet());

        Label amountLbl = new Label("Amount:");
        TextField amountInput = new TextField(); // assume user enters a number

        Button cancel = new Button("Cancel");
        Button pay = new Button("Transfer");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(cancel);
        hbBtn.getChildren().add(pay);

        gridPane.add(chooseLbl, 0, 0);
        gridPane.add(choiceBox, 1, 0);
        gridPane.add(amountLbl, 0, 1);
        gridPane.add(amountInput, 1, 1);
        gridPane.add(otherName, 0, 2);
        gridPane.add(otherNameInput, 1, 3);
        gridPane.add(hbBtn, 1, 4);

        cancel.setOnAction(event -> window.setScene(optionsScreen));
        pay.setOnAction(event -> {
            // [type, id] -> id -> account with id <id>
            String[] aInfo = choiceBox.getValue().split("\\s+");
            String aID = aInfo[1];
            Account account = accountManager.getAccount(aID);
            double amount = Double.valueOf(amountInput.getText());
            String otherAccount = otherNameInput.getText();
            if (userManager.isCustomer(otherAccount)) {
                User otherUser = userManager.getUser(otherAccount);
                assert account != null;
                if (((AccountTransferable) account).transferToAnotherUser(amount, otherUser.getUsername(), accountManager.getAccount(((Customer) otherUser).getPrimaryAccount()))) {
                    showAlert(Alert.AlertType.INFORMATION, window, "Success", "Transfer has been made", true);
                } else {
                    showAlert(Alert.AlertType.INFORMATION, window, "Success", "Transfer is unsuccessful", true);
                }
            } else {
                showAlert(Alert.AlertType.ERROR, window, "Error", "User is not a customer", true);
            }
            window.setScene(optionsScreen);
        });

        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        window.setScene(scene);
    }

    private void depositBanknoteScreen() {
        Chequing primary = (Chequing) accountManager.getAccount(((Customer) user).getPrimaryAccount());
        GridPane grid = createFormPane();

        Button goBack = new Button("Go Back");
        Button deposit = new Button("Deposit");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(goBack);
        hbBtn.getChildren().add(deposit);

        HashMap<Integer, TextField> textField = new HashMap<>(); // d -> input
        int rowIndex = 0;

        // This part is very similar to restockingScreen
        // Adding all labels and text fields
        for (Integer d : banknoteManager.DENOMINATIONS) {
            // Label
            Label dLabel = new Label("Amount of $" + d + " dollar bills:");
            grid.add(dLabel, 0, rowIndex);

            // TextField
            TextField dTextField = new TextField();
            grid.add(dTextField, 1, rowIndex);
            textField.put(d, dTextField);

            rowIndex++;
        }

        grid.add(hbBtn, 1, 4);

        // Handlers
        goBack.setOnAction(event -> depositScreen());
        deposit.setOnAction(event -> {
            // d -> quantity
            Map<Integer, Integer> depositedBills = new HashMap<>();

            for (Integer d : textField.keySet()) {
                depositedBills.put(d, Integer.valueOf(textField.get(d).getText()));
            }
            assert primary != null;
            primary.depositBill(depositedBills);
            showAlert(Alert.AlertType.INFORMATION, window, "Success", "Amount successfully deposited", true);
            window.setScene(optionsScreen);
        });

        Scene scene = new Scene(grid);
        scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        window.setScene(scene);
    }


    private void depositScreen() {
        if (((Customer) user).hasPrimary()) {
            GridPane gridPane = createFormPane();

            Button cancel = new Button("Cancel");
            Button depositBanknote = new Button("Deposit Banknote");
            Button depositCheque = new Button("Deposit Cheque");

            gridPane.add(cancel, 0, 0);
            gridPane.add(depositBanknote, 1, 0);
            gridPane.add(depositCheque, 2, 0);

            cancel.setOnAction(event -> window.setScene(optionsScreen));
            depositBanknote.setOnAction(event -> depositBanknoteScreen());
            depositCheque.setOnAction(event -> depositChequeScreen());

            Scene scene = new Scene(gridPane);
            scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
            window.setScene(scene);
        } else {
            window.setScene(optionsScreen);
            showAlert(Alert.AlertType.ERROR, window, "Error", "Deposit cannot be made since you have no primary accounts. " +
                    "Request a new account in the main menu.", true);
        }

    }

    private void depositChequeScreen() {
        Chequing primary = (Chequing) accountManager.getAccount(((Customer) user).getPrimaryAccount());
        GridPane gridPane = createFormPane();

        Button goBack = new Button("Go Back");
        Button deposit = new Button("Deposit");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(goBack);
        hbBtn.getChildren().add(deposit);

        Label amountLbl = new Label("Amount to deposit:");
        TextField amountInput = new TextField();

        gridPane.add(amountLbl, 0, 0);
        gridPane.add(amountInput, 1, 0);
        gridPane.add(hbBtn, 1, 1);

        goBack.setOnAction(event -> depositScreen());
        deposit.setOnAction(event -> {
            if (amountInput.getText() != null) {
                double amount = Double.parseDouble(amountInput.getText());
                assert primary != null;
                primary.deposit(amount);
                showAlert(Alert.AlertType.INFORMATION, window, "Success!", "Amount successfully deposited", true);
                window.setScene(optionsScreen);
            } else {
                showAlert(Alert.AlertType.ERROR, window, "Error", "Please enter a valid amount.", true);
            }
        });

        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        window.setScene(scene);
    }

    private void requestJointAccountScreen() {
        /*
        Enter username of secondary owner:
        Select an account type picker drop-down CHOICE-BOX control element
        Cancel | Request
         */
        GridPane gridPane = createFormPane();

        Label usernameSecondary = new Label("Enter username of secondary owner:");
        TextField secondaryUsername = new TextField();
        TextFields.bindAutoCompletion(secondaryUsername, userManager.getSubType_map().keySet());

        Label accountTypeLbl = new Label("Select an account type:");
        ChoiceBox<String> accountTypeDropDown = new ChoiceBox<>();

        Collection<String> accountTypes = accountManager.TYPES_OF_ACCOUNTS;
        for (String type : accountTypes) {
            if (!type.equalsIgnoreCase("GIC")) {
                accountTypeDropDown.getItems().add(type);
            }
        }
        accountTypeDropDown.getSelectionModel().selectFirst();

        HBox hbBtn = getTwoButtons("Request");
        Button cancel = (Button) hbBtn.getChildren().get(0);
        Button request = (Button) hbBtn.getChildren().get(1);

        gridPane.add(usernameSecondary, 0, 0);
        gridPane.add(secondaryUsername, 1, 0);
        gridPane.add(accountTypeLbl, 0, 1);
        gridPane.add(accountTypeDropDown, 1, 1);
        gridPane.add(hbBtn, 1, 2);

        cancel.setOnAction(event -> window.setScene(optionsScreen));
        request.setOnAction(event -> {
            String username = secondaryUsername.getText();
            if (username != null && userManager.getUser(username) instanceof Customer) {
                String accountType = accountTypeDropDown.getValue().split("\\s+")[0];
                if (!username.equals(user.getUsername()) && userManager.isPresent(username)) {
                    try {
                        ((Customer) user).requestJointAccount(accountType, username);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    showAlert(Alert.AlertType.INFORMATION, window, "Success!", "A request for " + accountType + " has been made", true);
                    window.setScene(optionsScreen);
                } else {
                    showAlert(Alert.AlertType.ERROR, window, "Error", "We are sorry. Something went wrong", true);
                }
            } else {
                showAlert(Alert.AlertType.ERROR, window, "Error", "Please enter a valid secondary owner.", true);
            }

        });

        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        window.setScene(scene);
    }

    private void withdrawalScreen() {
        GridPane gridPane = createFormPane();

        Label chooseLbl = new Label("Choose account:");
        ChoiceBox<String> choiceBox = getBankAccounts();
        choiceBox.getSelectionModel().selectFirst();

        Label amountLbl = new Label("Amount to Withdraw:");
        TextField amountInput = new TextField(); // assume user enters a number

        HBox hbBtn = getTwoButtons("Withdraw");

        List<Label> labels = new ArrayList<>();
        labels.add(chooseLbl);
        labels.add(amountLbl);
        List<Node> controls = new ArrayList<>();
        controls.add(choiceBox);
        controls.add(amountInput);

        addControlsToLayout(gridPane, labels, controls, hbBtn);

        ((Button) hbBtn.getChildren().get(0)).setOnAction(event -> window.setScene(optionsScreen));
        ((Button) hbBtn.getChildren().get(1)).setOnAction(event -> {
            Account account = getAccountFromChoiceBox(choiceBox);
            if (!amountInput.getText().equals("") && Pattern.compile("^[0-9]*$").matcher(amountInput.getText()).find()) {
                double amount = Double.valueOf(amountInput.getText());
                double actualAmount = amount - amount % 5;
                account.withdraw(actualAmount);
                String message = "$" + actualAmount + " withdrawn";
                showAlert(Alert.AlertType.INFORMATION, window, "Success!", message, true);
                window.setScene(optionsScreen);
            } else {
                showAlert(Alert.AlertType.ERROR, window, "Error", "Please enter a valid amount.", true);
            }
        });

        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        window.setScene(scene);
    }

    private void requestAccountScreen() {
        /*
        Would you like to make a joint account?
        Yes | No | Cancel

        If No...
        Select an account type picker drop-down CHOICE-BOX control element
        Cancel | Request

        If Yes...
        Enter username of secondary holder:
        Select an account type picker drop-down CHOICE-BOX control element
        Cancel | Request
         */
        GridPane gridPane = createFormPane();

        Label joint = new Label("Would you like to make a joint account?");
        Button yes = new Button("Yes");
        Button no = new Button("No");
        Button cancel = new Button("Cancel");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(yes);
        hbBtn.getChildren().add(no);
        hbBtn.getChildren().add(cancel);

        gridPane.add(joint, 0, 0);
        gridPane.add(hbBtn, 1, 1);

        yes.setOnAction(event -> requestJointAccountScreen());
        no.setOnAction(event -> requestNonJointAccountScreen());
        cancel.setOnAction(event -> window.setScene(optionsScreen));

        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        window.setScene(scene);
    }

    private void changePrimaryScreen() {
        GridPane gridPane = createFormPane();
        Label selectLbl = new Label("Select a new primary account:");
        ChoiceBox<String> choiceBox = new ChoiceBox<>();

        List<Account> accounts = accountManager.getListOfAccounts(user.getUsername());
        for (Account a : accounts) {
            if (a instanceof Chequing) {
                choiceBox.getItems().add(a.getType() + " " + a.getId());
            }
        }
        choiceBox.getSelectionModel().selectFirst();

        HBox hbBtn = getTwoButtons("Change Primary Account");
        Button cancel = (Button) hbBtn.getChildren().get(0);
        Button change = (Button) hbBtn.getChildren().get(1);

        gridPane.add(selectLbl, 0, 0);
        gridPane.add(choiceBox, 1, 0);
        gridPane.add(hbBtn, 1, 1);

        cancel.setOnAction(event -> window.setScene(optionsScreen));
        change.setOnAction(event -> {
            String id = choiceBox.getValue().split("\\s+")[1];
            Account newPrime = accountManager.getAccount(id);
            ((Customer) user).setPrimaryAccount(newPrime);
            showAlert(Alert.AlertType.INFORMATION, window, "Success!", "Your primary account has been changed.", true);
            window.setScene(optionsScreen);
        });

        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        window.setScene(scene);
    }

    private void requestNonJointAccountScreen() {
        /*
        Select an account type using picker drop-down CHOICE-BOX control element
        Cancel | Request
         */
        GridPane gridPane = createFormPane();

        Label accountTypeLbl = new Label("Select an account type:");
        ChoiceBox<String> accountTypeDropDown = new ChoiceBox<>();

        Collection<String> accountTypes = accountManager.TYPES_OF_ACCOUNTS;
        for (String type : accountTypes) {
            if (!type.equalsIgnoreCase("GIC")) {
                accountTypeDropDown.getItems().add(type);
            }
        }
        accountTypeDropDown.getSelectionModel().selectFirst();

        HBox hbBtn = getTwoButtons("Request");
        Button cancel = (Button) hbBtn.getChildren().get(0);
        Button request = (Button) hbBtn.getChildren().get(1);

        gridPane.add(accountTypeLbl, 0, 0);
        gridPane.add(accountTypeDropDown, 1, 0);
        gridPane.add(hbBtn, 1, 1);

        cancel.setOnAction(event -> window.setScene(optionsScreen));
        request.setOnAction(event -> {
            String accountType = accountTypeDropDown.getValue().split("\\s+")[0];
            try {
                ((Customer) user).requestAccount(accountType);
            } catch (IOException e) {
                e.printStackTrace();
            }
            showAlert(Alert.AlertType.INFORMATION, window, "Success!", "A request for " + accountType + " has been made", true);
            window.setScene(optionsScreen);
        });

        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        window.setScene(scene);
    }

    private ObservableList<Transaction> getTransaction() {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        List<Account> accounts = accountManager.getListOfAccounts(user.getUsername());
        for (Account a : accounts) {
            transactions.addAll(a.getTransactionHistory());
        }
        return transactions;
    }

    private Account getAccountFromChoiceBox(ChoiceBox<String> choiceBox) {
        /*
        choiceBox like this ->
        <account> <id>
            .
            .
            .
         */
        String[] aInfo = choiceBox.getValue().split("\\s+");
        String aID = aInfo[1];
        return accountManager.getAccount(aID);
    }

    private void showTransactionHistory() {
        // Start with the columns
        TableColumn<Transaction, String> dateCol = new TableColumn<>("DATE");
//        dateCol.setMinWidth(200);
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Transaction, String> typeCol = new TableColumn<>("TYPE");
        typeCol.setMinWidth(100);
        typeCol.setCellValueFactory(new PropertyValueFactory<>("TransactionType"));

        TableColumn<Transaction, Double> amountCol = new TableColumn<>("AMOUNT");
        amountCol.setMinWidth(100);
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Transaction, String> bnkAcc = new TableColumn<>("ACCOUNT");
        bnkAcc.setMinWidth(200);
        bnkAcc.setCellValueFactory(new PropertyValueFactory<>("accountType"));

        TableView<Transaction> table = new TableView<>();
        table.setItems(getTransaction());
        table.getColumns().addAll(Arrays.asList(dateCol, typeCol, amountCol, bnkAcc));

        Button goBack = new Button("Go Back");
        goBack.setOnAction(event -> window.setScene(optionsScreen));
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(goBack);

//        GridPane gridPane = createFormPane();
//        gridPane.add(table,0,0);
//        gridPane.add(hbBtn,1,1);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(table);
        vBox.getChildren().add(hbBtn);

        window.setScene(new Scene(vBox, 550, 300));
    }

    private void accountToJointScreen() {
        GridPane gridPane = createFormPane();
        Label selectLbl = new Label("Select non-joint account:");
        ChoiceBox<String> choiceBox = new ChoiceBox<>();

        List<Account> accounts = accountManager.getListOfAccounts(user.getUsername());
        for (Account a : accounts) {
            if (a.isNotJoint()) {
                choiceBox.getItems().add(a.getType() + " " + a.getId());
            }
        }
        choiceBox.getSelectionModel().selectFirst();

        Label usernameLbl = new Label("Enter username of secondary owner:");
        TextField input = new TextField();
        TextFields.bindAutoCompletion(input, userManager.getSubType_map().keySet());

        Button cancel = new Button("Cancel");
        Button request = new Button("Request joint account");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(cancel);
        hbBtn.getChildren().add(request);

        gridPane.add(selectLbl, 0, 0);
        gridPane.add(choiceBox, 1, 0);
        gridPane.add(usernameLbl, 0, 1);
        gridPane.add(input, 1, 1);
        gridPane.add(hbBtn, 1, 2);

        cancel.setOnAction(event -> window.setScene(optionsScreen));
        request.setOnAction(event -> {
            String username = input.getText();
            if (userManager.isPresent(username) && userManager.getUser(username) instanceof Customer) {
                String accountType = choiceBox.getValue().split("\\s+")[0];
                String accountID = choiceBox.getValue().split("\\s+")[1];
                try {
                    ((Customer) user).requestAccountToJoint(accountType, accountID, username);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                showAlert(Alert.AlertType.INFORMATION, window, "Success!", "A request for " + accountType + " has been made.", true);
                window.setScene(optionsScreen);
            } else {
                showAlert(Alert.AlertType.WARNING, window, "Warning", "Cannot find user " + username + " in our system.", true);
            }
        });

        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        window.setScene(scene);
    }

    private void addSellOfferScreen() {
        GridPane gridPane = createFormPane();

        Label itemForSale = new Label("What item would you like to sell?");
        TextField itemInput = new TextField();

        Label itemQuantity = new Label("How much do you have? (in integer unit)");
        TextField quantityInput = new TextField();

        Label itemPricing = new Label("How much are you selling it for? (in dollars)");
        TextField priceInput = new TextField();

        Button cancel = new Button("Cancel");
        Button add = new Button("Add");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(cancel);
        hbBtn.getChildren().add(add);

        gridPane.add(itemForSale, 0, 0);
        gridPane.add(itemInput, 1, 0);
        gridPane.add(itemQuantity, 0, 1);
        gridPane.add(quantityInput, 1, 1);
        gridPane.add(itemPricing, 0, 2);
        gridPane.add(priceInput, 1, 2);
        gridPane.add(hbBtn, 1, 3);

        cancel.setOnAction(event -> window.setScene(optionsScreen));
        add.setOnAction(event -> {
            String item = itemInput.getText();
            int quantity = Integer.valueOf(quantityInput.getText());
            int price = Integer.valueOf(priceInput.getText());
            TradeOffer tradeoffer = new TradeOffer(quantity, price, (Customer) user);
            tradingSystem.addSellOffer(item, tradeoffer);
            showAlert(Alert.AlertType.INFORMATION, window, "Success", "Item has been added", true);

            window.setScene(optionsScreen);
        });

        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        window.setScene(scene);
    }

    private void addBuyOfferScreen() {
        GridPane gridPane = createFormPane();

        Label itemForBuy = new Label("What item would you like to buy");
        TextField itemInput = new TextField();

        Label itemQuantityBuy = new Label("How much do you want? (in integer unit)");
        TextField quantityInput = new TextField();

        Label itemPricingBuy = new Label("How much are you buying it for? (in dollars)");
        TextField priceInput = new TextField();

        Button cancel = new Button("Cancel");
        Button add = new Button("Add");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(cancel);
        hbBtn.getChildren().add(add);

        gridPane.add(itemForBuy, 0, 0);
        gridPane.add(itemInput, 1, 0);
        gridPane.add(itemQuantityBuy, 0, 1);
        gridPane.add(quantityInput, 1, 1);
        gridPane.add(itemPricingBuy, 0, 2);
        gridPane.add(priceInput, 1, 2);
        gridPane.add(hbBtn, 1, 3);

        cancel.setOnAction(event -> window.setScene(optionsScreen));
        add.setOnAction(event -> {
            String item = itemInput.getText();
            int quantity = Integer.valueOf(quantityInput.getText());
            int price = Integer.valueOf(priceInput.getText());
            TradeOffer buyOffer = new TradeOffer(quantity, price, (Customer) user);
            tradingSystem.addBuyOffer(item, buyOffer);
            showAlert(Alert.AlertType.INFORMATION, window, "Success", "Item has been requested", true);

            window.setScene(optionsScreen);
        });

        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        window.setScene(scene);
    }

    private void seeOffersScreen() {
        GridPane gridPane = createFormPane();

        Label itemOffers = new Label("Would you like to see sell offers of buy offers?");
        Button cancel = new Button("Cancel");
        Button buy = new Button("Buy");
        Button sell = new Button("Sell");

        gridPane.add(itemOffers, 0, 0);
        gridPane.add(cancel, 1, 1);
        gridPane.add(buy, 2, 1);
        gridPane.add(sell, 3, 1);

        cancel.setOnAction(event -> window.setScene(optionsScreen));
        buy.setOnAction(event -> seeOffersBuyScreen());
        sell.setOnAction(event -> seeOffersSellScreen());

        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        window.setScene(scene);
    }

    private void seeOffersSellScreen() {
        GridPane gridPane = createFormPane();

        Label itemForCheck = new Label("Which item would you like to see offers for?");
        TextField itemCheck = new TextField();
        TextFields.bindAutoCompletion(itemCheck, tradingSystem.sell_offers.keySet());


        Button cancel = new Button("Cancel");
        Button add = new Button("Check");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(cancel);
        hbBtn.getChildren().add(add);

        gridPane.add(itemForCheck, 0, 0);
        gridPane.add(itemCheck, 1, 0);
        gridPane.add(hbBtn, 1, 2);

        cancel.setOnAction(event -> seeOffersScreen());
        add.setOnAction(event -> {
            String item = itemCheck.getText();
            ArrayList<String> sell_offers = tradingSystem.seeOffers(item, true);
            showAlert(Alert.AlertType.INFORMATION, window, "Success", sell_offers.toString(), true);

            window.setScene(optionsScreen);
        });

        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        window.setScene(scene);
    }

    private void seeOffersBuyScreen() {
        GridPane gridPane = createFormPane();

        Label itemForCheck = new Label("Which item would you like to see offers for?");
        TextField itemCheck = new TextField();
        TextFields.bindAutoCompletion(itemCheck, tradingSystem.buy_offers.keySet());


        Button cancel = new Button("Cancel");
        Button add = new Button("Check");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(cancel);
        hbBtn.getChildren().add(add);

        gridPane.add(itemForCheck, 0, 0);
        gridPane.add(itemCheck, 1, 0);
        gridPane.add(hbBtn, 1, 2);

        cancel.setOnAction(event -> seeOffersScreen());
        add.setOnAction(event -> {
            String item = itemCheck.getText();
            ArrayList<String> sell_offers = tradingSystem.seeOffers(item, false);
            showAlert(Alert.AlertType.INFORMATION, window, "Success", sell_offers.toString(), true);

            window.setScene(optionsScreen);
        });

        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        window.setScene(scene);
    }

    private void addToInventoryScreen() {
        GridPane gridPane = createFormPane();

        Label itemForCheck = new Label("Which item would you like to see deposit?");
        TextField itemCheck = new TextField();

        Label itemForAmount = new Label("How much of it would you like to add? (in integer unit)");
        TextField itemAmount = new TextField();


        Button cancel = new Button("Cancel");
        Button add = new Button("Deposit");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(cancel);
        hbBtn.getChildren().add(add);

        gridPane.add(itemForCheck, 0, 0);
        gridPane.add(itemCheck, 1, 0);
        gridPane.add(itemForAmount, 0, 1);
        gridPane.add(itemAmount, 1, 1);
        gridPane.add(hbBtn, 1, 2);

        cancel.setOnAction(event -> window.setScene(optionsScreen));
        add.setOnAction(event -> {
            String item = itemCheck.getText();
            Integer amount = Integer.valueOf(itemAmount.getText());
            Customer current_customer = (Customer) user;
            current_customer.getInventory().depositItem(item, amount);
            showAlert(Alert.AlertType.INFORMATION, window, "Success", "Item has been added to inventory", true);

            window.setScene(optionsScreen);
        });

        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        window.setScene(scene);
    }

    private void removeFromInventoryScreen() {
        GridPane gridPane = createFormPane();

        Label itemForCheck = new Label("Which item would you like to withdraw?");
        TextField itemCheck = new TextField();

        Label itemForAmount = new Label("How much of it would you like to remove? (in integer unit)");
        TextField itemAmount = new TextField();


        Button cancel = new Button("Cancel");
        Button add = new Button("Withdraw");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(cancel);
        hbBtn.getChildren().add(add);

        gridPane.add(itemForCheck, 0, 0);
        gridPane.add(itemCheck, 1, 0);
        gridPane.add(itemForAmount, 0, 1);
        gridPane.add(itemAmount, 1, 1);
        gridPane.add(hbBtn, 1, 2);

        cancel.setOnAction(event -> window.setScene(optionsScreen));
        add.setOnAction(event -> {
            String item = itemCheck.getText();
            Integer amount = Integer.valueOf(itemAmount.getText());
            Customer current_customer = (Customer) user;
            current_customer.getInventory().withdrawItem(item, amount);
            showAlert(Alert.AlertType.INFORMATION, window, "Success", "Item has been withdrawn from inventory", true);

            window.setScene(optionsScreen);
        });

        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        window.setScene(scene);
    }

    private void viewInventoryScreen() {
        Customer current_customer = (Customer) user;
        ArrayList<String> inventory = current_customer.getInventory().viewInventory();
        showAlert(Alert.AlertType.INFORMATION, window, "Success", "Inventory: " + inventory.toString(), true);
        window.setScene(optionsScreen);
    }

    private void InvestGICScreen() {
        GridPane gridPane = createFormPane();

        Label chooseLbl = new Label("Choose a Deal:");
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        for (GICDeals deal : GICDeals.gicDeals) {
            choiceBox.getItems().add(deal.toString());
        }
        choiceBox.getSelectionModel().selectFirst();

        Button cancel = new Button("Cancel");
        Button confirm = new Button("Confirm");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(cancel);
        hbBtn.getChildren().add(confirm);

        gridPane.add(chooseLbl, 0, 0);
        gridPane.add(choiceBox, 1, 0);
        gridPane.add(hbBtn, 1, 1);

        cancel.setOnAction(event -> window.setScene(optionsScreen));
        confirm.setOnAction(event -> {
            int id = Integer.valueOf(choiceBox.getValue().split("\\s+")[0]);
            GICDeals deal = GICDeals.gicDeals.get(id);
            try {
                ((Customer) user).requestGICAccount(deal.getPeriod(), deal.getRate());
            } catch (IOException e) {
                e.printStackTrace();
            }
            showAlert(Alert.AlertType.INFORMATION, window, "Success", "GIC has been requested.", true);
        });

        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        window.setScene(scene);

    }

    private void eTransferPromptScreen() {
        GridPane gridPane = createFormPane();
        Label selectLbl = new Label("Select eTransfer Options");

        Button makeTransfer = new Button("Make an eTransfer");
        Button accept = new Button("Accept incoming eTransfers");
        Button view = new Button("View requests");
        Button makeRequest = new Button("Make a request");
        Button cancel = new Button("Return to main menu");

        gridPane.add(selectLbl, 0, 0);
        gridPane.add(makeTransfer, 1, 0);
        gridPane.add(accept, 2, 0);
        gridPane.add(makeRequest, 1, 1);
        gridPane.add(view, 2, 1);
        gridPane.add(cancel, 0, 4);


        cancel.setOnAction(event -> window.setScene(optionsScreen));
        makeTransfer.setOnAction(event -> makeETransferScreen());
        accept.setOnAction(event -> acceptETransferScreen());
        view.setOnAction(event -> viewRequestScreen());
        makeRequest.setOnAction(event -> makeRequestScreen());

        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        window.setScene(scene);
    }

    private void makeETransferScreen() {
        GridPane gridPane = createFormPane();
        Label chooseLbl = new Label("Select account you would like to transfer from");
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getSelectionModel().selectFirst();

        // Add user's accounts as entries to ComboBox
        List<Account> accounts = accountManager.getListOfAccounts(user.getUsername());
        for (Account a : accounts) {
            String accountName = a.getClass().getSimpleName();
            if (!accountName.equals(CreditCard.class.getSimpleName())) {
                String choice = accountName + " " + a.getId();
                choiceBox.getItems().add(choice);
            }
        }
        choiceBox.getSelectionModel().selectFirst();

        Label otherName = new Label("Enter recipient username");
        TextField otherNameInput = new TextField();
        TextFields.bindAutoCompletion(otherNameInput, userManager.getSubType_map().keySet());

        Label amountLbl = new Label("Enter amount you want to send:");
        TextField amountInput = new TextField(); // assume user enters a number

        Label questionLbl = new Label("Enter security question: ");
        TextField questionInput = new TextField();

        Label responseLbl = new Label("Enter response to question: ");
        TextField responseInput = new TextField();

        Button cancel = new Button("Cancel");
        Button pay = new Button("Transfer");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(cancel);
        hbBtn.getChildren().add(pay);

        gridPane.add(chooseLbl, 0, 0);
        gridPane.add(choiceBox, 1, 0);
        gridPane.add(otherName, 0, 1);
        gridPane.add(otherNameInput, 1, 1);
        gridPane.add(amountLbl, 0, 2);
        gridPane.add(amountInput, 1, 2);
        gridPane.add(questionLbl, 0, 3);
        gridPane.add(questionInput, 1, 3);
        gridPane.add(responseLbl, 0, 4);
        gridPane.add(responseInput, 1, 4);
        gridPane.add(hbBtn, 1, 5);

        cancel.setOnAction(event -> eTransferPromptScreen());
        pay.setOnAction(event -> {
            String[] aInfo = choiceBox.getValue().split("\\s+");
            String aID = aInfo[1];
            Account account = accountManager.getAccount(aID);
            String otherAccount = otherNameInput.getText();
            String question = questionInput.getText();
            String response = responseInput.getText();

            if (otherAccount.equals("") || !userManager.isPresent(otherAccount)) {
                showAlert(Alert.AlertType.ERROR, window, "Error", "Please enter the valid recipient username.", true);
            } else if (amountInput.getText().equals("")) {
                showAlert(Alert.AlertType.ERROR, window, "Error", "Please enter the amount.", true);
            } else if (question == null || response == null) {
                showAlert(Alert.AlertType.ERROR, window, "Error", "Please enter the question and response.", true);
            } else {
                double amount = Double.valueOf(amountInput.getText());
                assert account != null;
                eTransferManager.send(user.getUsername(), account.getId(), otherAccount, question, response, amount);
                showAlert(Alert.AlertType.INFORMATION, window, "Success", "eTransfer has been made", true);
                window.setScene(optionsScreen);
            }
        });

        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        window.setScene(scene);
    }

    private void acceptETransferScreen() {
        GridPane gridPane = createFormPane();
        ETransfer oldest = eTransferManager.getOldestTransfer(user.getUsername());
        if (oldest == null) {
            showAlert(Alert.AlertType.INFORMATION, window, "No Incoming eTransfer", "There is no incoming eTransfer to date", true);
            window.setScene(optionsScreen);
        } else {
            Label chooseLbl = new Label("Select account to deposit to");
            ChoiceBox<String> choiceBox = new ChoiceBox<>();

            // Add user's accounts as entries to ComboBox
            List<Account> accounts = accountManager.getListOfAccounts(user.getUsername());
            for (Account a : accounts) {
                String accountName = a.getClass().getName();
                if (!accountName.equals(CustomerOptionsGUI.class.getPackage().getName() + ".CreditCard")) {
                    String choice = accountName + " " + a.getId();
                    choiceBox.getItems().add(choice);
                } else {
                    String choice = accountName + " " + a.getId();
                    choiceBox.getItems().add(choice);
                }
            }

            Label oldestLbl = new Label(oldest.toString());
            Label questionLbl = new Label("Security question: " + oldest.getQuestion() + "?");

            Label responseLbl = new Label("Enter response: ");
            TextField responseInput = new TextField();

            Button cancel = new Button("Cancel");
            Button pay = new Button("Transfer");
            HBox hbBtn = new HBox(10);
            hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
            hbBtn.getChildren().add(cancel);
            hbBtn.getChildren().add(pay);

            gridPane.add(chooseLbl, 0, 0);
            gridPane.add(choiceBox, 1, 0);
            gridPane.add(oldestLbl, 0, 2);
            gridPane.add(questionLbl, 0, 3);
            gridPane.add(responseLbl, 0, 4);
            gridPane.add(responseInput, 1, 4);
            gridPane.add(hbBtn, 1, 5);

            cancel.setOnAction(event -> eTransferPromptScreen());
            pay.setOnAction(event -> {
                String[] aInfo = choiceBox.getValue().split("\\s+");
                String aID = aInfo[1];
                Account account = accountManager.getAccount(aID);
                String response = responseInput.getText();
                boolean successful = eTransferManager.validate(response, account, user.getUsername());
                if (!successful) {
                    showAlert(Alert.AlertType.ERROR, window, "Error", "Invalid response, try again later.", true);
                } else {
                    showAlert(Alert.AlertType.INFORMATION, window, "Success", "eTransfer has been accepted", true);
                }
                window.setScene(optionsScreen);
            });

            Scene scene = new Scene(gridPane);
            scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
            window.setScene(scene);
        }
    }

    private void makeRequestScreen() {
        GridPane gridPane = createFormPane();

        Label user = new Label("Enter username you would like to request from");
        TextField userInput = new TextField();
        TextFields.bindAutoCompletion(userInput, userManager.getSubType_map().keySet());

        Label amount = new Label("Enter amount you would like to request");
        TextField amountInput = new TextField();

        Button cancel = new Button("Cancel");
        Button request = new Button("Request");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(cancel);
        hbBtn.getChildren().add(request);

        gridPane.add(user, 0, 0);
        gridPane.add(userInput, 1, 0);
        gridPane.add(amount, 0, 1);
        gridPane.add(amountInput, 1, 1);
        gridPane.add(hbBtn, 1, 2);

        cancel.setOnAction(event -> eTransferPromptScreen());
        request.setOnAction(event -> {
            double amountIn = Double.valueOf(amountInput.getText());
            String username = userInput.getText();

            if (userManager.isCustomer(username)) {
                eTransferManager.request(this.user.getUsername(), username, amountIn);
                showAlert(Alert.AlertType.INFORMATION, window, "Success", "request has been made", true);

            } else {
                showAlert(Alert.AlertType.ERROR, window, "Error", "request is unsuccessful", true);
            }
            window.setScene(optionsScreen);
        });
        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        window.setScene(scene);

    }

    private void viewRequestScreen() {
        HashMap<String, Double> requests = eTransferManager.readRequests(user.getUsername());
        int i = 1;
        List<String> list = new ArrayList<>();
        for (String s : requests.keySet()) {
            i++;
            list.add(i + ". " + s + " requested $" + requests.get(s) + "\n");
        }

        showAlert(Alert.AlertType.INFORMATION, window, "You have " + requests.size() + " requests: ",
                "Request List: " + list.toString(), true);

        window.setScene(optionsScreen);

    }

    // Has to be public. Variables will be used to displayed as table.
    public class AccountSummary {
        private final String isPrimary;
        private final String accountType;
        private final String creationDate;
        private final double balance;
        private final String mostRecent;
        private final String id;
        private final List<String> owners;

        AccountSummary(String p, String t, String d, double b, String r, String i, List<String> o) {
            this.isPrimary = p;
            this.accountType = t;
            this.creationDate = d;
            this.balance = b;
            this.mostRecent = r;
            this.id = i;
            this.owners = o;
        }

        @SuppressWarnings("unused")
        public String getId() {
            return id;
        }

        @SuppressWarnings("unused")
        public List<String> getOwners() {
            return owners;
        }

        @SuppressWarnings("unused")
        public String getIsPrimary() {
            return this.isPrimary;
        }

        @SuppressWarnings("unused")
        public String getAccountType() {
            return this.accountType;
        }

        @SuppressWarnings("unused")
        public String getCreationDate() {
            return this.creationDate;
        }

        @SuppressWarnings("unused")
        public double getBalance() {
            return this.balance;
        }

        @SuppressWarnings("unused")
        public String getMostRecent() {
            return this.mostRecent;
        }

    }


}

