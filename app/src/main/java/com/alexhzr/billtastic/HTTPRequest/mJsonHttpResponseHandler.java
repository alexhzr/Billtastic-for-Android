package com.alexhzr.billtastic.httpRequest;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.alexhzr.billtastic.R;
import com.alexhzr.billtastic.activities.Login;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class mJsonHttpResponseHandler extends JsonHttpResponseHandler {
    private Context context;

    public mJsonHttpResponseHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);
        try {
            if (response.getInt(context.getString(R.string.server_response)) == 2) {
                context.startActivity(new Intent(context, Login.class));
                Toast.makeText(context, R.string.e_session_expired, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        AsyncClient.doOnFailure(context, statusCode);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        AsyncClient.doOnFailure(context, statusCode);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        AsyncClient.doOnFailure(context, statusCode);
    }
}
