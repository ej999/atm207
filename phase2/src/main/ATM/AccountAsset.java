package ATM;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.EmptyStackException;

/**
 * Asset accounts include Chequing and Savings Accounts.
 */
abstract class AccountAsset extends Account implements AccountTransferable {

    AccountAsset(String id, double balance, Customer owner) {
        super(id, balance, owner);
    }

    AccountAsset(String id, double balance, Customer owner1, Customer owner2) {
        super(id, balance, owner1, owner2);
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
            String message = "\nUser " + this.getPrimaryOwner() + " paid $" + amount + " to " + accountName + " on " +
                    LocalDateTime.now();
            // Open the file for writing and write to it.
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFilePath, true)))) {
                out.println(message);
            }
            balance -= amount;
            transactionHistory.push(new Transaction("PayBill", amount, null, this.getClass().getName()));
            return true;
        }
        return false;
    }

    public void undoPaybill(double amount) {
        balance += amount;
    }

    private boolean validWithdrawal(double withdrawalAmount) {
        return withdrawalAmount > 0 && withdrawalAmount % 5 == 0 && balance > 0 &&
                new Cash().isThereEnoughBills(withdrawalAmount);
    }

    /**
     * Withdraw money from an account (This will decrease <balance>)
     *
     * @param withdrawalAmount amount to be withdrawn
     * @param condition        additional condition in order to successfully withdraw
     */
    void withdraw(double withdrawalAmount, boolean condition) {
        if (validWithdrawal(withdrawalAmount) && condition) {
            balance -= withdrawalAmount;
            new Cash().cashWithdrawal(withdrawalAmount);
            transactionHistory.push(new Transaction("Withdrawal", withdrawalAmount, null, this.getClass().getName()));
        }
    }

    @Override
    void undoWithdrawal(double withdrawalAmount) {
        balance += withdrawalAmount;
        new Cash().cashWithdrawal(-withdrawalAmount);
    }

    @Override
    void deposit(double depositAmount) {
        if (depositAmount > 0) {
            balance += depositAmount;
            transactionHistory.push(new Transaction("Deposit", depositAmount, null, this.getClass().getName()));
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
        return transferToAnotherUser(transferAmount, (Customer) UserManager.getUser(getPrimaryOwner()), account);
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
//            if (user == UserManager.getUser(getPrimaryOwner())) {
//                transactionHistory.push(new Transaction("TransferBetweenAccounts", transferAmount, account));
//            } else {
//                transactionHistory.push(new Transaction("TransferToAnotherUser", transferAmount, account));
//            }
            // Simplify things
            transactionHistory.push(new Transaction("Transfer", transferAmount, account, this.getClass().getName()));
            return true;
        }
        return false;
    }

    private void undoTransfer(double transferAmount, Account account) {
        balance += transferAmount;
        if (account instanceof AccountAsset) {
            account.balance -= transferAmount;
        } else {
            account.balance += transferAmount;
        }

    }

    private boolean validTransfer(double transferAmount, Customer user, Account account) {
        return transferAmount > 0 && (balance - transferAmount) >= 0 && user.hasAccount(account);
    }

//    @Override
//    void undoMostRecentTransaction() {
//        super.undoMostRecentTransaction();
//        if (getMostRecentTransaction().get("Type").equals("TransferBetweenAccounts") ||
//                getMostRecentTransaction().get("Type").equals("TransferToAnotherUser")) {
//            undoTransfer((Double) getMostRecentTransaction().get("Amount"), (Account) getMostRecentTransaction().get("Account"));
//        }
//
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
