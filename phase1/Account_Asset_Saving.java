package phase1;

/**
 * A savings account.
 */
class Account_Asset_Saving extends Account_Asset {

    public Account_Asset_Saving(Login_Customer owner) {
        super(owner);
    }

    public Account_Asset_Saving(double balance, Login_Customer owner) {
        super(balance, owner);
    }

    @Override
    double withdraw(double withdrawalAmount) {
        if (validWithdrawal(withdrawalAmount) && (balance - withdrawalAmount) >= 0) {
            return withdrawalAmount;
        }
        return 0;
    }

    /**
     * This method should be automatically invoked on the first of every month.
     * It should observe today's date and get called when necessary.
     */
    void update() {
        balance += 0.001 * balance;
    }

}
