package ATM;

import java.util.ArrayList;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.Stack;

abstract class Account {
    static final String outputFilePath = "phase2/src/resources/outgoing.txt";
    private static final String inputFilePath = "phase2/src/resources/deposits.txt";
    final long dateOfCreation;
    private final String id;
    private final ArrayList<String> owners = new ArrayList<>();
    double balance;
    //    /**
//     * Possible types include: Withdrawal, Deposit, TransferBetweenAccounts, TransferToAnotherUser, PayBill
//     */
//    private final HashMap<String, Object> mostRecentTransaction = new HashMap<String, Object>() {
//        {
//            put("Type", "");
//            put("Amount", 0.00);
//            put("Account", null);
//        }
//    };
    Stack<Transaction> transactionHistory;

    Account(String id, double balance, Customer owner) {
        this.balance = balance;
        this.owners.add(owner.getUsername());
        this.dateOfCreation = new Date().getTime();
        this.transactionHistory = new Stack<Transaction>();
        this.id = id;
    }

    Account(String id, Customer owner) {
        this(id, 0, owner);
    }

    Account(String id, double balance, Customer owner1, Customer owner2) {
        this(id, balance, owner1);
        this.owners.add(owner2.getUsername());
    }

    Account(String id, Customer owner1, Customer owner2) {
        this(id, 0, owner1, owner2);
    }

    public Stack<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public String getId() {
        return id;
    }

    public abstract String getType();

    private Transaction getMostRecentTransaction() {
        try {
            return transactionHistory.peek();
        } catch (EmptyStackException e) {
            return null;
        }
    }

//    void updateMostRecentTransaction(String type, double amount, Account account) {
//        mostRecentTransaction.put("Type", type);
//        mostRecentTransaction.put("Amount", amount);
//        mostRecentTransaction.put("Account", account);
//    }

//    void undoMostRecentTransaction() {
//        if (mostRecentTransaction.get("Type").equals("Withdrawal")) {
//            undoWithdrawal((Double) mostRecentTransaction.get("Amount"));
//        } else if (mostRecentTransaction.get("Type") == "Deposit") {
//            undoDeposit((Double) mostRecentTransaction.get("Amount"));
//        }
//    }

    //TODO see if it's needed
    public Long getDateOfCreation() {
        return dateOfCreation;
    }

    /**
     * Undoes the n most recent transactions.
     *
     * @param n transactions to be undone
     * @return true iff n transactions have been successfully undone, otherwise return false
     */
    boolean undoTransactions(int n) {
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
            return true;
        }
        return false;
    }

    abstract void deposit(double depositAmount);

    abstract void undoDeposit(double depositAmount);

    /**
     * Deposit money into their account by reading individual lines from deposits.txt
     */
    /*
    In the final version, customer can deposit simply by entering the amount. No need to read from deposits.txt
     */
//    void depositMoney() throws IOException {
//        Path path = Paths.get(inputFilePath);
//        try (BufferedReader fileInput = Files.newBufferedReader(path)) {
//            String line = fileInput.readLine();
//            while (line != null) { // Reading from a file will produce null at the end.
//                double amountToDeposit = Double.valueOf(line.substring(1));
//                deposit(amountToDeposit);
//                line = fileInput.readLine();
//            }
//        }
//    }
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
     * TODO: newOwner should be the username
     *
     * @param newOwner account holder
     * @return true iff newOwner is distinct
     */
    public boolean addOwner(Customer newOwner) {
        if (!owners.contains(newOwner.getUsername())) {
            owners.add(newOwner.getUsername());
            return true;
        }
        return false;
    }

    public boolean isJoint() {
        return owners.size() > 1;
    }

    public boolean removeOwner(Customer owner) {
        // An account has to have at least one owner.
        if (isJoint() && owners.contains(owner.getUsername())) {
            owners.remove(owner.getUsername());
            return true;
        }
        return false;
    }


    @Override
    public String toString() {
        //TODO:truman make it compatible with firebase
//        String mostRecentTransactionString;
//        if (getMostRecentTransaction() == null) {
//            mostRecentTransactionString = "n/a";
//        } else if (getMostRecentTransaction().getType().equals("Withdrawal")) {
//            mostRecentTransactionString = "$" + getMostRecentTransaction().getAmount() + " withdrawn.";
//        } else if (getMostRecentTransaction().getType().equals("Deposit")) {
//            mostRecentTransactionString = "$" + getMostRecentTransaction().getAmount() + " deposited.";
//        } else if (getMostRecentTransaction().getType().equals("Transfer")) {
//            mostRecentTransactionString = "$" + getMostRecentTransaction().getAmount() + " transferred.";
//        } else if (getMostRecentTransaction().getType().equals("PayBill")) {
//            mostRecentTransactionString = "$" + getMostRecentTransaction().getAmount() + " bill payment.";
//        } else {
//            mostRecentTransactionString = "n/a";
//        }

//        return this.getClass().getName() + "\t\t\t" + new Date(dateOfCreation) + "\t" + balance + "\t\t" + mostRecentTransactionString;
        return this.getClass().getName() + "\t\t\t" + new Date(dateOfCreation) + "\t" + balance + "\t\t";
    }

}
