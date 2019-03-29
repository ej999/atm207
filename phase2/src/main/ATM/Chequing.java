package ATM;

/**
 * A chequing account.
 */
class Chequing extends AccountAsset implements AccountTransferable {
    private static final String type = Chequing.class.getName();

    public Chequing(String id, double balance, Customer owner) {
        super(id, balance, owner);
    }

    Chequing(String id, double balance, Customer owner1, Customer owner2) {
        super(id, balance, owner1, owner2);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    void withdraw(double withdrawalAmount) {
        super.withdraw(withdrawalAmount, balance > 0 & (balance - withdrawalAmount >= -100));
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
