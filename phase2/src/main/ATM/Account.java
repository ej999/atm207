package ATM;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

abstract class Account {
    static final String outputFilePath = "phase2/src/resources/outgoing.txt";
    private static final String inputFilePath = "phase2/src/resources/deposits.txt";

    // Store as timestamp instead of Date object.
    final long dateOfCreation;

    // Since username is unique, we use username here instead of User_Customer object.
    private final ArrayList<String> owners = new ArrayList<>();
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
    /*
    The most recent transaction is at the beginning. Behaves like a stack.
     */
//    ArrayList<Transaction> transactionHistory;
    Stack<Transaction> transactionHistory;
    double balance;

    Account(double balance, User_Customer owner) {
        this.balance = balance;
        this.owners.add(owner.getUsername());
        this.dateOfCreation = new Date().getTime();
        this.transactionHistory = new Stack<Transaction>();
    }

    Account(User_Customer owner) {
        this(0, owner);
    }

    Account(double balance, User_Customer owner1, User_Customer owner2) {
        this(balance, owner1);
        this.owners.add(owner2.getUsername());
    }

    Account(User_Customer owner1, User_Customer owner2) {
        this(0, owner1, owner2);
    }

    public abstract String getType();

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

    //TODO truman: doesn't work for Gson.fromJson()
    public Long getDateOfCreation() {
        return dateOfCreation;
    }

    /**
     * Undoes the n most recent transactions.
     *
     * @param n transactions to be undone
     */
    void undoTransactions(int n) {
        if (n > 0) {

            for (int i = 0; i < n; i++) {
                try {
                    Transaction transaction = transactionHistory.pop();

                    if (transaction.getType().equals("Withdrawal")) {
                        undoWithdrawal(transaction.getAmount());
                    } else if (transaction.getType().equals("Deposit")) {
                        undoDeposit(transaction.getAmount());
                    }
                } catch (EmptyStackException e) {
                    System.out.println("All transactions on this account have been undone");
                }
            }
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

    public double getBalance() {
        return balance;
    }

//    @Override
//    public abstract String toString();

    String getPrimaryOwner() {
        // Assuming primary account holder
        return owners.get(0);
    }

    public ArrayList<String> getOwners() {
        return owners;
    }

    /**
     * Add another owner to this account.
     *
     * @param newOwner account holder
     * @return true iff newOwner is distinct
     */
    public boolean addOwner(User_Customer newOwner) {
        if (!owners.contains(newOwner)) {
            owners.add(newOwner.getUsername());
            return true;
        }
        return false;
    }

    public boolean isJoint() {
        return owners.size() > 1;
    }

    public boolean removeOwner(User_Customer owner) {
        if (isJoint() && owners.contains(owner)) {
            owners.remove(owner);
            return true;
        }
        return false;
    }

}
