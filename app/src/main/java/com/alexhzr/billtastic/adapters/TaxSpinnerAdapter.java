package com.alexhzr.billtastic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.alexhzr.billtastic.R;
import com.alexhzr.billtastic.models.Tax;

import java.util.List;

public class TaxSpinnerAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private List<Tax> lista;
    private static LayoutInflater inflater;

    public TaxSpinnerAdapter(Context context, int resource, List<Tax> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.lista = objects;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
            vh = new ViewHolder();
            vh.spinner_label = (TextView) convertView.findViewById(R.id.spinner_label);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.spinner_label.setText(lista.get(position).getName() + " (" + lista.get(position).getValue() + "%)");
        return convertView;
    }

    private static class ViewHolder{
        TextView spinner_label;
    }
}
