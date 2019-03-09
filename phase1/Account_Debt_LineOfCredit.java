package phase1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

class Account_Debt_LineOfCredit extends Account_Debt {

    /**
     * Balance is set to 0.00 as default if an initial balance is not provided.
     */
    public Account_Debt_LineOfCredit(Login_Customer owner) {
        super(owner);
    }

    Account_Debt_LineOfCredit(double balance, Login_Customer owner) {
        super(balance, owner);
    }

    /*
    Duplicate Code here. Is there a way around it?
     */
    /**
     * Pay a bill by transferring money to a non-user's account
     *
     * @param amount      transfer amount
     * @param accountName non-user's account name
     * @return true if bill has been payed successfully
     * @throws IOException
     */
    boolean payBill(double amount, String accountName) throws IOException {
        if (amount > 0) {
            String message = "User " + this.getOwner() + " paid " + amount + " to " + accountName + " on " +
                    LocalDateTime.now();
            // Open the file for writing and write to it.
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFilePath)))) {
                out.println(message);
                System.out.println("File has been written.");
            }
            balance += amount;
            updateMostRecentTransaction("PayBill",amount,null);
            return true;
        }
        return false;
    }

    /**
     * Transfer money between accounts the user owns. This increases the balance.
     *
     * @param transferAmount the amount to be transferred
     * @param account        another account the user owns
     * @return true if transfer was successful
     */
    boolean transferBetweenAccounts(double transferAmount, Account account) {
        return transferToAnotherUser(transferAmount, getOwner(), account);
    }

    /**
     * Transfer money to another user's account. This also increases the balance.
     * @param transferAmount transfer amount
     * @param user receiver of amount
     * @param account user account
     * @return true iff transfer was a success
     */
    boolean transferToAnotherUser(double transferAmount, Login_Customer user, Account account) {
        if (validTransfer(transferAmount, user, account)) {
            balance += transferAmount;
            if(account instanceof Account_Asset) {
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
        balance -= transferAmount;
        if (account instanceof Account_Asset) {
            account.balance -= transferAmount;
        } else {
            account.balance += transferAmount;
        }
    }

    private boolean validTransfer(double transferAmount, Login_Customer user, Account account) {
        return transferAmount > 0 && user.hasAccount(account);
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
