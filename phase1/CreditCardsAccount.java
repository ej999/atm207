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
    private double creditCardBalance;

    /**
     * Constructs a new Credit Card Account.
     */
    public CreditCardsAccount() {
        this.creditCardBalance = 0.00;
    }

    public void setBalance(double initialBalance) {
        creditCardBalance = initialBalance;
    }

    public void withdrawal(int withdrawalAmount) {
        creditCardBalance -= withdrawalAmount;
    }

    public void deposit(double depositAmount) {
        creditCardBalance += depositAmount;
    }

    public String viewBalance() {
        String stringBalance = Double.toString(-creditCardBalance);
        stringBalance = "$" + stringBalance;
        return stringBalance;
    }
}
