package ATM;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * A customer with username, password, list of their accounts, primary chequing account, and net total.
 */
class Customer extends User implements Observer {

    private static final String type = Customer.class.getName();
    /*
     * We store account info by its unique IDs. Note that we cannot store Customer object here,
     * otherwise JSON will be nested recursively and infinitely. https://imgur.com/a/5mxY6Yf
     */
    List<String> accountIDs;
    private String primaryAccount;
    private Inventory goods = new Inventory();
    private double netTotal;

    private String dob;
    //TODO make age useful
    private int age;
    private int creditScore;
    // if credit score is bellow a threshold the costumer wont be able to use certain credit base function


    public Customer(String username, String password) {
        super(username, password);
        this.accountIDs = new ArrayList<>();
        this.creditScore = 100;
    }

    public Customer(String username, String password, LocalDate dob) {
        this(username, password);
        this.dob = dob.toString();
        this.age = (int) dob.until(LocalDate.now(), ChronoUnit.YEARS);
        this.creditScore = 100;
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

    @Override
    Scene createOptionsScreen(Stage window, Scene welcomeScreen) {
        CustomerOptionsGUI gui = new CustomerOptionsGUI(window, welcomeScreen, this);
        return gui.createOptionsScreen();
    }


    /**
     * It should observe today's date and get called when necessary.
     */
    @Override
    public void update(Observable o, Object arg) {
//        if ((boolean) arg) {
//            List<Account> accounts = ATM.accountManager.getListOfAccounts(getUsername());
//            for (Account a : accounts) {
//                if (a instanceof AccountDebt) {
//                    if (a.balance <= 0) {
//                        creditScore += 1;
//                    } else {
//                        creditScore -= 10;
//                    }
//                }
//            }
//        }
    }


    /**
     * By default, first chequing account added is the primary account.
     *
     * @param account to be added
     */
    void addAccount(Account account) {
        accountIDs.add(account.getId());
        // If a user has only one checking account, it will be the default destination for any deposits.
        if (primaryAccount == null && account instanceof Chequing) {
            primaryAccount = account.getId();
        }
    }

    public List<String> getAccountIDs() {
        return accountIDs;
    }

    boolean hasAccount(Account account) {
        for (String a : this.accountIDs) {
            if (ATM.accountManager.getAccount(a).equals(account)) {
                return true;
            }

        }
        return false;
    }

    boolean hasMoreThanOneChequing() {
        int i = 0;
        for (String a : this.accountIDs) {
            if (ATM.accountManager.getAccount(a) instanceof Chequing) {
                i++;
            }
        }
        return i > 1;
    }

    // TODO: 2019-03-30 add observer to setNetTotal
    // The total of their debt account balances subtracted from the total of their asset account balances.
    public void setNetTotal() {
        double sum = 0;
        for (String a : this.accountIDs) {
            Account acc = ATM.accountManager.getAccount(a);
            if (acc instanceof AccountDebt) {
                sum -= acc.getBalance();
            } else {
                sum += acc.getBalance();
            }
        }
        netTotal = sum;
    }

    public double getNetTotal() {
        return netTotal;
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
        String n = new Date() + ": Requesting to create " + accountType + " account from " + this.getUsername();
        requestHelp(n);
    }

    void requestJointAccount(String accountType, String username) throws IOException {
        String n = new Date() + ": Requesting to create " + accountType + " joint account from " + this.getUsername() +
                " and " + username;
        requestHelp(n);
    }

    void requestAccountToJoint(String accountType, String accountId, String username) throws IOException {
        String request = new Date() + ": Requesting to change " + accountType + " with ID " + accountId + " from " +
                this.getUsername() + " to a joint account with secondary owner " + username;
        requestHelp(request);
    }

    public String getPrimaryAccount() {
        return primaryAccount;
    }

    void setPrimaryAccount(Account primaryAccount) {
        if (primaryAccount instanceof Chequing) {
            this.primaryAccount = primaryAccount.getId();
            System.out.println("Account is successfully set to primary");
        } else {
            throw new IllegalArgumentException("Only chequing account can be set to primary");
        }
    }

    public boolean hasPrimary() {
        return primaryAccount != null;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }

    public Inventory getGoods() {
        return goods;
    }
}
