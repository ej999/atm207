package phase1;

abstract class Account_Asset implements Account {
    /** Account_Debt Balance */
    double accountBalance;

    @Override
    public void deposit(double depositAmount) {
        accountBalance += depositAmount;
    }

    @Override
    public double withdraw(double withdrawalAmount) {
        accountBalance -= withdrawalAmount;
        return withdrawalAmount;
    }

    @Override
    public String viewBalance() {
        String stringBalance = Double.toString(-accountBalance);
        stringBalance = "$" + stringBalance;
        return stringBalance;
    }
}
