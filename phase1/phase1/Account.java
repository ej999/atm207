package phase1;

/**
 * An account of a user. So far there are two types of accounts: Debt and Asset.
 */
public abstract class Account {
    public double accountBalance;

    public abstract double viewBalance();

    /**
     * Withdraw money from an account (This will decrease their balance).
     * Returns the amount withdrawn.
     * @param withdrawAmount amount to be withdrawn
     */
    public abstract double withdraw(double withdrawAmount);

    /**
     * Deposit money into account by entering a cheque or cash into the machine.
     */
    public void deposit() {
        /*
        ToDo: read from input file <deposits.txt> line by line and increase balance.
         */
    }
}
