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
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ATM.ATM.accountManager;
import static ATM.ATM.serialization;

/**
 * A GUI for common options across all users.
 */
abstract class OptionsGUI {
    final Stage window;
    Scene optionsScreen;
    final User user;
    private final Scene welcomeScreen;
    private final ArrayList<Button> options = new ArrayList<>();
    private final ArrayList<String> optionsText = new ArrayList<>();

    /*
    This is the idea for options screen:
        options text (programmer's part) -> create buttons -> create layout with buttons -> generate options screen
        the programmer just needs to add in the options text.
     */

    OptionsGUI(Stage mainWindow, Scene welcomeScreen, User user) {
        this.window = mainWindow;
        this.welcomeScreen = welcomeScreen;
        this.user = user;
    }

    abstract Scene createOptionsScreen();

    void addOptionText(String text) {
        optionsText.add(text);
    }

    Button getOption(int i) {
        return options.get(i);
    }

    /**
     * Combine layout and controls to form Options Screen
     */
    Scene generateOptionsScreen() {
        GridPane gridPane = createFormPane();
        addOptionsToLayout(gridPane);
        addMessageToOptionsScreen(gridPane);
        optionsScreen = new Scene(gridPane);
        optionsScreen.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        return optionsScreen;
    }

    /**
     * Populates Options Screen with all the options
     *
     * @param layout gridPane
     */
    private void addOptionsToLayout(GridPane layout) {
        for (int i = 1; i <= options.size(); i++) {
            if ((i - 1) % 2 == 0) {
                layout.add(options.get(i - 1), 0, i);
            } else {
                layout.add(options.get(i - 1), 1, i - 1);
            }
        }
    }

    /**
     * Create all buttons with text from optionsText
     */
    void addOptions() {
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

    private void addMessageToOptionsScreen(GridPane gridPane) {
        Text message = new Text("Hi " + user.getUsername() + "! What can we do for you today?");
        message.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        gridPane.add(message, 0, 0, 2, 1);
    }

    /**
     * When user selects 'logout'
     */
    void logoutHandler() {
        serialization.serializeAll();

        showAlert(Alert.AlertType.INFORMATION, window, "Logout successful",
                "Your account has been logged out. Thank you for choosing CSC-Bank!", false);
        window.setScene(welcomeScreen);
    }

    /**
     * Common layout for the various scenes
     *
     * @return gridPane layout
     */
    GridPane createFormPane() {
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
                actionTarget.setText("Old password not valid");
            } else if (!newPass1.equals(newPassAgain)) {
                actionTarget.setText("Are you sure you typed the same password twice?");
            } else if (newPass1.isEmpty()) {
                actionTarget.setText("New password cannot be empty");
            } else {
                user.setPassword(newPass1);
                setPasswordHandler();
            }
        });

        cancel.setOnAction(e -> window.setScene(optionsScreen));
    }

    void changePasswordScreen() {
        GridPane grid = createFormPane();
        addUIControlsToPasswordScreen(grid);
        Scene scene = new Scene(grid, 400, 275);
        scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        window.setScene(scene);
    }

    private void setPasswordHandler() {
        showAlert(Alert.AlertType.INFORMATION, window, "Password changed", "Your password has been changed", true);
        window.setScene(optionsScreen);
    }

    /**
     * A pop-up alert window
     *
     * @param alertType e.g. INFORMATION, ERROR
     * @param owner     window
     * @param title     title of window
     * @param message   alert message
     */
    Optional<ButtonType> showAlert(Alert.AlertType alertType, Window owner, String title, String message, boolean wait) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.initStyle(StageStyle.UTILITY);
        if (wait) {
            return alert.showAndWait();
        } else {
            alert.show();
            return Optional.empty();
        }
    }

    HBox getTwoButtons(String second) {
        Button firstBtn = new Button("Cancel");
        Button secondBtn = new Button(second);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(firstBtn);
        hbBtn.getChildren().add(secondBtn);
        return hbBtn;
    }

    ChoiceBox<String> getBankAccounts() {
        /*
        drop-down of this user's accounts, excluding account <exclusion>
         */
        ChoiceBox<String> choiceBox = new ChoiceBox<>();

        List<Account> accounts = accountManager.getListOfAccounts(user.getUsername());
        for (Account a : accounts) {
            String accountName = a.getClass().getSimpleName();
            if (!accountName.equals("no exclusion")) {
                String choice = accountName + " " + a.getId();
                choiceBox.getItems().add(choice);
            }
        }
        return choiceBox;
    }

    void addControlsToLayout(GridPane gridPane, List<Label> labels, List<Node> controls, HBox buttons) {
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
