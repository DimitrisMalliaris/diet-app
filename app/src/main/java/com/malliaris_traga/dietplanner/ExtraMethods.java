package com.malliaris_traga.dietplanner;

import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ExtraMethods {
    static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static Calendar ConvertDateFromString(String dateString){
        try{
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(dateString));
            return calendar;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static int YearsBetweenDates(String dateFrom, String dateTo){
        Calendar calendarFrom = ConvertDateFromString(dateFrom);
        Calendar calendarTo = ConvertDateFromString(dateTo);

        return (int)calendarTo.get(Calendar.YEAR) - (int)calendarFrom.get(Calendar.YEAR);
    }

    public static String GetCurrentDate(){
        return sdf.format(Calendar.getInstance().getTime());
    }

    public static String GetDateStringFromDatePicker(DatePicker datePicker){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //TO MOVE!!!!

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return sdf.format(calendar.getTime());
    } //!!! TO MOVE
}
