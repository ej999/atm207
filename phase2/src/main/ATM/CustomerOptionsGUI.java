package ATM;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * GUI for customer options.
 */
public class CustomerOptionsGUI extends OptionsGUI{ //extends OptionsGUI{

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

        return generateOptionsScreen(350,450);
    }

    public void showAccountSummaryScreen() {
        //TODO
    }

    public void payBillScreen() {
        //TODO
    }

    public void makeTransferBetweenScreen() {
        //TODO
    }

    public void makeTransferAnotherScreen() {
        //TODO
    }

    public void depositScreen() {
        //TODO
    }

    public void withdrawalScreen() {
        //TODO
    }

    public void requestAccountScreen() {
        //TODO
    }

    public void changePrimaryScreen() {
        //TODO
    }
}

