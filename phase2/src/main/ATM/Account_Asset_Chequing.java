package ATM;

/**
 * A chequing account.
 */
class Account_Asset_Chequing extends Account_Asset implements Account_Transferable {
    private static final String account_type = Account_Asset_Chequing.class.getName();

    Account_Asset_Chequing(double balance, User_Customer owner) {
        super(balance, owner);
    }

    Account_Asset_Chequing(double balance, User_Customer owner1, User_Customer owner2) {
        super(balance, owner1, owner2);
    }

    @Override
    public String getAccount_type() {
        return account_type;
    }

    @Override
    void withdraw(double withdrawalAmount) {
        super.withdraw(withdrawalAmount, balance > 0 & (balance - withdrawalAmount >= -100));
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

        return "Chequing\t\t\t" + dateOfCreation + "\t" + balance + "\t\t" + mostRecentTransactionString;
    }
}
