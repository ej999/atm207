package phase1;

abstract class Account_Debt extends Account {

    Account_Debt(double balance, Login_Customer owner) {
        super(balance, owner);
    }

    Account_Debt(Login_Customer owner) {
        super(owner);
    }

    @Override
    double getBalance() {
        return -balance;
    }

    private boolean validWithdrawal(double withdrawalAmount) {
        return withdrawalAmount > 0 && withdrawalAmount % 5 == 0 &&
                Cash.isThereEnoughBills(withdrawalAmount);
    }

    /**
     * Withdraw money from an account (This will increase <balance> since you owe money)
     *
     * @param withdrawalAmount amount to be withdrawn
     */
    @Override
    void withdraw(double withdrawalAmount) {
        if (validWithdrawal(withdrawalAmount)) {
            balance += withdrawalAmount;
            Cash.cashWithdrawal(withdrawalAmount);
            updateMostRecentTransaction("Withdrawal", withdrawalAmount, null);
        }
    }

    @Override
    void undoWithdrawal(double amount) {
        balance -= amount;
        Cash.undoCashWithdrawal(amount);
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
