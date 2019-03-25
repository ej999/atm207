package ATM;

/**
 * A class that represents a transaction made by a user.
 * Possible transactions include: Withdrawal, Deposit, TransferBetweenAccounts, TransferToAnotherUser, PayBill
 */
public class Transaction {
    private String type;
    private double amount;
    private Account account;

    public Transaction(String type, double amount, Account account) {
        this.type = type;
        this.amount = amount;
        this.account = account;
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
}
