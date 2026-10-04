package com.mrudul.tourandtravel.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "CustomerSession";
    private static final String KEY_CUST_ID = "custId";
    private static final String KEY_LOGGED_IN = "loggedIn";

    private final SharedPreferences preferences;

    public SessionManager(Context context) {

        preferences = context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
        );
    }

    public void saveCustomerId(int custId) {

        preferences.edit()
                .putInt(KEY_CUST_ID, custId)
                .putBoolean(KEY_LOGGED_IN, true)
                .apply();
    }

    public int getCustomerId() {

        return preferences.getInt(KEY_CUST_ID, -1);
    }

    public boolean isLoggedIn() {

        return preferences.getBoolean(KEY_LOGGED_IN, false);
    }

    public void logout() {

        preferences.edit().clear().apply();
    }
}
