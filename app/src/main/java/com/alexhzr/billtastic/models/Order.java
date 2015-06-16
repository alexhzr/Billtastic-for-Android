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
    boolean printed;
    boolean sent;

    public enum State { DRAFT, PAID, PENDING }

    public Order(JSONObject json) {
        try {
            this._id = json.getString("_id");
            this.order_date = DateController.stringToDate(json.getString("order_date"));
            JSONObject customer = new JSONObject();
            customer = json.getJSONObject("customer");
            this.customer = customer.getString("name");
            this.total = json.getDouble("total");
            int state = json.getInt("status");
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
            this.printed = json.getBoolean("printed");
            this.sent = json.getBoolean("sent");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String get_id() {
        return _id;
    }

    public Date getOrder_date() {
        return order_date;
    }

    public String getCustomer() {
        return customer;
    }

    public double getTotal() {
        return total;
    }

    public State getState() {
        return state;
    }

    public double getPending() {
        return pending;
    }

    public boolean isSent() {
        return sent;
    }

    public boolean isPrinted() {
        return printed;
    }
}
