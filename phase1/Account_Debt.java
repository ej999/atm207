package phase1;

abstract class Account_Debt extends Account {
    /** Account_Debt Balance */
    double accountBalance;

    @Override
    public void deposit(double depositAmount) {
        accountBalance += depositAmount;
    }

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
