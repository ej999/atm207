package phase1;

import java.util.Scanner;

/**
 * An ATM that allows customers and employees to interact with their login accounts.
 * It will display options on the screen and the user will select an option by typing the corresponding number
 * on the keyboard.
 */
class ATM {
    /**
     * Allow user to login by entering username and password.
     *
     * It will return the Login account if the login is valid; otherwise, it'll consistently asking user to
     * enter username and password.
     */
    private static Login loginPrompt() {
        System.out.println("Welcome to CSC207 Banking Service.");

        Scanner reader = new Scanner(System.in);

        Login loginUser = null;
        while (loginUser == null) {
            System.out.print("Please enter your username: ");
            String username = reader.next();
            System.out.print("Please enter your password: ");
            String password = reader.next();

            loginUser = LoginManager.verifyLogin(username, password);

        }
        System.out.println("\nLogin success. Hi " + loginUser.getLoginType() + " " + loginUser.getUsername() + "!");
        return loginUser;
    }

    static void main(String[] args) {
        // Instantiate an Employee account here for basic functions here.
        Login_Employee_BankManager jen = new Login_Employee_BankManager("jen", "1234");
        LoginManager.addLogin(jen);
        jen.createLogin("1", "1");
        jen.addAccount("Chequing", ((Login_Customer) LoginManager.getLogin("1")), 1234);
        jen.addAccount("Chequing", ((Login_Customer) LoginManager.getLogin("1")), 12345);
        jen.addAccount("LineOfCredit", ((Login_Customer) LoginManager.getLogin("1")), 4321);
        jen.addAccount("Saving", ((Login_Customer) LoginManager.getLogin("1")), 1000);
        jen.addAccount("CreditCard", ((Login_Customer) LoginManager.getLogin("1")), 420);
        jen.addAccount("LineOfCredit", ((Login_Customer) LoginManager.getLogin("1")), 0);

        //TODO Check works or not
        // Load the back up of Login account lists after restarting the ATM.
        LoginManagerBackup load_backup = new LoginManagerBackup();
        LoginManager.login_map = load_backup.returnFileBackup().login_map;

        // TODO program should be shut down every night.
        // The ATM should displays Login interface all the time, until it is being shut down.
        //noinspection InfiniteLoopStatement
        while (true) {
            // A login session.
            Login loginUser = loginPrompt();
            new Options(loginUser);
        }

    }
}