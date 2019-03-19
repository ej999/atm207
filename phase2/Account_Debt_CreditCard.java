package phase2;

class Account_Debt_CreditCard extends Account_Debt {
    /**
     * Balance is set to 0.00 as default if an initial balance is not provided.
     */
    Account_Debt_CreditCard(SystemUser_Customer owner) {
        super(owner);
    }

    Account_Debt_CreditCard(double balance, SystemUser_Customer owner) {
        super(balance, owner);
    }

    Account_Debt_CreditCard(double balance, SystemUser_Customer owner1, SystemUser_Customer owner2) {
        super(balance, owner1, owner2);
    }

    @Override
    public String toString() {
        String mostRecentTransactionString;

        if (getMostRecentTransaction().get("Type") == "Withdrawal") {
            mostRecentTransactionString = "$" + getMostRecentTransaction().get("Amount") + " withdrawn.";
        } else if (getMostRecentTransaction().get("Type") == "Deposit") {
            mostRecentTransactionString = "$" + getMostRecentTransaction().get("Amount") + " deposited.";
        } else {
            mostRecentTransactionString = "n/a";
        }

        return "Credit Card\t\t\t" + dateOfCreation + "\t" + balance + "\t\t" + mostRecentTransactionString;
    }
}
