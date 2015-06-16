package com.alexhzr.billtastic.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alexhzr.billtastic.HTTPRequest.AsyncClient;
import com.alexhzr.billtastic.HTTPRequest.mJsonHttpResponseHandler;
import com.alexhzr.billtastic.R;
import com.alexhzr.billtastic.adapters.AutoCompleteTextViewAdapter;
import com.alexhzr.billtastic.models.Customer;
import com.alexhzr.billtastic.models.Product;
import com.alexhzr.billtastic.util.Converter;
import com.alexhzr.billtastic.util.DateController;
import com.alexhzr.billtastic.util.FontController;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class NewOrder extends ActionBarActivity implements DatePickerDialog.OnDateSetListener {
    private Context context;
    private AutoCompleteTextView customer;
    private AutoCompleteTextView product;
    private EditText orderDate;
    private EditText amount;
    private Spinner status;
    private TextView noProductsYet;
    private TextView subtotal;
    private TextView total;
    private TextView saveProduct;
    private TextView saveCustomer;
    private LinearLayout linesLayout;
    private Toolbar toolbar;

    private boolean isCustomerSelected;
    private boolean isDateSelected;

    private ArrayList<Object> customers;
    private ArrayList<Object> products;
    private ArrayList<Product> productsHelper;
    AutoCompleteTextViewAdapter customerAdapter;
    AutoCompleteTextViewAdapter productAdapter;

    private JSONArray selectedProducts;
    private Customer selectedCustomer;

    double mSubtotal;
    double mTotal;

    public static final String DATEPICKER_TAG = "datepicker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        context = this;
        customer = (AutoCompleteTextView) findViewById(R.id.newOrder_customer);
        product = (AutoCompleteTextView) findViewById(R.id.newOrder_product);
        orderDate = (EditText) findViewById(R.id.newOrder_order_date);
        amount = (EditText) findViewById(R.id.newOrder_product_amount);
        status = (Spinner) findViewById(R.id.newOrder_state);
        noProductsYet = (TextView) findViewById(R.id.newOrder_no_products_yet);
        subtotal = (TextView) findViewById(R.id.newOrder_subtotal);
        total = (TextView) findViewById(R.id.newOrder_total);
        saveProduct = (TextView) findViewById(R.id.newOrder_product_save);
        saveCustomer = (TextView) findViewById(R.id.newOrder_customer_save);

        //These are the checkers
        isCustomerSelected = false;
        isDateSelected = false;

        linesLayout = (LinearLayout) findViewById(R.id.newOrder_lines_layout);

        FontController.setFontToTextView(context, saveProduct, context.getString(R.string.icon_new_line));
        FontController.setFontToTextView(context, saveCustomer, context.getString(R.string.icon_save));

        customers = new ArrayList<>();
        products = new ArrayList<>();
        selectedProducts = new JSONArray();
        productsHelper = new ArrayList<>();
        mSubtotal = 0;
        mTotal = 0;

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.order_state_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status.setAdapter(adapter);

        loadData();
        prepareView();
    }

    private void prepareView() {
        total.setText(Converter.doubleToMoney(mTotal));
        subtotal.setText(Converter.doubleToMoney(mSubtotal));

        saveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = product.getText().toString();
                if (!query.equals("")) {
                    AsyncClient.get("/api/product/get_by_reference/" + query, null, new mJsonHttpResponseHandler(context) {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            try {
                                if (!response.has(context.getString(R.string.server_response))) {
                                    createLineView(new Product(response), Integer.parseInt(amount.getText().toString()));
                                    product.setText("");
                                    amount.setText("1");
                                } else if (response.getInt(context.getString(R.string.server_response)) == 2)
                                    AsyncClient.redirectToLogin(context);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
        saveCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = customer.getText().toString();
                if (!query.equals("") && !isCustomerSelected) {
                    AsyncClient.get("/api/customer/get_by_name/" + query, null, new mJsonHttpResponseHandler(context) {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            try {
                                if (!response.has(context.getString(R.string.server_response))) {
                                    selectedCustomer = new Customer(response);
                                    customer.setEnabled(false);
                                    customer.setBackgroundColor(context.getResources().getColor(R.color.button_disabled));
                                    FontController.setFontToTextView(context, saveCustomer, context.getString(R.string.icon_edit));
                                    isCustomerSelected = true;
                                } else if (response.getInt(context.getString(R.string.server_response)) == 2)
                                    AsyncClient.redirectToLogin(context);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                if (isCustomerSelected) {
                    customer.setEnabled(true);
                    customer.setBackgroundColor(Color.TRANSPARENT);
                    FontController.setFontToTextView(context, saveCustomer, context.getString(R.string.icon_save));
                    isCustomerSelected = false;
                }
            }
        });
        final Calendar cal = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), false);
        orderDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    datePickerDialog.setVibrate(false);
                    datePickerDialog.setYearRange(1990, 2037);
                    datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });
    }

    private void createLineView(final Product product, int amount) {
        JSONObject jsonProduct = new JSONObject();
        try {
            jsonProduct.put("product", product.get_id());
            jsonProduct.put("amount", amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        selectedProducts.put(jsonProduct);
        productsHelper.add(product);

        if (selectedProducts.length() > 0)
            noProductsYet.setVisibility(View.GONE);
        else noProductsYet.setVisibility(View.VISIBLE);

        View detail = View.inflate(context, R.layout.detail_new_order_product, null);
        TextView totalLinePrice = (TextView) detail.findViewById(R.id.line_detail_total_price);
        TextView amountTextView = (TextView) detail.findViewById(R.id.line_detail_amount);
        TextView productTextView = (TextView) detail.findViewById(R.id.line_detail_product);
        TextView unityPrice = (TextView) detail.findViewById(R.id.line_detail_unity_price);

        productTextView.setText(product.getReference());
        amountTextView.setText(String.valueOf(amount));
        unityPrice.setText(Converter.doubleToMoney(product.getSell_price()));
        totalLinePrice.setText(Converter.doubleToMoney(product.getSell_price() * amount));

        detail.setTag(product);
        linesLayout.addView(detail);

        mSubtotal = mSubtotal + (product.getSell_price() * amount);
        mTotal = mTotal + (product.getTax_price() * amount);
        total.setText(Converter.doubleToMoney(mTotal));
        subtotal.setText(Converter.doubleToMoney(mSubtotal));
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
                    customerAdapter = new AutoCompleteTextViewAdapter(context, R.layout.simple_item_dropdown, customers);
                    customer.setAdapter(customerAdapter);
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
                        productAdapter = new AutoCompleteTextViewAdapter(context, R.layout.simple_item_dropdown, products);
                        product.setAdapter(productAdapter);
                    }
                }
            }
        });
    }

    private void saveOrder() {
        if (isCustomerSelected && isDateSelected && selectedProducts.length() > 0) {
            RequestParams params = new RequestParams();
            params.put("order_date", orderDate.getText().toString());
            params.put("customer", selectedCustomer.get_id());
            params.put("products", selectedProducts);
            params.put("total", mTotal);
            params.put("status", status.getSelectedItemPosition());
            if (status.getSelectedItemPosition() == 3)
                params.put("pending", mTotal);
            params.put("sent", false);
            params.put("printed", false);
            AsyncClient.post("/api/order", params, new mJsonHttpResponseHandler(context) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        if (response.getInt(context.getString(R.string.server_response)) == 1) {
                            Toast.makeText(context, R.string.s_new_order_succeed, Toast.LENGTH_SHORT).show();
                            finish();
                        } else if (response.getInt(context.getString(R.string.server_response)) == 0)
                            Toast.makeText(context, R.string.s_new_order_failed, Toast.LENGTH_SHORT).show();
                        else if (response.getInt(context.getString(R.string.server_response)) == 2)
                            AsyncClient.redirectToLogin(context);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else Toast.makeText(context, R.string.e_new_order_incomplete, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save_order) {
            saveOrder();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        if (datePickerDialog.getTag().equals(DATEPICKER_TAG)) {
            orderDate.setText(DateController.toDisplayDate(day, month + 1, year));
            isDateSelected = true;
        }
    }
}
