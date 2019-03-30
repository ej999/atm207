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

public class ATMFrame extends Application {

    private Stage window;
    private Scene welcomeScreen, BMOptions, tellerOptions, customerOptions;
    private User user;

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

    //    @Override
//    public void stop() throws Exception {
//        super.stop();
//        System.err.println("Inside stop() method! Destroy resources. Perform Cleanup");
//    }


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

                boolean authResult = ATM.userManager.auth(username, password);

                if (!authResult) {
                    actionTarget.setText("Login attempt failed");
                } else {
                    user = ATM.userManager.getUser(username);
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

    @Override
    public void init() throws Exception {
        super.init();
//        System.err.println("Inside init() method! Perform necessary initializations here");
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
