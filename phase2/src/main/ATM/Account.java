package ATM;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static ATM.ATM.banknoteManager;
import static ATM.ATM.userManager;

abstract class Account implements Serializable {
    // payment and transfer to non-user is written in following path.
    static final String outputFilePath = "phase2/src/resources/outgoing.txt";
    private final String type = this.getClass().getName();
    private final String id;
    private final long dateCreated;
    private final List<String> ownersUsername;
    private final String primaryOwner;

    private Stack<Transaction> transactionHistory;

    private double balance;

    Account(String id, List<String> ownersUsername) {
        this.id = id;
        // We store the timestamp as a immutable long.
        this.dateCreated = new Date().getTime();
        this.ownersUsername = ownersUsername;
        // First customer in the list is set to be the primary owner of this account.
        this.primaryOwner = ownersUsername.get(0);
        this.transactionHistory = new Stack<Transaction>();
        this.balance = 0;
    }

    public Account(String id, String owner) {
        this(id, Collections.singletonList(owner));
    }


    void depositBill(Map<Integer, Integer> depositedBills) {
        int depositAmount = 0;
        for (Integer d : depositedBills.keySet()) {
            depositAmount += d * depositedBills.get(d);
        }

        if (depositAmount > 0) {
            balance += depositAmount;
            transactionHistory.push(new Transaction("Deposit", depositAmount, null, this.getClass().getName()));
            banknoteManager.banknoteDeposit(depositedBills);
        } else {
            System.out.println("invalid deposit");
        }

        for (String username : ownersUsername) {
            Customer customer = (Customer) userManager.getUser(username);
            customer.setNetTotal();
        }
    }

    @SuppressWarnings("WeakerAccess") // serialization property
    public Stack<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    void setTransactionHistory(Stack<Transaction> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    Transaction getMostRecentTransaction() {
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

    @SuppressWarnings({"unused"})
    public Long getDateCreated() {
        return dateCreated;
    }

    // Return dateCreated as String in a readable format.
    String getDateCreatedReadable() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(new Date(dateCreated));
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
                    if (transaction.getTransactionType().equals("Withdrawal")) {
                        undoWithdrawal(transaction.getAmount());
                    } else if (transaction.getTransactionType().equals("Deposit")) {
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

    abstract void withdraw(double withdrawalAmount);

    abstract void undoWithdrawal(double withdrawalAmount);

    public double getBalance() {
        return balance;
    }

    void setBalance(double balance) {
        this.balance = balance;
    }

    @SuppressWarnings("WeakerAccess")
    public String getPrimaryOwner() {
        // Assuming primary account owner.
        return primaryOwner;
    }

    @SuppressWarnings("WeakerAccess")
    public List<String> getOwnersUsername() {
        return ownersUsername;
    }

    /**
     * Add another owner to this account.
     *
     * @param newOwner account owner
     */
    void addOwner(String newOwner) {
        if (!ownersUsername.contains(newOwner)) {
            ownersUsername.add(newOwner);
        }
    }

    boolean isNotJoint() {
        return ownersUsername.size() <= 1;
    }

//    List<Customer> getOwners() {
//        ArrayList<Customer> owners = new ArrayList<>();
//        for (String username : ownersUsername) {
//            owners.add((Customer) userManager.getUser(username));
//        }
//        return owners;
//    }

//    public boolean removeOwner(Customer owner) {
//        // An account has to have at least one owner.
//        if (isNotJoint() && ownersUsername.contains(owner.getUsername())) {
//            ownersUsername.remove(owner.getUsername());
//            return true;
//        }
//        return false;
//    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(dateCreated);

        return this.getClass().getSimpleName() +
                " [id='" + id + '\'' +
                ", balance=" + balance +
                ", ownersUsername=" + ownersUsername +
                ", dateCreated=" + dateFormat.format(date) +
                ']';
    }
}
