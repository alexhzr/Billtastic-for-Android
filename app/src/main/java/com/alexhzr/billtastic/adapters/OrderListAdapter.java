package com.alexhzr.billtastic.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.alexhzr.billtastic.R;
import com.alexhzr.billtastic.models.Order;
import com.alexhzr.billtastic.util.DateController;
import com.alexhzr.billtastic.util.FontController;

import java.util.ArrayList;

public class OrderListAdapter extends ArrayAdapter<Order> {
    private ArrayList<Order> data;
    private Context context;
    private int resource;
    private static LayoutInflater inflater;

    public OrderListAdapter(ArrayList<Order> data, Context context, int resource) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
        this.resource = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
            vh = new ViewHolder();
            vh.customer = (TextView) convertView.findViewById(R.id.order_detail_customer);
            vh.order_date = (TextView) convertView.findViewById(R.id.order_detail_date);
            vh.total = (TextView) convertView.findViewById(R.id.order_detail_total);
            vh.pending = (TextView) convertView.findViewById(R.id.order_detail_pending);
            vh.ic_printed = (TextView) convertView.findViewById(R.id.order_detail_print_icon);
            vh.ic_sent = (TextView) convertView.findViewById(R.id.order_detail_sent_icon);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.customer.setText(data.get(position).getCustomer());
        vh.order_date.setText(DateController.dateToString(data.get(position).getOrder_date()));
        vh.total.setText(String.valueOf(data.get(position).getTotal()));
        if (data.get(position).getState() == Order.State.PENDING)
            vh.pending.setText(String.valueOf(data.get(position).getPending()));

        if (data.get(position).isPrinted())
            FontController.setFontToTextView(context, vh.ic_printed, context.getString(R.string.icon_printed));

        if (data.get(position).isSent())
            FontController.setFontToTextView(context, vh.ic_sent, context.getString(R.string.icon_sent));

        return convertView;
    }

    public static class ViewHolder {
        public TextView customer;
        public TextView order_date;
        public TextView total;
        public TextView pending;
        public TextView ic_printed;
        public TextView ic_sent;
    }
}