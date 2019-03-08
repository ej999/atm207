package phase1;

class Account_Debt_LineOfCredit extends Account_Debt {
    /*
     * A line of credit account allows you to transfer money in our out. But it also displays a positive balance
     * when the user owes money and a negative balance when the user overpays
     */

    /**
     * Balance is set to 0.00 as default if an initial balance is not provided.
     */
    public Account_Debt_LineOfCredit(Login_Customer owner) {
        super(owner);
    }

    public Account_Debt_LineOfCredit(double balance, Login_Customer owner) {
        super(balance, owner);
    }


    void transfer(double transferAmount, Account transferAccount, Login_Customer transferUser) {
        for (Account i : transferUser.getAccounts()) {
            if (i == transferAccount) {
                balance -= transferAmount;
                i.deposit(transferAmount);
            }
        }


    }
}
