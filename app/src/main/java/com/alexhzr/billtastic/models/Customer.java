package com.alexhzr.billtastic.models;

import org.json.JSONException;
import org.json.JSONObject;

// TODO: refactor 'telephone' -> 'phone'
public class Customer {
    String _id;
    String id_number;
    String name;
    int telephone;
    String address;
    String email;

    public Customer(JSONObject json) {
        try {
            this.id_number = json.getString("id_number");
            this._id = json.getString("_id");
            this.name = json.getString("name");
            this.telephone = json.getInt("phone");
            this.address = json.getString("address");
            this.email = json.getString("email");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId_number() {
        return id_number;
    }

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }


    public int getTelephone() {
        return telephone;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }
}
