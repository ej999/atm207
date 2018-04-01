package ATM;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.EmptyStackException;
import java.util.List;

import static ATM.ATM.*;

/**
 * Asset accounts include Chequing and Savings Accounts.
 */
abstract class AccountAsset extends Account implements AccountTransferable {
    AccountAsset(String id, List<String> owners) {
        super(id, owners);
    }

    AccountAsset(String id, String owner) {
        super(id, owner);
    }

    /**
     * Pay a bill by transferring money to a non-user's account
     *
     * @param amount      transfer amount
     * @param accountName non-user's account name
     * @return true if bill has been payed successfully
     */
    public boolean payBill(double amount, String accountName) throws IOException {
        if (amount > 0 && (getBalance() - amount) >= 0) {
            String message = "\nUser " + this.getPrimaryOwner() + " paid $" + amount + " to " + accountName + " on " +
                    LocalDateTime.now();
            // Open the file for writing and write to it.
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFilePath, true)))) {
                out.println(message);
            }
            setBalance(getBalance() - amount);
            getTransactionHistory().push(new Transaction("PayBill", amount, null, this.getClass().getName()));
            return true;
        }
        return false;
    }

    private void undoPayBill(double amount) {
        setBalance(getBalance() + amount);
    }

    private boolean validWithdrawal(double withdrawalAmount) {
        return withdrawalAmount > 0 && withdrawalAmount % 5 == 0 && getBalance() > 0 &&
                banknoteManager.isThereEnoughBankNote(withdrawalAmount);
    }

    /**
     * Withdraw money from an account (This will decrease <balance>)
     *
     * @param withdrawalAmount amount to be withdrawn
     * @param condition        additional condition in order to successfully withdraw
     */
    void withdraw(double withdrawalAmount, boolean condition) {
        if (validWithdrawal(withdrawalAmount) && condition) {
            setBalance(getBalance() - withdrawalAmount);
            banknoteManager.banknoteWithdrawal(withdrawalAmount);
            getTransactionHistory().push(new Transaction("Withdrawal", withdrawalAmount, null, this.getClass().getName()));
        }
    }

    @Override
    void undoWithdrawal(double withdrawalAmount) {
        setBalance(getBalance() + withdrawalAmount);
        banknoteManager.banknoteWithdrawal(-withdrawalAmount);
    }

    @Override
    void deposit(double depositAmount) {
        if (depositAmount > 0) {
            setBalance(getBalance() + depositAmount);
            getTransactionHistory().push(new Transaction("Deposit", depositAmount, null, this.getClass().getName()));
        } else {
            System.out.println("invalid deposit");
        }
    }

    @Override
    void undoDeposit(double depositAmount) {
        setBalance(getBalance() - depositAmount);
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
     * Transfer money from this account to another user's account (this will decrease their getBalance())
     *
     * @param transferAmount amount to transfer
     * @param username       receives transferAmount
     * @param account        of user
     * @return true iff transfer is valid
     */
    public boolean transferToAnotherUser(double transferAmount, String username, Account account) {
        if (validTransfer(transferAmount, username, account)) {
            setBalance(getBalance() - transferAmount);
            if (account instanceof AccountAsset) {
                account.setBalance(account.getBalance() + transferAmount);
            } else {
                account.setBalance(account.getBalance() - transferAmount);
            }
//            if (user == userManager.getUser(getPrimaryOwner())) {
//                getTransactionHistory().push(new Transaction("TransferBetweenAccounts", transferAmount, account));
//            } else {
//                getTransactionHistory().push(new Transaction("TransferToAnotherUser", transferAmount, account));
//            }
            // Simplify things
            getTransactionHistory().push(new Transaction("Transfer", transferAmount, account.getId(), this.getClass().getName()));
            return true;
        }
        return false;
    }

    private void undoTransfer(double transferAmount, Account account) {
        setBalance(getBalance() + transferAmount);
        if (account instanceof AccountAsset) {
            setBalance(getBalance() - transferAmount);
        } else {
            setBalance(getBalance() + transferAmount);
        }

    }

    private boolean validTransfer(double transferAmount, String username, Account account) {
        Customer customer = (Customer) userManager.getUser(username);
        return transferAmount > 0 && (getBalance() - transferAmount) >= 0 && customer.hasAccount(account);
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
