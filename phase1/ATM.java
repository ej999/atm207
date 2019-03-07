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
    public static void main(String[] args) {
        System.out.println("Welcome to 207 Banking Service.");

        Scanner reader = new Scanner(System.in);

        boolean logined = false;

        while (!logined) {
            System.out.println("Please enter your username: ");
            String username = reader.next();
            System.out.println("Please enter your password: ");
            String password = reader.next();

            logined = (LoginManager_Customer.checkUser(username, password))
                    ? LoginManager_Customer.checkUser(username, password)
                    : LoginManager_Employee.checkUser(username, password);
        }



        reader.close();

    }
}