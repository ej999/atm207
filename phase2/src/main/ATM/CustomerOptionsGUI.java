package ATM;

import javafx.scene.Scene;
import javafx.stage.Stage;

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

    public void showAccountSummaryScreen() {
        //TODO
        /*
        e.g.' Customer with username "steve" and password "cat" '
        table view
        Account | balance | date
        OK
         */
    }

    public void payBillScreen() {
        //TODO
        /*
        enter amount
        non-user account name
        Cancel | Pay
         */
    }

    public void makeTransferBetweenScreen() {
        //TODO
        /*
        choicebox
        transfer FROM
        transfer TO
        amount
        Cancel | Transfer
         */
    }

    public void makeTransferAnotherScreen() {
        //TODO
        /*
        username:
        amount:
        Cancel | Transfer
         */
    }

    public void depositScreen() {
        //TODO
        /*
        amount:
        Cancel | Deposit
         */
    }

    public void withdrawalScreen() {
        //TODO
        /*
        Select account drop-down
        amount
        Cancel | Withdraw
         */
    }

    public void requestAccountScreen() {
        //TODO
        /*
        Select account type picker drop-down CHOICE-BOX control element
        Cancel | Request
         */
    }

    public void changePrimaryScreen() {
        //TODO
    }
}

