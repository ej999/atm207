package ATM;

import java.util.List;

/**
 * A chequing account.
 */
class Chequing extends AccountAsset {
    private static final String type = Chequing.class.getName();

    Chequing(String id, List<Customer> owners) {
        super(id, owners);
    }

    Chequing(String id, Customer owner) {
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
//        } else if (getMostRecentTransaction().getType().equals("Withdrawal")) {
//            mostRecentTransactionString = "$" + getMostRecentTransaction().getAmount() + " withdrawn";
//        } else if (getMostRecentTransaction().getType().equals("Deposit")) {
//            mostRecentTransactionString = "$" + getMostRecentTransaction().getAmount() + " deposited";
//        } else if (getMostRecentTransaction().getType().equals("Transfer")) {
//            mostRecentTransactionString = "$" + getMostRecentTransaction().getAmount() + " transferred";
//        } else if (getMostRecentTransaction().getType().equals("PayBill")) {
//            mostRecentTransactionString = "$" + getMostRecentTransaction().getAmount() + " bill payment";
//        } else {
//            mostRecentTransactionString = "n/a";
//        }
//
//        return "Chequing\t\t\t" + new Date(dateOfCreation) + "\t" + balance + "\t\t" + mostRecentTransactionString;
//    }
}
