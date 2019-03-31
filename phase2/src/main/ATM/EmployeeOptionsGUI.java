package ATM;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * GUI for employee options.
 */
class EmployeeOptionsGUI extends OptionsGUI {

    EmployeeOptionsGUI(Stage mainWindow, Scene welcomeScreen, User user) {
        super(mainWindow, welcomeScreen, user);
    }

    @Override
    public Scene createOptionsScreen() {
        addOptionText("Read alerts");
        addOptionText("Create bank account for user");
        addOptionText("Create joint account");
        addOptionText("Change password");
        addOptionText("Undo transactions");
        addOptionText("Logout");
        addOptions();

        getOption(0).setOnAction(event -> readAlertsScreen());
        getOption(1).setOnAction(event -> createBankAccountScreen());
        getOption(2).setOnAction(event -> createJointAccountScreen());
        getOption(4).setOnAction(event -> undoTransactionsScreen());

        return generateOptionsScreen(325, 300);
    }

    void readAlertsScreen() {
//        GridPane grid = createFormPane();
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
//        grid.add(hbox, 0, 0);
//        grid.add(hbBtn, 0, 1);
        VBox vBox = new VBox();
        vBox.getChildren().add(listView);
        vBox.getChildren().add(hbBtn);
        window.setScene(new Scene(vBox, 400, 350));
    }

    void createBankAccountScreen() {
        GridPane gridPane = createFormPane();

        Label usernameLbl = new Label("Username of Customer:");
        TextField usernameInput = new TextField();

        Label typeLbl = new Label("Type of Bank Account:");
        ChoiceBox<String> typeChoice = new ChoiceBox<>();
        List<String> accountTypes = ATM.accountManager.TYPES_OF_ACCOUNTS;

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
            User user = ATM.userManager.getUser(username);
            String accountType = Options.class.getPackage().getName() + "." + typeChoice.getValue();
            System.out.println("Customer wants a " + accountType + " account");

            if (ATM.userManager.isPresent(username)) {
                ATM.accountManager.addAccount(accountType, Collections.singletonList((Customer) user));
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

//        Label accountLbl = new Label("Choose account:");
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
//        grid.add(accountLbl,0,1);
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
            Customer user = (Customer) ATM.userManager.getUser(username);
            if (ATM.userManager.isPresent(username)) {
                List<Account> accounts = ATM.accountManager.getListOfAccounts(user);
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
            if (ATM.userManager.isPresent(username)) {
                String[] aInfo = choiceBox.getValue().split("\\s+");
                String aID = aInfo[1];
                Account account2undo = ATM.accountManager.getAccount(aID);
                account2undo.undoTransactions(num);
                showAlert(Alert.AlertType.CONFIRMATION, window, "Undone", "Undo successful");
            } else {
                showAlert(Alert.AlertType.ERROR, window, "Error", "User not found");
            }
            window.setScene(optionsScreen);
        });

        window.setScene(new Scene(grid));
    }

    void createJointAccountScreen() {
        /*
        Would you like to make a preexisting account joint or open a new one?

        Pre ->
        Enter username of primary owner:
        Enter username of secondary owner:
        Select account to be made joint: <non-joint accounts from primary owner>
        Cancel | Make account joint

        Open new one ->
        Enter username of primary owner:
        Enter username of secondary owner:
        Select account:
        Cancel | Open a new joint account
         */
        GridPane gridPane = createFormPane();
        Label question = new Label("Would you like to make a preexisting account joint or open a new one?");
        Button pre = new Button("Make a preexisting account joint");
        Button openNew = new Button("Open a new one");
        Button cancel = new Button("Cancel");

        HBox hbBtn = new HBox();
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(cancel);

        gridPane.add(question, 0, 0, 2, 1);
        gridPane.add(pre, 0, 1);
        gridPane.add(openNew, 1, 1);
        gridPane.add(hbBtn, 1, 2);

        pre.setOnAction(event -> makePreexistingJointAccountScreen());
        openNew.setOnAction(event -> openNewBankAccountScreen());
        cancel.setOnAction(event -> window.setScene(optionsScreen));

        window.setScene(new Scene(gridPane));
    }

