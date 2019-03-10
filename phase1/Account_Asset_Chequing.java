package phase1;

/**
 * A chequing account.
 */
class Account_Asset_Chequing extends Account_Asset {

    Account_Asset_Chequing(Login_Customer owner) {
        super(owner);
    }

    Account_Asset_Chequing(double balance, Login_Customer owner) {
        super(balance, owner);
    }

    @Override
    double withdraw(double withdrawalAmount) {
        return super.withdraw(withdrawalAmount,(balance - withdrawalAmount >= -100));
    }

    @Override
    public String toString() {
        String mostRecentTransactionString;

        if (mostRecentTransaction.get("Type") == "Withdrawal") {
            mostRecentTransactionString = "$" + mostRecentTransaction.get("Amount") + " withdrawn.";
        } else if (mostRecentTransaction.get("Type") == "Deposit") {
            mostRecentTransactionString = "$" + mostRecentTransaction.get("Amount") + " deposited.";
        } else {
            mostRecentTransactionString = "n/a";
        }

        return "Chequing\t\t\t" + dateOfCreation + "\t" + balance + "\t\t" + mostRecentTransactionString;
    }
}
