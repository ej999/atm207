package phase1;

/**
 * Asset accounts include chequing and savings.
 */
public abstract class AssetAccount extends Account {

    /**
     * Pay a bill by transferring money out to a non-user's account.
     * This decreases their balance.
     * @param amount amount of money to be transferred
     * @param accountName the name of non-user's account
     */
    public void payBill(double amount, String accountName) {
        /*
        ToDo: Write to file <outgoing.txt>
         */
    }

    /**
     * A helper method.
     * @param amount
     * @param account
     * @return
     */
    private boolean transfer(double amount, Account account) {
        // ToDo
        return true;
    }

    /**
     * Transfer money between accounts user owns.
     * Precondition: account must belong to this user.
     * @param amount transfer amount
     * @param account another account of user
     * @return true if transfer was successful, otherwise false
     */
    public boolean transferBetweenAccounts(double amount, Account account) {
        // ToDo
        return transfer(amount, account);
    };

    /**
     * Transfer money from this user's account to another user's account.
     * This decreases the balance.
     * Precondition: account must belong to another user
     * @param amount transfer
     * @param account account of another user
     */
    public boolean transferToAnotherUser(double amount, Account account) {
        // ToDo
        return transfer(amount,account);
    }
}
