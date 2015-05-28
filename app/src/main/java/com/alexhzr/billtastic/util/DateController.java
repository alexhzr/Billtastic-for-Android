package com.alexhzr.billtastic.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateController {
    public static Date StringToDate(String sDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            Date date = dateFormat.parse(sDate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String DateToString(Date date) {
        SimpleDateFormat  dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String datetime = dateFormat.format(date);
        return datetime;
    }
}
