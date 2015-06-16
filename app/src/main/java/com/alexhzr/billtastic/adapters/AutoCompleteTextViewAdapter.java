package com.alexhzr.billtastic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.alexhzr.billtastic.models.Customer;
import com.alexhzr.billtastic.models.Product;

import java.util.ArrayList;

public class AutoCompleteTextViewAdapter extends ArrayAdapter {

    private int recurso;
    private ArrayList<Object> listaOriginal;
    private Filtro filter;


    private ArrayList<Object> sugerencias;
    private static LayoutInflater inflador;


    public AutoCompleteTextViewAdapter(Context context, int resource, ArrayList<Object> objects) {
        super(context, resource, objects);
        this.filter = new Filtro();
        this.recurso = resource;
        this.listaOriginal = new ArrayList<>(objects);
        this.sugerencias = new ArrayList<>();
        inflador = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflador.inflate(recurso, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (sugerencias.size() > position) {
            if (sugerencias.get(position).getClass().getName().contains("Customer"))
                holder.title.setText(((Customer) sugerencias.get(position)).getName());
            else if (sugerencias.get(position).getClass().getName().contains("Product"))
                holder.title.setText(((Product) sugerencias.get(position)).getReference());
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public static class ViewHolder {
        public TextView title;
    }

    private class Filtro extends Filter {

        @Override
        public String convertResultToString(Object resultValue) {
            if (resultValue.getClass().getName().contains("Customer")) {
                return ((Customer) (resultValue)).getName();
            } else if (resultValue.getClass().getName().contains("Product")) {
                return ((Product) (resultValue)).getReference();
            } else return "";
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Object> lista = new ArrayList<>(listaOriginal);
            if (constraint != null) {
                constraint = constraint.toString().trim();
                sugerencias = new ArrayList<>();
                for (Object object : lista) {
                    if (object.getClass().getName().contains("Customer")) {
                        if (((Customer) object).getName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            sugerencias.add(object);
                        }
                    } else if (object.getClass().getName().contains("Product")) {
                        if (((Product) object).getReference().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            sugerencias.add(object);
                        }
                    }

                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = sugerencias;
                filterResults.count = sugerencias.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Object> listaFinal = (ArrayList<Object>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Object object : listaFinal) {
                    add(object);
                }
                notifyDataSetChanged();
            }
        }
    }
}