package com.alexhzr.billtastic.models;

import com.alexhzr.billtastic.util.DateController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Order {
    String _id;
    Date order_date;
    String customer;
    double total;
    State state;
    double pending;
    int printed;
    int sent;

    private enum State { DRAFT, PAID, PENDING }

    public Order(JSONObject json) {
        try {
            this._id = json.getString("_id");
            this.order_date = DateController.StringToDate(json.getString("order_date"));
            this.customer = json.getString("customer");
            this.total = json.getDouble("total");
            int state = json.getInt("state");
            switch (state) {
                case 0:
                    this.state = State.DRAFT;
                    break;
                case 1:
                    this.state = State.PAID;
                    break;
                case 2:
                    this.state = State.PENDING;
                    break;
            }
            this.pending = json.getDouble("pending");
            this.printed = json.getInt("printed");
            this.sent = json.getInt("sent");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
