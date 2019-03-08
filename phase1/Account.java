package phase1;
import java.util.Date;
import java.io.BufferedReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.util.HashMap;

abstract class Account {
    /*
    * There are two main types of accounts: Debt and Asset.
    */
    double accountBalance;
    Login_Customer accountOwner;
    Date dateOfCreation;
    private static final String inputFilePath = "/deposits.txt"; // not sure if this is the correct path
    static final String outputFilePath = "/outgoing.txt";
    HashMap<String, String> recentTransaction = new HashMap<String, String>() {
        {
            put("Type", "");
            put("Amount", "");
            put("Account", "");
        }
    };
//    String mostRecentTransaction; // not sure if needed. e.g. "Withdraw: $20"

    /*
    What if we had a Transaction class that has all the undo methods?
    Plus it can have other methods share by Line of Credit and Asset accounts e.g. payBill, transfers
    These methods could be static...
     */

    void deposit(double depositAmount) {
        if (depositAmount > 0) {
            accountBalance += depositAmount;
            recentTransaction.put("Type", "Deposit");
            recentTransaction.put("Amount", Double.toString(depositAmount));
            recentTransaction.put("Account", "");
        }
    }

    void undoDeposit(double depositAmount) {
        accountBalance -= depositAmount;
    }

    /*
    The above deposit method is more like a helper method.
    The real deposit method reads individual lines from an input file called <deposits.txt>
     */

    /**
     * Deposit money into their account by entering a cheque or cash into the machine
     * @throws IOException
     */
    void depositMoney() throws IOException {
        Path path = Paths.get(inputFilePath);
        try (BufferedReader fileInput = Files.newBufferedReader(path)) {
            String line = fileInput.readLine();
            while (line != null) { // Reading from a file will produce null at the end.
                double amountToDeposit = Double.valueOf(line.substring(1));
                deposit(amountToDeposit);
                line = fileInput.readLine();
            }
        }
    }


    abstract double withdraw(double withdrawalAmount);
    void undoWithdrawal(double withdrawalAmount) {
        accountBalance += withdrawalAmount;
    }
    abstract String viewBalance();

    /**
     * A string representation of this account.
     * @return nice string rep.
     */
    @Override
    public String toString() {
        /*
        TODO: Include things like: most recent transaction, date of creation, account balance, username
         */
        return "";
    }

//    abstract void undoMostRecentTransaction(); // TODO: figure out how to work with most recent transactions
}
