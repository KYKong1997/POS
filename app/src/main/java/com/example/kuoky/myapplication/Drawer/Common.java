package com.example.kuoky.myapplication.Drawer;

import android.widget.DatePicker;

import com.kinvey.android.Client;
import com.kinvey.android.model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Common
{
    public static String currentToken="";

    private static String baseURL="https://fcm.googleapis.com/";

    public static User user=null;

    public static Client client=null;
    public static int notification=0;

    public static long parseKinveyLastModifiedTimestampToMillis(String kinveyLastModifiedTimestampString) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.ENGLISH);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            java.util.Date date = simpleDateFormat.parse(kinveyLastModifiedTimestampString);
            return date.getTime();
        } catch (Exception e) {

            return -1;
        }
    }
    public static String parseMillisToKinveyLastModifiedTimestamp(long timeMillis){

        Date date = new Date(timeMillis);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        return formatter.format(date);
    }
    public static long getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime().getTime();
    }

}
