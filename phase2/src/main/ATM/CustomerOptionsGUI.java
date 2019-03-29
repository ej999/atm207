package ATM;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GUI for customer options.
 * TODO: some methods contain duplicate code.
 */
public class CustomerOptionsGUI extends OptionsGUI {

    public CustomerOptionsGUI(Stage mainWindow, Scene welcomeScreen, User user) {
        super(mainWindow, welcomeScreen, user);
    }

    @Override
    public Scene createOptionsScreen() {
        addOptionText("Show my account summary");
        addOptionText("Pay a Bill");
        addOptionText("Make a Transfer between my Accounts");
        addOptionText("Make a Transfer to another User");
        addOptionText("Cash/Cheque Deposit");
        addOptionText("Cash Withdrawal");
        addOptionText("Request Creating an Account"); // Customer can request for a joint account
//        addOptionText("make Joint Account"); //TODO
//        addOptionText("Show transaction history"); //TODO
        addOptionText("Change Primary Account");
        addOptionText("Change Password");
        addOptionText("Logout");
        addOptions();

        getOption(0).setOnAction(event -> showAccountSummaryScreen());
        getOption(1).setOnAction(event -> payBillScreen());
        getOption(2).setOnAction(event -> makeTransferBetweenScreen());
        getOption(3).setOnAction(event -> makeTransferAnotherScreen());
        getOption(4).setOnAction(event -> depositScreen());
        getOption(5).setOnAction(event -> withdrawalScreen());
        getOption(6).setOnAction(event -> requestAccountScreen());
        getOption(7).setOnAction(event -> changePrimaryScreen());
        getOption(8).setOnAction(event -> changePasswordScreen());
        getOption(9).setOnAction(event -> logoutHandler());

        return generateOptionsScreen(350, 450);
    }

    private void showAccountSummaryScreen() {
        //TODO
        /*
        e.g.' Customer with username "steve" and password "cat" '
        table view
        Primary | Account Type | Creation Data | balance | Most Recent Transac
        OK -> net total
         */
        GridPane gridPane = createFormPane();
//        TableView<String> tableView = new TableView<>();
        // not doing table view, too much time...


        //        returnMessage.append("\n\u001B[1mPrimary\t\tAccount Type\t\tCreation Date\t\t\t\t\tBalance\t\tMost Recent Transaction" +
//                "\u001B[0m");
        //        returnMessage.append("\n\n\u001B[1mYour net total is \u001B[0m$").append(netTotal());
        window.setScene(new Scene(gridPane));
    }

