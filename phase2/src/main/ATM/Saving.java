package ATM;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * A savings account.
 */
class Saving extends AccountAsset implements Observer {
    private static final String type = Saving.class.getName();

    @SuppressWarnings({"unused"})
    public Saving(String id, List<String> owners) {
        super(id, owners);
    }

    @SuppressWarnings({"unused"})
    public Saving(String id, String owner) {
        super(id, owner);
    }


    public String getType() {
        return type;
    }

    @Override
    void withdraw(double withdrawalAmount) {
        super.withdraw(withdrawalAmount, (getBalance() - withdrawalAmount) >= 0);
    }

    /**
     * It should observe today's date and get called when necessary.
     */
    @Override
    public void update(Observable o, Object arg) {
        if ((boolean) arg) {
            setBalance(getBalance() + 0.001 * getBalance());
        }
    }

//    @Override
//    public String toString() {
//        String mostRecentTransactionString;
//        if (getMostRecentTransaction() == null) {
//            mostRecentTransactionString = "n/a";
//        } else if (getMostRecentTransaction().get("Type") == "Withdrawal") {
//            mostRecentTransactionString = "$" + getMostRecentTransaction().get("Amount") + " withdrawn";
//        } else if (getMostRecentTransaction().get("Type") == "Deposit") {
//            mostRecentTransactionString = "$" + getMostRecentTransaction().get("Amount") + " deposited";
//        } else {
//            mostRecentTransactionString = "n/a";
//        }
//
//        return "Saving\t\t\t\t" + new Date(dateOfCreation) + "\t" + balance + "\t\t" + mostRecentTransactionString;
//    }

}
