package com.alexhzr.billtastic.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.alexhzr.billtastic.R;
import com.alexhzr.billtastic.httpRequest.ApiClient;
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

        name = (EditText) findViewById(R.id.newcustomer_name);
        address = (EditText) findViewById(R.id.newcustomer_address);
        email = (EditText) findViewById(R.id.newcustomer_email);
        phone = (EditText) findViewById(R.id.newcustomer_phone);

        components = new ArrayList<>();
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
            newCustomer();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void newCustomer() {
        if (Validator.checkIfEmpty(components, context)) {
            RequestParams params = new RequestParams();
            params.put("name", name.getText().toString());
            params.put("phone", phone.getText().toString());
            params.put("email", email.getText().toString());
            params.put("address", address.getText().toString());
            ApiClient.post("api/customer", params, new mJsonHttpResponseHandler(this) {
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
}
