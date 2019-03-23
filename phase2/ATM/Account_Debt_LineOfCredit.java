package ATM;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

class Account_Debt_LineOfCredit extends Account_Debt implements Account_Transferable {

    /**
     * Balance is set to 0.00 as default if an initial balance is not provided.
     */
    Account_Debt_LineOfCredit(SystemUser_Customer owner) {
        super(owner);
    }

    Account_Debt_LineOfCredit(double balance, SystemUser_Customer owner) {
        super(balance, owner);
    }

    Account_Debt_LineOfCredit(double balance, SystemUser_Customer owner1, SystemUser_Customer owner2) {
        super(balance, owner1, owner2);
    }

    /**
     * Pay a bill by transferring money to a non-user's account
     *
     * @param amount      transfer amount
     * @param accountName non-user's account name
     * @return true if bill has been payed successfully
     */
    public boolean payBill(double amount, String accountName) throws IOException {
        if (amount > 0) {
            String message = "\nUser " + this.getOwner().getUsername() + " paid $" + amount + " to " + accountName + " on " +
                    LocalDateTime.now();
            // Open the file for writing and write to it.
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFilePath, true)))) {
                out.println(message);
                System.out.println("File has been written.");
            }
            balance += amount;
            updateMostRecentTransaction("PayBill", amount, null);
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
    public boolean transferBetweenAccounts(double transferAmount, Account account) {
        return transferToAnotherUser(transferAmount, getOwner(), account);
    }

    /**
     * Transfer money to another user's account. This also increases the balance.
     *
     * @param transferAmount transfer amount
     * @param user           receiver of amount
     * @param account        user account
     * @return true iff transfer was a success
     */
    public boolean transferToAnotherUser(double transferAmount, SystemUser_Customer user, Account account) {
        if (validTransfer(transferAmount, user, account)) {
            balance += transferAmount;
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

    private void undoTransfer(double transferAmount, Account account) {
        balance -= transferAmount;
        if (account instanceof Account_Asset) {
            account.balance -= transferAmount;
        } else {
            account.balance += transferAmount;
        }
    }

    private boolean validTransfer(double transferAmount, SystemUser_Customer user, Account account) {
        return validWithdrawal(transferAmount) && user.hasAccount(account);
    }

    @Override
    void undoMostRecentTransaction() {
        super.undoMostRecentTransaction();
        if (getMostRecentTransaction().get("Type").equals("TransferBetweenAccounts") ||
                getMostRecentTransaction().get("Type").equals("TransferToAnotherUser")) {
            undoTransfer((Double) getMostRecentTransaction().get("Amount"), (Account) getMostRecentTransaction().get("Account"));
        }
    }

    @Override
    public String toString() {
        String mostRecentTransactionString;

        if (getMostRecentTransaction().get("Type") == "Withdrawal") {
            mostRecentTransactionString = "$" + getMostRecentTransaction().get("Amount") + " withdrawn.";
        } else if (getMostRecentTransaction().get("Type") == "Deposit") {
            mostRecentTransactionString = "$" + getMostRecentTransaction().get("Amount") + " deposited.";
        } else {
            mostRecentTransactionString = "n/a";
        }

        return "Line of Credit\t\t" + dateOfCreation + "\t" + balance + ((balance == 0) ? " " : "") + "\t\t" + mostRecentTransactionString;
    }
}
