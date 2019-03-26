package ATM;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A customer with username, password, list of their accounts, primary chequing account, and net total.
 */
class Customer extends User {

    private static final String type = Customer.class.getName();
    private final List<String> accounts;
    private Account primary;
    private Inventory goods = new Inventory();

    private String dob;
    //TODO make age useful
    private int age;
    //TODO: make creitscore work
    private int creditScore;
    // credit Score should have a default value
    // each month if the customer payed everything on time it should be increased by a little amount
    // if anything is unpayed pass a certain deadline(30 days) the score should decrease drastically
    // if credit score is bellow a threshold the costumer wont be able to use certain credit base function


    public Customer(String username, String password) {
        super(username, password);
        this.accounts = new ArrayList<>();
    }

    public Customer(String username, String password, LocalDate dob) {
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
        accounts.add(account.getId());
        // If a user has only one checking account, it will be the default destination for any deposits.
        if (primary == null && account instanceof Chequing) {
            primary = account;
        }
    }

    public List<String> getAccounts() {
        return accounts;
    }

    boolean hasAccount(Account account) {
        for (String a : this.accounts) {
            if (AccountManager.getAccount(a).equals(account)) {
                return true;
            }

        }
        return false;
    }

    boolean hasMoreThanOneChequing() {
        int i = 0;
        for (String a : this.accounts) {
            if (AccountManager.getAccount(a) instanceof Chequing) {
                i++;
            }
        }
        return i > 1;
    }

    // The total of their debt account balances subtracted from the total of their asset account balances.
    private double netTotal() {
        double sum = 0;
        for (String a : this.accounts) {
            sum += AccountManager.getAccount(a).getBalance();
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

    @Override
    public String toString() {
        StringBuilder returnMessage = new StringBuilder();
        returnMessage.append("\n\u001B[1mPrimary\t\tAccount Type\t\tCreation Date\t\t\t\t\tBalance\t\tMost Recent Transaction" +
                "\u001B[0m");
        for (String id : getAccounts()) {
            if (AccountManager.getAccount(id) == primary) {
                returnMessage.append("\nX\t\t\t").append(AccountManager.getAccount(id));
            } else {
                returnMessage.append("\n\t\t\t").append(AccountManager.getAccount(id));
            }

        }

        returnMessage.append("\n\n\u001B[1mYour net total is \u001B[0m$").append(netTotal());

        return returnMessage.toString();
    }

//    @Override
//    public String toString() {
//        return "Customer with username \"" + getUsername() + "\" and password \"" + getPassword() + "\"";
//    }

    Account getPrimary() {
        return primary;
    }

    void setPrimary(Account primary) {
        if (primary instanceof Chequing) {
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
