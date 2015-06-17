package com.alexhzr.billtastic.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alexhzr.billtastic.HTTPRequest.AsyncClient;
import com.alexhzr.billtastic.HTTPRequest.mJsonHttpResponseHandler;
import com.alexhzr.billtastic.R;
import com.alexhzr.billtastic.activities.ShowCustomer;
import com.alexhzr.billtastic.adapters.CustomerListAdapter;
import com.alexhzr.billtastic.models.Customer;
import com.alexhzr.billtastic.util.RecyclerItemClickListener;
import com.alexhzr.billtastic.util.SimpleDividerItemDecoration;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CustomerList extends Fragment implements RecyclerItemClickListener.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.ItemDecoration itemDecoration;
    private TextView noResults;
    private Context context;
    private ArrayList<Customer> customers;
    private SwipeRefreshLayout swipeRefreshLayout;

    public CustomerList() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        customers = new ArrayList<>();
        loadCustomers();
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
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, this));
        mRecyclerView.setAdapter(mAdapter);
        noResults = (TextView) view.findViewById(R.id.empty);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorScheme(R.color.my_material_primary, R.color.my_material_primary_light, R.color.my_material_accent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (customers != null)
                    customers.clear();
                loadCustomers();
            }
        });

        return view;
    }

    private void loadCustomers() {
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
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    customers = null;
                    noResults.setText(R.string.s_no_customers_found);
                    mRecyclerView.setVisibility(View.GONE);
                    noResults.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onItemClick(View childView, int position) {
        Intent i = new Intent(context, ShowCustomer.class);
        i.putExtra("_id", customers.get(position).get_id());
        startActivity(i);
    }

    @Override
    public void onItemLongPress(View childView, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.cb_remove_customer_title);
        builder.setMessage(R.string.cb_remove_customer_body)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AsyncClient.delete("/api/customer/" + customers.get(position).get_id(), null, context, new mJsonHttpResponseHandler(context) {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                try {
                                    if (response.getInt(context.getString(R.string.server_response)) == 1) {
                                        Toast.makeText(context, R.string.cb_remove_customer_succeed, Toast.LENGTH_SHORT).show();
                                    } else Toast.makeText(context, R.string.cb_remove_customer_failure, Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();

    }
}
