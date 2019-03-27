package ATM;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;

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

        return generateOptionsScreen(325, 450);
    }

    public void createUserScreen() {
        GridPane gridPane = createFormPane();

        Label userType = new Label("User Type:");
        ChoiceBox<String> choiceBox = new ChoiceBox<>();

        for(String type : UserManager.getTypesOfAccounts()) {
            choiceBox.getItems().add(type);
        }

        Label usernameLbl = new Label("Username:");
        Label passwordLbl = new Label("Password:");

        TextField usernameInput = new TextField();
        PasswordField passwordField = new PasswordField();

        Button cancel = new Button("Cancel");
        Button create = new Button("Create");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(cancel);
        hbBtn.getChildren().add(create);

        gridPane.add(userType, 0,0);
        gridPane.add(choiceBox, 1,0);
        gridPane.add(usernameLbl, 0,1);
        gridPane.add(usernameInput, 1,1);
        gridPane.add(passwordLbl, 0,2);
        gridPane.add(passwordField, 1,2);
        gridPane.add(hbBtn, 1,3);

        // I'm not gonna check the validity of password

        cancel.setOnAction(event -> window.setScene(optionsScreen));
        create.setOnAction(event -> {
            String type = choiceBox.getValue();
            String username = usernameInput.getText();
            String password = passwordField.getText();
            boolean created = UserManager.createAccount(type, username, password);

            if (created && type.equals(Customer.class.getName())) {
                createDOBScreen(username);
            } else {
                showAlert(Alert.AlertType.ERROR, window, "Error", "We are sorry user couldn't be created");
                window.setScene(optionsScreen);
            }
        });

        window.setScene(new Scene(gridPane));
    }

    public void createDOBScreen(String username) {
        GridPane gridPane = createFormPane();

        Label question = new Label("Would you like to set the Date of Birth as well?");
        Button yes = new Button("Yes");
        Button no = new Button("No");
        HBox hbBtn = new HBox(10);
        hbBtn.getChildren().add(yes);
        hbBtn.getChildren().add(no);

        gridPane.add(question,0,0);
        gridPane.add(hbBtn,1,0);

        final Text actionTarget = new Text();
        TextField dobInput = new TextField();
        gridPane.add(actionTarget, 0, 3);

        no.setOnAction(event -> window.setScene(optionsScreen));
        yes.setOnAction(event -> {
            actionTarget.setFill(Color.BLACK);
            actionTarget.setText("Enter Date of Birth (YYYY-MM-DD):");
            gridPane.add(dobInput,1,3);

            String _dob = dobInput.getText();

            try {
                String dob = LocalDate.parse(_dob).toString();
                ((Customer) UserManager.getAccount(username)).setDob(dob);
                showAlert(Alert.AlertType.CONFIRMATION, window, "Success","Date of Birth for " + username + " is set to " + dob + "." );
            } catch (DateTimeException e) {
                showAlert(Alert.AlertType.ERROR, window, "Error","Are you sure you born on a day that doesn't exist?" );
            }
        });

        window.setScene(new Scene(gridPane));

    }

    public void restockATMScreen() {
        GridPane grid = createFormPane();

        Label five = new Label("Enter amount of 5 dollar bills:");
        Label ten = new Label("Enter amount of 10 dollar bills:");
        Label twenty = new Label("Enter amount of 20 dollar bills:");
        Label fifty = new Label("Enter amount of 50 dollar bills:");

        // Assume inputs are correct...
        TextField fiveI = new TextField();
        TextField tenI = new TextField();
        TextField twentyI = new TextField();
        TextField fiftyI = new TextField();

        Button cancel = new Button("Cancel");
        Button restock_ = new Button("Restock");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(cancel);
        hbBtn.getChildren().add(restock_);

        grid.add(five,0,0);
        grid.add(fiveI,1,0);
        grid.add(ten,0,1);
        grid.add(tenI,1,1);
        grid.add(twenty,0,2);
        grid.add(twentyI,1,2);
        grid.add(fifty,0,3);
        grid.add(fiftyI,1,3);
        grid.add(hbBtn,1,4);

        cancel.setOnAction(event -> window.setScene(optionsScreen));
        restock_.setOnAction(event -> {
            ArrayList<Integer> restock = new ArrayList<>();
            restock.add(Integer.valueOf(fiveI.getText()));
            restock.add(Integer.valueOf(tenI.getText()));
            restock.add(Integer.valueOf(twentyI.getText()));
            restock.add(Integer.valueOf(fiftyI.getText()));
            ((BankManager) user).restockMachine(restock);
            showAlert(Alert.AlertType.CONFIRMATION, window, "Success", "Restocking success!");
        });

        window.setScene(new Scene(grid));
    }

    public void clearBankDataScreen() {
        GridPane grid = createFormPane();

        Label warningLbl = new Label("\"WARNING: Committing a fraud with value exceeding one million dollars\nmight result in 14 year jail sentence!");
        Button proceed = new Button("Proceed");
        Button cancel = new Button("Cancel");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(cancel);
        hbBtn.getChildren().add(proceed);

        grid.add(warningLbl, 0, 0);
        grid.add(hbBtn, 1, 1);

        cancel.setOnAction(event -> window.setScene(optionsScreen));
        proceed.setOnAction(event -> {
            new UserManagerSerialization().deleteDatabase();
            showAlert(Alert.AlertType.CONFIRMATION, window, "Cleared", "Data has been cleared. Good Luck!");
            window.close();
            System.exit(0);
        });

        window.setScene(new Scene(grid));
    }

}
