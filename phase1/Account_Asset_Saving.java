package phase1;

import java.util.Observable;
import java.util.Observer;

/**
 * A savings account.
 */
class Account_Asset_Saving extends Account_Asset implements Observer {

    public Account_Asset_Saving(Login_Customer owner) {
        super(owner);
    }

    public Account_Asset_Saving(double balance, Login_Customer owner) {
        super(balance, owner);
    }

    @Override
    double withdraw(double withdrawalAmount) {
        return super.withdraw(withdrawalAmount, (balance - withdrawalAmount) >= 0);
    }

    /**
     * It should observe today's date and get called when necessary.
     */
    @Override
    public void update(Observable o, Object arg) {
        if ((boolean) arg) {
            balance += 0.001 * balance;
        }
    }

    @Override
    public String toString() {
        return "Saving\t\t\t\t" + dateOfCreation + "\t" + balance;
    }

}
