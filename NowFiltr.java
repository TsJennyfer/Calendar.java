package organizer;

import java.util.Date;

public class NowFiltr extends Filtr{
    Date now;
    NowFiltr (Date searchDate)
    {
        now = searchDate;
    }
    public boolean doFiltr(Event rec)
    {
        long h = 28800000L ; // == 8 hours
        long sum = now.getTime() + h;
        Date sumDate = new Date(sum);
        if ( now.before(rec.date) && (sumDate.after(rec.date)))
            return true; 
        else return false;
    }
}