    void makePreexistingJointAccountScreen() {
        GridPane gridPane = createFormPane();

        Label primaryLbl = new Label("Enter username of primary holder");
        TextField primaryTextField = new TextField();

        Label secondaryLbl = new Label("Enter username of secondary holder:");
        TextField secondaryTextField = new TextField();

        Button select = new Button("Select account to be made joint:");
        ChoiceBox<String> choices = new ChoiceBox<>();

        Button cancel = new Button("Cancel");
        Button make = new Button("Make account joint");
        HBox hbBtn = new HBox();
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(cancel);
        hbBtn.getChildren().add(make);

        gridPane.add(primaryLbl, 0, 0);
        gridPane.add(primaryTextField, 1, 0);
        gridPane.add(secondaryLbl, 0, 1);
        gridPane.add(secondaryTextField, 1, 1);
        gridPane.add(select, 0, 2);
        gridPane.add(choices, 1, 2);
        gridPane.add(hbBtn, 1, 3);

        select.setOnAction(event -> {
            String username = primaryTextField.getText();
            Customer customer = (Customer) ATM.userManager.getUser(username);
            if (ATM.userManager.isCustomer(username)) {
                List<Account> accounts = ATM.accountManager.getListOfAccounts(customer);
                for (Account a : accounts) {
                    if (!a.isJoint()) {
                        choices.getItems().add(a.getType() + " " + a.getId());
                    }
                }
            } else {
                showAlert(Alert.AlertType.WARNING, window, "Warning", username + " doesn't exist in our system.");
            }
        });

        cancel.setOnAction(event -> window.setScene(optionsScreen));
        make.setOnAction(event -> {
            String secondary = secondaryTextField.getText();
            Customer secondaryHolder = (Customer) ATM.userManager.getUser(secondary);
            if (ATM.userManager.isCustomer(secondary)) {
                String id = choices.getValue().split("\\w+")[1];
                Account account = ATM.accountManager.getAccount(id);
                account.addOwner(secondaryHolder);
                showAlert(Alert.AlertType.CONFIRMATION, window, "Success", "Your account has been made joint.");
            } else {
                showAlert(Alert.AlertType.WARNING, window, "Warning", secondary + " doesn't exist in our system.");
            }
            window.setScene(optionsScreen);
        });

        window.setScene(new Scene(gridPane));
    }

    void openNewBankAccountScreen() {
        /*
        Enter username of primary holder:
        Enter username of secondary holder:
        Select account:
        Cancel | Open a new joint account
         */
        GridPane gridPane = createFormPane();

        Label primaryLbl = new Label("Enter username of primary holder");
        TextField primaryTextField = new TextField();

        Label secondaryLbl = new Label("Enter username of secondary holder:");
        TextField secondaryTextField = new TextField();

        Label select = new Label("Select account:");
        ChoiceBox<String> choices = new ChoiceBox<>();

        List<String> accountTypes = ATM.accountManager.TYPES_OF_ACCOUNTS;

        for (String type : accountTypes) {
            choices.getItems().add(type);
        }

        Button cancel = new Button("Cancel");
        Button open = new Button("Open a new joint account");
        HBox hbBtn = new HBox();
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(cancel);
        hbBtn.getChildren().add(open);

        gridPane.add(primaryLbl, 0, 0);
        gridPane.add(primaryTextField, 1, 0);
        gridPane.add(secondaryLbl, 0, 1);
        gridPane.add(secondaryTextField, 1, 1);
        gridPane.add(select, 0, 2);
        gridPane.add(choices, 1, 2);
        gridPane.add(hbBtn, 1, 3);

        cancel.setOnAction(event -> window.setScene(optionsScreen));
        open.setOnAction(event -> {
            String primary = primaryTextField.getText();
            String secondary = secondaryTextField.getText();
            if (ATM.userManager.isCustomer(secondary) && ATM.userManager.isCustomer(primary)) {
                User primaryUser = ATM.userManager.getUser(primary);
                User secondaryUser = ATM.userManager.getUser(secondary);
                List<Customer> owners = new ArrayList<>();
                owners.add((Customer) primaryUser);
                owners.add((Customer) secondaryUser);
                String type = choices.getValue();
                ATM.accountManager.addAccount(type, owners);
                showAlert(Alert.AlertType.CONFIRMATION, window, "Success", "A new joint account has been made.");
            } else {
                showAlert(Alert.AlertType.ERROR, window, "Error", "Invalid usernames");
            }
            window.setScene(optionsScreen);
        });

        window.setScene(new Scene(gridPane));
    }
}
