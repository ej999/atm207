package phase1;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Scanner;

public class ATM {
    /*
     * TODO The program will display options on the screen and the user will select an option by typing on the keyboard.
     *
     *TODO: Your program will allow users to interact with their accounts by:
     * - viewing their account balance(s)
     * - transfer money between accounts that they own
     * - withdraw money from an account (This will decrease their balance.)
     * - transfer money from their account to another user's account (This will also decrease their balance.)
     * - pay a bill by transferring money out to a non-user's account (This can be stored in an outgoing.txt
     * file that is outside of the program. It also decreases their balance.)
     * - deposit money into their account by entering a cheque or cash into the machine
     * (This will be simulated by individual lines in an input file called deposits.txt.
     * You can decide the format of the file. This will increase their balance.)
     * - requesting the creation of an account from the bank manager
     *
     * Each user can have more than one account.
     * But no account can be co-owned by multiple users.
     * The user will have to use a login and password.
     * Only a bank manager can create and set the initial password for a user.
     * But the user can change their password, later.


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
                System.out.println("\nOops! You have " + loginAttempt + " failed login attempts. Please double-check " +
                        "your username and password.");
            } else if (!logged & loginAttempt >= 5) {
                System.out.println("\nSorry, you have 5 failed attempts of signing in. Please visit any of our branches " +
                        "to have one of our helpful managers assist you.");
                System.out.println("===========================================================\n");

                // restart the login for next users\
//                login();
                return null;
            }
        }
        Login loginUser = LoginManager.getLogin(username);
        System.out.println("\nLogin success. Hi " + loginUser.getUsername() + "!");
        return loginUser;
    }

    public static void main(String[] args) {
        // instantiate an Employee account here for basic functions here.
        Login_Employee_BankManager jen = new Login_Employee_BankManager("jen", "1234");
        LoginManager.addLogin(jen);
        jen.createLogin("1", "1");

        // TODO program should be shut down every night.
        //noinspection InfiniteLoopStatement
        while (true) {
            new Options(login());
        }

    }
}