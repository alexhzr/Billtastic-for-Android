package com.alexhzr.billtastic.navigationDrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.alexhzr.billtastic.R;
import com.alexhzr.billtastic.util.FontController;

import java.util.List;

public class NavigationDrawerAdapter extends ArrayAdapter {
    Context context;

    public NavigationDrawerAdapter(Context context, List objects) {
        super(context, 0, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater)parent.getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.drawer_item, null);
            vh = new ViewHolder();
            vh.icon = (TextView) convertView.findViewById(R.id.dw_icon);
            vh.name = (TextView) convertView.findViewById(R.id.dw_name);
            convertView.setTag(vh);
        } else vh = (ViewHolder) convertView.getTag();

        DrawerItem item = (DrawerItem) getItem(position);
        FontController.setFontToTextView(context, vh.icon, item.getIcon());
        vh.name.setText(item.getName());
        return convertView;
    }

    public static class ViewHolder {
        public TextView icon;
        public TextView name;
    }
}
