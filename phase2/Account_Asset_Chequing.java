package phase2;

/**
 * A chequing account.
 */
class Account_Asset_Chequing extends Account_Asset implements Account_Transferable {

    Account_Asset_Chequing(double balance, SystemUser_Customer owner) {
        super(balance, owner);
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
