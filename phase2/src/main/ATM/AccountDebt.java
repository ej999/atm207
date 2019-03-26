package ATM;

abstract class AccountDebt extends Account {

    private static final double DEBT_CAPACITY = 10000;

    public AccountDebt(String id, double balance, Customer owner) {
        super(id, balance, owner);
    }

    AccountDebt(String id, Customer owner) {
        super(id, owner);
    }

    AccountDebt(String id, double balance, Customer owner1, Customer owner2) {
        super(id, balance, owner1, owner2);
    }

    // Withdrawal is valid only when amount
    boolean validWithdrawal(double withdrawalAmount) {
        return withdrawalAmount > 0 &&
                withdrawalAmount % 5 == 0 &&
                Cash.isThereEnoughBills(withdrawalAmount) &&
                balance < DEBT_CAPACITY;
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
            transactionHistory.push(new Transaction("Withdrawal", withdrawalAmount, null));
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
            transactionHistory.push(new Transaction("Deposit", depositAmount, null));
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
