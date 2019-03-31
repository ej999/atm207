package ATM;

import java.util.List;

abstract class AccountDebt extends Account {
    private static final double DEBT_CAPACITY = 10000;

    AccountDebt(String id, List<Customer> owners) {
        super(id, owners);
    }

    AccountDebt(String id, Customer owner) {
        super(id, owner);
    }

    boolean validWithdrawal(double withdrawalAmount) {
        return withdrawalAmount > 0 &&
                withdrawalAmount % 5 == 0 &&
                ATM.banknoteManager.isThereEnoughBankNote(withdrawalAmount) &&
                checkDebtCapacity(withdrawalAmount);
    }

    /**
     * All Debt accounts should have a maximum amount of debt you can incur.
     *
     * @param amount involved in transaction
     * @return true iff amount does not increase balance past DEBT_CAPACITY.
     */
    boolean checkDebtCapacity(double amount) {
        return getBalance() + amount <= DEBT_CAPACITY;
    }

    /**
     * Withdraw money from an account (This will increase <balance> since you owe money)
     *
     * @param withdrawalAmount amount to be withdrawn
     */
    @Override
    void withdraw(double withdrawalAmount) {
        if (validWithdrawal(withdrawalAmount)) {
            setBalance(getBalance() + withdrawalAmount);
            ATM.banknoteManager.banknoteWithdrawal(withdrawalAmount);
            getTransactionHistory().push(new Transaction("Withdrawal", withdrawalAmount, null, this.getClass().getName()));
        }
    }

    @Override
    void undoWithdrawal(double amount) {
        setBalance(getBalance() - amount);
        ATM.banknoteManager.banknoteWithdrawal(-amount);
    }

    /*
    Depositing money onto a credit card decreases account balance (since you're paying back the bank)
     */
    @Override
    void deposit(double depositAmount) {
        if (depositAmount > 0) {
            setBalance(getBalance() - depositAmount);
            getTransactionHistory().push(new Transaction("Deposit", depositAmount, null, this.getClass().getName()));
            System.out.println("valid deposit");
        } else {
            System.out.println("invalid deposit");
        }
    }

    @Override
    void undoDeposit(double depositAmount) {
        setBalance(getBalance() + depositAmount);
    }
}
