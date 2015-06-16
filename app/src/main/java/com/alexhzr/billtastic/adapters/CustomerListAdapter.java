package com.alexhzr.billtastic.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alexhzr.billtastic.R;
import com.alexhzr.billtastic.models.Customer;
import com.alexhzr.billtastic.util.FontHandler;

import java.util.ArrayList;

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.ViewHolder> {
    private ArrayList<Customer> data;
    private Context context;

    public CustomerListAdapter(ArrayList<Customer> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public CustomerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_customer_list, parent, false);

        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.name.setText(data.get(position).getName());
        FontHandler.setFontToButton(context, holder.phone, context.getString(R.string.icon_phone));
        FontHandler.setFontToButton(context, holder.email, context.getString(R.string.icon_email));
        FontHandler.setFontToButton(context, holder.fav, context.getString(R.string.icon_favorite_customers));

        holder.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "tel:" + String.valueOf(data.get(position).getTelephone()).trim();
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse(uri));
                context.startActivity(i);
            }
        });

        holder.email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SENDTO);
                i.setData(Uri.parse("mailto:" + data.get(position).getEmail()));
                i.putExtra(Intent.EXTRA_TEXT, "\n \n \nSent via Billtastic - http://billtasticjs-alexhzr.rhcloud.com");
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public Button phone;
        public Button email;
        public Button fav;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            fav = (Button) itemLayoutView.findViewById(R.id.button_favourite_customer);
            name = (TextView) itemLayoutView.findViewById(R.id.detail_customerlist_name);
            phone = (Button) itemLayoutView.findViewById(R.id.detail_customerlist_phone);
            email = (Button) itemLayoutView.findViewById(R.id.detail_customerlist_email);
        }
    }
}
