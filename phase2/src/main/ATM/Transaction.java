package ATM;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A class that represents a transaction made by a user.
 * Possible transactions include: Withdrawal, Deposit, Transfer, PayBill, ETransfer
 */
public class Transaction {
    private String TransactionType;
    private double amount;
    private String accountId; // the id of account involved in transfer
    private long date;
    private String accountType; // the account type involved in transaction

    public Transaction(String TransactionType, double amount, String accountId, String accountType) {
        this.TransactionType = TransactionType;
        this.amount = amount;
        this.accountId = accountId;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        this.date = new Date().getTime();
        this.accountType = accountType;
    }

    public String getTransactionType() {
        return this.TransactionType;
    }

    public double getAmount() {
        return this.amount;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public long getDate() {
        return this.date;
    }

    // Return dateCreated as String in a readable format.
    String getDateCreatedReadable() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(new Date(date));
    }

    public String getAccountType() {
        return this.accountType;
    }

    @Override
    public String toString() {
        return this.TransactionType + " of $" + this.amount;
    }


}
