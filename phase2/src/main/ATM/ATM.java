package ATM;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.stage.Window;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

/**
 * An ATM that allows customers and employees to interact with their login accounts.
 * It will display options on the screen and the user will select an option by typing the corresponding number
 * on the keyboard.
 */

//TODO ATM class no longer extends to Observable. Check how it affects the program.
public class ATM extends Application implements Serializable {
    private Stage window;
    private Scene welcomeScreen, BMOptions, tellerOptions, customerOptions;
    private SystemUser systemUser;
    private boolean helped = false;

    /**
     * Allow user to login by entering username and password.
     * <p>
     * It will return the SystemUser account if the login is valid; otherwise, it'll consistently asking user to
     * enter username and password.
     */
    private static SystemUser loginPrompt() {
        System.out.println("Welcome to CSC207 Banking Service!");

        Scanner reader = new Scanner(System.in);

        SystemUser systemUser = null;
        while (systemUser == null) {
            System.out.print("Please enter your username: ");
            String username = reader.next();
            System.out.print("Please enter your password: ");
            String password = reader.next();

            systemUser = LoginManager.verifyLogin(username, password);

        }
        System.out.println("\nSystemUser success. Hi " + systemUser.getUsername() + "!");
        return systemUser;
    }

    /**
     * Preloaded bank manage account: {username: jen, password: 1234}
     * Preloaded customer account: {username: steve, password: 1234}
     */
    public static void main(String[] args) {

        // Load the back up of SystemUser account lists after restarting the ATM.
        LoginManagerBackup load_backup = new LoginManagerBackup();
        LoginManagerBackup backup = load_backup.returnFileBackup();
        LoginManager.login_map = new HashMap<>();
        // Instantiate an Employee account here for basic functions here.
        SystemUser_Employee_BankManager jen = new SystemUser_Employee_BankManager("jen", "1234");
        LoginManager.addLogin(jen);
        if (backup.deleted == 0) {
            LoginManager.login_map = load_backup.returnFileBackup().login_map;

        } else {
            // If the backup was deleted, recreate the default state here.

            jen.createLogin("Customer", "steve", "1234");
            jen.addAccount("Chequing", ((SystemUser_Customer) LoginManager.getLogin("steve")), 1234);
            jen.addAccount("LineOfCredit", ((SystemUser_Customer) LoginManager.getLogin("steve")), 4321);
            jen.addAccount("Saving", ((SystemUser_Customer) LoginManager.getLogin("steve")), 1000);
            jen.addAccount("CreditCard", ((SystemUser_Customer) LoginManager.getLogin("steve")), 420);
        }

        //Java FX -> invoke start method
        launch(args);


        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        int now = (calendar.get(Calendar.MONTH));

        // The ATM should displays SystemUser interface all the time, until it is being shut down.
        //noinspection InfiniteLoopStatement
        while (true) {
            // Constantly checking if now is the start of the month.
            now = new ATMSystem().checkMonth(now);


            // A login session.
            SystemUser systemUser = loginPrompt();
            new Options(systemUser);
        }

    }

    @Override
    public void init() throws Exception {
        super.init();
        System.out.println("Inside init() method! Perform necessary initializations here.");
        createBMOptionsScreen();
        createTellerOptions();
        createCustomerOptions();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("Inside stop() method! Destroy resources. Perform Cleanup.");
    }

    /**
     * Main entry point of a JavaFX application. This is where the user interface is created and made visible.
     *
     * @param primaryStage the main window
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        //TODO: How to make gui work with Options class? MVC?
        //TODO: use software design principles

        /*
        A bit of terminology
        stage - window
        scene - content inside window
        primaryStage - main window
        layout - how everything's arranged on screen

        Tutorial: https://docs.oracle.com/javafx/2/get_started/jfxpub-get_started.htm
         */
        window = primaryStage;
        window.setTitle("CSC207 Banking Services");

        GridPane gridPane = createLoginFormPane();
        addUIControls(gridPane);
        welcomeScreen = new Scene(gridPane, 300, 275);

        window.setScene(welcomeScreen);
        window.setResizable(false);
        window.show();

    }

    private void createBMOptionsScreen() {
        // Options for bank manager
        Button button1 = new Button("Read alerts");
        Button button2 = new Button("Create login for user");
        Button button3 = new Button("Create bank account for user");
        Button button4 = new Button("Restock ATM");
        Button button5 = new Button("Undo transaction");
        Button button6 = new Button("Change password");
        Button button7 = new Button("Load custom bank data");
        Button button8 = new Button("Clear all bank data");
        Button button9 = new Button("Logout");

        // TODO: Then we need handlers for all nine buttons...


        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setVgap(10);

        gridPane.add(button1, 0, 1);
        gridPane.add(button2, 0, 2);
        gridPane.add(button3, 0, 3);
        gridPane.add(button4, 0, 4);
        gridPane.add(button5, 0, 5);
        gridPane.add(button6, 0, 6);
        gridPane.add(button7, 0, 7);
        gridPane.add(button8, 0, 8);
        gridPane.add(button9, 0, 9);

        Text message = new Text("How can we help you today?");
        message.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        gridPane.add(message, 0, 0, 2, 1);

        BMOptions = new Scene(gridPane, 300, 450);
    }

    private void createTellerOptions() {
        Button button1 = new Button("Read alerts");
        Button button2 = new Button("Create bank account for user");
        Button button3 = new Button("Change password");
        Button button4 = new Button("Undo transaction");
        Button button5 = new Button("Logout");

        // TODO: Then we need handlers for all five buttons...

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setVgap(10);

        gridPane.add(button1, 0, 1);
        gridPane.add(button2, 0, 2);
        gridPane.add(button3, 0, 3);
        gridPane.add(button4, 0, 4);
        gridPane.add(button5, 0, 5);

        Text message = new Text("How can we help you today?");
        message.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        gridPane.add(message, 0, 0, 2, 1);

        tellerOptions = new Scene(gridPane, 300, 300);
    }

    private void createCustomerOptions() {
        // TODO
    }

    private GridPane createLoginFormPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // manage the spacing between rows and cols
        grid.setVgap(10);
        grid.setHgap(10);
        return grid;
    }

    private void addUIControls(GridPane grid) {
        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("Username:");
        grid.add(userName, 0, 1);
        TextField userTextField = new TextField();
        userTextField.setPromptText("username");
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);
        PasswordField pwBox = new PasswordField();
        pwBox.setPromptText("password");
        grid.add(pwBox, 1, 2);

        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        final Text actionTarget = new Text();
        grid.add(actionTarget, 1, 6);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                actionTarget.setFill(Color.FIREBRICK);
                String username = userTextField.getText();
                String password = pwBox.getText();

                if (username.isEmpty()) {
                    actionTarget.setText("Please enter your username");
                } else if (password.isEmpty()) {
                    actionTarget.setText("Please enter your password");
                } else {
                    systemUser = LoginManager.verifyLogin(username, password);

                    if (systemUser == null) {
                        actionTarget.setText("Login attempt failed");
                    } else {
                        showAlert(Alert.AlertType.CONFIRMATION, grid.getScene().getWindow(), "Login Successful!",
                                "Hi " + username);

                        if (systemUser instanceof SystemUser_Employee_BankManager) {
                            window.setScene(BMOptions);
                        } else if (systemUser instanceof SystemUser_Employee_Teller) {
                            window.setScene(tellerOptions);
                        } else if (systemUser instanceof SystemUser_Customer) {
                            window.setScene(customerOptions);
                        }
                    }
                }
            }
        });

    }

    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

}