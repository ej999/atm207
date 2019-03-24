package ATM;

public final class AccountFactory {
    private static ATMSystem myATM = new ATMSystem();

    public static Account createAccount(String accountType, SystemUser_Customer owner, double initialAmount) {

        if (accountType == null) {
            System.out.println("Invalid account type. Account cannot be created.");
            return null;
        }
        if (accountType.equalsIgnoreCase("CHEQUING")) {
            return new Account_Asset_Chequing(initialAmount, owner);

        } else if (accountType.equalsIgnoreCase("SAVINGS")) {
            Account_Asset_Saving newAccount = new Account_Asset_Saving(initialAmount, owner);
            myATM.addObserver(newAccount);
            return new Account_Asset_Saving(initialAmount, owner);

        } else if (accountType.equalsIgnoreCase("CREDITCARD")) {
            return new Account_Debt_CreditCard(initialAmount, owner);
        } else if (accountType.equalsIgnoreCase("LINEOFCREDIT")) {
            return new Account_Debt_LineOfCredit(initialAmount, owner);
        } else if (accountType.equalsIgnoreCase("STUDENT")) {
            //TODO: return a new Student account
        }

        return null;
    }

}
