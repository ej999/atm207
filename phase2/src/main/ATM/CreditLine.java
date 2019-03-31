package ATM;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.EmptyStackException;
import java.util.List;

import static ATM.ATM.accountManager;
import static ATM.ATM.userManager;

class CreditLine extends AccountDebt implements AccountTransferable {

    private static final String type = CreditLine.class.getName();

    /**
     * Balance is set to 0.00 as default if an initial balance is not provided.
     */
    @SuppressWarnings({"unused"})
    public CreditLine(String id, List<String> owners) {
        super(id, owners);
    }

    @SuppressWarnings({"unused"})
    public CreditLine(String id, String owner) {
        super(id, owner);
    }

    public String getType() {
        return type;
    }

    /**
     * Pay a bill by transferring money to a non-user's account
     *
     * @param amount      transfer amount
     * @param accountName non-user's account name
     * @return true iff bill has been payed successfully
     */
    public boolean payBill(double amount, String accountName) throws IOException {
        if (amount > 0 && checkDebtCapacity(amount)) {
            String message = "\nUser " + this.getPrimaryOwner() + " paid $" + amount + " to " + accountName + " on " +
                    LocalDateTime.now();
            // Open the file for writing and write to it.
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFilePath, true)))) {
                out.println(message);
                System.out.println("File has been written");
            }
            setBalance(getBalance() - amount);
            getTransactionHistory().push(new Transaction("PayBill", amount, null, type));
            return true;
        }
        return false;
    }

    private void undoPayBill(double amount) {
        setBalance(getBalance() - amount);
    }

    /**
     * Transfer money between accounts the user owns. This increases the balance.
     *
     * @param transferAmount the amount to be transferred
     * @param account        another account the user owns
     * @return true if transfer was successful
     */
    public boolean transferBetweenAccounts(double transferAmount, Account account) {
        return transferToAnotherUser(transferAmount, getPrimaryOwner(), account);
    }

    /**
     * Transfer money to another user's account. This also increases the balance.
     *
     * @param transferAmount transfer amount
     * @param username       receiver of amount
     * @param account        user account
     * @return true iff transfer was a success
     */
    public boolean transferToAnotherUser(double transferAmount, String username, Account account) {
        if (checkDebtCapacity(transferAmount) && validTransfer(transferAmount, username, account)) {
            setBalance(getBalance() - transferAmount);
            if (account instanceof AccountAsset) {
                account.setBalance(getBalance() + transferAmount);
            } else {
                account.setBalance(getBalance() - transferAmount);
            }
            getTransactionHistory().push(new Transaction("Transfer", transferAmount, account.getId(), type));
            return true;
        }
        return false;
    }

    private void undoTransfer(double transferAmount, Account account) {
        setBalance(getBalance() - transferAmount);
        if (account instanceof AccountAsset) {
            account.setBalance(getBalance() - transferAmount);
        } else {
            account.setBalance(getBalance() + transferAmount);
        }
    }

    private boolean validTransfer(double transferAmount, String username, Account account) {
        Customer customer = (Customer) userManager.getUser(username);
        return validWithdrawal(transferAmount) && customer.hasAccount(account);
    }

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
