package ATM;

import java.util.ArrayList;

class GICDeals {
    static final ArrayList<GICDeals> gicDeals = new ArrayList<GICDeals>();
    private final int period;
    private final double rate;

    GICDeals(int p, double r, int id) {
        this.period = p;
        this.rate = r;
        gicDeals.add(id, this);
    }

    static void removeDeal(int index) {
        gicDeals.remove(index);
    }

    int getPeriod() {
        return period;
    }

    double getRate() {
        return rate;
    }

    @Override
    public String toString() {
        return GICDeals.gicDeals.indexOf(this) + " You will get " + this.rate + " percent interest in " + this.period + " Months";
    }
}
