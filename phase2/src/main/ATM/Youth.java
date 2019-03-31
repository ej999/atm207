package ATM;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Youth account is qualified to people under 20 years old.
 * A youth account has a limited number of transactions of 20 and a transfer limit of $250.
 * At the beginning of every month, reset their transactions and transferTotal.
 */
class Youth extends Account implements AccountTransferable, Observer {
    private static final String type = Youth.class.getName();
    private int transactions;
    private int maxTransactions;
    private int transferLimit;
    private int transferTotal;

    @SuppressWarnings({"unused", "WeakerAccess"})
    public Youth(String id, List<Customer> owners) {
        super(id, owners);
        this.maxTransactions = 20;
        this.transferLimit = 250;
    }

    @SuppressWarnings("unused")
    public Youth(String id, Customer owner) {
        this(id, Collections.singletonList(owner));
    }

    public String getType() {
        return type;
    }

    private boolean makeTransfer(double amount) {
        return transferTotal + amount <= transferLimit;
    }

    @Override
    public void update(Observable o, Object arg) {
        if ((boolean) arg) {
            transferTotal = 0;
            transactions = 0;
        }
    }

    private boolean validWithdrawal(double withdrawalAmount) {
        return withdrawalAmount > 0 && withdrawalAmount % 5 == 0 && getBalance() > 0 &&
                new Cash().isThereEnoughBills(withdrawalAmount) && (transactions < maxTransactions);
    }

    @Override
    void withdraw(double withdrawalAmount) {
        if (validWithdrawal(withdrawalAmount)) {
            setBalance(getBalance() - withdrawalAmount);
            new Cash().cashWithdrawal(withdrawalAmount);
            transactions += 1;
            getTransactionHistory().push(new Transaction("Withdraw", withdrawalAmount, null, type));
        }
    }

    @Override
    public boolean payBill(double amount, String accountName) throws IOException {
        if (amount > 0 && (transactions < maxTransactions)) {
            String message = "\nUser " + this.getPrimaryOwner() + " paid $" + amount + " to " + accountName + " on " +
                    LocalDateTime.now();
            // Open the file for writing and write to it.
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFilePath, true)))) {
                out.println(message);
                System.out.println("File has been written");
            }
            setBalance(getBalance() - amount);
            transactions += 1;
            getTransactionHistory().push(new Transaction("PayBill", amount, null, type));
            return true;
        }
        return false;
    }

    private void undoPayBill(double amount) {
        setBalance(getBalance() + amount);
        transactions -= 1;
    }

    /**
     * Sets the monthly Transactions that a Student Account has.
     *
     * @param transactionsAmount the set amount of transactions
     */
    void setMaxTransactions(int transactionsAmount) {
        maxTransactions = transactionsAmount;
    }

    /**
     * Sets the monthly amount that a Student Account has for allowance of transfers.
     *
     * @param transferLimitAmount the set amount of transfers
     */
    void setTransferLimit(int transferLimitAmount) {
        transferLimit = transferLimitAmount;
    }


    @Override
    void undoWithdrawal(double withdrawalAmount) {
        setBalance(getBalance() + withdrawalAmount);
        transactions -= 1;
        new Cash().cashWithdrawal(-withdrawalAmount);
    }

    @Override
    void deposit(double depositAmount) {
        if ((depositAmount > 0) && (transactions < maxTransactions)) {
            setBalance(getBalance() + depositAmount);
            transactions += 1;
            getTransactionHistory().push(new Transaction("Deposit", depositAmount, null, type));
        } else {
            System.out.println("invalid deposit");
        }
    }

    @Override
    void undoDeposit(double depositAmount) {
        setBalance(getBalance() - depositAmount);
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
        return transferToAnotherUser(transferAmount, getPrimaryOwner(), account);

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
            setBalance(getBalance() - transferAmount);
            if (account instanceof AccountAsset) {
                account.setBalance(account.getBalance() + transferAmount);
            } else {
                account.setBalance(account.getBalance() - transferAmount);
            }
            getTransactionHistory().push(new Transaction("Transfer", transferAmount, account, type));
            transactions += 1;
            transferTotal += transferAmount;
            return true;
        }
        return false;
    }

    private void undoTransfer(double transferAmount, Account account) {
        setBalance(getBalance() + transferAmount);
        transactions -= 1;
        transferTotal -= transferAmount;
        if (account instanceof AccountAsset) {
            account.setBalance(account.getBalance() - transferAmount);
        } else {
            account.setBalance(account.getBalance() + transferAmount);
        }

    }

    private boolean validTransfer(double transferAmount, Customer user, Account account) {
        return transferAmount > 0 && (getBalance() - transferAmount) >= 0 && user.hasAccount(account) &&
                (transactions < maxTransactions) && (transferAmount + transferTotal < transferLimit);
    }


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
    void undoTransactions(int n) {
        if (n > 0) {
            for (int i = 0; i < n; i++) {
                try {
                    Transaction transaction = getTransactionHistory().pop();
                    String type = transaction.getType();

                    if (transaction.getType().equals("Withdrawal")) {
                        undoWithdrawal(transaction.getAmount());
                    } else if (transaction.getType().equals("Deposit")) {
                        undoDeposit(transaction.getAmount());
                    } else if (transaction.getType().equals("Transfer")) {
                        undoTransfer(transaction.getAmount(), transaction.getAccount());
                    } else if (type.equals("PayBill")) {
                        undoPayBill(transaction.getAmount());
                    }

                } catch (EmptyStackException e) {
                    System.out.println("All transactions on this account have been undone");
                }
            }
        }
    }
}
