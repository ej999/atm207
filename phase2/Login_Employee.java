package phase2;

import java.io.Serializable;

abstract class Login_Employee extends Login implements Serializable {
    Login_Employee(String username, String password) {
        this(username, password, "Employee");
    }

    /**
     * 3 parameter constructor: different kind of login type required for different employees.
     */
    Login_Employee(String username, String password, String loginType) {
        super(username, password, loginType);
    }

    void addAccount(String accountType, Login_Customer username, @SuppressWarnings("SameParameterValue") double amount) {
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
    void addAccount(String accountType, Login_Customer username) {
        this.addAccount(accountType, username, 0);
    }

}
