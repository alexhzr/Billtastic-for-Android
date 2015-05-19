package com.alexhzr.billtastic.httpRequest;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

/**
 * Created by Álex on 18/05/2015.
 */
public class ApiClient {
    private static final String BASE_URL = "http://192.168.1.2:3000/";
    PersistentCookieStore cookieStore;

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
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
}
