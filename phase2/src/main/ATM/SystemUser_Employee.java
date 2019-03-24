package ATM;

import java.io.Serializable;

abstract class SystemUser_Employee extends SystemUser implements Serializable {
    SystemUser_Employee(String username, String password) {
        super(username, password);
    }

    void addAccount(String accountType, SystemUser_Customer username, @SuppressWarnings("SameParameterValue") double amount) {
        Account newAccount = AccountFactory.createAccount(accountType, username, amount);

        if (newAccount != null) {
            username.addAccount(newAccount);
            System.out.println("A " + accountType + " account with $" + amount + " balance is successfully created for "
                    + username.getUsername() + ". ");
        }

    }

    /**
     * Create an account for a Customer. Amount is not initialized here.
     */
    void addAccount(String accountType, SystemUser_Customer username) {
        this.addAccount(accountType, username, 0);
    }

}
