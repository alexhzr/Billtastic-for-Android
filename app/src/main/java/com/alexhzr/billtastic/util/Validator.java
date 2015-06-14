package com.alexhzr.billtastic.util;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.alexhzr.billtastic.R;

import java.util.ArrayList;

public class Validator {

    public static boolean areNotEmpty(ArrayList<EditText> components, Context ctx) {
        for (EditText et : components) {
            if (et.getText().toString().equals("")) {
                et.requestFocus();
                Toast.makeText(ctx, R.string.e_cant_be_empty, Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }
}
