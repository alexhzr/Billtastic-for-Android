package com.alexhzr.billtastic.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexhzr.billtastic.R;
import com.alexhzr.billtastic.adapters.ProductListAdapter;
import com.alexhzr.billtastic.httpRequest.ApiClient;
import com.alexhzr.billtastic.models.Product;
import com.alexhzr.billtastic.util.SimpleDividerItemDecoration;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ProductList extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.ItemDecoration itemDecoration;
    private ArrayList<Product> products;

    public ProductList() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        products = new ArrayList<>();
        loadProducts();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ProductListAdapter(products);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycleview_productlist);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private void loadProducts() {
        ApiClient.get("api/product", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.v("productos", response.toString());
                if (!response.isNull(0)) {
                    for (int i = 0; i < response.length(); i++)
                        try {
                            products.add(new Product(response.getJSONObject(i)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    mAdapter.notifyDataSetChanged();
                } else products = null;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                ApiClient.doOnFailure(getActivity(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ApiClient.doOnFailure(getActivity(), statusCode);
            }
        });
    }



}
