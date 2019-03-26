package ATM;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * GUI for employee options.
 */
public class EmployeeOptionsGUI extends OptionsGUI {

    public EmployeeOptionsGUI(Stage mainWindow, Scene welcomeScreen, User user) {
        super(mainWindow, welcomeScreen, user);
    }

    @Override
    public Scene createOptionsScreen() {
        addOptionText("Read alerts");
        addOptionText("Create bank account for user");
        addOptionText("Change password");
        addOptionText("Undo transactions");
        addOptionText("Logout");
        addOptions();

        getOption(0).setOnAction(event -> readAlertsScreen());
        getOption(1).setOnAction(event -> createBankAccountScreen());
        getOption(3).setOnAction(event -> undoTransactionsScreen());

        return generateOptionsScreen(325, 300);
    }

    void readAlertsScreen() {
        //TODO
        /*
        Perhaps a table view?
        Date | Request
         */
    }

    void createBankAccountScreen() {
        //TODO
        /*
        Username of Customer: <username>
        Type of Bank Account: <Account Picker control>
        <Cancel> <Create>
         */
    }

    void undoTransactionsScreen() {
        //TODO
        /*
        Username: <username>
        Select Account: <Account picker>
        Number of transactions to undo: <n>
        <Cancel> <Submit>
         */
    }
}
