package phase1;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

//import java.util.Account;
//Import ATM


class Login_Employee_BankManager extends Login_Employee {

    Login_Employee_BankManager(String username, String password){
        super(username, password, "BankManager");
    }

    // Assume ATM stores bills as HashMap
    public void addToBill(HashMap<Integer, Integer> bills, HashMap<Integer, Integer> ATM){
        for (Map.Entry<Integer, Integer> denom : bills.entrySet()) {
            ATM.replace(denom.getKey(), denom.getValue() + ATM.get(denom.getKey()));
        }
    }

    /** Only a bank manager can create and set the initial password for a user. */
    public void createLogin(String username, String password){
        Login_Customer newUser = new Login_Customer(username, password);
        LoginManager.addLogin(newUser);
    }

    /** Create an account for a Customer. */
    void addAccount(String accountType, Login_Customer username, double amount){
        switch (accountType) {
            case "Chequing": {
                Account_Asset_Chequing newAccount = new Account_Asset_Chequing(amount, username);
                username.addAccount(newAccount);
                break;
            }
            case "Saving": {
                Account_Asset_Saving newAccount = new Account_Asset_Saving(amount, username);
                username.addAccount(newAccount);
                break;
            }
            case "CreditCard": {
                Account_Debt_CreditCard newAccount = new Account_Debt_CreditCard(amount, username);
                username.addAccount(newAccount);
                break;
            }
            case "LineOfCredit": {
                Account_Debt_LineOfCredit newAccount = new Account_Debt_LineOfCredit(amount, username);
                username.addAccount(newAccount);
                break;
            }
            default:
                System.out.println("Invalid account type");
                break;
        }
    }

    void displayOptions(){
        System.out.println("1. Create a login for a user.");
        System.out.println("2. Create a bank account for a user.");
        System.out.println("3. Restock the ATM.");
        System.out.println("4. Under the most recent transaction on a user's account.");
    }

    void selectOptions(String option){
        Scanner reader = new Scanner(System.in);
        switch (option){
            case "1": {
                createLoginPrompt(reader);
                break;
            }
            case "2": {
                createAccountPrompt(reader);
                break;
                }
            case "3":{
                restockPrompt(reader);
                break;
            }

        }
    }
    private void createLoginPrompt(Scanner reader){
        System.out.println("Creating Login...");
        System.out.println("Enter username: ");
        String username = reader.next();
        System.out.println("Enter password: ");
        String password = reader.next();
        createLogin(username, password);
    }
    private void createAccountPrompt(Scanner reader){
        System.out.println("Enter username: ");
        String username = reader.next();
        if (LoginManager.checkLoginExistence(username)) {
            System.out.println("Enter account type : \n" +
                    "Chequing \n Saving \n Credit Card \n Line of Credit ");
        }
        String account = reader.next();
        addAccount(account, (Login_Customer) LoginManager.getLogin(username));
    }
    private void restockPrompt(Scanner reader){
        System.out.println("Enter amount of 5 dollar bills: ");
        int fives = reader.nextInt();
        System.out.println("Enter amount of 10 dollar bills: ");
        int tens = reader.nextInt();
        System.out.println("Enter amount of 20 dollar bills: ");
        int twenties = reader.nextInt();
        System.out.println("Enter amount of 50 dollar bills: ");
        int fifties = reader.nextInt();
        HashMap<Integer, Integer> restock = new HashMap<Integer, Integer>();
        restock.put(5, fives);
        restock.put(10, tens);
        restock.put(20, twenties);
        restock.put(50, fifties);
        //addToBill(restock, ); TODO: figure out how to access Cash: need to know where cash is stored
    }


    /** Create an account for a Customer. Amount is not initialized here. */
    void addAccount(String accountType, Login_Customer username){
        this.addAccount(accountType, username, 0);
    }

}
