package phase1;

/**
 * A savings account.
 */
public class SavingsAccount extends AssetAccount {

    /**
     * Constructs a new savings account.
     */
    public SavingsAccount() {
        this.accountBalance = 0.00;
    }

    public double viewBalance() {
        return accountBalance;
    }

    public double withdraw(double amount) {
        //ToDo
        return 0.00;
    }

    public void increase() {
        //ToDo
        //account balance increase by factor of 0.1%.
        //collaborates with Time object
    }
}
