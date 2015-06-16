package com.alexhzr.billtastic.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexhzr.billtastic.HTTPRequest.AsyncClient;
import com.alexhzr.billtastic.HTTPRequest.mJsonHttpResponseHandler;
import com.alexhzr.billtastic.R;
import com.alexhzr.billtastic.adapters.ProductListAdapter;
import com.alexhzr.billtastic.models.Product;
import com.alexhzr.billtastic.util.SimpleDividerItemDecoration;

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
    private TextView noResults;
    private Context context;

    public ProductList() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        products = new ArrayList<>();
        loadProducts();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ProductListAdapter(products);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_list);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        noResults = (TextView) view.findViewById(R.id.empty);

        return view;
    }

    private void loadProducts() {
        AsyncClient.get("/api/product", null, new mJsonHttpResponseHandler(context) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if (!response.isNull(0)) {
                    for (int i = 0; i < response.length(); i++)
                        try {
                            products.add(new Product(response.getJSONObject(i)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    mAdapter.notifyDataSetChanged();
                } else {
                    products = null;
                    noResults.setText(R.string.s_no_products_found);
                    mRecyclerView.setVisibility(View.GONE);
                    noResults.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                AsyncClient.doOnFailure(getActivity(), statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                AsyncClient.doOnFailure(getActivity(), statusCode);
            }
        });
    }



}
