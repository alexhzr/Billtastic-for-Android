package com.alexhzr.billtastic.util;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

public class FontController {
    public static class FontCache {

        private static Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();

        public static Typeface get(String name, Context context) {
            Typeface tf = fontCache.get(name);
            if(tf == null) {
                try {
                    tf = Typeface.createFromAsset(context.getAssets(), name);
                }
                catch (Exception e) {
                    return null;
                }
                fontCache.put(name, tf);
            }
            return tf;
        }
    }
}
