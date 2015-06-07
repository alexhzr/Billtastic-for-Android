package com.alexhzr.billtastic.util;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import com.alexhzr.billtastic.R;

import java.util.Hashtable;

public class FontController {
    private static Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();

    private FontController() {}

    public static Typeface getFont(String name, Context context) {
        Typeface tf = fontCache.get(name);
        if (tf == null) {
            try {
                tf = Typeface.createFromAsset(context.getAssets(), name);
            } catch (Exception e) {
                return null;
            }
            fontCache.put(name, tf);
        }
        return tf;
    }

    public static void setFontToTextView(Context ctx, TextView tv, String text) {
            Typeface t = FontController.getFont(ctx.getString(R.string.fontawesome), ctx);
            tv.setTypeface(t);
            tv.setText(text);
    }
}
