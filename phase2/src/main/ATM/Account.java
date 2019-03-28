package ATM;

import java.util.*;

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

    // TODO may need to make abstract and move it to another hierarchy
    void depositBill(Map<Integer, Integer> depositedBills) {
        int depositAmount = 0;
        for (Integer d : depositedBills.keySet()) {
            depositAmount += d * depositedBills.get(d);
        }

        if (depositAmount > 0) {
            balance += depositAmount;
            transactionHistory.push(new Transaction("Deposit", depositAmount, null));
            new Cash().cashDeposit(depositedBills);
        } else {
            System.out.println("invalid deposit");
        }
    }

    public Stack<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public String getId() {
        return id;
    }

    public abstract String getType();

    private Transaction getMostRecentTransaction() {
        Transaction mostRecentTransaction;
        try {
            mostRecentTransaction = transactionHistory.peek();
        } catch (EmptyStackException e) {
            return null;
        }
        return mostRecentTransaction;
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
    public boolean addOwner(String newOwner) {
        if (!owners.contains(newOwner)) {
            owners.add(newOwner);
            return true;
        }
        return false;
    }

    public boolean isJoint() {
        return owners.size() > 1;
    }

    public boolean removeOwner(String owner) {
        // An account has to have at least one owner.
        if (isJoint() && owners.contains(owner)) {
            owners.remove(owner);
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
