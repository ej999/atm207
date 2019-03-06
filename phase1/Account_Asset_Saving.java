package phase1;

import java.util.Date;

/**
 * A savings account.
 */
class Account_Asset_Saving extends Account_Asset {

    Account_Asset_Saving(Login_User owner) {
        accountOwner = owner;
        accountBalance = 0.00;
        dateOfCreation = new Date();
    }

    @Override
    public String viewBalance() {
        return "$" + accountBalance;
    }

    @Override
    double withdraw(double withdrawalAmount) {
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
