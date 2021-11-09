package com.example.expense.application;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private Context context;
    // Shared Preferences
    private SharedPreferences pref;

    // Editor for Shared preferences
    private SharedPreferences.Editor editor;

    // Shared mode
    private final int PRIVATE_MODE = 0;
    // SharedPref file name
    private static final String PREF_NAME = "expensePREF";

    // Shared Preference Keys
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERNAME = "username";

    PreferenceManager(Context context) {
        this.context = context;

        pref = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }

    public String getEmail() {
        return pref.getString(KEY_EMAIL, null);
    }

    public void setEmail(String email) {
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }

    public String getUserName() {
        return pref.getString(KEY_USERNAME, null);
    }

    public void setUserName(String username) {
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }
}
