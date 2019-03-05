package phase1;

/**
 * A savings account.
 */
class SavingsAccount extends AssetAccount {

    SavingsAccount(Login_User owner) {
        accountOwner = owner;
        accountBalance = 0.00;
    }

    @Override
    String viewBalance() {
        return "$" + accountBalance;
    }

    @Override
    int withdraw(int withdrawalAmount) {
        if (validWithdrawal(withdrawalAmount) && (accountBalance - withdrawalAmount) >= 0) {
            return withdrawalAmount;
        }
        return 0;
    }

    /**
     * This method should be automatically invoked on the first of every month.
     * It should observe today's date and get called when necessary.
     */
    void update() {
        accountBalance += 0.001 * accountBalance;
    }

}
