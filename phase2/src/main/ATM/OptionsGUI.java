package ATM;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * GUI for common options across all users.
 */
public abstract class OptionsGUI {
    Stage window;
    Scene welcomeScreen, optionsScreen;
    User user;

    public OptionsGUI(Stage mainWindow, Scene welcomeScreen, User user) {
        this.window = mainWindow;
        this.welcomeScreen = welcomeScreen;
        this.user = user;
    }

    public abstract Scene createOptionsScreen();

    /**
     * When user clicks 'logout'
     */
    public void logoutHandler() {
        //Every time the user logs out, the UserManager's contents will be serialized and saved.
        UserManagerSerialization backUp = new UserManagerSerialization();
        //TODO truman
//        try {
//            FileOutputStream fileOut = new FileOutputStream("phase2/src/resources/LoginManagerStorage.txt");
//            ObjectOutputStream out = new ObjectOutputStream(fileOut);
//            out.writeObject(backUp);
//            out.close();
//            fileOut.close();
//            System.err.print("Serialized data saved. ");
//        } catch (IOException i) {
//            i.printStackTrace();
//        }

        showAlert(Alert.AlertType.CONFIRMATION, window, "Logout successful",
                "Your account has been logged out. Thank you for choosing CSC207 Bank!");
        window.setScene(welcomeScreen);
    }

    public GridPane createFormPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // manage the spacing between rows and cols
        grid.setVgap(10);
        grid.setHgap(10);
        return grid;
    }

    public Scene setPasswordScreen() {
        GridPane grid = createFormPane();

        Label oldPass = new Label("Old Password:");
        grid.add(oldPass, 0, 0);
        PasswordField userTextField = new PasswordField();
        userTextField.setPromptText("old password");
        grid.add(userTextField, 1, 1);

        Label pw = new Label("New Password:");
        grid.add(pw, 0, 2);
        PasswordField pwBox = new PasswordField();
        pwBox.setPromptText("new password");
        grid.add(pwBox, 1, 2);

        Label pw1 = new Label("Confirm New Password:");
        grid.add(pw1, 0, 3);
        PasswordField pwBox1 = new PasswordField();
        grid.add(pwBox1, 1, 3);

        Button btn = new Button("Save");
        Button btn2 = new Button("Cancel");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        hbBtn.getChildren().add(btn2);
        grid.add(hbBtn, 1, 4);

        final Text actionTarget = new Text();
        grid.add(actionTarget, 1, 6);

        // Save btn
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                actionTarget.setFill(Color.FIREBRICK);
                String oldpassreal = user.getPassword();
                String oldpassenter = userTextField.getText();
                String newpass = pwBox.getText();
                String newpassagain = pwBox1.getText();

                if (!oldpassreal.equals(oldpassenter)) {
                    actionTarget.setText("Old password incorrect");
                } else if (!newpass.equals(newpassagain)) {
                    actionTarget.setText("Check new password");
                } else {
                    user.setPassword(newpass);
                    setPasswordHandler();
                }
            }
        });

        btn2.setOnAction(e -> {
            window.setScene(optionsScreen);
        });

        return new Scene(grid, 300,275);
    }

    public void setPasswordHandler() {
        showAlert(Alert.AlertType.CONFIRMATION, window, "Password changed",
                "Your password has been changed.");
        window.setScene(optionsScreen);
    }

    /**
     * A pop-up alert window
     * @param alertType e.g. CONFIRMATION, ERROR
     * @param owner window
     * @param title title of window
     * @param message alert message
     */
    public void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

}
