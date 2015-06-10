package com.alexhzr.billtastic.httpRequest;

import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;


public class mJsonHttpResponseHandler extends JsonHttpResponseHandler {
    private Context context;

    public mJsonHttpResponseHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        ApiClient.doOnFailure(context, statusCode);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        ApiClient.doOnFailure(context, statusCode);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        ApiClient.doOnFailure(context, statusCode);
    }
}
