package phase1;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ATMFrame {

    /** check if today is the start of the month*/
    public static boolean checkMonth(){
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        int now = (calendar.get(Calendar.MONTH));
        calendar.add(Calendar.DATE, -1);
        int past = (calendar.get(Calendar.MONTH));
        return !(past == now);

    }

    }



