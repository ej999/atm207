package ATM;

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
    List<String> accounts;
    private String primary;
    private Inventory goods = new Inventory();
    private double netTotal;

    private String dob;
    //TODO make age useful
    private int age;
    private int creditScore;
    // if credit score is bellow a threshold the costumer wont be able to use certain credit base function


    public Customer(String username, String password) {
        super(username, password);
        this.accounts = new ArrayList<>();
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


    /**
     * It should observe today's date and get called when necessary.
     */
    @Override
    public void update(Observable o, Object arg) {
        if ((boolean) arg) {
            List<Account> accounts = ATM.accountManager.getListOfAccounts(getUsername());
            for (Account a : accounts) {
                if (a instanceof AccountDebt) {
                    if (a.balance <= 0) {
                        creditScore += 1;
                    } else {
                        creditScore -= 10;
                    }
                }
            }
        }
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
            primary = account.getId();
        }
    }

    public List<String> getAccounts() {
        return accounts;
    }

    boolean hasAccount(Account account) {
        for (String a : this.accounts) {
            if (ATM.accountManager.getAccount(a).equals(account)) {
                return true;
            }

        }
        return false;
    }

    boolean hasMoreThanOneChequing() {
        int i = 0;
        for (String a : this.accounts) {
            if (ATM.accountManager.getAccount(a) instanceof Chequing) {
                i++;
            }
        }
        return i > 1;
    }

    // The total of their debt account balances subtracted from the total of their asset account balances.
    public double getNetTotal() {
        double sum = 0;
        for (String a : this.accounts) {
            Account acc = ATM.accountManager.getAccount(a);
            if (acc instanceof AccountDebt) {
                sum -= acc.getBalance();
            } else {
                sum += acc.getBalance();
            }
        }
        netTotal = sum;
        return netTotal;
    }

//    public double getNetTotal() {
//        return netTotal;
//    }

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
                this.getUsername() + " to a joint account with secondary holder " + username;
        requestHelp(request);
    }

    //TODO truman: causing issue when deserializing from Firebase.
//    @Override
//    public String toString() {
//        StringBuilder returnMessage = new StringBuilder();
//        returnMessage.append("\n\u001B[1mPrimary\t\tAccount Type\t\tCreation Date\t\t\t\t\tBalance\t\tMost Recent Transaction" +
//                "\u001B[0m");
//        for (String id : getAccounts()) {
//            if (ATM.accountManager.getAccount(id).equals(primary)) {
//                returnMessage.append("\nX\t\t\t").append(ATM.accountManager.getAccount(id));
//            } else {
//                returnMessage.append("\n\t\t\t").append(ATM.accountManager.getAccount(id));
//            }
//
//        }
//
//        returnMessage.append("\n\n\u001B[1mYour net total is \u001B[0m$").append(netTotal);
//
//        return returnMessage.toString();
//    }

    @Override
    public String toString() {
        return "Customer " + getUsername() + " has a net total of " + getNetTotal();
    }

    public String getPrimary() {
        return primary;
    }

    void setPrimary(Account primary) {
        if (primary instanceof Chequing) {
            this.primary = primary.getId();
            System.out.println("Account is successfully set to primary");
        } else {
            throw new IllegalArgumentException("Only chequing account can be set to primary");
        }
    }

    public boolean hasPrimary() {
        return primary != null;
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
