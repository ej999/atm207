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
public class BankManagerOptionsGUI extends EmployeeOptionsGUI{
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
        // Options for Bank Manager.
        Button button1 = new Button("Read alerts");
        Button button2 = new Button("Create login for user");
        Button button3 = new Button("Create bank account for user");
        Button button4 = new Button("Restock ATM");
        Button button5 = new Button("Undo transaction");
        Button button6 = new Button("Change password");
        Button button7 = new Button("Load custom bank data");
        Button button8 = new Button("Clear all bank data");
        Button button9 = new Button("Logout");

        // TODO: Then we need handlers for all nine buttons...
        button6.setOnAction(event -> window.setScene(setPasswordScreen()));
        button9.setOnAction(event -> logoutHandler());


        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setVgap(10);

        gridPane.add(button1, 0, 1);
        gridPane.add(button2, 0, 2);
        gridPane.add(button3, 0, 3);
        gridPane.add(button4, 0, 4);
        gridPane.add(button5, 0, 5);
        gridPane.add(button6, 0, 6);
        gridPane.add(button7, 0, 7);
        gridPane.add(button8, 0, 8);
        gridPane.add(button9, 0, 9);

        Text message = new Text("How can we help you today?");
        message.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        gridPane.add(message, 0, 0, 2, 1);
        optionsScreen = new Scene(gridPane, 300, 450);

        return optionsScreen;
    }


}
