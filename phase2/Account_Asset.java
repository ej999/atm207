package phase2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * Asset accounts include Chequing and Savings Accounts.
 */
abstract class Account_Asset extends Account implements Account_Transferable {

    Account_Asset(double balance, SystemUser_Customer owner) {
        super(balance, owner);
    }

    /**
     * Pay a bill by transferring money to a non-user's account
     *
     * @param amount      transfer amount
     * @param accountName non-user's account name
     * @return true if bill has been payed successfully
     */
    public boolean payBill(double amount, String accountName) throws IOException {
        if (amount > 0 && (balance - amount) >= 0) {
            String message = "\nUser " + this.getOwner().getUsername() + " paid $" + amount + " to " + accountName + " on " +
                    LocalDateTime.now();
            // Open the file for writing and write to it.
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFilePath, true)))) {
                out.println(message);
            }
            balance -= amount;
            updateMostRecentTransaction("PayBill", amount, null);
            return true;
        }
        return false;
    }

    private boolean validWithdrawal(double withdrawalAmount) {
        return withdrawalAmount > 0 && withdrawalAmount % 5 == 0 && balance > 0 &&
                Cash.isThereEnoughBills(withdrawalAmount);
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
            Cash.cashWithdrawal(withdrawalAmount);

            updateMostRecentTransaction("Withdrawal", withdrawalAmount, null);
        }
    }

    @Override
    void undoWithdrawal(double withdrawalAmount) {
        balance += withdrawalAmount;
        Cash.undoCashWithdrawal(withdrawalAmount);
    }

    @Override
    void deposit(double depositAmount) {
        if (depositAmount > 0) {
            balance += depositAmount;
            updateMostRecentTransaction("Deposit", depositAmount, null);
        } else {
            System.out.println("invalid deposit");
        }
    }

    @Override
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
    public boolean transferBetweenAccounts(double transferAmount, Account account) {
        return transferToAnotherUser(transferAmount, getOwner(), account);
    }

    /**
     * Transfer money from this account to another user's account (this will decrease their balance)
     *
     * @param transferAmount amount to transfer
     * @param user           receives transferAmount
     * @param account        of user
     * @return true iff transfer is valid
     */
    public boolean transferToAnotherUser(double transferAmount, SystemUser_Customer user, Account account) {
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

    private void undoTransfer(double transferAmount, Account account) {
        balance += transferAmount;
        if (account instanceof Account_Asset) {
            account.balance -= transferAmount;
        } else {
            account.balance += transferAmount;
        }

    }

    private boolean validTransfer(double transferAmount, SystemUser_Customer user, Account account) {
        //TODO: any login user should have a hasAccount method
        return transferAmount > 0 && (balance - transferAmount) >= 0 && user.hasAccount(account);
    }

    @Override
    void undoMostRecentTransaction() {
        super.undoMostRecentTransaction();
        if (getMostRecentTransaction().get("Type").equals("TransferBetweenAccounts") ||
                getMostRecentTransaction().get("Type").equals("TransferToAnotherUser")) {
            undoTransfer((Double) getMostRecentTransaction().get("Amount"), (Account) getMostRecentTransaction().get("Account"));
        }
    }
}
