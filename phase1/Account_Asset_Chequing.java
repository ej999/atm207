package phase1;

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
        super(owner);
    }

    Account_Asset_Chequing(double balance, Login_Customer owner) {
        super(balance, owner);
    }

    @Override
    double withdraw(double withdrawalAmount) {
        if (validWithdrawal(withdrawalAmount) && (balance - withdrawalAmount >= -100)) {
            balance -= withdrawalAmount;
            return withdrawalAmount;
        }
        return 0;
    }

}
