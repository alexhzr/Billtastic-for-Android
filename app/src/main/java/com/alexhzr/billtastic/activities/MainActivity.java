package com.alexhzr.billtastic.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alexhzr.billtastic.R;
import com.alexhzr.billtastic.fragments.CustomerList;
import com.alexhzr.billtastic.fragments.OrderList;
import com.alexhzr.billtastic.navigationDrawer.DrawerItem;
import com.alexhzr.billtastic.navigationDrawer.NavigationDrawerAdapter;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {
    TextView tv;

    String[] tagTitles;
    String[] icons;
    DrawerLayout dwLayout;
    ListView dwList;

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
        initiateDrawer();
        populateDrawer();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private void initiateDrawer() {
        tagTitles = getResources().getStringArray(R.array.item_names);
        icons = getResources().getStringArray(R.array.item_icons);
        dwLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        dwList = (ListView) findViewById(R.id.left_drawer);
        dwList.setOnItemClickListener(new DrawerItemClickListener());
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


