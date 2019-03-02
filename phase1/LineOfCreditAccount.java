class LineOfCreditAccount extends DebtAccount {
    /*
     * A line of credit account allows you to transfer money in our out. But it also displays a positive balance
     * when the user owes money and a negative balance when the user overpays
     */

    /*
     * Constructs a new Credit Card Account.
     */

    /** LineOfCreditAccount Balance */
    private double LOCbalance;

    public LineOfCreditAccount() {
        LOCbalance = 0.00;
    }

    public void withdrawal(int withdrawalAmount) {
        LOCbalance -= withdrawalAmount;
    }

    public void deposit(double depositAmount) {
        LOCbalance += depositAmount;
    }

    public String viewBalance() {
        String stringBalance = Double.toString(-LOCbalance);
        stringBalance = "$" + stringBalance;
        return stringBalance;
    }

    public void transfer(double transferAmount, Account transferAccount) {
    }
}
