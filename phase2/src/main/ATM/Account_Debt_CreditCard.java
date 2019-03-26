package ATM;

import java.util.Date;

class Account_Debt_CreditCard extends Account_Debt {
    private static final String type = Account_Debt_CreditCard.class.getName();

    /**
     * Balance is set to 0.00 as default if an initial balance is not provided.
     */
    Account_Debt_CreditCard(User_Customer owner) {
        super(owner);
    }

    Account_Debt_CreditCard(double balance, User_Customer owner) {
        super(balance, owner);
    }


    Account_Debt_CreditCard(double balance, User_Customer owner1, User_Customer owner2) {
        super(balance, owner1, owner2);
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
////            mostRecentTransactionString = "$" + getMostRecentTransaction().get("Amount") + " withdrawn.";
////        } else if (getMostRecentTransaction().get("Type") == "Deposit") {
////            mostRecentTransactionString = "$" + getMostRecentTransaction().get("Amount") + " deposited.";
////        } else {
////            mostRecentTransactionString = "n/a";
////        }
////
////        return "Credit Card\t\t\t" + new Date(dateOfCreation) + "\t" + balance + "\t\t" + mostRecentTransactionString;
////    }
}
