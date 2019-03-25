package ATM;

import java.util.Calendar;
import java.util.Date;
import java.util.Observable;

/**
 * ATM frame that handles update.
 */
class ATMSystem extends Observable {
    /**
     * check if today is the start of the month
     */
    int checkMonth(int past) {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        int now = (calendar.get(Calendar.MONTH));

        setChanged();
        notifyObservers(!(past == now));
        return now;
    }
}



