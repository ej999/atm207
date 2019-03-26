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

        // TODO: Then we need handlers for all nine buttons...

        GridPane gridPane = createFormPane();
        addOptionsToLayout(gridPane);
        addMessageToOptionsScreen("What can we do for you today?", gridPane);
        optionsScreen = new Scene(gridPane, 325, 450);
        optionsScreen.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        return optionsScreen;
    }


}
