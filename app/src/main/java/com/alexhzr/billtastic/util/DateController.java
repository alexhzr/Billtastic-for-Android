package com.alexhzr.billtastic.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateController {
    public static Date stringToDate(String sDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date date = dateFormat.parse(sDate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String dateToString(Date date) {
        SimpleDateFormat  dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String datetime = dateFormat.format(date);
        return datetime;
    }

    public static String toDisplayDate(int d, int m, int y) {
        String dia = ("0" + d).substring(("0" + d).length() - 2);
        String mes = ("0" + m).substring(("0" + m).length() - 2);
        String anio = "" + y;
        return dia + "/" + mes + "/" + anio;
    }
}
