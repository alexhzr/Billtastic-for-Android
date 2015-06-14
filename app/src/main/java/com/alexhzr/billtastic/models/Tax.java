package com.alexhzr.billtastic.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Tax {
    private String _id;
    private String name;
    private int value;

    public Tax(JSONObject json) {
        try {
            this._id = json.getString("_id");
            this.name = json.getString("name");
            this.value = json.getInt("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
