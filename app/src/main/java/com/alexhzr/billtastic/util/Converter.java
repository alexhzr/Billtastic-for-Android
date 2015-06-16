package com.alexhzr.billtastic.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

public class Converter {
    public static String doubleToMoney(double value) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(Converter.round(value, 2));
    }

    public static String doubleToString(double value) {
        return String.format(Locale.US, "%.2f", Converter.round(value, 2));
    }

    public static double stringToDouble(String value){
        double d;
        try{
            d = Double.valueOf(value);
        } catch (NumberFormatException e){
            d = 0;
        }
        return d;
    }

    public static double round(double value, int decimals) {
        if (decimals > -1) {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(decimals, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }
        return value;
    }
}
