package organizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.TreeMap;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class OrganizerLib {
    
    private final TreeMap<Date, Event> myMap  = new TreeMap<>(); 
    private boolean modifiedCalendar = false;
    
    public boolean loadCalendarMap()
    {
        try 
        {
            File file = new File("timetable.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            while ((line = bufferedReader.readLine()) != null) 
            {
                Event rec = new Event();
                String[] separated = line.split("\\|");
                rec.category = separated[0];
                try
                {
                    rec.date = simpleDateFormat.parse(separated[1]);
                }
                catch (ParseException ex)
                {
                    System.out.println("ERROR. Can't tranform date. ");
                }
                rec.len = Integer.parseInt(separated[2]);
                rec.activity = separated[3];
                myMap.put(rec.date, rec);
            }
            fileReader.close();
            return true;
        } 
        catch (IOException e) 
        {
            System.out.println("Input ERROR. Can't load calendar. ");
            return false;
        }
    }
    
    public boolean workWithOutdated()
    {
        Calendar cal = Calendar.getInstance();
        
        int res = printMap( new OutdatedFiltr( cal.getTime() ) );
        if(res !=0)
        {
            System.out.println("You have some outdated events :( ");
        
            System.out.println("Do you want to delete it? ");     
            System.out.println("Press 1 to delete it");
            System.out.println("Press 2 to go on ");
            int countMenu = (-1);
            
            while(countMenu != 0)
            {
                System.out.println("Put your number: ");

                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                try
                {
                    String puts = input.readLine();
                    if( puts.compareTo("1") == 0 )
                    {   
                        int count = deleteMap( new OutdatedFiltr(cal.getTime()) );
                        if(count == 1)
                            System.out.println("You delete " + count + " event. ");
                        else if(count != 1 && count != 0)
                            System.out.println("You delete " + count + " events. ");
                
                        countMenu  = 0;
                        return false;
                    }
                    else if( puts.compareTo("2") == 0)
                    {
                        countMenu  = 0;
                        return false;
                    }
                    else 
                    {
                        countMenu  = (-1); 
                        System.out.println("Try again ;) ");
                    }
                }
                catch  (IOException e)
                {
                    System.out.println("Input ERROR. Unknown putting number. ");
                    return false;
                }
            }
        }
        return true;
    }
    
    public void searchNowDate()
    {
        Calendar cal = Calendar.getInstance();
        int res = printMap( new NowFiltr( cal.getTime() ) );
        if(res == 0)
            System.out.println("You have not got any events for now. ");
    }
    
    public boolean addEvent()
    {
        Event rec = new Event();
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Put date and time of event (dd/mm/yyyy hh:mm) : ");
        try
        {
            String puts = input.readLine();
            try
            {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                date = simpleDateFormat.parse(puts);
                
                if(date.before(cal.getTime() ) )
                {
                    System.out.println("It is not allowed to create event in this time. ");
                    return false;
                }
                for (Event value : myMap.values()) 
                {
                    long sum = value.date.getTime() + (value.len*60*60) ;
                    
                    if(value.date.equals(date) )
                    {
                        System.out.println("It is not allowed to create event in this time. "
                                + "You already have event in this time. ");
                        return false;
                    }
                    else if( value.date.getTime() < date.getTime() && date.getTime() <= sum )
                    {
                        System.out.println("You already have event in this time. ");
                        return false;
                    }
                }
                rec.date = date;
            }
            catch(ParseException e)
            {
                System.out.println("Input ERROR. Unknown date format. ");
                    return false;
            }
        }
        catch(IOException e)
        {
            System.out.println("Input ERROR. Unknown date format. ");
                return false;
        }
        
        System.out.println("Put length of our event : ");
        try
        {
            String putsLenStr;
            int putsLenInt = 0;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            putsLenStr = bufferedReader.readLine();
            putsLenInt = Integer.parseInt(putsLenStr);
            if(putsLenInt == 0)
            {
                System.out.println("It is not allowed to create event with this lenght. ");
                return false;
            }
            rec.len = putsLenInt;  
        }
        catch (NumberFormatException ex) 
        {
            System.out.println("Not a number !"); 
            return false;
        }
        catch (IOException e) 
        {
            System.out.println("Input ERROR. Unknown lenght format. ");
            return false;
        }

        System.out.println("Put category of our event : ");
        try
        {
            String putsCategory = input.readLine();
            rec.category = putsCategory;
            if(putsCategory.compareTo("c") == 0)
            {    
                System.out.println("Ok. Exit to main menu. ");
                return false;
            }
        }
        catch(IOException e)
        {
            System.out.println("Input ERROR. Unknown category format. ");
                return false;
        }   
        
        System.out.println("Put your activity : ");
        try
        {
            String putsActivity = input.readLine();
            rec.activity = putsActivity;
            if(putsActivity.compareTo("c") == 0)
            {    
                System.out.println("Ok. Exit to main menu. ");
                return false;
            }
        }
        catch(IOException e)
        {
            System.out.println("Input ERROR. Unknown activity format. ");
                return false;
        }
        
        myMap.put(date, rec);
        modifiedCalendar = true;
        System.out.println("Your event added ;) ");

        return true;
    }
    
    public int deleteMap( Filtr filtr )
    {
        int count = 0;
        Vector<Date> deleteVector = new Vector<Date>();
     
        for (Event value : myMap.values() ) 
        {
            if( filtr.doFiltr(value) )
            {
                deleteVector.add(value.date);
                count++;
            }
        }
        for( Date date : deleteVector )
            myMap.remove( date );
        if( count != 0 )
        {
            modifiedCalendar = true;
            if( count == 1 )
                System.out.println("Your event deleted! ");
            else System.out.println("Your events deleted! ");
        }
        return count;
    }
    
    public boolean deleteByDate()
    {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Put date and time of deleting event (dd/mm/yyyy hh:mm) : ");
        try
        {
            String puts = input.readLine();
            try
            {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                Date date = simpleDateFormat.parse(puts);
                int res = deleteMap(new DateFiltr( date ) );
                if(res == 0)
                    System.out.println("You don't have any events in this time. ");
            }
            catch(ParseException e)
             {
                 System.out.println("Input ERROR. Unknown date format. ");
                     return false;
             }
        }
        catch(IOException e)
        {
            System.out.println("Input ERROR. Unknown date format. ");
                return false;
        }
        return true;
    }
    
    public boolean searchByCategory()
    {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Put category of searching event : ");
        try
        {
            String puts = input.readLine();
            int res = printMap(new CategoryFiltr( puts ) );
            if(res == 0)
                System.out.println("You have not got any events in this category. ");
        }
        catch(IOException e)
        {
            System.out.println("Input ERROR. Unknown putting category. ");
            return false;
        }
        return true;
    }
    
    public boolean saveFile()
    {
        try 
        {
            File file = new File("timetable.txt");
            FileWriter fw = new FileWriter( file.getAbsoluteFile() );
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            BufferedWriter bw = new BufferedWriter (fw);

            for (Event value : myMap.values()) 
            {
                String res = simpleDateFormat.format(value.date);

                bw.write(value.category + "|" 
                    + res + "|" 
                    + value.len + "|" 
                    + value.activity + "\n"); 
            }
            bw.close();
            System.out.println("Your calendar saved! ");
            modifiedCalendar = false;
            return true;
        } 
        catch (IOException e) 
        {
            System.out.println("Can't save calendar. :( ");
            return false;
        }
    }
    
    public boolean checkChanges()
    {
        if(modifiedCalendar == true)
        {
            System.out.println("You have changes in calendar . Do you want to save it? ");
            System.out.println("Press 1 to save. ");
            System.out.println("Press 2 to discard changes. ");
            int countMenu = (-1);
            while(countMenu != 0)
            {
                System.out.println("Put your number: ");

                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                try
                {
                    String puts = input.readLine();
                    if( puts.compareTo("1") == 0 )
                    {   
                        saveFile();
                        countMenu  = 0;
                        return false;
                    }
                    else if( puts.compareTo("2") == 0)
                    {
                        countMenu  = 0;
                        return false;
                    }
                    else 
                    { 
                        System.out.println("Try again ;) ");
                        countMenu  = (-1);
                    } 
                }
                catch  (IOException e)
                {
                    System.out.println("Input ERROR. Unknown putting number. ");
                    return false;
                }
            }
        }
        return false;
    }
    
    public boolean searchByDate()
    {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Put date and time of searching event (dd/mm/yyyy hh:mm) : ");
        try
        {
            String puts = input.readLine();
            try
            {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                Date date = simpleDateFormat.parse(puts);
                int res = printMap(new DateFiltr(  date  ) );
                if(res == 0)
                    System.out.println("You don't have any events in this time. ");
            }
            catch(ParseException e)
             {
                 System.out.println("Input ERROR. Unknown date format. ");
                     return false;
             }
        }
        catch(IOException e)
        {
            System.out.println("Input ERROR. Unknown date format. ");
                return false;
        }
        return true;
    }
    
    public int printMap( Filtr filtr )
    {
        int count = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
     
        for (Event value : myMap.values()) 
        {
            if( filtr.doFiltr(value) )
            {
                if(count == 0)
                    System.out.println("Your events: ");
            
                System.out.println(simpleDateFormat.format(value.date) + " - " 
                    + value.len + " min. -> " 
                    + value.category + " -> " + value.activity);
                count++;
            }
        }
        return count;
    }
}
