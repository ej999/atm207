package phase1;

/**
 * Asset accounts include Chequing and Savings Accounts.
 */
abstract class AssetAccount implements Account {
    double accountBalance;

    /**
     * Withdraw money from an account (This will decrease <accountBalance>)
     * @param withdrawalAmount amount to be withdrawn
     * @return withdrawalAmount, otherwise 0.
     */
    abstract double withdraw(double withdrawalAmount);

    /*
    What would actually happen is that the user would load an input file called <deposits.txt>
    Need to create a public deposit method that reads from file <deposits.txt>
     */
    private void deposit(double depositAmount) {
        accountBalance += depositAmount;
    }

    abstract String viewBalance();

    /*
    Figure out transferring money, paying a bill
     */
}
