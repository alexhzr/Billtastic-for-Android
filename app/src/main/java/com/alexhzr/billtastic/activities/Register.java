package com.alexhzr.billtastic.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alexhzr.billtastic.HTTPRequest.AsyncClient;
import com.alexhzr.billtastic.HTTPRequest.mJsonHttpResponseHandler;
import com.alexhzr.billtastic.R;
import com.alexhzr.billtastic.util.Validator;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Register extends ActionBarActivity {
    private EditText username;
    private EditText password;
    private EditText passwordCheck;
    private Context context;
    private ArrayList<EditText> components;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = this;
        username = (EditText) findViewById(R.id.register_username);
        password = (EditText) findViewById(R.id.register_password);
        passwordCheck = (EditText) findViewById(R.id.register_password_check);

        components = new ArrayList<>();
        components.add(username);
        components.add(password);
        components.add(passwordCheck);
    }


    public void register(View v) {
        if (Validator.areNotEmpty(components, context) && password.getText().toString().equals(passwordCheck.getText().toString())) {
            checkUsername(v);
        }
    }

    private void checkUsername(final View v) {
        String query = username.getText().toString();
        AsyncClient.get("/register/check_username/" + query, null, new mJsonHttpResponseHandler(context, v) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt(context.getString(R.string.server_response)) == 4) {
                        signIn(v);
                    } else if (response.getInt(context.getString(R.string.server_response)) == 3)
                        Toast.makeText(context, R.string.s_register_username_used, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void signIn(View v) {
        RequestParams params = new RequestParams();
        params.put("username", username.getText().toString());
        params.put("password", password.getText().toString());
        AsyncClient.post("/register", params, new mJsonHttpResponseHandler(this, v) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt(context.getString(R.string.server_response)) == 1) {
                        Toast.makeText(context, R.string.s_register_succeed, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(context, MainActivity.class));
                    } else if (response.getInt(context.getString(R.string.server_response)) == 0) {
                        Toast.makeText(context, R.string.s_register_failure, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
