package ATM;

import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.List;
import javax.swing.*;

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
        addOptionText("Request Creating an Account");
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
        //TODO
        /*
        choose account
        enter amount
        non-user account name
        Cancel | Pay
         */
        GridPane gridPane = createFormPane();
        Label chooseLbl = new Label("Choose account:");
//        ChoiceBox<String> choices = new ChoiceBox<>();
//
//        List<String> accounts = ((Customer) user).getAccounts();
//        for(String a : accounts) {
//
//        }

//        Scanner reader = new Scanner(System.in);
//
//        System.out.println();
//        List<String> accounts = customer.getAccounts();
//        int i = 1;
//        for (String a : accounts) {
//            if (!AccountManager.getAccount(a).getClass().getName().contains(exclusion)) {
//                System.out.println("[" + i + "] " + AccountManager.getAccount(a));
//                i++;
//            }
//        }
//
//        int option = -99;
//        while (option > accounts.size() || option < 0) {
//            System.out.print("Please select an account: ");
//            option = reader.nextInt();
//        }
//        return AccountManager.getAccount(accounts.get(option - 1));

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
//                System.out.println("Bill has been paid.");
//            } else {
//                System.err.println("Payment is unsuccessful.");
//            }
//        } catch (IOException e) {
//            // do nothing?
//        }
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

