package phase2;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import javafx.scene.text.Text;
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
    private Button button;
    private Stage window;

    /**
     * Allow user to login by entering username and password.
     * <p>
     * It will return the SystemUser account if the login is valid; otherwise, it'll consistently asking user to
     * enter username and password.
     */
    private static SystemUser loginPrompt() {
        System.out.println("Welcome to CSC207 Banking Service.");

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
            now = new ATMTime().checkMonth(now);


            // A login session.
            SystemUser systemUser = loginPrompt();
            new Options(systemUser);
        }

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
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

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(25,25,25,25));

        // manage the spacing between rows and cols
        grid.setVgap(10);
        grid.setHgap(10);

        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn,1,4);

        final Text actionTarget = new Text();
        grid.add(actionTarget,1,6);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                actionTarget.setFill(Color.FIREBRICK);
                actionTarget.setText("Sign in button pressed");
            }
        });

        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0,0,2,1);

        Label userName = new Label("Username:");
        grid.add(userName,0,1);

        TextField userTextField = new TextField();
        grid.add(userTextField,1,1);

        Label pw = new Label("Password:");
        grid.add(pw,0,2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox,1,2);

//        grid.setGridLinesVisible(true);

        Scene scene = new Scene(grid, 300, 275);

        window.setScene(scene);
        window.setResizable(false);
        window.show();

//        button = new Button();
//        button.setText("Click me");
//        button.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                System.out.println("Login");
//            }
//        });
//
//        StackPane layout = new StackPane();
//        layout.setId("pane");
//        layout.getChildren().add(button);
//
//        Scene scene = new Scene(layout, 300, 250);
//
//        // Set background using css
//        scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
//
//
//        primaryStage.setScene(scene);
//        primaryStage.setResizable(false);
//        primaryStage.show();
    }
}