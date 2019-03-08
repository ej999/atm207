package phase1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Asset accounts include Chequing and Savings Accounts.
 */
abstract class Account_Asset extends Account {
    boolean validWithdrawal(double withdrawalAmount) {
        return withdrawalAmount > 0 && withdrawalAmount % 5 == 0 && balance > 0;
    }

    public Account_Asset(double balance, Login_Customer owner) {
        super(balance, owner);
    }

    public Account_Asset(Login_Customer owner) {
        super(owner);
    }

    /**
     * Pay a bill by transferring money to a non-user's account
     * @param amount transfer amount
     * @param accountName non-user's account name
     * @throws IOException
     * @return true if bill has been payed successfully
     */
    boolean payBill(double amount, String accountName) throws IOException {
        if (amount > 0 && (balance -amount) >= 0) {
            String message = "User " + this.getOwner() + " paid " + amount + " to " + accountName;
            // TODO: add date and time to message
            // Open the file for writing and write to it.
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFilePath)))) {
                out.println(message);
                System.out.println("File has been written.");
            }
            balance -= amount;
            recentTransaction.put("Type", "PayBill");
            recentTransaction.put("Amount", amount);
            recentTransaction.put("Account", accountName);
            return true;
        }
        return false;
    }

    /**
     * Transfer money between accounts the user owns
     * @param transferAmount the amount to be transferred
     * @param account another account the user owns
     * @return true if transfer was successful
     */
    boolean transferBetweenAccounts(double transferAmount, Account account) {
        return transferToAnotherUser(transferAmount, getOwner(), account);
    }

    boolean transferToAnotherUser(double transferAmount, Login_Customer user, Account account) {
        if (validTransfer(transferAmount, user, account)) {
            balance -= transferAmount;
            account.balance += transferAmount;
            return true;
            recentTransaction.put("Type", "TransferToAnotherUser");
            recentTransaction.put("Amount", transferAmount);
            recentTransaction.put("Account", account);
        }
        return false;
    }

    void undoTransferToAnotherUser(double transferAmount, Account account) {
        balance += transferAmount;
        account.balance -= transferAmount;
    }

    private boolean validTransfer(double transferAmount, Login_Customer user, Account account) {
        return transferAmount > 0 && (balance - transferAmount) >= 0 && user.hasAccount(account);
    }
}
