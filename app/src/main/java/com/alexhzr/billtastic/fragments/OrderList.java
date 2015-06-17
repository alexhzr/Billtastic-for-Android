package com.alexhzr.billtastic.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexhzr.billtastic.HTTPRequest.AsyncClient;
import com.alexhzr.billtastic.HTTPRequest.mJsonHttpResponseHandler;
import com.alexhzr.billtastic.R;
import com.alexhzr.billtastic.adapters.OrderListAdapter;
import com.alexhzr.billtastic.models.Order;
import com.alexhzr.billtastic.util.RecyclerItemClickListener;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderList extends Fragment implements RecyclerItemClickListener.OnItemClickListener {
    private OrderListAdapter adapter;
    private ArrayList<Order> orders;
    private Context context;
    private int page;
    private int customer_id;
    private boolean todas;
    private String query;

    private TextView noResults;
    private ListView listView;

    private int estadoFactura;

    public static OrderList newInstance(boolean todasFacturas, String query, int idCliente, int estadoFactura) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("todo", todasFacturas);
        bundle.putString("query", query);
        bundle.putInt("customer_id", idCliente);
        bundle.putInt("estadoFactura", estadoFactura);

        OrderList fragmento = new OrderList();
        fragmento.setArguments(bundle);

        return fragmento;
    }

    public OrderList() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        page = 0;
        context = getActivity();
        if (orders != null)
            cargarLista();
        inicializarListView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orders = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();

        if (args != null) {
            todas = args.getBoolean("todo");
            query = args.getString("query");
            customer_id = args.getInt("customer_id");
            estadoFactura = args.getInt("estadoFactura");
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("all", todas);
    }

    private void cargarLista() {
        String url = null;
        switch (estadoFactura) {
            case 0:
                url = "/api/order/pending";
                break;
            case 1:
                url = "/api/order/paid";
                break;
            case 2:
                url = "/api/order/draft";
                break;
        }
        AsyncClient.get(url, null, new mJsonHttpResponseHandler(context) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                noResults.setText(R.string.s_no_orders_found);
                listView.setEmptyView(noResults);
                for (int i = 0; i < response.length(); i++) {
                    JSONObject json = null;
                    orders.clear();
                    try {
                        json = response.getJSONObject(i);
                        Order order = new Order(json);

                        if (!orders.contains(order))
                            orders.add(order);

                        if (adapter != null)
                            adapter.notifyDataSetChanged();
                    } catch (JSONException e1) {
                        orders = null;
                    }
                }
            }
        });
    }

    private void inicializarListView() {
        if (orders != null) {
            if (getView() != null) {
                listView = (ListView) getView().findViewById(R.id.listview);
                noResults = (TextView) getView().findViewById(R.id.empty);
                adapter = new OrderListAdapter(orders, getActivity(), R.layout.detail_order_list);
                listView.setAdapter(adapter);
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(R.string.cb_remove_order_title);
                        builder.setMessage(R.string.cb_remove_order_body)
                                .setCancelable(false)
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        AsyncClient.delete("/api/order/" + orders.get(position).get_id(), null, context, new mJsonHttpResponseHandler(context) {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                super.onSuccess(statusCode, headers, response);
                                                try {
                                                    if (response.getInt(context.getString(R.string.server_response)) == 1) {
                                                        Toast.makeText(context, R.string.cb_remove_order_succeed, Toast.LENGTH_SHORT).show();
                                                    } else
                                                        Toast.makeText(context, R.string.cb_remove_order_failure, Toast.LENGTH_SHORT).show();
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
                        return true;
                    }
                });


            }
        }

    }

    private void verFactura(int position) {
        Toast.makeText(getActivity(), "Verias facutra pedido " + orders.get(position).get_id(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(View childView, int position) {

    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }
}