package phase2;

abstract class Account_Student extends Account implements Account_Transferable {
    int transactions;

    // Transactions, student account has maximum 20 transfers that they can have
    // TODO: Interest, age, email
    Account_Student(double balance, SystemUser_Customer owner) {
        super(balance, owner);
        this.transactions = 0;
    }

    private boolean validWithdrawal(double withdrawalAmount) {
        return withdrawalAmount > 0 && withdrawalAmount % 5 == 0 && balance > 0 &&
                Cash.isThereEnoughBills(withdrawalAmount) && (transactions < 20);
    }

    /**
     * Withdraw money from an account (This will decrease <balance>)
     *
     * @param withdrawalAmount amount to be withdrawn
     * @param condition        additional condition in order to successfully withdraw
     */
    void withdraw(double withdrawalAmount, boolean condition) {
        if (validWithdrawal(withdrawalAmount) && (condition)) {
            balance -= withdrawalAmount;
            Cash.cashWithdrawal(withdrawalAmount);
            transactions += 1;
            updateMostRecentTransaction("Withdrawal", withdrawalAmount, null);
        }
    }

    @Override
    void undoWithdrawal(double withdrawalAmount) {
        balance += withdrawalAmount;
        transactions -= 1;
        Cash.undoCashWithdrawal(withdrawalAmount);
    }

    @Override
    void deposit(double depositAmount) {
        if ((depositAmount > 0) && (transactions < 20)) {
            balance += depositAmount;
            transactions += 1;
            updateMostRecentTransaction("Deposit", depositAmount, null);
        } else {
            System.out.println("invalid deposit");
        }
    }

    @Override
    void undoDeposit(double depositAmount) {
        balance -= depositAmount;
        transactions -= 1;
    }

    /**
     * Transfer money between accounts the user owns
     *
     * @param transferAmount the amount to be transferred
     * @param account        another account the user owns
     * @return true if transfer was successful
     */
    public boolean transferBetweenAccounts(double transferAmount, Account account) {
        transactions += 1;
        return transferToAnotherUser(transferAmount, getOwner(), account);

    }

    /**
     * Transfer money from this account to another user's account (this will decrease their balance)
     *
     * @param transferAmount amount to transfer
     * @param user           receives transferAmount
     * @param account        of user
     * @return true iff transfer is valid
     */
    public boolean transferToAnotherUser(double transferAmount, SystemUser_Customer user, Account account) {
        if (validTransfer(transferAmount, user, account)) {
            balance -= transferAmount;
            if (account instanceof Account_Asset) {
                account.balance += transferAmount;
            } else {
                account.balance -= transferAmount;
            }
            if (user == getOwner()) {
                updateMostRecentTransaction("TransferBetweenAccounts", transferAmount, account);
            } else {
                updateMostRecentTransaction("TransferToAnotherUser", transferAmount, account);
            }
            transactions += 1;
            return true;
        }
        return false;
    }

    private void undoTransfer(double transferAmount, Account account) {
        balance += transferAmount;
        transactions -= 1;
        if (account instanceof Account_Asset) {
            account.balance -= transferAmount;
        } else {
            account.balance += transferAmount;
        }

    }

    private boolean validTransfer(double transferAmount, SystemUser_Customer user, Account account) {
        return transferAmount > 0 && (balance - transferAmount) >= 0 && user.hasAccount(account) && (transactions < 20);
    }

    @Override
    void undoMostRecentTransaction() {
        super.undoMostRecentTransaction();
        transactions += 1;
        if (getMostRecentTransaction().get("Type").equals("TransferBetweenAccounts") ||
                getMostRecentTransaction().get("Type").equals("TransferToAnotherUser")) {
            undoTransfer((Double) getMostRecentTransaction().get("Amount"), (Account) getMostRecentTransaction().get("Account"));
        }
    }
}
