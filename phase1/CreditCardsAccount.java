package phase1;

public class CreditCardsAccount extends DebtAccount{
    /*
     * These are accounts that display a positive balance when the user owes a money and a negative balance
     * when the user overpays. It is not possible to transfer money out of a credit account.
     * But it is possible to transfer money in.
     *
     * To Do:
     *  Implement:
     *  - Transfer
     *  - ViewBalance
     *  - Deposit
     *  - Withdrawal
     */

    /** CreditCardsAccount Balance */
    private double accountBalance;

    /**
     * Constructs a new Credit Card Account.
     */
    public CreditCardsAccount() {
        this.accountBalance = 0.00;
    }

    public void setBalance(double initialBalance) {
        accountBalance = initialBalance;
    }

    public double withdraw(double withdrawalAmount) {
        accountBalance -= withdrawalAmount;
        return withdrawalAmount;
    }

    public void deposit(double depositAmount) {
        accountBalance += depositAmount;
    }

    public String viewBalance() {
        String stringBalance = Double.toString(-accountBalance);
        stringBalance = "$" + stringBalance;
        return stringBalance;
    }
}
