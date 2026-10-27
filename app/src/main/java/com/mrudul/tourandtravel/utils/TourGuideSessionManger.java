package com.mrudul.tourandtravel.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class TourGuideSessionManger {

    private static final String PREF_NAME = "TourGuideSession";

    private static final String KEY_GUIDE_ID = "guideId";
    private static final String KEY_GUIDE_IMAGE = "guideImage";
    private static final String KEY_GUIDE_EMAIL = "guideEmail";
    private static final String KEY_LOGGED_IN = "loggedIn";

    private final SharedPreferences preferences;

    public TourGuideSessionManger(Context context) {
        preferences = context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
        );
    }

    public void saveGuideId(int guideId) {

        preferences.edit()
                .putInt(KEY_GUIDE_ID, guideId)
                .putBoolean(KEY_LOGGED_IN, true)
                .apply();
    }

    public int getGuideId() {

        return preferences.getInt(
                KEY_GUIDE_ID,
                -1
        );
    }

    // SAVE GUIDE IMAGE
    public void setKeyGuideImage(String imageUrl) {

        preferences.edit()
                .putString(KEY_GUIDE_IMAGE, imageUrl)
                .apply();
    }

    // GET GUIDE IMAGE
    public String getKeyGuideImage() {

        return preferences.getString(
                KEY_GUIDE_IMAGE,
                null
        );
    }

    public void setKeyGuideEmail(String email) {

        preferences.edit()
                .putString(KEY_GUIDE_EMAIL, email)
                .apply();
    }

    public String getKeyGuideEmail() {

        return preferences.getString(
                KEY_GUIDE_EMAIL,
                null
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