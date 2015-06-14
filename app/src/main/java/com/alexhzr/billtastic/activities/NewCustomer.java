package com.alexhzr.billtastic.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.alexhzr.billtastic.R;
import com.alexhzr.billtastic.httpRequest.AsyncClient;
import com.alexhzr.billtastic.httpRequest.mJsonHttpResponseHandler;
import com.alexhzr.billtastic.util.Validator;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewCustomer extends ActionBarActivity {
    private Toolbar toolbar;
    private Context context;

    private EditText id;
    private EditText name;
    private EditText address;
    private EditText email;
    private EditText phone;

    private ArrayList<EditText> components;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);

        context = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        id = (EditText) findViewById(R.id.newcustomer_ID);
        name = (EditText) findViewById(R.id.newcustomer_name);
        address = (EditText) findViewById(R.id.newcustomer_address);
        email = (EditText) findViewById(R.id.newcustomer_email);
        phone = (EditText) findViewById(R.id.newcustomer_phone);

        components = new ArrayList<>();
        components.add(id);
        components.add(name);
        components.add(address);
        components.add(email);
        components.add(phone);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_customer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_newclient_submit) {
            existsIdNumber();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void existsIdNumber() {
        if (Validator.areNotEmpty(components, context)) {
            AsyncClient.get("/api/customer/check_id/" + id.getText().toString(), null, new mJsonHttpResponseHandler(this) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        if (response.getInt(context.getString(R.string.server_response)) == 2)
                            AsyncClient.redirectToLogin(context);
                        else if (response.getInt(context.getString(R.string.server_response)) == 3) {
                            id.setBackgroundColor(Color.RED);
                            id.requestFocus();
                            Toast.makeText(context, R.string.e_newcustomer_id_used, Toast.LENGTH_SHORT).show();
                        } else if (response.getInt(context.getString(R.string.server_response)) == 4)
                            newCustomer();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void newCustomer() {
        RequestParams params = new RequestParams();
        params.put("id_number", id.getText().toString());
        params.put("name", name.getText().toString());
        params.put("phone", phone.getText().toString());
        params.put("email", email.getText().toString());
        params.put("address", address.getText().toString());
        AsyncClient.post("/api/customer", params, new mJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getInt(context.getString(R.string.server_response)) == 1) {
                        Toast.makeText(name.getContext(), R.string.newcustomer_succeed, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
