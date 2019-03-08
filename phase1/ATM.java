package phase1;

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

    private static Login loggedInAccount;

    private static Login getLoggedIn() {
        return loggedInAccount;
    }

    private static void setLoggedIn(Login loggedIn) {
        ATM.loggedInAccount = loggedIn;
    }

    private static void login() {
        System.out.println("Welcome to 207 Banking Service.");

        Scanner reader = new Scanner(System.in);
        boolean logined = false;
        int loginAttempt = 0;

        String username = null;
        while (!logined) {
            System.out.print("Please enter your username: ");
            username = reader.next();
            System.out.print("Please enter your password: ");
            String password = reader.next();

            logined = LoginManager.verifyLogin(username, password);

            loginAttempt++;
            if (!logined & loginAttempt < 5) {
                System.out.println();
                System.out.println("Oops! Something's not right. Please double-check your username and password.");
            } else if (!logined & loginAttempt >= 5) {
                System.out.println();
                System.out.println("Sorry, you have 5 failed attempts of signing in. Please visit any of our branches " +
                        "to have one of our helpful managers assist you.");
                return;
            }
        }

        setLoggedIn(LoginManager.getLogin(username));
        System.out.println();
        System.out.println("Login success. Hi " + getLoggedIn().getUsername() + "!");

        reader.close();
    }

    private static void options() {
        System.out.println();
        System.out.println("How can we help you today?");

        boolean optionSelected = false;
        while (!optionSelected) {
            Login user = getLoggedIn();
            user.displayOptions();
            optionSelected = true;
            //TODO: add something to update <optionSelected>
        }




    }

    public static void main(String[] args) {
        Login_Employee_BankManager jen = new Login_Employee_BankManager("jen", "1234");
        LoginManager.addLogin(jen);
        jen.createLogin("steve", "1234");

        Login_Customer steve = (Login_Customer) LoginManager.getLogin("steve");
        jen.addAccount("Chequing", steve);


        login();
        options();
    }
}