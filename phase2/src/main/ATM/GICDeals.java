package ATM;

import java.util.ArrayList;

public class GICDeals {
    static ArrayList<GICDeals> gicDeals = new ArrayList<GICDeals>();
    final int period;
    final double rate;

    public GICDeals(int p, double r) {
        this.period = p;
        this.rate = r;
        gicDeals.add(this);
    }

    public int getPeriod() {
        return period;
    }

    public double getRate() {
        return rate;
    }

    void removeOldestDeal() {
        if (gicDeals.size() != 0) {
            gicDeals.remove(0);
        }
    }

    void removeNewestDeal() {
        if (gicDeals.size() != 0) {
            gicDeals.remove(gicDeals.size() - 1);
        }
    }

    @Override
    public String toString() {
        return "You will get" + this.rate * 100. + " percent interest in" + this.period + "Months";
    }
}
