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
    private double accountBalance;

    public LineOfCreditAccount() {
        this.accountBalance = 0.00;
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

    public void transfer(double transferAmount, Account transferAccount, User transferUser) {
        for (int i : (transferUser.accounts).length) {
            if (transferUser.accounts(i) == transferAccount) {
                accountBalance -= transferAmount;
                (transferUser.accounts(i)).accountBalance += transferAmount;
            }
        }


    }
}
