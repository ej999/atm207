package phase1;

import java.util.Scanner;

/**
 * An ATM that allows customers and employees to interact with their login accounts.
 * It will display options on the screen and the user will select an option by typing the corresponding number
 * on the keyboard.
 */
public class ATM {
    /*
     *TODO: Your program will allow users to interact with their accounts by:
     * Each user can have more than one account.
     * But no account can be co-owned by multiple users.
     * The user will have to use a login and password.
     * Only a bank manager can create and set the initial password for a user.
     * But the user can change their password, later.
     */

    /**
     * Allow user to login by entering username and password.
     * It will return the username is the login is valid; otherwise return null.
     */
    private static Login login() {
        System.out.println("Welcome to CSC207 Banking Service.");

        Scanner reader = new Scanner(System.in);
        boolean logged = false;
        int loginAttempt = 0;

        String username = null;
        while (!logged) {
            System.out.print("Please enter your username: ");
            username = reader.next();
            System.out.print("Please enter your password: ");
            String password = reader.next();

            logged = LoginManager.verifyLogin(username, password);

            loginAttempt++;
            if (!logged & loginAttempt < 5) {
                System.out.println("\nOops! You have " + loginAttempt + " failed login attempts. Please " +
                        "double-check your username and password.");
            } else if (!logged & loginAttempt >= 5) {
                System.out.println("\nSorry, you have 5 failed attempts of signing in. Please visit any of our " +
                        "branches to have one of our helpful managers assist you.");
                System.out.println("===========================================================\n");

                // restart the login for next users\
                return null;
            }
        }
        Login loginUser = LoginManager.getLogin(username);
        System.out.println("\nLogin success. Hi " + loginUser.getUsername() + "!");
        return loginUser;
    }

    public static void main(String[] args) {
        // Instantiate an Employee account here for basic functions here.
        Login_Employee_BankManager jen = new Login_Employee_BankManager("jen", "1234");
        LoginManager.addLogin(jen);
        jen.createLogin("1", "1");
        jen.addAccount("Chequing", ((Login_Customer)LoginManager.getLogin("1")),1234);
        jen.addAccount("LineOfCredit", ((Login_Customer)LoginManager.getLogin("1")),4321);
        jen.addAccount("Saving", ((Login_Customer)LoginManager.getLogin("1")),1000);
        jen.addAccount("CreditCard", ((Login_Customer)LoginManager.getLogin("1")),420);


        // Load the back up of Login account lists after restarting the ATM.
        LoginManagerBackup load_backup = new LoginManagerBackup();
        LoginManager.login_map = load_backup.returnFileBackup().login_map;

        // TODO program should be shut down every night.
        // The ATM should displays Login interface all the time, until it is being shut down.
        //noinspection InfiniteLoopStatement
        while (true) {
            new Options(login());
        }

    }
}