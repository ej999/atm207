package phase1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

class Account_Debt_LineOfCredit extends Account_Debt {
    /*
     * TODO: A line of credit account allows you to transfer money in our out. But it also displays a positive balance
     * when the user owes money and a negative balance when the user overpays
     */

    /**
     * Balance is set to 0.00 as default if an initial balance is not provided.
     */
    public Account_Debt_LineOfCredit(Login_Customer owner) {
        super(owner);
    }

    Account_Debt_LineOfCredit(double balance, Login_Customer owner) {
        super(balance, owner);
    }

//    public String viewBalance() {
//        String stringBalance = Double.toString(-balance);
//        stringBalance = "$" + stringBalance;
//        return stringBalance;
//    }
//
//
//    void transferOut(double transferAmount, Account transferAccount, Login_Customer transferUser) {
//        for (Account i : transferUser.getAccounts()) {
//            if (i == transferAccount) {
//                balance -= transferAmount;
//                i.deposit(transferAmount);
//            }
//        }
//    }
//
//    void transferBetween(double transferAmount, Account transferAccount) {
//        for (Account i : owner.getAccounts()) {
//            if (i == transferAccount) {
//                balance -= transferAmount;
//                i.setBalance(i.getBalance() + transferAmount);
//            }
//        }
//    }
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
