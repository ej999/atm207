package ATM;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static ATM.ATM.*;

/**
 * Youth account is qualified to people under 20 years old (age 19 or below).
 * A youth account has a limited number of transactions of 20 and a transfer limit of $250.
 * At the beginning of every month, reset their transactions and transferTotal.
 */
class Youth extends Account implements AccountTransferable {
    private int transactions;
    private int maxTransactions;
    private int transferLimit;
    private int transferTotal;

    @SuppressWarnings({"unused", "WeakerAccess"})
    public Youth(String id, List<String> ownersUsername) {
        super(id, ownersUsername);
        this.maxTransactions = 20;
        this.transferLimit = 250;
    }

    @SuppressWarnings("unused")
    public Youth(String id, String username) {
        this(id, Collections.singletonList(username));
    }

    @SuppressWarnings("unused")
    public int getTransactions() {
        return transactions;
    }

    @SuppressWarnings("unused")
    public int getMaxTransactions() {
        return maxTransactions;
    }

    /**
     * Sets the monthly Transactions that a Student Account has.
     *
     * @param transactionsAmount the set amount of transactions
     */
    void setMaxTransactions(int transactionsAmount) {
        maxTransactions = transactionsAmount;
    }

    @SuppressWarnings("unused")
    public int getTransferLimit() {
        return transferLimit;
    }

//    private boolean makeTransfer(double amount) {
//        return transferTotal + amount <= transferLimit;
//    }

    /**
     * Sets the monthly amount that a Student Account has for allowance of transfers.
     *
     * @param transferLimitAmount the set amount of transfers
     */
    void setTransferLimit(int transferLimitAmount) {
        transferLimit = transferLimitAmount;
    }

    @SuppressWarnings("unused")
    public int getTransferTotal() {
        return transferTotal;
    }

//    @Override
//    public void update(Observable o, Object arg) {
//        if ((boolean) arg) {
//            transferTotal = 0;
//            transactions = 0;
//        }
//    }

    public void newDay() {
        if (checkToday()) {
            transferTotal = 0;
            transactions = 0;
        }
    }

    private boolean checkToday() {
        LocalDate today = LocalDate.now();
        return today.getDayOfMonth() == 1;
    }

    public void timeSkipTo(int year, int month, int day) {
        LocalDate skipTo = LocalDate.of(year, month, day);
        if (skipTo.getDayOfMonth() == 1) {
            transferTotal = 0;
            transactions = 0;
        }
    }

    private boolean validWithdrawal(double withdrawalAmount) {
        return withdrawalAmount > 0 && withdrawalAmount % 5 == 0 && getBalance() > 0 &&
                banknoteManager.isThereEnoughBankNote(withdrawalAmount) && (transactions < maxTransactions);
    }

    @Override
    void withdraw(double withdrawalAmount) {
        if (validWithdrawal(withdrawalAmount)) {
            setBalance(getBalance() - withdrawalAmount);
            banknoteManager.banknoteWithdrawal(withdrawalAmount);
            transactions += 1;
            getTransactionHistory().push(new Transaction("Withdraw", withdrawalAmount, null, getType()));
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
            getTransactionHistory().push(new Transaction("PayBill", amount, null, getType()));
            return true;
        }
        return false;
    }

    private void undoPayBill(double amount) {
        setBalance(getBalance() + amount);
        transactions -= 1;
    }

    @Override
    void undoWithdrawal(double withdrawalAmount) {
        setBalance(getBalance() + withdrawalAmount);
        transactions -= 1;
        banknoteManager.banknoteWithdrawal(-withdrawalAmount);
    }

    @Override
    void deposit(double depositAmount) {
        if ((depositAmount > 0) && (transactions < maxTransactions)) {
            setBalance(getBalance() + depositAmount);
            transactions += 1;
            getTransactionHistory().push(new Transaction("Deposit", depositAmount, null, getType()));
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
     * @param username       receives transferAmount
     * @param account        of user
     * @return true iff transfer is valid
     */
    public boolean transferToAnotherUser(double transferAmount, String username, Account account) {
        if (validTransfer(transferAmount, username, account)) {
            setBalance(getBalance() - transferAmount);
            if (account instanceof AccountAsset || account instanceof Youth) {
                account.setBalance(account.getBalance() + transferAmount);
            } else {
                account.setBalance(account.getBalance() - transferAmount);
            }
            getTransactionHistory().push(new Transaction("Transfer", transferAmount, account.getId(), getType()));
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

    private boolean validTransfer(double transferAmount, String username, Account account) {
        Customer customer = (Customer) userManager.getUser(username);
        return transferAmount > 0 && (getBalance() - transferAmount) >= 0 && customer.hasAccount(account) &&
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
                    String type = transaction.getTransactionType();

                    if (transaction.getTransactionType().equals("Withdrawal")) {
                        undoWithdrawal(transaction.getAmount());
                    } else if (transaction.getTransactionType().equals("Deposit")) {
                        undoDeposit(transaction.getAmount());
                    } else if (transaction.getTransactionType().equals("Transfer")) {
                        undoTransfer(transaction.getAmount(), accountManager.getAccount(transaction.getAccountId()));
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
