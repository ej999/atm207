package ATM;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.EmptyStackException;

class CreditLine extends AccountDebt implements AccountTransferable {

    private static final String type = CreditLine.class.getName();

    /**
     * Balance is set to 0.00 as default if an initial balance is not provided.
     */
    CreditLine(String id, Customer owner) {
        super(id, owner);
    }

    public CreditLine(String id, double balance, Customer owner) {
        super(id, balance, owner);
    }

    CreditLine(String id, double balance, Customer owner1, Customer owner2) {
        super(id, balance, owner1, owner2);
    }

    public String getType() {
        return type;
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
            String message = "\nUser " + this.getPrimaryOwner() + " paid $" + amount + " to " + accountName + " on " +
                    LocalDateTime.now();
            // Open the file for writing and write to it.
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFilePath, true)))) {
                out.println(message);
                System.out.println("File has been written");
            }
            balance += amount;
            transactionHistory.push(new Transaction("PayBill", amount, null, this.getClass().getName()));
            return true;
        }
        return false;
    }

    public void undoPaybill(double amount) {
        balance -= amount;
    }

    /**
     * Transfer money between accounts the user owns. This increases the balance.
     *
     * @param transferAmount the amount to be transferred
     * @param account        another account the user owns
     * @return true if transfer was successful
     */
    public boolean transferBetweenAccounts(double transferAmount, Account account) {
        return transferToAnotherUser(transferAmount, (Customer) ATM.userManager.getUser(getPrimaryOwner()), account);
    }

    /**
     * Transfer money to another user's account. This also increases the balance.
     *
     * @param transferAmount transfer amount
     * @param user           receiver of amount
     * @param account        user account
     * @return true iff transfer was a success
     */
    public boolean transferToAnotherUser(double transferAmount, Customer user, Account account) {
        if (validTransfer(transferAmount, user, account)) {
            balance += transferAmount;
            if (account instanceof AccountAsset) {
                account.balance += transferAmount;
            } else {
                account.balance -= transferAmount;
            }
            transactionHistory.push(new Transaction("Transfer", transferAmount, account, this.getClass().getName()));
            return true;
        }
        return false;
    }

    private void undoTransfer(double transferAmount, Account account) {
        balance -= transferAmount;
        if (account instanceof AccountAsset) {
            account.balance -= transferAmount;
        } else {
            account.balance += transferAmount;
        }
    }

    private boolean validTransfer(double transferAmount, Customer user, Account account) {
        return validWithdrawal(transferAmount) && user.hasAccount(account);
    }

//    @Override
//    void undoMostRecentTransaction() {
//        super.undoMostRecentTransaction();
//        if (getMostRecentTransaction().get("Type").equals("TransferBetweenAccounts") ||
//                getMostRecentTransaction().get("Type").equals("TransferToAnotherUser")) {
//            undoTransfer((Double) getMostRecentTransaction().get("Amount"), (Account) getMostRecentTransaction().get("Account"));
//        }
//    }

//    @Override
//    public String toString() {
//        String mostRecentTransactionString;
//
//        if (getMostRecentTransaction() == null) {
//            mostRecentTransactionString = "n/a";
//        } else if (getMostRecentTransaction().get("Type") == "Withdrawal") {
//            mostRecentTransactionString = "$" + getMostRecentTransaction().get("Amount") + " withdrawn";
//        } else if (getMostRecentTransaction().get("Type") == "Deposit") {
//            mostRecentTransactionString = "$" + getMostRecentTransaction().get("Amount") + " deposited";
//        } else {
//            mostRecentTransactionString = "n/a";
//        }
//
//        return "Line of Credit\t\t" + new Date(dateOfCreation) + "\t" + balance + ((balance == 0) ? " " : "") + "\t\t" + mostRecentTransactionString;
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
