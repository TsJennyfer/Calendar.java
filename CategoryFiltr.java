package organizer;

public class CategoryFiltr extends Filtr{
    String search;
    CategoryFiltr (String searchCategory)
    {
        search = searchCategory;
    }
    public boolean doFiltr(Event rec)
    {
        return search.compareTo(rec.category) == 0 ;
    }
}