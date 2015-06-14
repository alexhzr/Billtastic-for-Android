package com.alexhzr.billtastic.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexhzr.billtastic.R;
import com.alexhzr.billtastic.models.Product;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    private ArrayList<Product> data;

    public ProductListAdapter(ArrayList<Product> data) {
        this.data = data;
    }

    @Override
    public ProductListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_product_list, parent, false);

        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.reference.setText(data.get(position).getReference());
        holder.description.setText(data.get(position).getDescription());
        holder.sell_price.setText(String.valueOf(data.get(position).getSell_price()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView reference;
        public TextView description;
        public TextView sell_price;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            reference = (TextView) itemLayoutView.findViewById(R.id.product_reference);
            description = (TextView) itemLayoutView.findViewById(R.id.product_description);
            sell_price = (TextView) itemLayoutView.findViewById(R.id.product_sell_price);
        }
    }
}
