package com.alexhzr.billtastic.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alexhzr.billtastic.R;
import com.alexhzr.billtastic.httpRequest.ApiClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


public class Login extends Activity {
    EditText username;
    EditText password;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);
        context = this.getApplicationContext();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void login(View v) {
        RequestParams params = new RequestParams();
        params.put("username", username.getText().toString());
        params.put("password", password.getText().toString());
        ApiClient.post("login", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.v("Login", String.valueOf(statusCode));
                    if (response.getInt("SERVER_RESPONSE") == 1) {
                        Toast.makeText(context, response.getString("SERVER_MESSAGE"), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(context, MainActivity.class);
                        startActivity(i);
                        //overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
                        finish();
                    } else if (response.getInt("SERVER_RESPONSE") == 0)
                        Toast.makeText(context, response.getString("SERVER_MESSAGE"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
