package ATM;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;

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
        //TODO
        /*
        Perhaps a table view?
        Date | Request
         */
    }

    void createBankAccountScreen() {
        //TODO
        /*
        Username of Customer: <username>
        Type of Bank Account: <Account Picker control>
        <Cancel> <Create>
         */
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
