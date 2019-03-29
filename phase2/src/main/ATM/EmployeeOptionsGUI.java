package ATM;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * GUI for employee options.
 * //TODO: refactor
 */
public class EmployeeOptionsGUI extends OptionsGUI {

    public EmployeeOptionsGUI(Stage mainWindow, Scene welcomeScreen, User user) {
        super(mainWindow, welcomeScreen, user);
    }

    @Override
    public Scene createOptionsScreen() {
        addOptionText("Read alerts");
        addOptionText("Create bank account for user");
//        addOptionText("Create joint account"); // TODO
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
        GridPane grid = createFormPane();
        ListView<String> listView = new ListView<>();

        Button goBack = new Button("Go Back");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(goBack);

        goBack.setOnAction(event -> window.setScene(optionsScreen));

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("phase2/src/resources/alerts.txt"));
            String alert = reader.readLine();
            while (alert != null) {
                listView.getItems().add(alert);
                alert = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HBox hbox = new HBox(listView);
        grid.add(hbox, 0, 0);
        grid.add(hbBtn, 0, 1);
        window.setScene(new Scene(grid, 400, 350));
    }

    void createBankAccountScreen() {
        GridPane gridPane = createFormPane();

        Label usernameLbl = new Label("Username of Customer:");
        TextField usernameInput = new TextField();

        Label typeLbl = new Label("Type of Bank Account:");
        ChoiceBox<String> typeChoice = new ChoiceBox<>();
        List<String> accountTypes = AccountManager.TYPES_OF_ACCOUNTS;

        for (String type : accountTypes) {
            typeChoice.getItems().add(type);
        }

        HBox hbox = new HBox(typeChoice);

        Button cancel = new Button("Cancel");
        Button create = new Button("Create");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(cancel);
        hbBtn.getChildren().add(create);

        gridPane.add(usernameLbl, 0, 0);
        gridPane.add(usernameInput, 1, 0);
        gridPane.add(typeLbl, 0, 1);
        gridPane.add(hbox, 1, 1);
        gridPane.add(hbBtn, 1, 2);

        cancel.setOnAction(event -> window.setScene(optionsScreen));
        create.setOnAction(event -> {
            String username = usernameInput.getText();
            String accountType = Option.class.getPackage().getName() + typeChoice.getValue();
            System.out.println("Customer wants a " + accountType + " account");

            if (UserManager.isPresent(username)) {
                AccountManager.addAccount(accountType, (Customer) UserManager.getUser(username));
                showAlert(Alert.AlertType.CONFIRMATION, window, "Success!", "A new bank account has been created");
            } else {
                showAlert(Alert.AlertType.ERROR, window, "Error", "Invalid customer. Please try again");
            }
            window.setScene(optionsScreen);
        });

        window.setScene(new Scene(gridPane));
    }

    void undoTransactionsScreen() {
        GridPane grid = createFormPane();

        Label usernameLbl = new Label("Username of Customer:");
        TextField usernameInput = new TextField();
        usernameInput.setPromptText("username");

        Button accountPicker = new Button("Choose account:");
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        HBox hbox = new HBox(choiceBox);

        Label n = new Label("Number of transactions\nto undo:");
        TextField numberInput = new TextField();

        Button submit = new Button("Submit");
        Button cancel = new Button("Cancel");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(cancel);
        hbBtn.getChildren().add(submit);

        grid.add(usernameLbl, 0, 0);
        grid.add(usernameInput, 1, 0);
        grid.add(accountPicker, 0, 1);
        grid.add(hbox, 1, 1);
        grid.add(n, 0, 2);
        grid.add(numberInput, 1, 2);
        grid.add(hbBtn, 1, 3);

        final Text actionTarget = new Text();
        grid.add(actionTarget, 1, 5);

        accountPicker.setOnAction(event -> {
            actionTarget.setFill(Color.FIREBRICK);
            // Add user's account entries to ComboBox
            String username = usernameInput.getText();
            if (UserManager.isPresent(username)) {
                List<Account> accounts = AccountManager.getListOfAccounts(username);
                for (Account a : accounts) {
                    String choice = a.getClass().getName() + " " + a.getId();
                    choiceBox.getItems().add(choice);
                }
            } else {
                actionTarget.setText("User doesn't exists");
            }
        });

        cancel.setOnAction(event -> window.setScene(optionsScreen));
        submit.setOnAction(event -> {
            actionTarget.setFill(Color.FIREBRICK);
            String username = usernameInput.getText();
            int num = Integer.valueOf(numberInput.getText());
            // What I'm trying to do is to get account obj user has selected
            if (UserManager.isPresent(username)) {
                String[] aInfo = choiceBox.getValue().split("\\s+");
                String aID = aInfo[1];
                Account account2undo = AccountManager.getAccount(aID);
                account2undo.undoTransactions(num);
                showAlert(Alert.AlertType.CONFIRMATION, window, "Undone", "Undo successful");
            } else {
                showAlert(Alert.AlertType.ERROR, window, "Error", "User not found");
            }
            window.setScene(optionsScreen);
        });

        window.setScene(new Scene(grid));
    }
}
