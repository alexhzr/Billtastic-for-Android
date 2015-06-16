package com.alexhzr.billtastic.activities;

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

import com.alexhzr.billtastic.R;
import com.alexhzr.billtastic.fragments.CustomerList;
import com.alexhzr.billtastic.fragments.OrderListContainer;
import com.alexhzr.billtastic.fragments.ProductList;
import com.alexhzr.billtastic.navigationDrawer.DrawerItem;
import com.alexhzr.billtastic.navigationDrawer.NavigationDrawerAdapter;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {
    private Toolbar toolbar;

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
        PRODUCT_LIST,
        PURCHASE_LIST
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
                Toast.makeText(this, "My purchases", Toast.LENGTH_SHORT).show();
                break;

            case 3:
                fragment = new ProductList();
                actualFragment = FragmentList.PRODUCT_LIST;
                break;

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


