package phase1;

import java.util.Observable;
import java.util.Observer;

/**
 * A savings account.
 */
class Account_Asset_Saving extends Account_Asset implements Observer, Account_Transferable {

    Account_Asset_Saving(double balance, Login_Customer owner) {
        super(balance, owner);
    }

    @Override
    void withdraw(double withdrawalAmount) {
        super.withdraw(withdrawalAmount, (balance - withdrawalAmount) >= 0);
    }

    /**
     * It should observe today's date and get called when necessary.
     */
    @Override
    public void update(Observable o, Object arg) {
        if ((boolean) arg) {
            balance += 0.001 * balance;
        }
    }

    @Override
    public String toString() {
        String mostRecentTransactionString;

        if (getMostRecentTransaction().get("Type") == "Withdrawal") {
            mostRecentTransactionString = "$" + getMostRecentTransaction().get("Amount") + " withdrawn.";
        } else if (getMostRecentTransaction().get("Type") == "Deposit") {
            mostRecentTransactionString = "$" + getMostRecentTransaction().get("Amount") + " deposited.";
        } else {
            mostRecentTransactionString = "n/a";
        }

        return "Saving\t\t\t\t" + dateOfCreation + "\t" + balance + "\t\t" + mostRecentTransactionString;
    }

}
