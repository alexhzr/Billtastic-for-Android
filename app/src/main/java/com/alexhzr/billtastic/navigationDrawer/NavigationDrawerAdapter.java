package com.alexhzr.billtastic.navigationDrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.alexhzr.billtastic.R;

import java.util.List;

public class NavigationDrawerAdapter extends ArrayAdapter {
    Context context;

    public NavigationDrawerAdapter(Context context, List objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)parent.getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.drawer_item, null);
            vh = new ViewHolder();
            vh.icon = (TextView) convertView.findViewById(R.id.dw_icon);
            vh.name = (TextView) convertView.findViewById(R.id.dw_name);
        } else vh = (ViewHolder) convertView.getTag();

        DrawerItem item = (DrawerItem) getItem(position);
        name.setText(item.getName());
        return convertView;
    }

    private void makeAwesome(Context context, View v, int resId, String text) {

    }

    public static class ViewHolder {
        public TextView icon;
        public TextView name;
    }
}
