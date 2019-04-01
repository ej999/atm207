package ATM;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.List;

/**
 * GIC account.
 * Money is locked up in this account for a period of time with high interest.
 * Withdrawing before the end of a period will result in the endDate being reset.
 * customer should choose the period and the bank manager give the correct rate when creating the account.
 */
class GIC extends AccountAsset {

    private static final String type = GIC.class.getName();
    private final double rate;
    private final Period period;// in months
    // period = Period.ofMonths(12)
    private LocalDate endDate;

    @SuppressWarnings({"WeakerAccess"})
    public GIC(String id, double rate, int p, List<String> owners) {
        super(id, owners);
        this.rate = rate / 100;
        this.period = Period.ofMonths(p);
        LocalDate startDate = LocalDate.now();
        endDate = startDate.plus(period);
    }

    @SuppressWarnings({"unused"})
    public GIC(String id, double rate, int p, String owner) {
        this(id, rate, p, Collections.singletonList(owner));
    }

    public String getType() {
        return type;
    }

    @Override
        // withdrawing money from GIC causes the payout date to be extended by the period of time
    void withdraw(double withdrawalAmount) {

        super.withdraw(withdrawalAmount, (getBalance() - withdrawalAmount) >= 0);
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

    void timeSkipTo(int year, int month, int day) {
        LocalDate skipTo = LocalDate.of(year, month, day);
        if (endDate.equals(skipTo) || endDate.isBefore(skipTo)) {
            setBalance(getBalance() + rate * getBalance());
            endDate = endDate.plus(period);
        }
    }

    @Override
    void deposit(double depositAmount) {
        super.deposit(depositAmount);
        this.endDate = LocalDate.now().plus(period);
    }

    @Override
    // when transfer money with GIC account it reset the time of the period

    public boolean transferToAnotherUser(double transferAmount, String username, Account account) {
        boolean end = super.transferToAnotherUser(transferAmount, username, account);
        if (end) {
            this.endDate = LocalDate.now().plus(period);
        }
        return end;
    }

    // need to call this everyday for all GIC accounts
    void newDay() {
        if (checkToday()) {
            setBalance(getBalance() + rate * getBalance());
            this.endDate = endDate.plus(period);
        }
    }
}
