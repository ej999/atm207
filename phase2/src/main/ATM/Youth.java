package ATM;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.EmptyStackException;
import java.util.Observable;

/**
 * Youth account is qualified to people under 20 years old.
 */
//TODO refactor student to youth
//TODO: wouldn't it make more sense to make this class extend AccountAsset?
class Youth extends Account implements AccountTransferable {
    private static final String type = Youth.class.getName();
    int transactions;
    int maxTransactions;
    int transferLimit;
    int transferTotal;

    // Transactions, student account has maximum 20 transfers that they can have
    // TODO: Interest, age
    // TODO: When user reaches 20, change to chequing
    // Default 20 transactions, 250 transferTotal
    public Youth(String id, double balance, Customer owner) {
        super(id, balance, owner);
        this.transactions = 20;
        this.transferTotal = 250;
    }

    public String getType() {
        return type;
    }

//    //TODO: check age of owners
//    //TODO combine owner1 and owner2 to List of owners.
//    Youth(double balance, ArrayList<Customer> owners) {
////        super(balance, owner1, owner2);
//    }

    public void update(Observable o, Object arg) {
        if ((boolean) arg) {
            transferLimit = 0;
            transactions = 0;
        }
    }


    private boolean validWithdrawal(double withdrawalAmount) {
        return withdrawalAmount > 0 && withdrawalAmount % 5 == 0 && balance > 0 &&
                new Cash().isThereEnoughBills(withdrawalAmount) && (transactions < maxTransactions);
    }

    /**
     * Withdraw money from an account (This will decrease <balance>)
     *
     * @param withdrawalAmount amount to be withdrawn
     * @param condition        additional condition in order to successfully withdraw
     */
    void withdraw(double withdrawalAmount, boolean condition) {
        if (validWithdrawal(withdrawalAmount) && (condition)) {
            balance -= withdrawalAmount;
            new Cash().cashWithdrawal(withdrawalAmount);
            transactions += 1;
            transactionHistory.push(new Transaction("Withdraw", withdrawalAmount, null, this.getClass().getName()));
        }
    }

    public boolean payBill(double amount, String accountName) throws IOException {
        if (amount > 0) {
            String message = "\nUser " + this.getPrimaryOwner() + " paid $" + amount + " to " + accountName + " on " +
                    LocalDateTime.now();
            // Open the file for writing and write to it.
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFilePath, true)))) {
                out.println(message);
                System.out.println("File has been written");
            }
            balance += amount;
            transactionHistory.push(new Transaction("PayBill", amount, null, this.getClass().getName()));
            return true;
        }
        return false;
    }

    public void undoPaybill(double amount) {
        balance += amount;
    }

    void withdraw(double withdrawalAmount) {
        withdraw(withdrawalAmount, true);
    }

    /**
     * Sets the monthly Transactions that a Student Account has.
     *
     * @param transactionsAmount the set amount of transactions
     */
    public void setMaxTransactions(int transactionsAmount) {
        maxTransactions = transactionsAmount;
    }

    /**
     * Sets the monthly amount that a Student Account has for allowance of transfers.
     *
     * @param transferLimitAmount the set amount of transfers
     */
    public void setTransferLimit(int transferLimitAmount) {
        transferLimit = transferLimitAmount;
    }


    @Override
    void undoWithdrawal(double withdrawalAmount) {
        balance += withdrawalAmount;
        transactions -= 1;
        new Cash().cashWithdrawal(-withdrawalAmount);
    }

    @Override
    void deposit(double depositAmount) {
        if ((depositAmount > 0) && (transactions < maxTransactions)) {
            balance += depositAmount;
            transactions += 1;
            transactionHistory.push(new Transaction("Deposit", depositAmount, null, this.getClass().getName()));
        } else {
            System.out.println("invalid deposit");
        }
    }

    @Override
    void undoDeposit(double depositAmount) {
        balance -= depositAmount;
        transactions -= 1;
    }

    /**
     * Transfer money between accounts the user owns
     *
     * @param transferAmount the amount to be transferred
     * @param account        another account the user owns
     * @return true if transfer was successful
     */
    public boolean transferBetweenAccounts(double transferAmount, Account account) {
        transactions += 1;
        transferTotal += transferAmount;
        return transferToAnotherUser(transferAmount, (Customer) ATM.userManager.getUser(getPrimaryOwner()), account);

    }

    /**
     * Transfer money from this account to another user's account (this will decrease their balance)
     *
     * @param transferAmount amount to transfer
     * @param user           receives transferAmount
     * @param account        of user
     * @return true iff transfer is valid
     */
    public boolean transferToAnotherUser(double transferAmount, Customer user, Account account) {
        if (validTransfer(transferAmount, user, account)) {
            balance -= transferAmount;
            if (account instanceof AccountAsset) {
                account.balance += transferAmount;
            } else {
                account.balance -= transferAmount;
            }
            transactionHistory.push(new Transaction("Transfer", transferAmount, account, this.getClass().getName()));
            transactions += 1;
            return true;
        }
        return false;
    }

    private void undoTransfer(double transferAmount, Account account) {
        balance += transferAmount;
        transactions -= 1;
        transferTotal -= transferAmount;
        if (account instanceof AccountAsset) {
            account.balance -= transferAmount;
        } else {
            account.balance += transferAmount;
        }

    }

    private boolean validTransfer(double transferAmount, Customer user, Account account) {
        return transferAmount > 0 && (balance - transferAmount) >= 0 && user.hasAccount(account) &&
                (transactions < maxTransactions) && (transferAmount + transferTotal < transferLimit);
    }

//    @Override
//    void undoMostRecentTransaction() {
//        super.undoMostRecentTransaction();
//        transactions += 1;
//        if (getMostRecentTransaction().get("Type").equals("TransferBetweenAccounts") ||
//                getMostRecentTransaction().get("Type").equals("TransferToAnotherUser")) {
//            undoTransfer((Double) getMostRecentTransaction().get("Amount"), (Account) getMostRecentTransaction().get("Account"));
//        }
//    }

//    public String toString() {
//        String mostRecentTransactionString;
//
//        if (getMostRecentTransaction().get("Type") == "Withdrawal") {
//            mostRecentTransactionString = "$" + getMostRecentTransaction().get("Amount") + " withdrawn";
//        } else if (getMostRecentTransaction().get("Type") == "Deposit") {
//            mostRecentTransactionString = "$" + getMostRecentTransaction().get("Amount") + " deposited";
//        } else {
//            mostRecentTransactionString = "n/a";
//        }
//
//        return "Student\t\t\t" + new Date(dateOfCreation) + "\t" + balance + "\t\t" + mostRecentTransactionString;
//    }

    @Override
    boolean undoTransactions(int n) {
        if (n > 0) {
            for (int i = 0; i < n; i++) {
                try {
                    Transaction transaction = transactionHistory.pop();
                    String type = transaction.getType();

                    if (transaction.getType().equals("Withdrawal")) {
                        undoWithdrawal(transaction.getAmount());
                    } else if (transaction.getType().equals("Deposit")) {
                        undoDeposit(transaction.getAmount());
                    } else if (transaction.getType().equals("Transfer")) {
                        undoTransfer(transaction.getAmount(), transaction.getAccount());
                    } else if (type.equals("PayBill")) {
                        undoPaybill(transaction.getAmount());
                    }

                } catch (EmptyStackException e) {
                    System.out.println("All transactions on this account have been undone");
                }
            }
            return true;
        }
        return false;
    }
}
