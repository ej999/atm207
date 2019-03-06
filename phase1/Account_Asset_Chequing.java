package phase1;

import java.util.Date;

/**
 * A chequing account.
 */
class Account_Asset_Chequing extends Account_Asset {
    /*
    I need a way to see how many chequing accounts this user has.
    If this is the user's only chequing account, then isPrimary is true.
    Otherwise, one of the chequing accounts should be selected as a "primary"
    account. Perhaps a ChequingAccountManager?
     */
//    private boolean isPrimary; may not be needed

    Account_Asset_Chequing(Login_Customer owner) {
        this.accountBalance = 0.00;
        this.accountOwner = owner;
        this.dateOfCreation = new Date();
    }

    @Override
    double withdraw(double withdrawalAmount) {
        if (validWithdrawal(withdrawalAmount) && (accountBalance - withdrawalAmount >= -100)) {
            accountBalance -= withdrawalAmount;
            return withdrawalAmount;
        }
        return 0;
    }

    @Override
    public String viewBalance() {
        if (accountBalance >= 0) {
            return "$" + accountBalance;
        } else {
            return "-$" + Math.abs(accountBalance);
        }
    }
}
