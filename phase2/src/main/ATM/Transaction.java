package ATM;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A class that represents a transaction made by a User.
 * Possible transactions include: Withdrawal, Deposit, Transfer, PayBill, ETransfer
 */
public class Transaction {
    private final String transactionType;
    private final double amount;
    private final String accountId; // the id of account involved in transfer
    private final long date;
    private final String accountType; // the account type involved in transaction

    public Transaction(String TransactionType, double amount, String accountId, String accountType) {
        this.transactionType = TransactionType;
        this.amount = amount;
        this.accountId = accountId;
        this.date = new Date().getTime();
        this.accountType = accountType;
    }

    @SuppressWarnings("WeakerAccess")
    public String getTransactionType() {
        return this.transactionType;
    }

    @SuppressWarnings("WeakerAccess")
    public String getAccountId() {
        return this.accountId;
    }

    @SuppressWarnings("unused")
    public String getAccountType() {
        return this.accountType;
    }

    @SuppressWarnings("unused")
    public long getDate() {
        return this.date;
    }


    public double getAmount() {
        return this.amount;
    }

    // Return dateCreated as String in a readable format.
    String getDateCreatedReadable() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(new Date(date));
    }

    @Override
    public String toString() {
        return this.transactionType + " of $" + this.amount;
    }


}
