package ATM;

import java.util.Date;

/**
 * A chequing account.
 */
class Account_Asset_Chequing extends Account_Asset implements Account_Transferable {
    private static final String type = Account_Asset_Chequing.class.getName();

    Account_Asset_Chequing(double balance, User_Customer owner) {
        super(balance, owner);
    }

    Account_Asset_Chequing(double balance, User_Customer owner1, User_Customer owner2) {
        super(balance, owner1, owner2);
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
//            mostRecentTransactionString = "$" + getMostRecentTransaction().getAmount() + " withdrawn.";
//        } else if (getMostRecentTransaction().getType().equals("Deposit")) {
//            mostRecentTransactionString = "$" + getMostRecentTransaction().getAmount() + " deposited.";
//        } else if (getMostRecentTransaction().getType().equals("Transfer")) {
//            mostRecentTransactionString = "$" + getMostRecentTransaction().getAmount() + " transferred.";
//        } else if (getMostRecentTransaction().getType().equals("PayBill")) {
//            mostRecentTransactionString = "$" + getMostRecentTransaction().getAmount() + " bill payment.";
//        } else {
//            mostRecentTransactionString = "n/a";
//        }
//
//        return "Chequing\t\t\t" + new Date(dateOfCreation) + "\t" + balance + "\t\t" + mostRecentTransactionString;
//    }
}
