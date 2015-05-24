package com.alexhzr.billtastic.navigationDrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexhzr.billtastic.R;
import java.util.List;

public class NavigationDrawerAdapter extends ArrayAdapter {
    public NavigationDrawerAdapter(Context context, List objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)parent.getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.drawer_item, null);
        }


        ImageView icon = (ImageView) convertView.findViewById(R.id.drawer_item_icon);
        TextView name = (TextView) convertView.findViewById(R.id.drawer_item_name);

        DrawerItem item = (DrawerItem) getItem(position);
        icon.setImageResource(item.getIconId());
        name.setText(item.getName());

        return convertView;
    }
}
