package com.alexhzr.billtastic.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.alexhzr.billtastic.adapters.ProductListAdapter;
import com.alexhzr.billtastic.models.Product;
import com.alexhzr.billtastic.util.RecyclerItemClickListener;
import com.alexhzr.billtastic.util.SimpleDividerItemDecoration;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductList extends Fragment implements RecyclerItemClickListener.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.ItemDecoration itemDecoration;
    private ArrayList<Product> products;
    private TextView noResults;
    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;

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
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, this));
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorScheme(R.color.my_material_primary, R.color.my_material_primary_light, R.color.my_material_accent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (products != null)
                    products.clear();
                loadProducts();
            }
        });

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
                    swipeRefreshLayout.setRefreshing(false);
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

    @Override
    public void onItemClick(View childView, int position) {
    }

    @Override
    public void onItemLongPress(View childView, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.cb_remove_product_title);
        builder.setMessage(R.string.cb_remove_product_body)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AsyncClient.delete("/api/product/" + products.get(position).get_id(), null, context, new mJsonHttpResponseHandler(context) {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                try {
                                    if (response.getInt(context.getString(R.string.server_response)) == 1) {
                                        Toast.makeText(context, R.string.cb_remove_product_succeed, Toast.LENGTH_SHORT).show();
                                    } else if (response.getInt(context.getString(R.string.server_response)) == 4) {
                                        Toast.makeText(context, R.string.cb_remove_product_not_found, Toast.LENGTH_SHORT).show();
                                    }
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
