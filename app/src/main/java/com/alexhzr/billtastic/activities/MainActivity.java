package com.alexhzr.billtastic.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alexhzr.billtastic.HTTPRequest.AsyncClient;
import com.alexhzr.billtastic.HTTPRequest.mJsonHttpResponseHandler;
import com.alexhzr.billtastic.R;
import com.alexhzr.billtastic.fragments.CustomerList;
import com.alexhzr.billtastic.fragments.OrderListContainer;
import com.alexhzr.billtastic.fragments.ProductList;
import com.alexhzr.billtastic.navigationDrawer.DrawerItem;
import com.alexhzr.billtastic.navigationDrawer.NavigationDrawerAdapter;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private Context context;

    private String[] tagTitles;
    private String[] icons;
    private DrawerLayout dwLayout;
    private ListView dwList;
    private ActionBarDrawerToggle dwToggle;

    private String activity_title;

    private static enum FragmentList {
        none,
        CUSTOMER_LIST,
        ORDER_LIST,
        PRODUCT_LIST
    }

    public static FragmentList actualFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initializeDrawer();
        populateDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        selectItem(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean isDrawerOpened = dwLayout.isDrawerOpen(dwList);
        
        if(isDrawerOpened) {
            menu.findItem(R.id.action_newclient).setVisible(false);
            menu.findItem(R.id.action_new_order).setVisible(false);
            menu.findItem(R.id.action_newproduct).setVisible(false);
        } else {
            if (actualFragment == FragmentList.CUSTOMER_LIST) {
                menu.findItem(R.id.action_newclient).setVisible(true);
            } else {
                menu.findItem(R.id.action_newclient).setVisible(false);
            }
            if (actualFragment == FragmentList.ORDER_LIST) {
                menu.findItem(R.id.action_new_order).setVisible(true);
            } else {
                menu.findItem(R.id.action_new_order).setVisible(false);
            }
            if (actualFragment == FragmentList.PRODUCT_LIST)
                menu.findItem(R.id.action_newproduct).setVisible(true);
            else menu.findItem(R.id.action_newproduct).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (dwToggle.onOptionsItemSelected(item)) {
            return true;
        } else if (id == R.id.action_newclient) {
            startActivity(new Intent(this, NewCustomer.class));
            return true;
        } else if (id == R.id.action_newproduct) {
            startActivity(new Intent(this, NewProduct.class));
            return true;
        } else if (id == R.id.action_new_order) {
            startActivity(new Intent(this, NewOrder.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dwToggle.onConfigurationChanged(newConfig);
    }

    private void initializeDrawer() {
        dwLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        dwList = (ListView) findViewById(R.id.drawer_list);
        dwList.setOnItemClickListener(new DrawerItemClickListener());
        dwToggle = new ActionBarDrawerToggle(this, dwLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.nd_dw_open);
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
        tagTitles = getResources().getStringArray(R.array.item_names);
        icons = getResources().getStringArray(R.array.item_icons);
        ArrayList<DrawerItem> dwItems = new ArrayList<>();
        for (int i = 0; i < tagTitles.length; i++) {
            dwItems.add(new DrawerItem(tagTitles[i], icons[i]));
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
                fragment = new OrderListContainer();
                Bundle bundle = new Bundle();
                bundle.putBoolean("todas", true);
                bundle.putString("query", "");
                fragment.setArguments(bundle);
                actualFragment = FragmentList.ORDER_LIST;
                break;

            case 2:
                fragment = new ProductList();
                actualFragment = FragmentList.PRODUCT_LIST;
                break;

            case 3:
                AsyncClient.get("/logout", null, new mJsonHttpResponseHandler(this) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            if (response.getInt(context.getString(R.string.server_response)) == 1) {
                                Toast.makeText(context, R.string.s_logged_out, Toast.LENGTH_SHORT).show();
                                AsyncClient.redirectToLogin(context);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;

            case 4:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.cb_remove_account_title);
                builder.setMessage(R.string.cb_remove_account_body)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AsyncClient.delete("/my_profile", null, context, new mJsonHttpResponseHandler(context) {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        super.onSuccess(statusCode, headers, response);
                                        try {
                                            if (response.getInt(context.getString(R.string.server_response)) == 1) {
                                                Toast.makeText(context, R.string.account_removed, Toast.LENGTH_SHORT).show();
                                                AsyncClient.redirectToLogin(context);
                                            } else AsyncClient.redirectToLogin(context);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).show();

        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
            dwList.setItemChecked(position, true);
            getSupportActionBar().setTitle(tagTitles[position]);
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


