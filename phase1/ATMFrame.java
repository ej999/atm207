package phase1;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;

public class ATMFrame extends Observable {

    /** check if today is the start of the month*/
    /*
    Is there a way to call this method after a certain time duration? e.g. every day
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



