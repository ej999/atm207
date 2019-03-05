package phase1;

/**
 * Asset accounts include Chequing and Savings Accounts.
 */
abstract class AssetAccount extends Account {

    /**
     * Withdraw money from an account (This will decrease <accountBalance>)
     * @param withdrawalAmount amount to be withdrawn
     * @return withdrawalAmount, otherwise 0.
     */
    @Override
    abstract int withdraw(int withdrawalAmount);

    boolean validWithdrawal(int withdrawalAmount) {
        return withdrawalAmount % 5 == 0 && withdrawalAmount > 0 && accountBalance > 0;
    }

//    /*
//    What would actually happen is that the user would load an input file called <deposits.txt>
//    Need to create a public deposit method that reads from file <deposits.txt>
//     */
//    private void deposit(double depositAmount) {
//        accountBalance += depositAmount;
//    }

    @Override
    abstract String viewBalance();

    /*
    Figure out transferring money, paying a bill
     */
//    void payBill(double amount, String accountName, <File goes here>)
//    If user has sufficient funds, proceed to paying bill
//    Write to <outgoing.txt> the amount and accountName
//    Something like "User paid (amount) to (accountName) on (date)"

    /**
     * Transfer money between accounts the user owns
     * @param transferAmount the amount to be transferred
     * @param account another account the user owns
     * @return true if transfer was successful
     */
    boolean transferBetweenAccounts(double transferAmount, Account account) {
        return transferToAnotherUser(transferAmount, accountOwner, account);
    }

    boolean transferToAnotherUser(double transferAmount, Login_User user, Account account) {
        if (transferAmount > 0 && (accountBalance - transferAmount) >= 0 && user.hasAccount(account)) {
            accountBalance -= transferAmount;
            account.accountBalance += transferAmount;
            return true;
        }
        return false;
    }
}
