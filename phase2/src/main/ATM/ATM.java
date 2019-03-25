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

import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * An ATM that allows customers and employees to conduct a range of financial transactions and operations by using
 * their login accounts. It is displayed on both PrintStream and GUI.
 */

//TODO ATM class no longer extends to Observable. Check how it affects the program.
public class ATM extends Application {
    private Stage window;
    private Scene welcomeScreen, BMOptions, tellerOptions, customerOptions;
    private User user;
    private boolean helped = false;

    /**
     * Allow user to login by entering username and password.
     * <p>
     * It will return the User account if the login is valid; otherwise, it'll consistently asking user to
     * enter username and password.
     */
    private static User loginPrompt() {
        System.out.println("Welcome to CSC207 Banking Service!");

        Scanner reader = new Scanner(System.in);

        User user = null;
        while (user == null) {
            System.out.print("Please enter your username: ");
            String username = reader.next();
            System.out.print("Please enter your password: ");
            String password = reader.next();

            user = UserManager.verifyLogin(username, password);

        }
        System.out.println("\nUser success. Hi " + user.getUsername() + "!");
        return user;
    }


    public static void main(String[] args) {
        // Load the back up of User account lists after restarting the ATM.
        UserManagerSerialization serialization = new UserManagerSerialization();
        serialization.deserialize();

        // If the backup was deleted or database is empty, recreate the default state here.
        if (UserManager.user_map.isEmpty()) {
            System.err.println("Warning: data is not retrieved from Firebase database!");

            // Instantiate an Employee account here for basic functions here.
            UserManager.createLogin("BankManager", "jen", "1234");
            UserManager.createLogin("Teller", "pete", "1234");
            UserManager.createLogin("Customer", "steve", "1234");

            User_Employee_BankManager jen = (User_Employee_BankManager) UserManager.user_map.get("jen");
            User pete = UserManager.user_map.get("pete");
            User steve = UserManager.user_map.get("steve");


            jen.addAccount("Chequing", ((User_Customer) UserManager.getLogin("steve")), 1234);
            jen.addAccount("LineOfCredit", ((User_Customer) UserManager.getLogin("steve")), 4321);
            jen.addAccount("Saving", ((User_Customer) UserManager.getLogin("steve")), 1000);
            jen.addAccount("CreditCard", ((User_Customer) UserManager.getLogin("steve")), 420);

            // Save to FireBase database.
            FireBaseDBAccess fbDb = new FireBaseDBAccess();
            fbDb.save(jen, "User", jen.getUsername());
            fbDb.save(pete, "User", pete.getUsername());
            fbDb.save(steve, "User", steve.getUsername());
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
            User user = loginPrompt();
            new Options(user);
        }
    }

    @Override
    public void init() throws Exception {
        super.init();
        System.out.println("Inside init() method! Perform necessary initializations here.");
        // Cannot initialize options screen just yet because main window, welcome screen, and user don't exist
//        createBMOptionsScreen();
//        createTellerOptions();
//        createCustomerOptions();
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
        BankManagerOptionsGUI gui = new BankManagerOptionsGUI(window, welcomeScreen, user);
        BMOptions = gui.createOptionsScreen();
    }

    private void createTellerOptions() {
        EmployeeOptionsGUI gui = new EmployeeOptionsGUI(window, welcomeScreen, user);
        tellerOptions = gui.createOptionsScreen();
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
                    user = UserManager.verifyLogin(username, password);

                    if (user == null) {
                        actionTarget.setText("Login attempt failed");
                    } else {
                        showAlert(Alert.AlertType.CONFIRMATION, grid.getScene().getWindow(), "Login Successful!",
                                "Hi " + username);


                        // At this point user has been created so we can create all the gui.

                        if (user instanceof User_Employee_BankManager) {
                            createBMOptionsScreen();
                            window.setScene(BMOptions);
                        } else if (user instanceof User_Employee_Teller) {
                            createTellerOptions();
                            window.setScene(tellerOptions);
                        } else if (user instanceof User_Customer) {
                            createCustomerOptions();
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