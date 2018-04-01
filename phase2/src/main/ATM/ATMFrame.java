package ATM;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import static ATM.ATM.userManager;

public class ATMFrame extends Application {

    private Stage window;
    private Scene welcomeScreen;

    /**
     * Main entry point of a JavaFX application. This is where the user interface is created and made visible.
     *
     * @param primaryStage the main window
     */
    @Override
    public void start(Stage primaryStage) {
        /*
        A bit of terminology
        stage - window
        scene - content inside window
        primaryStage - main window
        layout - how everything's arranged on screen

        Tutorial: https://docs.oracle.com/javafx/2/get_started/jfxpub-get_started.htm
         */
        window = primaryStage;
        window.setTitle("CSC-Bank ATM");

        GridPane gridPane = createUserFormPane();
        addUIControls(gridPane);
        welcomeScreen = new Scene(gridPane, 300, 275);
//        welcomeScreen = new Scene(gridPane);
        // Apply styles
        welcomeScreen.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        window.setScene(welcomeScreen);
        window.show();
    }

    private GridPane createUserFormPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // manage the spacing between rows and cols
        grid.setVgap(10);
        grid.setHgap(10);
        return grid;
    }

    /**
     * Add controls to the grid (e.g. labels, textFields, buttons).
     *
     * @param grid layout
     */
    private void addUIControls(GridPane grid) {
        Text sceneTitle = new Text("Welcome!");
        grid.add(sceneTitle, 0, 0, 2, 1);
        sceneTitle.setId("welcome-text");

        Label userName = new Label("Username:");
        grid.add(userName, 0, 1);
        TextField userTextField = new TextField();
        TextFields.bindAutoCompletion(userTextField, userManager.user_map.keySet());
        userTextField.setPromptText("username");

        userName.setId("login");
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);
        PasswordField pwBox = new PasswordField();
        pwBox.setPromptText("password");
        pw.setId("login");
        grid.add(pwBox, 1, 2);

        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        final Text actionTarget = new Text();
        grid.add(actionTarget, 1, 6);
        actionTarget.setId("actionTarget");
        btn.setOnAction(event -> buttonHandler(userTextField, pwBox, actionTarget));

    }

    private void buttonHandler(TextField userTextField, PasswordField pwBox, Text actionTarget) {
        String username = userTextField.getText();
        String password = pwBox.getText();

        if (username.isEmpty()) {
            actionTarget.setText("Please enter your username");
        } else if (password.isEmpty()) {
            actionTarget.setText("Please enter your password");
        } else {

            boolean authResult = userManager.auth(username, password);

            if (!authResult) {
                actionTarget.setText("Login attempt failed");
                System.err.println("Login attempt failed");
            } else {
                actionTarget.setText("");
                User user = userManager.getUser(username);
                window.setScene(user.createOptionsScreen(window, welcomeScreen));
            }
        }
    }

}
