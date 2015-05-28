package com.alexhzr.billtastic.httpRequest;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.alexhzr.billtastic.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

public class ApiClient {
    private static final String BASE_URL = "http://192.168.1.102:3000/";
    PersistentCookieStore cookieStore;

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(10000);
        client.get(getAbsoluteUrl(url), params, responseHandler);
        Log.v("ApiClient", getAbsoluteUrl(url));
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
        Log.v("ApiClient", getAbsoluteUrl(url));
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public static void doOnFailure(Context context, int errorCode) {
        switch (errorCode) {
            case 0:
                Toast.makeText(context, R.string.code_0, Toast.LENGTH_SHORT).show();
                break;
            case 404:
                Toast.makeText(context, R.string.code_404, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
