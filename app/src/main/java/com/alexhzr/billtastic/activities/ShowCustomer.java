package com.alexhzr.billtastic.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.alexhzr.billtastic.HTTPRequest.AsyncClient;
import com.alexhzr.billtastic.HTTPRequest.mJsonHttpResponseHandler;
import com.alexhzr.billtastic.R;
import com.alexhzr.billtastic.models.Customer;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class ShowCustomer extends ActionBarActivity {
    private Context context;
    private TextView name;
    private TextView id;
    private TextView address;
    private TextView phone;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_customer);
        context = this;
        name = (TextView) findViewById(R.id.showCustomer_name);
        id = (TextView) findViewById(R.id.showCustomer_id);
        address = (TextView) findViewById(R.id.showCustomer_address);
        phone = (TextView) findViewById(R.id.showCustomer_phone);
        email = (TextView) findViewById(R.id.showCustomer_email);
        loadCustomer(getIntent().getExtras().getString("_id"));
    }

    private void loadCustomer(String _id) {
        AsyncClient.get("/api/customer/get/" + _id, null, new mJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.has(context.getString(R.string.server_response))) {
                        if (response.getInt(context.getString(R.string.server_response)) == 2)
                            AsyncClient.redirectToLogin(context);
                        else if (response.getInt(context.getString(R.string.server_response)) == 0)
                            finish();
                    } else {
                        Customer customer = new Customer(response);
                        name.setText(customer.getName());
                        id.setText(customer.getId_number());
                        address.setText(customer.getAddress());
                        phone.setText(String.valueOf(customer.getTelephone()));
                        email.setText(customer.getEmail());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_client, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
