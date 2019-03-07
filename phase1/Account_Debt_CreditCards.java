package phase1;

class Account_Debt_CreditCards extends Account_Debt {
    /*
     * These are accounts that display a positive balance when the user owes a money and a negative balance
     * when the user overpays. It is not possible to transfer money out of a credit account.
     * But it is possible to transfer money in.
     *
     */

    /**
     * Balance is set to 0.00 as default if an initial balance is not provided.
     */
    Account_Debt_CreditCards() {
        this(0.00);
    }

    Account_Debt_CreditCards(double initialBalance) {
        this.accountBalance = initialBalance;
    }

    void setBalance(double initialBalance) {
        accountBalance = initialBalance;
    }
}
