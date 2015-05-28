package com.alexhzr.billtastic.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alexhzr.billtastic.R;
import com.alexhzr.billtastic.fragments.CustomerList;
import com.alexhzr.billtastic.fragments.OrderList;
import com.alexhzr.billtastic.httpRequest.ApiClient;
import com.alexhzr.billtastic.navigationDrawer.DrawerItem;
import com.alexhzr.billtastic.navigationDrawer.NavigationDrawerAdapter;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {
    TextView tv;
    Toolbar toolbar;

    String[] tagTitles;
    String[] icons;
    DrawerLayout dwLayout;
    ListView dwList;
    ActionBarDrawerToggle dwToggle;

    private static enum FragmentList {
        none,
        CUSTOMER_LIST,
        ORDER_LIST
    }

    public static FragmentList actualFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initializeDrawer();
        populateDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public void test(View v) {
        ApiClient.get("", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.v("mio", "guay");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.v("mio1", String.valueOf(statusCode));
                if (throwable.getCause() instanceof ConnectTimeoutException) {
                    Log.v("mio1", "timeout");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.v("mio2", String.valueOf(statusCode));
                if (throwable.getCause() instanceof ConnectTimeoutException) {
                    Log.v("mio2", "timeout");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.v("mio3", String.valueOf(statusCode));
                if (throwable.getCause() instanceof ConnectTimeoutException) {
                    Log.v("mio3", "timeout");
                }
            }
        });
    }

    public void test2(View v) {
        ApiClient.get("zxczxc", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.v("mio", "guay");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.v("mio1", String.valueOf(statusCode));
                if ( throwable.getCause() instanceof ConnectTimeoutException) {
                    Log.v("mio1", "timeout");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.v("mio2", String.valueOf(statusCode));
                if ( throwable.getCause() instanceof ConnectTimeoutException) {
                    Log.v("mio2", "timeout");
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (dwToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dwToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        //dwToggle.syncState();
    }

    private void initializeDrawer() {
        tagTitles = getResources().getStringArray(R.array.item_names);
        icons = getResources().getStringArray(R.array.item_icons);
        dwLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        dwList = (ListView) findViewById(R.id.drawer_list);
        dwList.setOnItemClickListener(new DrawerItemClickListener());
        dwToggle = new ActionBarDrawerToggle(this, dwLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle("pepe");
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("pop");
                invalidateOptionsMenu();
            }
        };
        dwLayout.setDrawerListener(dwToggle);
        dwLayout.post(new Runnable() {
            @Override
            public void run() {
                dwToggle.syncState();
            }
        });
    }

    private void populateDrawer() {
        ArrayList<DrawerItem> dwItems = new ArrayList<>();
        for (int i = 0; i < tagTitles.length; i++) {
            dwItems.add(new DrawerItem(tagTitles[i], Resources.getSystem().getIdentifier("abc_ic_menu_copy_mtrl_am_alpha.png", "drawable", getBaseContext().getPackageResourcePath())));
        }

        dwList.setAdapter(new NavigationDrawerAdapter(this, dwItems));
    }

    private void selectItem(int position) {
        Fragment fragment = null;
        Intent i;
        switch (position) {
            case 0:
                fragment = new CustomerList();
                actualFragment = FragmentList.CUSTOMER_LIST;
                break;

            case 1:
                fragment = new OrderList();
                actualFragment = FragmentList.ORDER_LIST;
                break;

        }

        if (position != 5 && position != 4) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
            dwList.setItemChecked(position, true);
            //setTituloActividad(titulos[position]);
            dwLayout.closeDrawer(dwList);
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
}


