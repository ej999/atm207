package ATM;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.ArrayList;
import java.util.List;

/**
 * GUI for common options across all users.
 */
public abstract class OptionsGUI {
    Stage window;
    Scene welcomeScreen, optionsScreen;
    User user;
    private ArrayList<Button> options = new ArrayList<>();
    private ArrayList<String> optionsText = new ArrayList<>();
    /*
    This is the idea for options screen: optionsText(programmer's part) -> create buttons -> create layout with buttons
    So basically create the Options screen is semi-automatically done for you.
     */

    public OptionsGUI(Stage mainWindow, Scene welcomeScreen, User user) {
        this.window = mainWindow;
        this.welcomeScreen = welcomeScreen;
        this.user = user;
    }

    public abstract Scene createOptionsScreen();

    public void addOptionText(String text) {
        optionsText.add(text);
    }

    public Button getOption(int i) {
        return options.get(i);
    }

    /**
     * Combine layout and controls to form Options Screen
     *
     * @param width  of screen
     * @param height of screen
     */
    public Scene generateOptionsScreen(int width, int height) {
        GridPane gridPane = createFormPane();
        addOptionsToLayout(gridPane);
        addMessageToOptionsScreen("What can we do for you today?", gridPane);
        optionsScreen = new Scene(gridPane, width, height);
        optionsScreen.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        return optionsScreen;
    }

    /**
     * Populates Option Screen with all the options
     *
     * @param layout gridPane
     */
    public void addOptionsToLayout(GridPane layout) {
        for (int i = 1; i <= options.size(); i++) {
            layout.add(options.get(i - 1), 0, i);
        }
    }

    /**
     * Create all buttons with text from optionsText
     */
    public void addOptions() {
        for (String text : optionsText) {
            Button btn = new Button(text);
            if (text.equals("Change password")) {
                btn.setOnAction(e -> changePasswordScreen());
            } else if (text.equals("Logout")) {
                btn.setOnAction(event -> logoutHandler());
            }
            options.add(btn);
        }
    }

    public void addMessageToOptionsScreen(String message, GridPane gridPane) {
        Text _message = new Text(message);
        _message.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        gridPane.add(_message, 0, 0, 2, 1);
    }

    /**
     * When user clicks 'logout'
     */
    public void logoutHandler() {
        ATM.serialization.serializeAll();

        showAlert(Alert.AlertType.CONFIRMATION, window, "Logout successful",
                "Your account has been logged out. Thank you for choosing CSC207 Bank!");
        window.setScene(welcomeScreen);
    }

    /**
     * Common layout for the various scenes
     *
     * @return gridPane layout
     */
    public GridPane createFormPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // manage the spacing between rows and cols
        grid.setVgap(10);
        grid.setHgap(10);
        return grid;
    }

    private void addUIControlsToPasswordScreen(GridPane grid) {
        Label oldPass = new Label("Old Password:");
        grid.add(oldPass, 0, 0);
        PasswordField oldPassField = new PasswordField();
        oldPassField.setPromptText("old password");
        grid.add(oldPassField, 1, 0);

        Label newPass = new Label("New Password:");
        grid.add(newPass, 0, 1);
        PasswordField pwBox = new PasswordField();
        pwBox.setPromptText("new password");
        grid.add(pwBox, 1, 1);

        Label confirmPass = new Label("Confirm New Password:");
        grid.add(confirmPass, 0, 2);
        PasswordField pwBox1 = new PasswordField();
        grid.add(pwBox1, 1, 2);

        Button save = new Button("Save");
        Button cancel = new Button("Cancel");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(cancel);
        hbBtn.getChildren().add(save);
        grid.add(hbBtn, 1, 3);

        final Text actionTarget = new Text();
        grid.add(actionTarget, 1, 5);

        save.setOnAction(event -> {
            actionTarget.setFill(Color.FIREBRICK);
            String realOldPass = user.getPassword();
            String oldPassTyped = oldPassField.getText();
            String newPass1 = pwBox.getText();
            String newPassAgain = pwBox1.getText();

            if (!realOldPass.equals(oldPassTyped)) {
                actionTarget.setText("Old password incorrect");
            } else if (!newPass1.equals(newPassAgain)) {
                actionTarget.setText("Check new password");
            } else if (newPass1.isEmpty()) {
                actionTarget.setText("New password cannot be empty");
            } else {
                user.setPassword(newPass1);
                setPasswordHandler();
            }
        });

        cancel.setOnAction(e -> window.setScene(optionsScreen));
    }

    public void changePasswordScreen() {
        GridPane grid = createFormPane();
        addUIControlsToPasswordScreen(grid);
        window.setScene(new Scene(grid, 400, 275));
    }

    public void setPasswordHandler() {
        showAlert(Alert.AlertType.CONFIRMATION, window, "Password changed",
                "Your password has been changed.");
        window.setScene(optionsScreen);
    }

    /**
     * A pop-up alert window
     *
     * @param alertType e.g. CONFIRMATION, ERROR
     * @param owner     window
     * @param title     title of window
     * @param message   alert message
     */
    public void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    public HBox getTwoButtons(String first, String second) {
        /*
        first | second
        e.g. Cancel | Submit
         */
        Button firstBtn = new Button(first);
        Button secondBtn = new Button(second);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(firstBtn);
        hbBtn.getChildren().add(secondBtn);
        return hbBtn;
    }

    public ChoiceBox<String> getBankAccounts(String exclusion) {
        /*
        drop-down of this user's accounts, excluding account <exclusion>
         */
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        String username = user.getUsername();
        List<Account> accounts = AccountManager.getListOfAccounts(username);
        for (Account a : accounts) {
            String accountName = a.getClass().getName();
            if (!accountName.equals(exclusion)) {
                String choice = accountName + " " + a.getId();
                choiceBox.getItems().add(choice);
            }
        }
        return choiceBox;
    }

    public void addControlsToLayout(GridPane gridPane, List<Label> labels, List<Node> controls, HBox buttons) {
        /*
        Like this ->
        label : control
        label : control
            .
            .
            .
        two buttons
         */
        int r = 0;
        for (; r < labels.size(); r++) {
            gridPane.add(labels.get(r), 0, r);
            gridPane.add(controls.get(r), 1, r);
        }
        gridPane.add(buttons, 1, r);
    }

}
