package phase1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * Asset accounts include Chequing and Savings Accounts.
 */
abstract class Account_Asset extends Account {
    public Account_Asset(double balance, Login_Customer owner) {
        super(balance, owner);
    }

    public Account_Asset(Login_Customer owner) {
        super(owner);
    }

    /**
     * Pay a bill by transferring money to a non-user's account
     *
     * @param amount      transfer amount
     * @param accountName non-user's account name
     * @return true if bill has been payed successfully
     * @throws IOException
     */
    boolean payBill(double amount, String accountName) throws IOException {
        if (amount > 0 && (balance - amount) >= 0) {
            String message = "User " + this.getOwner() + " paid " + amount + " to " + accountName + " on " +
                    LocalDateTime.now();
            // Open the file for writing and write to it.
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFilePath)))) {
                out.println(message);
                System.out.println("File has been written.");
            }
            balance -= amount;
            updateMostRecentTransaction("PayBill",amount,null);
            return true;
        }
        return false;
    }

    private boolean validWithdrawal(double withdrawalAmount) {
        return withdrawalAmount > 0 && withdrawalAmount % 5 == 0 && balance > 0;
    }

    /**
     * Withdraw money from an account (This will decrease <balance>)
     * TODO: notify the Cash class about this withdrawal
     *
     * @param withdrawalAmount amount to be withdrawn
     * @param condition additional condition in order to successfully withdraw
     * @return withdrawalAmount, otherwise 0.
     */
    double withdraw(double withdrawalAmount, boolean condition) {
        if (validWithdrawal(withdrawalAmount) && (condition)) {
            balance -= withdrawalAmount;
            updateMostRecentTransaction("Withdrawal", withdrawalAmount,null);
            return withdrawalAmount;
        }
        return 0;
    }

    void deposit(double depositAmount) {
        if (depositAmount > 0) {
            balance += depositAmount;
            updateMostRecentTransaction("Deposit", depositAmount, null);
            System.out.println("valid deposit");
        } else {
            System.out.println("invalid deposit");
        }
    }

    void undoDeposit(double depositAmount) {
        balance -= depositAmount;
    }

    /**
     * Transfer money between accounts the user owns
     *
     * @param transferAmount the amount to be transferred
     * @param account        another account the user owns
     * @return true if transfer was successful
     */
    boolean transferBetweenAccounts(double transferAmount, Account account) {
        return transferToAnotherUser(transferAmount, getOwner(), account);
    }

    boolean transferToAnotherUser(double transferAmount, Login_Customer user, Account account) {
        if (validTransfer(transferAmount, user, account)) {
            balance -= transferAmount;
            if (account instanceof Account_Asset) {
                account.balance += transferAmount;
            } else {
                account.balance -= transferAmount;
            }
            if (user == getOwner()) {
                updateMostRecentTransaction("TransferBetweenAccounts", transferAmount, account);
            } else {
                updateMostRecentTransaction("TransferToAnotherUser", transferAmount, account);
            }
            return true;
        }
        return false;
    }

    void undoTransfer(double transferAmount, Account account) {
        balance += transferAmount;
        if (account instanceof Account_Asset) {
            account.balance -= transferAmount;
        } else {
            account.balance += transferAmount;
        }

    }

    private boolean validTransfer(double transferAmount, Login_Customer user, Account account) {
        return transferAmount > 0 && (balance - transferAmount) >= 0 && user.hasAccount(account);
    }

    @Override
    void undoMostRecentTransaction() {
        super.undoMostRecentTransaction();
        if (mostRecentTransaction.get("Type").equals("TransferBetweenAccounts") ||
                mostRecentTransaction.get("Type").equals("TransferToAnotherUser")) {
            undoTransfer((Double) mostRecentTransaction.get("Amount"), (Account) mostRecentTransaction.get("Account"));
        }
    }
}
