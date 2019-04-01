package ATM;

import java.time.LocalDate;
import java.util.List;

/**
 * A savings account.
 */
class Saving extends AccountAsset {
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

    public void newDay() {
        if (checkToday()) {
            setBalance(getBalance() + 0.001 * getBalance());
        }
    }

    private boolean checkToday() {
        LocalDate today = LocalDate.now();
        return today.getDayOfMonth() == 1;
    }


    public void timeSkipTo(int year, int month, int day) {
        LocalDate skipTo = LocalDate.of(year, month, day);
        if (skipTo.getDayOfMonth() == 1) {
            setBalance(getBalance() + 0.001 * getBalance());
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
}

