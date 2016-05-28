package organizer;

import java.io.*;
import java.text.SimpleDateFormat; 
import java.util.Calendar;

public class Organizer 
{
    static OrganizerLib app = new OrganizerLib();
        
    public static void showTime()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Calendar cal = Calendar.getInstance();

        System.out.println();
        System.out.println("Hello! This is your new organizer. ");
        System.out.print("Todaj is ");
        System.out.print(simpleDateFormat.format( cal.getTime() ) );
        System.out.println(". ");
    }
    
    public static boolean printMenu()
    {
        try 
        {
            File file = new File("menu.txt");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) 
            {
                System.out.println(line);
            }
            fileReader.close();
            return true;
        } 
        catch (IOException e) 
        {
            System.out.println("ERROR. Can't load menu. ");
            return false;
        }
    }
    
    public static boolean doFunction( String puts )
    {
        if( puts.compareTo("1") == 0 || puts.compareTo("c") == 0 )
            printMenu();
        else if( puts.compareTo("2") == 0 )
            app.searchNowDate();
        else if( puts.compareTo("3") == 0 )
            app.addEvent();
        else if( puts.compareTo("4") == 0 )
            app.searchByDate();
        else if( puts.compareTo("5") == 0 )
            app.deleteByDate();
        else if( puts.compareTo("6") == 0 )
            app.searchByCategory();
        else if( puts.compareTo("7") == 0 )
        {
                app.saveFile();
        }
        else if( puts.compareTo("8") == 0 )
            app.printMap( new AllFiltr() ); 
        else if (puts.compareTo("0") == 0 ) 
        {
            app.checkChanges();
            return false;
        } 
        else 
        {
            System.out.println("Try again ;) ");
        }
        return true;
    }
    
    public static boolean putNumber()
    {
        String countMenu = "-1";
        while(countMenu.compareTo("0") != 0)
        {
            System.out.println("Put your number: ");
            
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            try 
            {
                countMenu = input.readLine();
                doFunction( countMenu );
            }
            catch  (IOException e)
            {
                System.out.println("Input ERROR. Unknown putting number. ");
                return false;
            }
        }
        return true;
    }
    
    public static void main(String[] args) 
    {
        app.loadCalendarMap();
        showTime();
        app.workWithOutdated();
        printMenu();
        putNumber();         
    }
}
