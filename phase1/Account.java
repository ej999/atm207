package phase1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;

abstract class Account implements Serializable {
    // TODO: verify these file paths actually work
    static final String outputFilePath = "/outgoing.txt"; // pay bills
    private static final String inputFilePath = "/deposits.txt";
    double balance;
    Login_Customer owner;
    Date dateOfCreation;
    HashMap<String, Object> mostRecentTransaction = new HashMap<String, Object>() {
        {
            put("Type", "");
            put("Amount", 0.00);
            put("Account", null);
        }
    };

    public Account(double balance, Login_Customer owner) {
        this.balance = balance;
        this.owner = owner;
        this.dateOfCreation = new Date();
    }

    public Account(Login_Customer owner) {
        this(0, owner);
    }

    void updateMostRecentTransaction(String type, double amount, Account account) {
        mostRecentTransaction.put("Type", type);
        mostRecentTransaction.put("Amount", amount);
        mostRecentTransaction.put("Account", account);
    }

    abstract void deposit(double depositAmount);
    abstract void undoDeposit(double depositAmount);

    /**
     * Deposit money into their account by entering a cheque or cash into the machine
     *
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
        balance += withdrawalAmount;
    }

    public double getBalance() {
        return balance;
    }

    void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * A string representation of this account.
     *
     * @return nice string rep.
     */
    @Override
    public String toString() {
        return "Username: " + owner.getUsername() + "\nBalance: " + balance + "\nDate Created: " + dateOfCreation +
                "\nMost Recent Transaction: " + mostRecentTransaction;
    }

    public Login_Customer getOwner() {
        return owner;
    }

    void undoMostRecentTransaction() {
        if (mostRecentTransaction.get("Type").equals("Withdrawal")) {
            undoWithdrawal((Double) mostRecentTransaction.get("Amount"));
        } else if (mostRecentTransaction.get("Type") == "Deposit") {
            undoDeposit((Double) mostRecentTransaction.get("Amount"));
        }
    }
}
