package phase1;

import java.util.Date;
import java.util.Observer;

/**
 * A savings account.
 */
class SavingsAccount extends AssetAccount implements Observer {

    SavingsAccount(Login_User owner) {
        accountOwner = owner;
        accountBalance = 0.00;
        dateOfCreation = new Date();
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
    @Override
    void update() {
        accountBalance += 0.001 * accountBalance;
    }

}
