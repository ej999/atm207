package ATM;

import java.util.Observable;
import java.util.Observer;

/**
 * A savings account.
 */
class Saving extends AccountAsset implements Observer, AccountTransferable {
    private static final String type = Saving.class.getName();

    public Saving(String id, double balance, Customer owner) {
        super(id, balance, owner);
    }

    Saving(String id, double balance, Customer owner1, Customer owner2) {
        super(id, balance, owner1, owner2);
    }

    public String getType() {
        return type;
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

//    @Override
//    public String toString() {
//        String mostRecentTransactionString;
//        if (getMostRecentTransaction() == null) {
//            mostRecentTransactionString = "n/a";
//        } else if (getMostRecentTransaction().get("Type") == "Withdrawal") {
//            mostRecentTransactionString = "$" + getMostRecentTransaction().get("Amount") + " withdrawn.";
//        } else if (getMostRecentTransaction().get("Type") == "Deposit") {
//            mostRecentTransactionString = "$" + getMostRecentTransaction().get("Amount") + " deposited.";
//        } else {
//            mostRecentTransactionString = "n/a";
//        }
//
//        return "Saving\t\t\t\t" + new Date(dateOfCreation) + "\t" + balance + "\t\t" + mostRecentTransactionString;
//    }

}
