package com.alexhzr.billtastic.fragments;

import android.content.Context;
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

import com.alexhzr.billtastic.R;
import com.alexhzr.billtastic.adapters.OrderListAdapter;
import com.alexhzr.billtastic.httpRequest.AsyncClient;
import com.alexhzr.billtastic.httpRequest.mJsonHttpResponseHandler;
import com.alexhzr.billtastic.models.Order;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderList extends Fragment {
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
    private static final int ITEMS_BAJO_LISTA = 5;

    private static final int CODIGO_CONSULTA_FACTURAS = 1;
    private static final int CODIGO_PEDIR_PDF = 2;


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
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        verFactura(position);
                    }
                });
            }
        }
    }

    /*private void intentFactura(String respuesta) {
        Intent intentCompartir = new Intent(context, LectorPDF.class);
        intentCompartir.putExtra("pdf", respuesta);
        context.startActivity(intentCompartir);
    }*/

    private void verFactura(int position) {
        Toast.makeText(getActivity(), "Verias facutra pedido "+orders.get(position).get_id(), Toast.LENGTH_SHORT).show();
        /*String url = Constantes.PDF_URL + String.valueOf(orders.get(position).getIdImpresion());
        AsyncTaskGet a = new AsyncTaskGet(context, this, url, true, CODIGO_PEDIR_PDF);
        if (context.getClass().getName().contains(MostrarCliente.class.getSimpleName())) {
            ((MostrarCliente) context).mostrarDialogo(context);
        } else if (context.getClass().getName().contains(Principal.class.getSimpleName())) {
            ((Principal) context).mostrarDialogo(context);
        }
        a.execute(new Hashtable<String, String>());*/
    }
}