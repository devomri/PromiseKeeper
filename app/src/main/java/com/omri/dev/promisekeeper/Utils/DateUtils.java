package com.omri.dev.promisekeeper.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by omri on 8/31/17.
 */

public class DateUtils {
    public static Date convertStringToDate(String dateString) {
        Date date;

        try {
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
            date = format.parse(dateString);
        } catch (ParseException e) {
            date = new Date();
        }

        return date;
    }

    public static String convertDateToString(Date date) {
        String dateString;

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
        dateString = df.format(date);

        return dateString;
    }

    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }
}