    private void payBillScreen() {
        GridPane gridPane = createFormPane();
        Label chooseLbl = new Label("Choose account:");
        ChoiceBox<String> choiceBox = new ChoiceBox<>();

        // Add user's accounts as entries to ComboBox
        String username = user.getUsername();
        List<Account> accounts = AccountManager.getListOfAccounts(username);
        for (Account a : accounts) {
            String accountName = a.getClass().getName();
            if (!accountName.equals("ATM.CreditCard")) {
                String choice = accountName + " " + a.getId();
                choiceBox.getItems().add(choice);
            }
        }

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
            Account account = AccountManager.getAccount(aID);
            double amount = Double.valueOf(amountInput.getText());
            String payee = nameInput.getText();
            try {
                if (((AccountTransferable) account).payBill(amount, payee)) {
                    showAlert(Alert.AlertType.CONFIRMATION, window, "Success", "Bill has been paid.");
                } else {
                    showAlert(Alert.AlertType.ERROR, window, "Error", "Payment is unsuccessful.");
                }
            } catch (IOException e) {
                // do nothing?
            }
            window.setScene(optionsScreen);
        });

        window.setScene(new Scene(gridPane));
    }

    private void makeTransferBetweenScreen() {
        GridPane gridPane = createFormPane();
        Label chooseLbl = new Label("Choose account:");
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        ChoiceBox<String> otherChoiceBox = new ChoiceBox<>();

        // Add user's accounts as entries to ComboBox
        String username = user.getUsername();
        List<Account> accounts = AccountManager.getListOfAccounts(username);
        for (Account a : accounts) {
            String accountName = a.getClass().getName();
            if (!accountName.equals("ATM.CreditCard")) {
                String choice = accountName + " " + a.getId();
                choiceBox.getItems().add(choice);
                otherChoiceBox.getItems().add(choice);
            } else {
                String choice = accountName + " " + a.getId();
                otherChoiceBox.getItems().add(choice);
            }
        }

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
            Account account = AccountManager.getAccount(aID);
            Account otherAccount = AccountManager.getAccount(oID);
            double amount = Double.valueOf(amountInput.getText());
            if (((AccountTransferable) account).transferBetweenAccounts(amount, otherAccount)) {
                showAlert(Alert.AlertType.CONFIRMATION, window, "Success", "Transfer has been made.");
            } else {
                showAlert(Alert.AlertType.ERROR, window, "Error", "Transfer is unsuccessful.");
            }
            window.setScene(optionsScreen);
        });

        window.setScene(new Scene(gridPane));
    }

    private void makeTransferAnotherScreen() {
        //TODO
        /*
        username:
        amount:
        Cancel | Transfer
         */
        GridPane gridPane = createFormPane();
        window.setScene(new Scene(gridPane));
    }

    private void depositScreen() {
        GridPane gridPane = createFormPane();

        Button cancel = new Button("Cancel");
        Button depoCash = new Button("Deposit Cash");
        Button depoCheque = new Button("Deposit Cheque");

        gridPane.add(cancel, 0, 0);
        gridPane.add(depoCash, 1, 0);
        gridPane.add(depoCheque, 2, 0);

        cancel.setOnAction(event -> window.setScene(optionsScreen));
        depoCash.setOnAction(event -> depoCashScreen());
        depoCheque.setOnAction(event -> depoChequeScreen());

        window.setScene(new Scene(gridPane));
    }

    private void depoCashScreen() {
        Chequing primary = (Chequing) AccountManager.getAccount(((Customer) user).getPrimary());
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
        for (Integer d : Cash.DENOMINATIONS) {
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
        goBack.setOnAction(event -> depoCashScreen());
        deposit.setOnAction(event -> {
            // d -> quantity
            Map<Integer, Integer> depositedBills = new HashMap<>();

            for (Integer d : textField.keySet()) {
                depositedBills.put(d, Integer.valueOf(textField.get(d).getText()));
            }
            primary.depositBill(depositedBills);
            showAlert(Alert.AlertType.CONFIRMATION, window, "Success", "Amount successfully deposited");
            window.setScene(optionsScreen);
        });

        window.setScene(new Scene(grid));
    }

    private void depoChequeScreen() {
        Chequing primary = (Chequing) AccountManager.getAccount(((Customer) user).getPrimary());
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
            primary.deposit(Double.valueOf(amountInput.getText()));
            showAlert(Alert.AlertType.CONFIRMATION, window, "Success!", "Amount successfully deposited");
            window.setScene(optionsScreen);
        });

        window.setScene(new Scene(gridPane));
    }

    private void withdrawalScreen() {
        GridPane gridPane = createFormPane();

        Label chooseLbl = new Label("Choose account:");
        ChoiceBox<String> choiceBox = getBankAccounts("no exclusion");

        Label amountLbl = new Label("Amount to Withdraw:");
        TextField amountInput = new TextField(); // assume user enters a number

        HBox hbBtn = getTwoButtons("Cancel", "Withdraw");

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
            double amount = Double.valueOf(amountInput.getText());
            double actualAmount = amount - amount % 5;
            account.withdraw(actualAmount);
            String message = "$" + actualAmount + " withdrawn";
            showAlert(Alert.AlertType.CONFIRMATION, window, "Success!", message);
            window.setScene(optionsScreen);
        });

        window.setScene(new Scene(gridPane));
    }

    private void requestAccountScreen() {
        //TODO
        /*
        Select account type picker drop-down CHOICE-BOX control element
        Cancel | Request
         */
        GridPane gridPane = createFormPane();
        window.setScene(new Scene(gridPane));
    }

    private void changePrimaryScreen() {
        //TODO
        GridPane gridPane = createFormPane();
        window.setScene(new Scene(gridPane));
    }

    private Account getAccountFromChoiceBox(ChoiceBox<String> choiceBox) {
        /*
        choiceBox like this ->
        ATM.<account> <id>
            .
            .
            .
         */
        String[] aInfo = choiceBox.getValue().split("\\s+");
        String aID = aInfo[1];
        return AccountManager.getAccount(aID);
    }
}

