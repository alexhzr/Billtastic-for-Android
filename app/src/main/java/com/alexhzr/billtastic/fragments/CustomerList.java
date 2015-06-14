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

import com.alexhzr.billtastic.R;
import com.alexhzr.billtastic.adapters.CustomerListAdapter;
import com.alexhzr.billtastic.httpRequest.AsyncClient;
import com.alexhzr.billtastic.httpRequest.mJsonHttpResponseHandler;
import com.alexhzr.billtastic.models.Customer;
import com.alexhzr.billtastic.util.SimpleDividerItemDecoration;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class CustomerList extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.ItemDecoration itemDecoration;
    private TextView noResults;
    private Context context;
    private ArrayList<Customer> customers;

    public CustomerList() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        customers = new ArrayList<>();
        loadProducts();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new CustomerListAdapter(customers, getActivity());
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
        AsyncClient.get("/api/customer", null, new mJsonHttpResponseHandler(context) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if (!response.isNull(0)) {
                    for (int i = 0; i < response.length(); i++)
                        try {
                            customers.add(new Customer(response.getJSONObject(i)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    mAdapter.notifyDataSetChanged();
                } else {
                    customers = null;
                    noResults.setText(R.string.s_no_customers_found);
                    mRecyclerView.setVisibility(View.GONE);
                    noResults.setVisibility(View.VISIBLE);
                }
            }
        });
    }

}
