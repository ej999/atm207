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
import org.controlsfx.control.textfield.TextFields;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import static ATM.ATM.accountManager;
import static ATM.ATM.userManager;

/**
 * A GUI for employee options.
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
        addOptionText("Create GIC account");
        addOptionText("Change password");
        addOptionText("Undo transactions");
        addOptionText("Logout");
        addOptions();

        getOption(0).setOnAction(event -> readAlertsScreen());
        getOption(1).setOnAction(event -> createBankAccountScreen());
        getOption(2).setOnAction(event -> createJointAccountScreen());
        getOption(5).setOnAction(event -> undoTransactionsScreen());
        getOption(3).setOnAction(event -> createGICScreen());

        return generateOptionsScreen();
    }

    void readAlertsScreen() {
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

        VBox vBox = new VBox();
        vBox.getChildren().add(listView);
        vBox.getChildren().add(hbBtn);
        window.setScene(new Scene(vBox, 750, 350));
    }

    void createBankAccountScreen() {
        GridPane gridPane = createFormPane();

        Label usernameLbl = new Label("Username of Customer:");
        TextField usernameInput = new TextField();
        TextFields.bindAutoCompletion(usernameInput, userManager.getSubType_map().keySet());

        Label typeLbl = new Label("Type of Bank Account:");
        ChoiceBox<String> typeChoice = new ChoiceBox<>();
        typeChoice.getSelectionModel().selectFirst();
        Collection<String> accountTypes = accountManager.TYPES_OF_ACCOUNTS;

        for (String type : accountTypes) {
            if (!type.equalsIgnoreCase("GIC")) {
                typeChoice.getItems().add(type);
            }
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
            String accountType = typeChoice.getValue();

            if (userManager.isCustomer(username)) {
                User user = userManager.getUser(username);
                if (accountType.equals("Youth") && ((Customer) user).getDob() != null) {
                    int age = ((Customer) user).getAge();
                    if (age < 20) {
                        accountManager.addAccount(accountType, Collections.singletonList(username));
                        showAlert(Alert.AlertType.INFORMATION, window, "Success!", "A new bank account has been created", true);
                        window.setScene(optionsScreen);
                    } else {
                        showAlert(Alert.AlertType.ERROR, window, "Error", "You are not eligible for a Youth account.", true);
                    }
                } else {
                    accountManager.addAccount(accountType, Collections.singletonList(username));
                    showAlert(Alert.AlertType.INFORMATION, window, "Success!", "A new bank account has been created", true);
                    window.setScene(optionsScreen);
                }
            } else {
                showAlert(Alert.AlertType.ERROR, window, "Error", "Invalid customer. Please try again", true);
            }

        });
        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        window.setScene(scene);
    }

    void undoTransactionsScreen() {
        GridPane grid = createFormPane();

        Label usernameLbl = new Label("Username of Customer:");
        TextField usernameInput = new TextField();
        TextFields.bindAutoCompletion(usernameInput, userManager.getSubType_map().keySet());
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
            choiceBox.getItems().clear();
            actionTarget.setFill(Color.FIREBRICK);
            // Add user's account entries to ComboBox
            String username = usernameInput.getText();
            if (userManager.isPresent(username)) {
                List<Account> accounts = accountManager.getListOfAccounts(username);
                for (Account a : accounts) {
                    String choice = a.getClass().getName() + " " + a.getId();
                    choiceBox.getItems().add(choice);
                }
                choiceBox.getSelectionModel().selectFirst();
            } else {
                actionTarget.setText("User doesn't exists");
            }
        });

        cancel.setOnAction(event -> window.setScene(optionsScreen));
        submit.setOnAction(event -> {
            actionTarget.setFill(Color.FIREBRICK);
            String username = usernameInput.getText();
            if (!userManager.isPresent(username)) {
                showAlert(Alert.AlertType.ERROR, window, "Error", "Please enter a valid username", true);
            } else if (!Pattern.compile("^[0-9]*$").matcher(numberInput.getText()).find()) {
                showAlert(Alert.AlertType.ERROR, window, "Error", "Please enter a valid number of transaction.", true);
            } else {
                int num = Integer.valueOf(numberInput.getText());
                // What I'm trying to do is to get account obj user has selected
                if (userManager.isPresent(username)) {
                    String[] aInfo = choiceBox.getValue().split("\\s+");
                    String aID = aInfo[1];
                    Account account2undo = accountManager.getAccount(aID);
                    assert account2undo != null;
                    account2undo.undoTransactions(num);
                    showAlert(Alert.AlertType.INFORMATION, window, "Undone", "Undo successful", true);
                } else {
                    showAlert(Alert.AlertType.ERROR, window, "Error", "User not found", true);
                }
                window.setScene(optionsScreen);
            }
        });

        Scene scene = new Scene(grid);
        scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        window.setScene(scene);
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

        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        window.setScene(scene);
    }

    private void makePreexistingJointAccountScreen() {
        GridPane gridPane = createFormPane();

        Label primaryLbl = new Label("Enter username of primary holder");
        TextField primaryTextField = new TextField();

        Label secondaryLbl = new Label("Enter username of secondary holder:");
        TextField secondaryTextField = new TextField();
        TextFields.bindAutoCompletion(secondaryTextField, userManager.getSubType_map().keySet());

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
            if (userManager.isCustomer(username)) {
                List<Account> accounts = accountManager.getListOfAccounts(username);
                for (Account a : accounts) {
                    if (a.isNotJoint()) {
                        choices.getItems().add(a.getType() + " " + a.getId());
                    }
                }
            } else {
                showAlert(Alert.AlertType.WARNING, window, "Warning", username + " doesn't exist in our system.", true);
            }
        });

        cancel.setOnAction(event -> window.setScene(optionsScreen));
        make.setOnAction(event -> {
            String secondary = secondaryTextField.getText();
            if (userManager.isCustomer(secondary)) {
                String id = choices.getValue().split("\\w+")[1];
                Account account = accountManager.getAccount(id);
                User user = userManager.getUser(secondary);
                int age = ((Customer) user).getAge();

                assert account != null;
                if (account.getType().equals("Youth")) {
                    if (age < 20) {
                        account.addOwner(secondary);
                        showAlert(Alert.AlertType.INFORMATION, window, "Success", "Your account has been made joint.", true);
                        window.setScene(optionsScreen);
                    } else {
                        showAlert(Alert.AlertType.ERROR, window, "Error", secondary + " is not eligible for a Youth account.", true);
                    }
                } else {
                    account.addOwner(secondary);
                    showAlert(Alert.AlertType.INFORMATION, window, "Success", "Your account has been made joint.", true);
                    window.setScene(optionsScreen);
                }

            } else {
                showAlert(Alert.AlertType.WARNING, window, "Warning", secondary + " doesn't exist in our system.", true);
            }

        });

        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        window.setScene(scene);
    }

    private void openNewBankAccountScreen() {
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
        TextFields.bindAutoCompletion(secondaryTextField, userManager.getSubType_map().keySet());

        Label select = new Label("Select account:");
        ChoiceBox<String> choices = new ChoiceBox<>();

        Collection<String> accountTypes = accountManager.TYPES_OF_ACCOUNTS;

        for (String type : accountTypes) {
            if (!type.equalsIgnoreCase("GIC")) {
                choices.getItems().add(type);
            }
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
            if (userManager.isCustomer(secondary) && userManager.isCustomer(primary)) {
                Customer user1 = (Customer) userManager.getUser(primary);
                Customer user2 = (Customer) userManager.getUser(secondary);
                List<String> ownersUsername = new ArrayList<>();
                ownersUsername.add(primary);
                ownersUsername.add(secondary);
                String type = choices.getValue();
                if (type.equals("Youth")) {
                    if (user1.isAdult() && user2.isAdult()) {
                        accountManager.addAccount(type, ownersUsername);
                        showAlert(Alert.AlertType.INFORMATION, window, "Success", "A new joint account has been made.", true);
                        window.setScene(optionsScreen);
                    } else {
                        showAlert(Alert.AlertType.ERROR, window, "Error", "Invalid age.", true);
                    }
                } else {
                    accountManager.addAccount(type, ownersUsername);
                    showAlert(Alert.AlertType.INFORMATION, window, "Success", "A new joint account has been made.", true);
                    window.setScene(optionsScreen);
                }
            } else {
                showAlert(Alert.AlertType.ERROR, window, "Error", "Invalid username", true);
            }
        });

        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        window.setScene(scene);
    }

    void createGICScreen() {
        GridPane gridPane = createFormPane();

        Label usernameLbl = new Label("Username of Customer:");
        TextField usernameInput = new TextField();
        TextFields.bindAutoCompletion(usernameInput, userManager.getSubType_map().keySet());
        Label periodLabel = new Label("Months");
        TextField period = new TextField();
        Label rateLabel = new Label("Interest Rate");
        TextField rate = new TextField();


        Button cancel = new Button("Cancel");
        Button create = new Button("Create");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(cancel);
        hbBtn.getChildren().add(create);

        gridPane.add(usernameLbl, 0, 0);
        gridPane.add(usernameInput, 1, 0);
        gridPane.add(periodLabel, 0, 1);
        gridPane.add(period, 1, 1);
        gridPane.add(rateLabel, 0, 2);
        gridPane.add(rate, 1, 2);
        gridPane.add(hbBtn, 1, 3);

        cancel.setOnAction(event -> window.setScene(optionsScreen));
        create.setOnAction(event -> {
            String username = usernameInput.getText();
            int month = Integer.valueOf(period.getText());
            double interest = Double.valueOf(rate.getText());

            if (userManager.isPresent(username)) {
                accountManager.addGICAccount(interest, month, Collections.singletonList(username));
                showAlert(Alert.AlertType.INFORMATION, window, "Success!", "A new GIC account has been created", true);
            } else {
                showAlert(Alert.AlertType.ERROR, window, "Error", "Invalid customer. Please try again", true);
            }
            window.setScene(optionsScreen);
        });

        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(ATM.class.getResource("style.css").toExternalForm());
        window.setScene(scene);
    }
}
