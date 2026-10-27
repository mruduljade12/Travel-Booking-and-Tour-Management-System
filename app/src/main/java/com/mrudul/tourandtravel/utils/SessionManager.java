package com.mrudul.tourandtravel.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "CustomerSession";

    private static final String KEY_CUST_ID = "custId";
    private static final String KEY_CUST_NAME = "custName";
    private static final String KEY_CUST_EMAIL = "email";
    private static final String KEY_CUST_PHONE = "phone";
    private static final String KEY_CUST_IMAGE = "image";
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

    public void saveCustomerEmail(String email) {
        preferences.edit()
                .putString(KEY_CUST_EMAIL, email)
                .putBoolean(KEY_LOGGED_IN, true)
                .apply();
    }

    public void saveCustomerPhone(String phone) {
        preferences.edit()
                .putString(KEY_CUST_PHONE, phone)
                .putBoolean(KEY_LOGGED_IN, true)
                .apply();
    }

    public void saveCustomerImage(String image) {
        preferences.edit()
                .putString(KEY_CUST_IMAGE, image)
                .putBoolean(KEY_LOGGED_IN, true)
                .apply();
    }

    public void setKeyCustName(String name) {
        preferences.edit()
                .putString(KEY_CUST_NAME, name)
                .putBoolean(KEY_LOGGED_IN, true)
                .apply();
    }

    public int getCustomerId() {
        return preferences.getInt(KEY_CUST_ID, -1);
    }

    public String getKeyCustName() {
        return preferences.getString(KEY_CUST_NAME, null);
    }

    public String getKeyCustEmail() {
        return preferences.getString(KEY_CUST_EMAIL, null);
    }

    public String getKeyCustPhone() {
        return preferences.getString(KEY_CUST_PHONE, null);
    }

    public String getKeyCustImage() {
        return preferences.getString(KEY_CUST_IMAGE, null);
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_LOGGED_IN, false);
    }

    public void logout() {
        preferences.edit().clear().apply();
    }
}