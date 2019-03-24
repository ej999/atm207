package ATM;

/**
 * A chequing account.
 */
class Account_Asset_Chequing extends Account_Asset implements Account_Transferable {

    Account_Asset_Chequing(double balance, SystemUser_Customer owner) {
        super(balance, owner);
    }

    Account_Asset_Chequing(double balance, SystemUser_Customer owner1, SystemUser_Customer owner2) {
        super(balance, owner1, owner2);
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
