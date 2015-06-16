package com.alexhzr.billtastic.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Product {
    String _id;
    String reference;
    String description;
    double purchase_price;
    double sell_price;
    double tax_price;
    String tax;

    public Product(JSONObject json) {
        try {
            this._id = json.getString("_id");
            this.reference = json.getString("reference");
            this.description = json.getString("description");
            this.sell_price = json.getDouble("sell_price");
            this.purchase_price = json.getDouble("purchase_price");
            this.tax_price = json.getDouble("tax_price");
            this.tax = json.getString("tax");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String get_id() {
        return _id;
    }

    public String getReference() {
        return reference;
    }

    public String getDescription() {
        return description;
    }

    public double getPurchase_price() {
        return purchase_price;
    }

    public double getSell_price() {
        return sell_price;
    }

    public double getTax_price() {
        return tax_price;
    }

    public String getTax() {
        return tax;
    }
}
