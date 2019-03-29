package ATM;

import java.time.LocalDate;
import java.time.Period;
import java.util.Observable;
import java.util.Observer;

/**
 * GIC account.
 * Money is locked up in this account for a period of time with high interest.
 * Withdrawing before the end of a period will result in the endDate being reset.
 */
class GIC extends AccountAsset implements AccountTransferable {

    private static final String type = GIC.class.getName();
    double rate;
    Period period;// in months
    // period = Period.ofMonths(12)
    LocalDate startDate;
    LocalDate endDate;

    GIC(String id, double balance, double rate, int p, Customer owner) {

        super(id, balance, owner);
        this.rate = rate;
        this.period = Period.ofMonths(p);
        startDate = LocalDate.now();
        endDate = startDate.plus(period);
    }

    GIC(String id, double balance, double rate, Period period, Customer owner1, Customer owner2) {
        super(id, balance, owner1, owner2);
        this.rate = rate;
        this.period = period;
        startDate = LocalDate.now();
        endDate = startDate.plus(period);

    }

    public String getType() {
        return type;
    }

    @Override
        // withdrawing money from GIC causes the payout date to be extended by the period of time
    void withdraw(double withdrawalAmount) {

        super.withdraw(withdrawalAmount, (balance - withdrawalAmount) >= 0);
        endDate = LocalDate.now().plus(period);
    }

    //    @Override
//    public String toString() {
//        return "GIC\t\t\t\t" + startDate + " - " + endDate + "\t" + balance + "\t" + rate + "\t\t";
//    }\
    private boolean checkToday() {
        LocalDate today = LocalDate.now();
        return today.equals(endDate);

    }

    public void timeSkipTo(int year, int month, int day){
        LocalDate skipTo = LocalDate.of(year,month,day);
        if (endDate.equals(skipTo) || endDate.isBefore(skipTo)){
           balance += rate * balance;
           endDate = endDate.plus(period);
        }
    }

    // not finish
    public void update() {
        if (checkToday()) {
            balance += rate * balance;
            this.endDate = endDate.plus(period);
        }
    }
}
