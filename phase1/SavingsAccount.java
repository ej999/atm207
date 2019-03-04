package phase1;

/**
 * A savings account.
 */
class SavingsAccount extends AssetAccount {

    SavingsAccount() {
        accountBalance = 0.00;
    }

    @Override
    String viewBalance() {
        return "$" + accountBalance;
    }

    @Override
    double withdraw(double withdrawalAmount) {
        if (accountBalance - withdrawalAmount >= 0) {
            return withdrawalAmount;
        }
        return 0;
    }

    /**
     * This method should be automatically invoked on the first of every month.
     * It should observe today's date and get called when necessary.
     */
    void increase() {
        accountBalance += 0.001 * accountBalance;
    }

}
