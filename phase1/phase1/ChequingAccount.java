package phase1;

/**
 * A chequing account.
 */
public class ChequingAccount extends AssetAccount {
    public boolean primaryAccount;

    /**
     * Constructs a new chequing account.
     */
    public ChequingAccount() {
        this.accountBalance = 0.00;
    }

    public double viewBalance() {
        return accountBalance;
    }

    public double withdraw(double amount) {
        //ToDo
        return 0.00;
    }
}
