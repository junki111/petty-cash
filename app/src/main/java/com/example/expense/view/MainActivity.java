package com.example.expense.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.expense.util.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {
    private Activity activity = this;
    private Context context = this;

    private EditText email;
    private EditText password;

    private Button loginSubmit;
    private String emailAd;
    private String pass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.emailAddress);
        password = findViewById(R.id.password);
        loginSubmit = findViewById(R.id.loginSubmit);
        setListeners();

    }

    private void setListeners() {
        loginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(activity, LandingActivity.class);
//                startActivity(i);
                validation();
            }
        });
    }

    private void validation() {
        emailAd = email.getText().toString().trim();
        pass = password.getText().toString().trim();

        if (emailAd.isEmpty()) {
            Toast.makeText(context, "Email Address is required.", Toast.LENGTH_LONG);
            return;
        }
        if (pass.isEmpty()) {
            Toast.makeText(context, "Password is required.", Toast.LENGTH_LONG);
            return;
        }
//        try {
//            JSONObject jo = new JSONObject();
//            jo.put("email",emailAd);
//        } catch (Exception e ){
//            e.printStackTrace();
//        }

//        Map<String, String> map = new HashMap<>();
//        map.put("phone_number", phone);
//        map.put("id_number", idNumber);
//        map.put("pin", pin);
//        map.put("account", phone);
//        map.put("version", version);
//        map.put("imei", imei);

        processRequest(emailAd, pass);
    }

    private void processRequest(String em, String pin) {
        final String requestTag = "login_request";
        final String url = Config.URL;

        final Map<String, String> map = new HashMap<>();
        map.put("email", em);
        map.put("pin", pin);
        map.put("username","channel");
        map.put("password","$AG_@3xp3ns3");
        map.put("processingCode","100000");



        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest loginReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getString("response")) {
                        case "000":
                            String username = response.getJSONObject("empDetails").getString("username");
                            //set the email and username to preference manager
                            AppController.getInstance().getPreferenceManager().setEmail(em);
                            AppController.getInstance().getPreferenceManager().setUserName(username);
                            Intent i = new Intent(activity, LandingActivity.class);
                            startActivity(i);
                            break;
                        case "999":
                            Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show();
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
        loginReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(loginReq);
    }
}