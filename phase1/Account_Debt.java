package phase1;

abstract class Account_Debt extends Account {

    public Account_Debt(double balance, Login_Customer owner) {
        super(balance, owner);
    }

    public Account_Debt(Login_Customer owner) {
        super(owner);
    }

    private boolean validWithdrawal(double withdrawalAmount) {
        return withdrawalAmount > 0 && withdrawalAmount % 5 == 0;
    }

    /**
     * Withdraw money from an account (This will increase <balance> since you owe money)
     * TODO: notify the Cash class about this withdrawal
     *
     * @param withdrawalAmount amount to be withdrawn
     * @return withdrawalAmount, otherwise 0.
     */
    @Override
    double withdraw(double withdrawalAmount) {
        if (validWithdrawal(withdrawalAmount)) {
            balance += withdrawalAmount;
            updateMostRecentTransaction("Withdrawal", withdrawalAmount,null);
            return withdrawalAmount;
        }
        return 0;
    }

    @Override
    void undoWithdrawal(double amount) {
        balance -= amount;
    }

    /*
    Depositing money onto a credit card decreases account balance (since you're paying back the bank)
     */
    @Override
    void deposit(double depositAmount) {
        if (depositAmount > 0) {
            balance -= depositAmount;
            updateMostRecentTransaction("Deposit", depositAmount, null);
            System.out.println("valid deposit");
        } else {
            System.out.println("invalid deposit");
        }
    }

    @Override
    void undoDeposit(double depositAmount) {
        balance += depositAmount;
    }
}
