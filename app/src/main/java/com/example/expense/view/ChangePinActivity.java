package com.example.expense.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class ChangePinActivity extends AppCompatActivity {
    private Activity activity = this;
    private Context context = this;

    private EditText currentPin;
    private EditText newPin;
    private EditText confirmNewPin;

    private String textCurrentPin;
    private String textNewPin;
    private String textConfirmNewPin;


    private Button send;

    private String email;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);

        currentPin = findViewById(R.id.etCurrentPinChangePin);
        newPin = findViewById(R.id.etNewPinChangePin);
        confirmNewPin = findViewById(R.id.etConfirmNewPinChangePin);

        send = findViewById(R.id.btSubmitChangePin);
        email = AppController.getInstance().getPreferenceManager().getEmail();
        username = AppController.getInstance().getPreferenceManager().getUserName();

        setListeners();
    }

    private void setListeners() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });
    }

    private void validation() {
        textCurrentPin = currentPin.getText().toString().trim();
        textNewPin = newPin.getText().toString().trim();
        textConfirmNewPin = confirmNewPin.getText().toString().trim();

        if(textCurrentPin.isEmpty()) {
            Toast.makeText(this, "Please input the current pin", Toast.LENGTH_LONG);
            return;
        }

        if(textNewPin.isEmpty()) {
            Toast.makeText(this, "Please input the new pin", Toast.LENGTH_LONG);
            return;
        }

        if(textConfirmNewPin.isEmpty()) {
            Toast.makeText(this, "Please confirm the new pin", Toast.LENGTH_LONG);
            return;
        }

        if(!textNewPin.equals(textConfirmNewPin)) {
            Toast.makeText(this, "New pin and Confirmation pin do not match. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        processRequest(textCurrentPin, textNewPin);
    }

    private void processRequest(String textCurrentPin, String textNewPin) {
        final String requestTag = "changepin_request";
        final String url = Config.URL;

        final Map<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("appUser", username);
        map.put("oldPin", textCurrentPin);
        map.put("newPin", textNewPin);
        map.put("username","channel");
        map.put("password","$AG_@3xp3ns3");
        map.put("processingCode","110000");

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest changepinReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getString("response")) {
                        case "000":
                            Intent i = new Intent(activity, LandingActivity.class);
                            startActivity(i);
                            break;
                        case "999":
                            Toast.makeText(context, "Change Pin failed", Toast.LENGTH_SHORT).show();
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
        changepinReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(changepinReq);
    }
}