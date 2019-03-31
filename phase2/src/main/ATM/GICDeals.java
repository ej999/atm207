package ATM;

import java.util.ArrayList;

public class GICDeals {
    public static ArrayList<GICDeals> gicDeals = new ArrayList<GICDeals>();
    private final int period;
    private final double rate;

    public GICDeals(int p, double r,int id) {
        this.period = p;
        this.rate = r;
        gicDeals.add(id,this);
    }

    public int getPeriod() {
        return period;
    }

    public double getRate() {
        return rate;
    }

    static void removeDeal(int index) {
            gicDeals.remove(index);
        }


    @Override
    public String toString() {
        return "id" + GICDeals.gicDeals.indexOf(this) + "You will get" + this.rate  + " percent interest in" + this.period + "Months";
    }
}
