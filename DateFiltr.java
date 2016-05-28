package organizer;

import java.util.Date;

public class DateFiltr extends Filtr {
    Date search;
    DateFiltr(Date searchDate)
    {
        search = searchDate;
    }
    public boolean doFiltr(Event rec)
    {
        return search.equals(rec.date);
    }
}