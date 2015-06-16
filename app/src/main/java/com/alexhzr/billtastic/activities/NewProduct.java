package com.alexhzr.billtastic.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.alexhzr.billtastic.R;
import com.alexhzr.billtastic.adapters.TaxSpinnerAdapter;
import com.alexhzr.billtastic.httpRequest.AsyncClient;
import com.alexhzr.billtastic.httpRequest.mJsonHttpResponseHandler;
import com.alexhzr.billtastic.models.Tax;
import com.alexhzr.billtastic.util.Converter;
import com.alexhzr.billtastic.util.Validator;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewProduct extends ActionBarActivity {
    private Toolbar toolbar;
    private Context context;

    private EditText reference;
    private EditText description;
    private EditText purchase_price;
    private EditText sell_price;
    private EditText tax_price;
    private EditText benefit;
    private Spinner tax;

    private double cTax;
    private double cSellPrice;
    private double cPurchasePrice;
    private double cTaxPrice;
    private double cBenefit;

    private ArrayList<Tax> taxes;
    private TaxSpinnerAdapter adTaxes;
    private ArrayList<EditText> components;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        components = new ArrayList<>();
        context = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        reference = (EditText) findViewById(R.id.newProduct_reference);
        description = (EditText) findViewById(R.id.newProduct_description);
        purchase_price = (EditText) findViewById(R.id.newProduct_purchase_price);
        sell_price = (EditText) findViewById(R.id.newProduct_sell_price);
        tax_price = (EditText) findViewById(R.id.newProduct_tax_price);
        benefit = (EditText) findViewById(R.id.newProduct_benefit);
        tax = (Spinner) findViewById(R.id.newProduct_tax);

        components.add(reference);
        components.add(description);
        components.add(purchase_price);
        components.add(sell_price);
        components.add(tax_price);
        components.add(benefit);

        AsyncClient.get("/api/tax", null, new mJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                if (!response.isNull(0)) {
                    taxes = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++)
                        try {
                            taxes.add(new Tax(response.getJSONObject(i)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    if (taxes != null && taxes.size() > 0) {
                        if (adTaxes == null) {
                            adTaxes = new TaxSpinnerAdapter(context, R.layout.detail_spinner, taxes);
                            adTaxes.setDropDownViewResource(R.layout.detail_spinner);
                            tax.setAdapter(adTaxes);
                        } else adTaxes.notifyDataSetChanged();
                    }
                }
            }
        });

        setPricesListener();
    }

    private void existsReference() {
        if (Validator.areNotEmpty(components, this)) {
            mJsonHttpResponseHandler responseHandler = new mJsonHttpResponseHandler(this) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        if (response.getInt(context.getString(R.string.server_response)) == 2)
                            AsyncClient.redirectToLogin(context);
                        else if (response.getInt(context.getString(R.string.server_response)) == 4) {
                            newProduct();
                        } else if (response.getInt(context.getString(R.string.server_response)) == 3) {
                            reference.setBackgroundColor(Color.RED);
                            Toast.makeText(context, R.string.e_reference_exists, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            };
            AsyncClient.get("/api/product/check_reference/" + reference.getText().toString(), null, responseHandler);
        }
    }

    private void newProduct() {
        RequestParams params = new RequestParams();
        params.put("reference", reference.getText().toString());
        params.put("tax", ((Tax) tax.getSelectedItem()).get_id());
        params.put("purchase_price", purchase_price.getText().toString());
        params.put("sell_price", sell_price.getText().toString());
        params.put("tax_price", tax_price.getText().toString());
        params.put("description", description.getText().toString());
        AsyncClient.post("/api/product", params, new mJsonHttpResponseHandler(this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt(context.getString(R.string.server_response)) == 2) {
                        AsyncClient.redirectToLogin(context);
                    } else if (response.getInt(context.getString(R.string.server_response)) == 0) {
                        Toast.makeText(context, R.string.e_petition, Toast.LENGTH_SHORT).show();
                    } else if (response.getInt(context.getString(R.string.server_response)) == 1) {
                        Toast.makeText(context, R.string.s_newproduct_success, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setPricesListener() {
        tax.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (view != null)
                    recalculatePrices(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapterView.setSelection(1);
            }
        });

        View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    if (!((EditText) view).getText().toString().equals(""))
                        recalculatePrices(view);
                }
                if (view == purchase_price) {
                    if(!hasFocus && ((EditText) view).getText().toString().equals("")){
                        purchase_price.setText(Converter.doubleToString(0.00));
                    } else if (hasFocus && ((EditText) view).getText().toString().equals(Converter.doubleToString(0.00))){
                        purchase_price.setText("");
                    }
                }
            }
        };

        purchase_price.setOnFocusChangeListener(focusChangeListener);
        benefit.setOnFocusChangeListener(focusChangeListener);
        sell_price.setOnFocusChangeListener(focusChangeListener);
        tax_price.setOnFocusChangeListener(focusChangeListener);
    }

    private void recalculatePrices(View v) {
        cPurchasePrice = (Converter.stringToDouble(purchase_price.getText().toString()));
        cTax = taxes.get(tax.getSelectedItemPosition()).getValue();
        cTax = cTax / 100;

        if (v.equals(benefit)) {
            cBenefit = (Converter.stringToDouble(benefit.getText().toString())) / 100;
            cSellPrice = cPurchasePrice + (cPurchasePrice * cBenefit);
            cTaxPrice = cSellPrice + (cSellPrice * cTax);

            sell_price.setText(Converter.doubleToString(cSellPrice));
            tax_price.setText(Converter.doubleToString(cTaxPrice));

        } else if (v.equals(sell_price)) {
            cSellPrice = Converter.stringToDouble(sell_price.getText().toString());
            cTaxPrice = cSellPrice + (cSellPrice * cTax);
            cBenefit = (100 * (cSellPrice - cPurchasePrice)) / cPurchasePrice;

            tax_price.setText(Converter.doubleToString(cTaxPrice));
            benefit.setText(Converter.doubleToString(cBenefit));

        } else if (v.equals(tax_price)) {
            cTaxPrice = Converter.stringToDouble(tax_price.getText().toString());
            cSellPrice = cTaxPrice / (1 + cTax);
            cBenefit = (100 * (cSellPrice - cPurchasePrice)) / cPurchasePrice;

            sell_price.setText(Converter.doubleToString(cSellPrice));
            benefit.setText(Converter.doubleToString(cBenefit));

        } else {
            cTaxPrice = cSellPrice + (cSellPrice * cTax);
            tax_price.setText(Converter.doubleToString(cTaxPrice));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_newproduct_submit) {
            existsReference();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
