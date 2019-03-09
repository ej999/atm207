package phase1;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;

/**
 * TODO: write description for this class
 */
public class ATMFrame extends Observable {

    /** check if today is the start of the month*/
    /*
    TODO: this method should be called every night by ________
     */
    public void checkMonth(){
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



