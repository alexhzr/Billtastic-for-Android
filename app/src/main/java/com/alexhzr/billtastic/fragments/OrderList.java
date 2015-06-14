package com.alexhzr.billtastic.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexhzr.billtastic.R;

public class OrderList extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView noResults;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ItemData itemData[] = {
                new ItemData("Hola", R.drawable.abc_ab_share_pack_mtrl_alpha),
                new ItemData("Adios", R.drawable.abc_btn_check_material)
        };

        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new MyAdapter(itemData);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders_list, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycleview_orderlist);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        noResults = (TextView) view.findViewById(R.id.empty);

        return view;
    }

    public class ItemData {
        private String title;
        private int imageUrl;

        public ItemData(String title,int imageUrl){
            this.title = title;
            this.imageUrl = imageUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(int imageUrl) {
            this.imageUrl = imageUrl;
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private ItemData[] itemsData;

        public MyAdapter(ItemData[] itemsData) {
            this.itemsData = itemsData;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycleview_item_layout, null);

            return new ViewHolder(itemLayoutView);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            viewHolder.txtViewTitle.setText(itemsData[position].getTitle());
            viewHolder.imgViewIcon.setImageResource(itemsData[position].getImageUrl());
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView txtViewTitle;
            public ImageView imgViewIcon;

            public ViewHolder(View itemLayoutView) {
                super(itemLayoutView);
                txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.item_title);
                imgViewIcon = (ImageView) itemLayoutView.findViewById(R.id.item_icon);
            }
        }

        @Override
        public int getItemCount() {
            return itemsData.length;
        }
    }
}
