package phase1;

class LineOfCreditAccount extends DebtAccount {
    /*
     * A line of credit account allows you to transfer money in our out. But it also displays a positive balance
     * when the user owes money and a negative balance when the user overpays
     */

    /*
     * Constructs a new Credit Card Account.
     */

    /** LineOfCreditAccount Balance */
    private double loCreditBalance;

    public LineOfCreditAccount() {
        this.loCreditBalance = 0.00;
    }

    public void withdrawal(int withdrawalAmount) {
        loCreditBalance -= withdrawalAmount;
    }

    public void deposit(double depositAmount) {
        loCreditBalance += depositAmount;
    }

    public String viewBalance() {
        String stringBalance = Double.toString(-loCreditBalance);
        stringBalance = "$" + stringBalance;
        return stringBalance;
    }

    public void transfer(double transferAmount, Account transferAccount, User transferUser) {
    }
}
