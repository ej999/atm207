package phase1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Asset accounts include Chequing and Savings Accounts.
 */
abstract class Account_Asset extends Account {

    /**
     * Withdraw money from an account (This will decrease <accountBalance>)
     * TODO: notify the Cash class about this withdrawal
     * @param withdrawalAmount amount to be withdrawn
     * @return withdrawalAmount, otherwise 0.
     */
    @Override
    abstract double withdraw(double withdrawalAmount);

    boolean validWithdrawal(double withdrawalAmount) {
        return withdrawalAmount > 0 && withdrawalAmount % 5 == 0 && accountBalance > 0;
    }

    /**
     * Pay a bill by transferring money to a non-user's account
     * @param amount transfer amount
     * @param accountName non-user's account name
     * @throws IOException
     * @return true if bill has been payed successfully
     */
    boolean payBill(double amount, String accountName) throws IOException {
        if (amount > 0 && (accountBalance-amount) >= 0) {
            String message = "User " + accountOwner.getUsername() + " paid " + amount + " to " + accountName;
            // TODO: add date and time to message
            // Open the file for writing and write to it.
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFilePath)))) {
                out.println(message);
                System.out.println("File has been written.");
            }
            accountBalance -= amount;
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
        return transferToAnotherUser(transferAmount, accountOwner, account);
    }

    boolean transferToAnotherUser(double transferAmount, Login_Customer user, Account account) {
        if (validTransfer(transferAmount, user, account)) {
            accountBalance -= transferAmount;
            account.accountBalance += transferAmount;
            return true;
        }
        return false;
    }

    void undoTransferToAnotherUser(double transferAmount, Account account) {
        accountBalance += transferAmount;
        account.accountBalance -= transferAmount;
    }

    private boolean validTransfer(double transferAmount, Login_Customer user, Account account) {
        return transferAmount > 0 && (accountBalance - transferAmount) >= 0 && user.hasAccount(account);
    }
}
