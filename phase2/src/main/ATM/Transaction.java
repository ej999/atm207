package ATM;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * A class that represents a transaction made by a user.
 * Possible transactions include: Withdrawal, Deposit, Transfer, PayBill, ETransfer
 */
public class Transaction {
    private String type;
    private double amount;
    private Account account; // the account involved in transfer
    private String date;
    private String accountType; // the account type involved in transaction

    public Transaction(String type, double amount, Account account, String accountType) {
        this.type = type;
        this.amount = amount;
        this.account = account;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        this.date = dateFormat.format(date);
        this.accountType = accountType;
    }

    public String getType() {
        return this.type;
    }

    public double getAmount() {
        return this.amount;
    }

    public Account getAccount() {
        return this.account;
    }

    public String getDate() {
        return this.date;
    }

    public String getAccountType() {
        return this.accountType;
    }

    @Override
    public String toString() {
        return this.type + " of $" + this.amount;
    }


}
