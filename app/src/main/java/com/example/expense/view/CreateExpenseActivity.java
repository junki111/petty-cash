package com.example.expense.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.expense.R;
import com.example.expense.application.AppController;
import com.example.expense.data.entity.Category;
import com.example.expense.data.entity.Expense;
import com.example.expense.util.Config;
import com.example.expense.viewmodel.CategoryViewModel;
import com.example.expense.viewmodel.ExpenseViewModel;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class CreateExpenseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private EditText mEditMerchantView;
    private EditText mEditDateView;
    private EditText mEditAmountView;
    private EditText mEditDescriptionView;
    private Button saveExpense;
    private Button cancelExpense;
    private CategoryViewModel categoryViewModel;
    private List<Category> categoryList;
    Spinner currency;
    Spinner category;


    private Activity activity = this;
    private Context context = this;
    static final float END_SCALE = 0.7f;

    String expenseCurrency;
    String expenseCategory;
    List categoryNames = new ArrayList();

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ConstraintLayout contentView;
    ImageView menuIcon;
    private ExpenseViewModel expenseViewModel;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_expense);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        contentView = findViewById(R.id.content);
        menuIcon = findViewById(R.id.menu_icon);
        navigationDrawer();

        email = AppController.getInstance().getPreferenceManager().getEmail();
        System.out.println("EMAILLLLLLLLLLL:::::::"+email);

        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);

        categoryList = categoryViewModel.getCategoriesList();


        for(int i =0; i < categoryList.size(); i++){
            Category currentCategory = categoryList.get(i);
            categoryNames.add(currentCategory.getCategory());
        }

        mEditMerchantView = findViewById(R.id.merchantName);
        mEditDateView = findViewById(R.id.expenseDate);
        mEditAmountView = findViewById(R.id.expenseAmount);
        mEditDescriptionView = findViewById(R.id.expenseDescription);
        expenseViewModel = ViewModelProviders.of(this).get(ExpenseViewModel.class);
        currency = (Spinner) findViewById(R.id.expenseCurrency);
        category = (Spinner) findViewById(R.id.expenseCategory);

        ArrayAdapter<CharSequence>adapter = ArrayAdapter.createFromResource(this, R.array.currencies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        currency.setAdapter(adapter);

        ArrayAdapter categoryAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryNames);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        category.setAdapter(categoryAdapter);

        currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                expenseCurrency = parent.getItemAtPosition(position).toString();
                currency.setSelection(0);
                currency.setSelection(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                expenseCategory = parent.getItemAtPosition(position).toString();
                category.setSelection(0);
                category.setSelection(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        saveExpense = findViewById(R.id.saveExpense);
        cancelExpense = findViewById(R.id.cancelExpense);

        setListeners();
    }

    private void setListeners() {
        saveExpense.setOnClickListener(view -> {
            validation();
        });

        cancelExpense.setOnClickListener(view -> {
            mEditMerchantView.setText("");
            mEditDateView.setText("");
            mEditAmountView.setText("");
            mEditDescriptionView.setText("");
            category.setSelection(0);
            Intent i = new Intent(activity, LandingActivity.class);
            startActivity(i);
        });
    }

    private void validation() {
        String merchantName = mEditMerchantView.getText().toString();
        String stringDate = mEditDateView.getText().toString();
        String amount = mEditAmountView.getText().toString();
        String currencyAndAmount = "" + expenseCurrency + " " + mEditAmountView.getText().toString();
        String description = mEditDescriptionView.getText().toString();
        String tCategory = expenseCategory;


        if (merchantName.isEmpty()) {
            Toast.makeText(context, "Merchant name is required.", Toast.LENGTH_LONG);
            return;
        }
        if (stringDate.isEmpty()) {
            Toast.makeText(context, "Date is required.", Toast.LENGTH_LONG);
            return;
        }
        if (amount.isEmpty()) {
            Toast.makeText(context, "Amount is required.", Toast.LENGTH_LONG);
            return;
        }

        Expense expense = new Expense();
        expense.setMerchant(merchantName);
        expense.setDate(stringDate);
        expense.setAmount(currencyAndAmount);
        expense.setDescription(description);
        expense.setCategory(tCategory);

        expenseViewModel.insertExpense(expense);
        System.out.println("Expense Created");
        finish();
        mEditMerchantView.setText("");
        mEditDateView.setText("");
        mEditAmountView.setText("");
        mEditDescriptionView.setText("");
        category.setSelection(0);
        Intent i = new Intent(activity, LandingActivity.class);
        startActivity(i);

        processRequest(merchantName, stringDate, amount, description, tCategory);
    }

    private void processRequest(String merchantName, String stringDate, String amount, String description, String expCategory) {
        final String requestTag = "Create_Expense";
        final String url = Config.URL;

        final Map<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("category", expCategory);
        map.put("merchantName", merchantName);
        map.put("expenseDate", stringDate);
        map.put("amount", amount);
        map.put("narration", description);
        map.put("username","channel");
        map.put("password","$AG_@3xp3ns3");
        map.put("processingCode","130000");

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest transReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getString("response")) {
                        case "000":
                            Intent i = new Intent(activity, LandingActivity.class);
                            startActivity(i);
                            break;
                        case "999":
                            Toast.makeText(context, "Expense creation failed", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    JSONObject response;
                    switch (networkResponse.statusCode) {
                        case 422:
                            try {
                                String responseErrorString = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers, "utf-8"));
                                if (responseErrorString.equals("The custom error module does not recognize this error")) {
                                    Toasty.info(context, getString(R.string.validation_failed), Toast.LENGTH_LONG, true).show();
                                    return;
                                }
                                response = new JSONObject(responseErrorString);

                                switch (response.getString("status")) {
                                    case "fatal":
                                /* Validation errors - Sample response
                                  {
                                      "status": "fatal",
                                        "errors": [{
                                          "location": "body",
                                          "param": "agent_number",
                                          "msg": "Validation failed | CO::00-1"
                                        }]
                                 }
                                 */

                                        Toasty.info(context, getString(R.string.validation_failed), Toast.LENGTH_LONG, true).show();

                                        break;

                                    default:
                                        Toasty.info(context, getString(R.string.validation_failed), Toast.LENGTH_LONG, true).show();
                                        break;
                                }
                            } catch (JSONException e) {
                                Toasty.info(context, getString(R.string.validation_failed), Toast.LENGTH_SHORT, true).show();
                            } catch (UnsupportedEncodingException e) {
                                Toasty.info(context, getString(R.string.validation_failed), Toast.LENGTH_SHORT, true).show();
                            }
                            break;
                        default:
                            Toasty.info(context, "Error " + networkResponse.statusCode + ": " + getString(R.string.service_unavailable), Toast.LENGTH_LONG, true).show();
                            break;
                    }
                } else {
                    if (error instanceof NoConnectionError) {
                        Toasty.info(context, "No connection to the server", Toast.LENGTH_SHORT, true).show();
                    } else if (error instanceof TimeoutError) {
                        Toasty.info(context, "The request has timed out", Toast.LENGTH_SHORT, true).show();
                    } else if (error instanceof AuthFailureError) {
                        Toasty.info(context, "Authentication failed", Toast.LENGTH_SHORT, true).show();
                    } else if (error instanceof ServerError) {
                        Toasty.info(context, getString(R.string.service_unavailable), Toast.LENGTH_LONG, true).show();
                    } else if (error instanceof NetworkError) {
                        Toasty.info(context, "Network error", Toast.LENGTH_LONG, true).show();
                    } else if (error instanceof ParseError) {
                        Toasty.info(context, "Network error", Toast.LENGTH_LONG, true).show();
                    } else {
                        Toasty.info(context, getString(R.string.service_unavailable), Toast.LENGTH_LONG, true).show();
                    }
                }
            }
        });

        //it caters for retries in slow networks preventing sending of multiple requests
        transReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(transReq);
    }

    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_expense);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        animateNavigationDrawer();

    }

    private void animateNavigationDrawer() {
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                final float xoffset = drawerView.getWidth() * slideOffset;
                final float xoffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xoffset - xoffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });
    }

    public void onBackPressed() {
        if(drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.nav_home):
                Intent intent = new Intent(activity, LandingActivity.class);
                startActivity(intent);
                break;
            case(R.id.nav_category):
                Intent intent1 = new Intent(activity, Categories.class);
                startActivity(intent1);
                break;
            case(R.id.nav_expense):
                break;
            case(R.id.nav_changePin):
                Intent intent3 = new Intent(activity, ChangePinActivity.class);
                startActivity(intent3);
                break;
        }
        return true;
    }
}