package ATM;

import java.util.List;

class CreditCard extends AccountDebt {
    private static final String type = CreditCard.class.getName();

    @SuppressWarnings("unused")
    public CreditCard(String id, List<String> owners) {
        super(id, owners);
    }

    @SuppressWarnings("unused")
    public CreditCard(String id, String owner) {
        super(id, owner);
    }

    public String getType() {
        return type;
    }

//    @Override
////    public String toString() {
////        String mostRecentTransactionString;
////
////        if (getMostRecentTransaction() == null) {
////            mostRecentTransactionString = "n/a";
////        } else if (getMostRecentTransaction().get("Type") == "Withdrawal") {
////            mostRecentTransactionString = "$" + getMostRecentTransaction().get("Amount") + " withdrawn";
////        } else if (getMostRecentTransaction().get("Type") == "Deposit") {
////            mostRecentTransactionString = "$" + getMostRecentTransaction().get("Amount") + " deposited";
////        } else {
////            mostRecentTransactionString = "n/a";
////        }
////
////        return "Credit Card\t\t\t" + new Date(dateOfCreation) + "\t" + balance + "\t\t" + mostRecentTransactionString;
////    }
}
