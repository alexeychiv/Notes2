package gb.android.notes2.Utils;

import java.util.Calendar;

public class NoteDate {

    public static String dateToString(int day, int month, int year) {
        String result = String.valueOf(year);

        switch (month) {
            case 0:
                result += "-jan-";
                break;
            case 1:
                result += "-feb-";
                break;
            case 2:
                result += "-mar-";
                break;
            case 3:
                result += "-apr-";
                break;
            case 4:
                result += "-may-";
                break;
            case 5:
                result += "-jun-";
                break;
            case 6:
                result += "-jul-";
                break;
            case 7:
                result += "-aug-";
                break;
            case 8:
                result += "-sep-";
                break;
            case 9:
                result += "-oct-";
                break;
            case 10:
                result += "-nov-";
                break;
            case 11:
                result += "-dec-";
                break;
        }

        result += String.valueOf(day);

        return result;
    }

    public static String getCurrentDateString()
    {
        Calendar c = Calendar.getInstance();
        return dateToString(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH), c.get(Calendar.YEAR));
    }
}
