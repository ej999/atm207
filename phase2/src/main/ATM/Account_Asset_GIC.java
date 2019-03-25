package ATM;

import java.time.LocalDate;
import java.time.Period;
import java.util.Observable;
import java.util.Observer;

/**
 * GIC account.
 * Money is locked up in this account for a period of time with high interest.
 * Withdrawing before the end of a period will result in no interest.
 */
class Account_Asset_GIC extends Account_Asset implements Observer, Account_Transferable {

    private static final String account_type = Account_Asset_GIC.class.getName();
    double rate;
    Period period;
    // period = Period.ofMonths(12)
    LocalDate startDate;
    LocalDate endDate;

    Account_Asset_GIC(double balance, double rate, Period period, User_Customer owner) {

        super(balance, owner);
        this.rate = rate;
        this.period = period;
        startDate = LocalDate.now();
        endDate = startDate.plus(period);
    }

    Account_Asset_GIC(double balance, double rate, Period period, User_Customer owner1, User_Customer owner2) {
        super(balance, owner1, owner2);
        this.rate = rate;
        this.period = period;
        startDate = LocalDate.now();
        endDate = startDate.plus(period);

    }

    public String getAccount_type() {
        return account_type;
    }

    @Override
    void withdraw(double withdrawalAmount) {

        super.withdraw(withdrawalAmount, (balance - withdrawalAmount) >= 0);
    }

    @Override
    public String toString() {
        return "GIC\t\t\t\t" + startDate + " - " + endDate + "\t" + balance + "\t" + rate + "\t\t";
    }

    @Override
    // not finish
    public void update(Observable o, Object arg) {
        if ((boolean) arg) {
            balance += rate * balance;
        }
    }
}
