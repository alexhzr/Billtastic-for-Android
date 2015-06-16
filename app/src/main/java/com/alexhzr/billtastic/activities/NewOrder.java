package com.alexhzr.billtastic.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alexhzr.billtastic.R;
import com.alexhzr.billtastic.adapters.AutoCompleteTextViewAdapter;
import com.alexhzr.billtastic.httpRequest.AsyncClient;
import com.alexhzr.billtastic.httpRequest.mJsonHttpResponseHandler;
import com.alexhzr.billtastic.models.Customer;
import com.alexhzr.billtastic.models.Product;
import com.alexhzr.billtastic.util.Converter;
import com.alexhzr.billtastic.util.FontController;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;

public class NewOrder extends ActionBarActivity {
    private Context context;
    private AutoCompleteTextView customer;
    private AutoCompleteTextView product;
    private EditText orderDate;
    private EditText amount;
    private Spinner status;
    private TextView noProductsYet;
    private TextView newLine;
    private TextView subtotal;
    private TextView total;
    private TextView saveProduct;

    private LinearLayout linesLayout;

    private ArrayList<Object> customers;
    private ArrayList<Object> products;

    private Hashtable<String, Integer> selectedProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        context = this;
        customer = (AutoCompleteTextView) findViewById(R.id.newOrder_customer);
        product = (AutoCompleteTextView) findViewById(R.id.newOrder_product);
        orderDate = (EditText) findViewById(R.id.newOrder_order_date);
        amount = (EditText) findViewById(R.id.newOrder_product_amount);
        status = (Spinner) findViewById(R.id.newOrder_state);
        noProductsYet = (TextView) findViewById(R.id.newOrder_no_products_yet);
        newLine = (TextView) findViewById(R.id.newOrder_new_line);
        subtotal = (TextView) findViewById(R.id.newOrder_subtotal);
        total = (TextView) findViewById(R.id.newOrder_total);
        saveProduct = (TextView) findViewById(R.id.newOrder_product_save);
        linesLayout = (LinearLayout) findViewById(R.id.newOrder_lines_layout);

        FontController.setFontToTextView(context, saveProduct, context.getString(R.string.icon_new_line));

        customers = new ArrayList<>();
        products = new ArrayList<>();
        selectedProducts = new Hashtable<>();
        loadData();
    }

    private void createLineView(final Product product, int amount) {
        View detail = View.inflate(context, R.layout.detail_new_order_product, null);
        TextView amountTextView = (TextView) detail.findViewById(R.id.line_detail_amount);
        TextView productTextView = (TextView) detail.findViewById(R.id.line_detail_product);
        TextView totalPrice = (TextView) detail.findViewById(R.id.line_detail_total_price);

        productTextView.setText(product.getReference());
        amountTextView.setText(String.valueOf(amountTextView));
        totalPrice.setText(Converter.doubleToMoney(product.getSell_price() * amount));

        detail.setTag(product);
        linesLayout.addView(detail);
    }

    private void loadData() {
        AsyncClient.get("/api/customer/", null, new mJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                if (!response.isNull(0)) {
                    JSONObject json = new JSONObject();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            json = response.getJSONObject(i);
                            customers.add(new Customer(json));
                        } catch (JSONException e) {
                            customers = null;
                        }
                    }
                    AutoCompleteTextViewAdapter adapter = new AutoCompleteTextViewAdapter(context, R.layout.simple_item_dropdown, customers);
                    customer.setAdapter(adapter);
                }
            }
        });

        AsyncClient.get("/api/product/", null, new mJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                if (!response.isNull(0)) {
                    JSONObject json = new JSONObject();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            json = response.getJSONObject(i);
                            products.add(new Product(json));
                        } catch (JSONException e) {
                            product = null;
                        }
                        AutoCompleteTextViewAdapter adapter = new AutoCompleteTextViewAdapter(context, R.layout.simple_item_dropdown, products);
                        product.setAdapter(adapter);
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
