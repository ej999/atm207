package ATM;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * GUI for employee options.
 */
public class EmployeeOptionsGUI extends OptionsGUI {

    public EmployeeOptionsGUI(Stage mainWindow, Scene welcomeScreen, User user) {
        super(mainWindow, welcomeScreen, user);
    }

    @Override
    public Scene createOptionsScreen() {
        addOptionText("Read alerts");
        addOptionText("Create bank account for user");
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
        grid.add(hbox,0,0);
        grid.add(hbBtn,0,1);
        window.setScene(new Scene(grid, 400, 350));
    }

    void createBankAccountScreen() {
        GridPane gridPane = createFormPane();

        Label usernameLbl = new Label("Username of Customer:");
        TextField usernameInput = new TextField();

        Label typeLbl = new Label("Type of Bank Account:");
        ChoiceBox<String> typeChoice= new ChoiceBox<>();
        List<String> accountTypes = AccountManager.getTypesOfAccounts();

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

        gridPane.add(usernameLbl,0,0);
        gridPane.add(usernameInput,1,0);
        gridPane.add(typeLbl,0,1);
        gridPane.add(hbox,1,1);
        gridPane.add(hbBtn,1,2);

        cancel.setOnAction(event -> window.setScene(optionsScreen));
        create.setOnAction(event -> {
            String username = usernameInput.getText();
            String accountType = typeChoice.getValue();
            System.out.println("Customer wants a " + accountType + " account");

            if (UserManager.isPresent(username) && accountType != null) {
                //TODO: account cannot be created for some reason (AccountManager)
                AccountManager.addAccount(accountType, (Customer) UserManager.getAccount(username));
                showAlert(Alert.AlertType.CONFIRMATION, window, "Success!", "A new bank account has been created");
            } else {
                showAlert(Alert.AlertType.ERROR, window, "Error","The username does not exist. No account has been created." );
            }
            window.setScene(optionsScreen);
        });

        window.setScene(new Scene(gridPane));
    }

    void undoTransactionsScreen() {
        //TODO
        /*
        Username: <username>
        Select Account: <Account picker>
        Number of transactions to undo: <n>
        <Cancel> <Submit>
         */
        GridPane grid = createFormPane();

        Label usernameLbl = new Label("Username of Customer:");
        TextField usernameInput = new TextField();
        usernameInput.setPromptText("username");



//                    if (UserManager.isPresent(usernameLbl.getText())) {
////                Account account2undo = selectAccountPrompt((User_Customer) UserManager.getUser(username));
//                ((User_Employee_BankManager) user).undoTransactions(account2undo, Integer.valueOf(numberInput.getText()));
//                System.out.println("Undo successful.");
//            } else {
//                System.out.print("User not found. Try again? (Y/N)");
//                String proceed = reader.next().toUpperCase().trim();
//                if (proceed.equals("N")) finished = true;
//            }

        Button accountPicker = new Button("Choose account:");
        ChoiceBox<String> choiceBox = new ChoiceBox<>();

        // Once username has been entered, get their accounts
        HBox hbox = new HBox(choiceBox);

        Label n = new Label("How many:");
        TextField numberInput = new TextField();

        Button submit = new Button("Submit");
        Button cancel = new Button("Cancel");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(cancel);
        hbBtn.getChildren().add(submit);

        grid.add(usernameLbl,0,0);
        grid.add(usernameInput,1,0);
        grid.add(accountPicker,0,1);
        grid.add(hbox, 1,1);
        grid.add(n,0,2);
        grid.add(numberInput,1,2);
        grid.add(hbBtn,1,3);

        final Text actionTarget = new Text();
        grid.add(actionTarget, 1, 5);

        accountPicker.setOnAction(event -> {
//            String usernameEntered = usernameInput.getText();
//            if (UserManager.isPresent(usernameEntered)) {
//                ArrayList<Account> accounts = (UserManager.getAccount(usernameEntered));
//                System.out.println(accounts); // null but why???
//                for(Account account : accounts) {
//                    String choice = account.getClass().getName() + " " + account.getAccountNumber();
//                    choiceBox.getItems().add(choice);
//                }
//            }
        });
        cancel.setOnAction(event -> window.setScene(optionsScreen));
        submit.setOnAction(event -> {
            actionTarget.setFill(Color.FIREBRICK);
            //TODO
            // assume numberInput is a number for now...
//            if (UserManager.isPresent(usernameLbl.getText())) {
////                Account account2undo = selectAccountPrompt((User_Customer) UserManager.getUser(username));
//                ((User_Employee_BankManager) user).undoTransactions(account2undo, Integer.valueOf(numberInput.getText()));
//                System.out.println("Undo successful.");
//            } else {
//                System.out.print("User not found. Try again? (Y/N)");
//                String proceed = reader.next().toUpperCase().trim();
//                if (proceed.equals("N")) finished = true;
//            }
        });

        window.setScene(new Scene(grid));
    }
}
