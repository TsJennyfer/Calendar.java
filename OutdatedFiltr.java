package organizer;

import java.util.Date;

public class OutdatedFiltr extends Filtr{
    Date now;
    OutdatedFiltr (Date searchDate)
    {
        now = searchDate;
    }
    public boolean doFiltr(Event rec)
    {
        if ( now.after(rec.date) )
            return true; 
        else return false;
    }
}