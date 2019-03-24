package ATM;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

abstract class Account implements Serializable {
    static final String outputFilePath = "phase1/outgoing.txt";
    private static final String inputFilePath = "phase1/deposits.txt";
    final Date dateOfCreation;

    /**
     * Possible types include: Withdrawal, Deposit, TransferBetweenAccounts, TransferToAnotherUser, PayBill
     */
    private final HashMap<String, Object> mostRecentTransaction = new HashMap<String, Object>() {
        {
            put("Type", "");
            put("Amount", 0.00);
            put("Account", null);
        }
    };
    // An array of account holders
    private final ArrayList<SystemUser_Customer> owners = new ArrayList<>();
    double balance;

    Account(double balance, SystemUser_Customer owner) {
        this.balance = balance;
        this.owners.add(owner);
        this.dateOfCreation = new Date();
    }

    Account(SystemUser_Customer owner) {
        this(0, owner);
    }

    Account(double balance, SystemUser_Customer owner1, SystemUser_Customer owner2) {
        this(balance, owner1);
        this.owners.add(owner2);
    }

    Account(SystemUser_Customer owner1, SystemUser_Customer owner2) {
        this(0, owner1, owner2);
    }

    HashMap<String, Object> getMostRecentTransaction() {
        return mostRecentTransaction;
    }

    void updateMostRecentTransaction(String type, double amount, Account account) {
        mostRecentTransaction.put("Type", type);
        mostRecentTransaction.put("Amount", amount);
        mostRecentTransaction.put("Account", account);
    }

    void undoMostRecentTransaction() {
        if (mostRecentTransaction.get("Type").equals("Withdrawal")) {
            undoWithdrawal((Double) mostRecentTransaction.get("Amount"));
        } else if (mostRecentTransaction.get("Type") == "Deposit") {
            undoDeposit((Double) mostRecentTransaction.get("Amount"));
        }
    }

    abstract void deposit(double depositAmount);

    abstract void undoDeposit(double depositAmount);

    /**
     * Deposit money into their account by reading individual lines from deposits.txt
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

    abstract void withdraw(double withdrawalAmount);

    abstract void undoWithdrawal(double withdrawalAmount);

    double getBalance() {
        return balance;
    }

    @Override
    public abstract String toString();

    SystemUser_Customer getOwner() {
        // Assuming primary account holder
        return owners.get(0);
    }

    /**
     * Add another owner to this account.
     * @param newOwner account holder
     * @return true iff newOwner is distinct
     */
    public boolean addOwner(SystemUser_Customer newOwner) {
        if (!owners.contains(newOwner)) {
            owners.add(newOwner);
            return true;
        }
        return false;
    }

    public boolean isJoint() {
        return owners.size() > 1;
    }

    public boolean removeOwner(SystemUser_Customer owner) {
        if (isJoint() && owners.contains(owner)) {
            owners.remove(owner);
            return true;
        }
        return false;
    }

}
