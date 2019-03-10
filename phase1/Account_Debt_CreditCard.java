package phase1;

class Account_Debt_CreditCard extends Account_Debt {
    /**
     * Balance is set to 0.00 as default if an initial balance is not provided.
     */
    public Account_Debt_CreditCard(Login_Customer owner) {
        super(owner);
    }

    public Account_Debt_CreditCard(double balance, Login_Customer owner) {
        super(balance, owner);
    }

    @Override
    public String toString() {
        return "Credit Card\t\t\t" + dateOfCreation + "\t" + balance;
    }
}
