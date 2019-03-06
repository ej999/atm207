package phase1;

class Account_Debt_LineOfCredit extends Account_Debt {
    /*
     * A line of credit account allows you to transfer money in our out. But it also displays a positive balance
     * when the user owes money and a negative balance when the user overpays
     */

    /**
     * Balance is set to 0.00 as default if an initial balance is not provided.
     */
    Account_Debt_LineOfCredit() {
        this(0.00);
    }

    Account_Debt_LineOfCredit(double initialBalance) {
        this.accountBalance = initialBalance;
    }

    void transfer(double transferAmount, Account transferAccount, Login_User transferUser) {
        for (Account i : transferUser.getAccounts()) {
            if (i == transferAccount) {
                accountBalance -= transferAmount;
                i.deposit(transferAmount);
            }
        }


    }
}
