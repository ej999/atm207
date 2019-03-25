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
 * GUI for employee options.
 */
public class EmployeeOptionsGUI extends OptionsGUI {

    public EmployeeOptionsGUI(Stage mainWindow, Scene welcomeScreen, SystemUser user) {
        super(mainWindow, welcomeScreen, user);
    }

    @Override
    public Scene createOptionsScreen() {
        Button button1 = new Button("Read alerts");
        Button button2 = new Button("Create bank account for user");
        Button button3 = new Button("Change password");
        Button button4 = new Button("Undo transaction");
        Button button5 = new Button("Logout");

        button3.setOnAction(event -> setPasswordScreen());
        button5.setOnAction(event -> logoutHandler());

        // TODO: Then we need handlers for all five buttons...

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setVgap(10);

        gridPane.add(button1, 0, 1);
        gridPane.add(button2, 0, 2);
        gridPane.add(button3, 0, 3);
        gridPane.add(button4, 0, 4);
        gridPane.add(button5, 0, 5);

        Text message = new Text("How can we help you today?");
        message.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        gridPane.add(message, 0, 0, 2, 1);

        return new Scene(gridPane, 300, 300);
    }
}
