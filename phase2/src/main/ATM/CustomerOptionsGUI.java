package ATM;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * GUI for customer options.
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
        //TODO
        /*
        choicebox
        transfer FROM
        transfer TO
        amount
        Cancel | Transfer
         */
        //        JComboBox<Account> accountsFrom = new JComboBox<Account>();
//        JComboBox<Account> accountsTo = new JComboBox<Account>();
//        for (int i = 0; (user.accounts).length(); i++) {
//            accountsFrom.addItem(user.accounts.get(i));
//            accountsTo.addItem(user.accounts.get(i));
//        }
        GridPane gridPane = createFormPane();
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
        //TODO
        /*
        amount:
        Cancel | Deposit
         */
        GridPane gridPane = createFormPane();
        window.setScene(new Scene(gridPane));
    }

    private void withdrawalScreen() {
        //TODO
        /*
        Select account drop-down
        amount
        Cancel | Withdraw
         */
        GridPane gridPane = createFormPane();
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
}

