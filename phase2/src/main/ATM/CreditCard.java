package ATM;

class CreditCard extends AccountDebt {
    private static final String type = CreditCard.class.getName();

    /**
     * Balance is set to 0.00 as default if an initial balance is not provided.
     */
    CreditCard(String id, Customer owner) {
        super(id, owner);
    }

    public CreditCard(String id, double balance, Customer owner) {
        super(id, balance, owner);
    }

    CreditCard(String id, double balance, Customer owner1, Customer owner2) {
        super(id, balance, owner1, owner2);
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
