package ATM;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

/**
 * A customer with username, password, list of their accounts, primary chequing account, and net total.
 */
class User_Customer extends User {

    private static final String type = User_Customer.class.getName();
    private final ArrayList<Account> accounts;
    private Account primary;
    private Inventory goods = new Inventory();

    private String dob;
    //TODO make age ueful
    private int age;
    //TODO: make creitscore work
    private int creditScore;
    // credit Score should have a default value
    // each month if the customer payed everything on time it should be increased by a little amount
    // if anything is unpayed pass a certain deadline(30 days) the score should decrease drastically
    // if credit score is bellow a threshold the costumer wont be able to use certain credit base function


    public User_Customer(String username, String password) {
        super(username, password);
        this.accounts = new ArrayList<>();
    }

    public User_Customer(String username, String password, LocalDate dob) {
        this(username, password);
        this.dob = dob.toString();
        this.age = (int) dob.until(LocalDate.now(), ChronoUnit.YEARS);
    }

    public String getDob() {
        return dob;
    }

    void setDob(String dob) {
        this.dob = dob;
    }

    int getAge() {
        return (int) LocalDate.parse(dob).until(LocalDate.now(), ChronoUnit.YEARS);
    }

    public String getType() {
        return type;
    }

    /**
     * By default, first chequing account added is the primary account.
     *
     * @param account to be added
     */
    void addAccount(Account account) {
        this.accounts.add(account);
        // If a user has only one checking account, it will be the default destination for any deposits.
        if (primary == null && account instanceof Account_Asset_Chequing) {
            primary = account;
        }
    }

    ArrayList<Account> getAccounts() {
        return accounts;
    }

    boolean hasAccount(Account account) {
        for (Account a : this.accounts) {
            if (a.equals(account)) {
                return true;
            }

        }
        return false;
    }

    boolean hasMoreThanOneChequing() {
        int i = 0;
        for (Account a : this.accounts) {
            if (a instanceof Account_Asset_Chequing) {
                i++;
            }
        }
        return i > 1;
    }

    // The total of their debt account balances subtracted from the total of their asset account balances.
    private double netTotal() {
        double sum = 0;
        for (Account a : this.accounts) {
            sum += a.getBalance();
        }
        return sum;
    }

    // add a line of request in the alerts.text
    private void requestHelp(String s) throws IOException {
        String content = s + "\n";
        File file = new File("phase2/src/resources/alerts.txt");
        FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(content);
        bw.close(); // Be sure to close BufferedWriter
    }

    // add a line of string in alert that request for new account
    void requestAccount(String accountType) throws IOException {
        String n = new Date() + ": Requesting to create " + accountType + " account from " + this.getUsername() + ".";
        requestHelp(n);
    }

    //TODO truman: toString interferes with serialization in Gson.fromJson
//    @Override
//    public String toString() {
//        StringBuilder returnMessage = new StringBuilder();
//        returnMessage.append("\n\u001B[1mPrimary\t\tAccount Type\t\tCreation Date\t\t\t\t\tBalance\t\tMost Recent Transaction" +
//                "\u001B[0m");
//        for (Account account : getAccounts()) {
//            if (account == primary) {
//                returnMessage.append("\nX\t\t\t").append(account);
//            } else {
//                returnMessage.append("\n\t\t\t").append(account);
//            }
//
//        }
//
//        returnMessage.append("\n\n\u001B[1mYour net total is \u001B[0m$").append(netTotal());
//
//        return returnMessage.toString();
//    }

    @Override
    public String toString() {
        return "Customer with username \"" + getUsername() + "\" and password \"" + getPassword() + "\"";
    }

    Account getPrimary() {
        return primary;
    }

    //TODO based on DOB
//    int getAge() {return age;}

    void setPrimary(Account primary) {
        if (primary instanceof Account_Asset_Chequing) {
            this.primary = primary;
            System.out.println("Account is successfully set to primary.");
        } else {
            throw new IllegalArgumentException("Only chequing account can be set to primary.");
        }
    }

    public int getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }
}
