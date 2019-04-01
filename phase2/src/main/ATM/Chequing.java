package ATM;

import java.util.List;

/**
 * A chequing account.
 */
class Chequing extends AccountAsset {
    private static final String type = Chequing.class.getName();

    @SuppressWarnings("unused")
    public Chequing(String id, List<String> owners) {
        super(id, owners);
    }

    @SuppressWarnings("unused")
    public Chequing(String id, String owner) {
        super(id, owner);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    void withdraw(double withdrawalAmount) {
        super.withdraw(withdrawalAmount, getBalance() > 0 & (getBalance() - withdrawalAmount >= -100));
    }

//    @Override
//    public String toString() {
//        String mostRecentTransactionString;
//        if (getMostRecentTransaction() == null) {
//            mostRecentTransactionString = "n/a";
//        } else if (getMostRecentTransaction().getTransactionType().equals("Withdrawal")) {
//            mostRecentTransactionString = "$" + getMostRecentTransaction().getAmount() + " withdrawn";
//        } else if (getMostRecentTransaction().getTransactionType().equals("Deposit")) {
//            mostRecentTransactionString = "$" + getMostRecentTransaction().getAmount() + " deposited";
//        } else if (getMostRecentTransaction().getTransactionType().equals("Transfer")) {
//            mostRecentTransactionString = "$" + getMostRecentTransaction().getAmount() + " transferred";
//        } else if (getMostRecentTransaction().getTransactionType().equals("PayBill")) {
//            mostRecentTransactionString = "$" + getMostRecentTransaction().getAmount() + " bill payment";
//        } else {
//            mostRecentTransactionString = "n/a";
//        }
//
//        return "Chequing\t\t\t" + new Date(dateOfCreation) + "\t" + balance + "\t\t" + mostRecentTransactionString;
//    }
}
