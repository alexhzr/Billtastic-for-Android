package com.alexhzr.billtastic.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Order {
    String _id;
    Date order_date;
    String customer;
    float total;
    State state;
    float pending;
    int printed;
    int sent;

    private static enum State { DRAFT, PAID, PENDING }

    public Order(JSONObject json) {
        try {
            this._id = json.getString("_id");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
