package ATM;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An ATM that allows customers and employees to conduct a range of financial transactions and operations by using
 * their user accounts. It is displayed on both PrintStream and GUI.
 */

//TODO ATM class no longer extends to Observable. Check how it affects the program.
public class ATM extends Application {
    private Stage window;
    private Scene welcomeScreen, BMOptions, tellerOptions, customerOptions;
    private User user;

    public static void main(String[] args) {
        // Disable java.util.logging, since we are using PrintStream to interact with users.
        Logger.getLogger("").setLevel(Level.OFF);

        // Load the Maps of User and Account objects to their respected Manager class from FireBase database.
        ManagersSerialization serialization = new ManagersSerialization();
        serialization.deserialize();

        // Firebase known bug: https://stackoverflow.com/questions/48462093/storing-empty-arrays-in-firebase
        for (String id : AccountManager.account_map.keySet()) {
            Account account = AccountManager.getAccount(id);
            if (account.getTransactionHistory() == null) {
                account.transactionHistory = new Stack<Transaction>();
            }
        }


        // If the Map of User objects is empty or deleted, then create a demo.
        if (UserManager.user_map.isEmpty() || AccountManager.account_map.isEmpty()) {
            UserManager.createAccount(BankManager.class.getName(), "jen", "1234");
            UserManager.createAccount(Teller.class.getName(), "pete", "1234");
            UserManager.createAccount(Customer.class.getName(), "steve", "1234");

            AccountManager.addAccount(Chequing.class.getName(), ((Customer) UserManager.getUser("steve")), 1234);
            AccountManager.addAccount(CreditLine.class.getName(), ((Customer) UserManager.getUser("steve")), 4321);
            AccountManager.addAccount(Saving.class.getName(), ((Customer) UserManager.getUser("steve")), 1000);
            AccountManager.addAccount(CreditCard.class.getName(), ((Customer) UserManager.getUser("steve")), 420);

            // Save to FireBase database.
            serialization.serialize();
        }

        //Java FX -> invoke start method
        launch(args);

        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        int now = (calendar.get(Calendar.MONTH));

        // The ATM should displays User interface all the time, until it is being shut down.
        //noinspection InfiniteLoopStatement
        while (true) {
            // Constantly checking if now is the start of the month.
            now = new ATMSystem().checkMonth(now);

            // A login session.
            User user = authPrompt();
            new Options(user);
        }
    }

    /**
     * Allow user to login by entering username and password.
     * <p>
     * It will return the User if the login is valid; otherwise, it'll consistently asking user to
     * enter username and password.
     */
    private static User authPrompt() {
        System.out.println("Welcome to CSC207 Banking Service!");

        Scanner reader = new Scanner(System.in);

        String username = null;
        User user;
        boolean authResult = false;
        while (!authResult) {
            System.out.print("Please enter your username: ");
            username = reader.next();
            System.out.print("Please enter your password: ");
            String password = reader.next();

            authResult = UserManager.auth(username, password);
        }
        user = UserManager.getUser(username);

        System.out.println("\nUser success. Hi " + user.getUsername() + "!");
        return user;
    }

    @Override
    public void init() throws Exception {
        super.init();
//        System.err.println("Inside init() method! Perform necessary initializations here.");
    }

//    @Override
//    public void stop() throws Exception {
//        super.stop();
//        System.err.println("Inside stop() method! Destroy resources. Perform Cleanup.");
//    }

    /**
     * Main entry point of a JavaFX application. This is where the user interface is created and made visible.
     *
     * @param primaryStage the main window
     */
    @Override
    public void start(Stage primaryStage) {
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

        GridPane gridPane = createUserFormPane();
        addUIControls(gridPane);
        welcomeScreen = new Scene(gridPane, 300, 275);
        // Apply styles
        welcomeScreen.getStylesheets().add(ATM.class.getResource("Login.css").toExternalForm());
        window.setScene(welcomeScreen);
        window.setResizable(false);
        window.show();

    }

    private void createBMOptionsScreen() {
        BankManagerOptionsGUI gui = new BankManagerOptionsGUI(window, welcomeScreen, user);
        BMOptions = gui.createOptionsScreen();
    }

    private void createTellerOptionsScreen() {
        EmployeeOptionsGUI gui = new EmployeeOptionsGUI(window, welcomeScreen, user);
        tellerOptions = gui.createOptionsScreen();
    }

    private void createCustomerOptionsScreen() {
        CustomerOptionsGUI gui = new CustomerOptionsGUI(window, welcomeScreen, user);
        customerOptions = gui.createOptionsScreen();
    }

    private GridPane createUserFormPane() {
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
        grid.add(scenetitle, 0, 0, 2, 1);
        scenetitle.setId("welcome-text");

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
        actionTarget.setId("actiontarget");

        btn.setOnAction(event -> {
            String username = userTextField.getText();
            String password = pwBox.getText();

            if (username.isEmpty()) {
                actionTarget.setText("Please enter your username");
            } else if (password.isEmpty()) {
                actionTarget.setText("Please enter your password");
            } else {

                boolean authResult = UserManager.auth(username, password);

                if (!authResult) {
                    actionTarget.setText("Login attempt failed");
                } else {
                    user = UserManager.getUser(username);
                    showAlert(Alert.AlertType.CONFIRMATION, grid.getScene().getWindow(), "Login Successful!",
                            "Hi " + username);


                    // At this point the user has been created so we can create the options screen

                    if (user instanceof BankManager) {
                        createBMOptionsScreen();
                        window.setScene(BMOptions);
                    } else if (user instanceof Teller) {
                        createTellerOptionsScreen();
                        window.setScene(tellerOptions);
                    } else if (user instanceof Customer) {
                        createCustomerOptionsScreen();
                        window.setScene(customerOptions);
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