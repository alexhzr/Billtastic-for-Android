package com.alexhzr.billtastic.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alex on 6/1/15.
 */
public class Product {
    String name;
    float price;
    String tax;

    //TODO: añadir más campos al producto (precio venta, descripción, precio compra, etc)

    public Product(JSONObject json) {
        try {
            this.name = json.getString("name");
            this.price = Float.parseFloat(json.getString("price"));
            this.tax = json.getString("tax");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public String getTax() {
        return tax;
    }
}
