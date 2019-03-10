package phase1;

import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.io.Serializable;

/**
 * TODO: write description for this class
 */
class ATMFrame extends Observable implements Serializable {

    /**
     * check if today is the start of the month
     */
    /*
    TODO: this method should be called every night by ________
     */
    void checkMonth() {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        int now = (calendar.get(Calendar.MONTH));
        calendar.add(Calendar.DATE, -1);
        int past = (calendar.get(Calendar.MONTH));

        setChanged();
        notifyObservers(!(past == now));

    }
}



