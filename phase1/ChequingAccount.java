package phase1;

/**
 * A chequing account.
 */
class ChequingAccount extends AssetAccount {
    /*
    I need a way to see how many chequing accounts this user has.
    If this is the user's only chequing account, then isPrimary is true.
    Otherwise, one of the chequing accounts should be selected as a "primary"
    account. Perhaps a ChequingAccountManager?
     */
//    private boolean isPrimary; may not be needed

    ChequingAccount(Login_User owner) {
        this.accountBalance = 0.00;
        this.accountOwner = owner;
    }

    @Override
    int withdraw(int withdrawalAmount) {
        if (validWithdrawal(withdrawalAmount) && (accountBalance - withdrawalAmount >= -100)) {
            accountBalance -= withdrawalAmount;
            return withdrawalAmount;
        }
        return 0;
    }

    @Override
    String viewBalance() {
        if (accountBalance >= 0) {
            return "$" + accountBalance;
        } else {
            return "-$" + Math.abs(accountBalance);
        }
    }
}
