package phase1;

abstract class Account_Debt extends Account {

    public Account_Debt(double balance, Login_Customer owner) {
        super(balance, owner);
    }

    public Account_Debt(Login_Customer owner) {
        super(owner);
    }

    public double withdraw(double withdrawalAmount) {
        return super.withdraw(withdrawalAmount, true);
    }
}
