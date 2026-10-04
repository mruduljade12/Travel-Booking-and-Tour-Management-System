package com.mrudul.tourandtravel.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class DriverSessionManager {

    private static final String PREF_NAME = "DriverSession";
    private static final String KEY_DRIVER_ID = "driverId";
    private static final String KEY_LOGGED_IN = "loggedIn";

    private final SharedPreferences preferences;

    public DriverSessionManager(Context context) {

        preferences = context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
        );
    }

    public void saveDriverId(int driverId) {

        preferences.edit()
                .putInt(KEY_DRIVER_ID, driverId)
                .putBoolean(KEY_LOGGED_IN, true)
                .apply();
    }

    public int getDriverId() {

        return preferences.getInt(
                KEY_DRIVER_ID,
                -1
        );
    }

    public boolean isLoggedIn() {

        return preferences.getBoolean(
                KEY_LOGGED_IN,
                false
        );
    }

    public void logout() {

        preferences.edit()
                .clear()
                .apply();
    }
}
