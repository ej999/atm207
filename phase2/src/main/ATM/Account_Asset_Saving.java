package ATM;

import java.util.Observable;
import java.util.Observer;

/**
 * A savings account.
 */
class Account_Asset_Saving extends Account_Asset implements Observer, Account_Transferable {
    private static final String account_type = Account_Asset_Saving.class.getName();

    Account_Asset_Saving(double balance, User_Customer owner) {
        super(balance, owner);
    }

    Account_Asset_Saving(double balance, User_Customer owner1, User_Customer owner2) {
        super(balance, owner1, owner2);
    }

    public String getAccount_type() {
        return account_type;
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
