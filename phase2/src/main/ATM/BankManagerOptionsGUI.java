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
 * GUI for Bank Manager.
 */
public class BankManagerOptionsGUI extends EmployeeOptionsGUI {
    /**
     * Constructor should take in main window, welcome screen, user? from ATM
     * Let's think of some methods...
     * - createBMOptionsScreen
     * - createAlerts
     */

    public BankManagerOptionsGUI(Stage mainWindow, Scene welcomeScreen, User user) {
        super(mainWindow, welcomeScreen, user);
    }

    @Override
    public Scene createOptionsScreen() {
        addOptionText("Read alerts");
        addOptionText("Create user");
        addOptionText("Create bank account for user");
        addOptionText("Restock ATM");
        addOptionText("Undo transactions");
        addOptionText("Change password");
        addOptionText("Clear all bank data");
        addOptionText("Logout");
        addOptions();

        getOption(0).setOnAction(event -> readAlertsScreen());
        getOption(1).setOnAction(event -> createUserScreen());
        getOption(2).setOnAction(event -> createBankAccountScreen());
        getOption(3).setOnAction(event -> restockATMScreen());
        getOption(4).setOnAction(event -> undoTransactionsScreen());
        getOption(6).setOnAction(event -> clearBankDataScreen());

        return generateOptionsScreen(325,450);
    }

    public void createUserScreen() {
        //TODO
        /*
        User type: drop-down (choice-box)
        username:
        password:
        Cancel | Create
        New screen -> DOBScreen
        ...
         */
    }

    public void restockATMScreen() {
        //TODO
        /*
        $5 bills:
        $10 bills:
        $20 bills:
        $50 bills:
        Cancel | Restock
         */
    }

    public void clearBankDataScreen() {
        //TODO
        /*
        Warning label
        Go back | Proceed
         */
    }

}
