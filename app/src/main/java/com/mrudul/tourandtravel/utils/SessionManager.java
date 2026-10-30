package com.mrudul.tourandtravel.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SessionManager — SmartTour360
 * ══════════════════════════════
 * Extended to cache the full customer profile locally in SharedPreferences.
 * This means the Profile screen shows real data immediately, even offline,
 * without waiting for a network round-trip.
 *
 * Root-cause fix: previously only custId was stored. Name/email/phone/address
 * were only available from the API. If the device is offline or on a different
 * network than the backend, the profile fields appeared blank.
 *
 * Backward-compatible: existing callers of getCustomerId(), isLoggedIn(),
 * saveCustomerId(), and logout() all continue to work unchanged.
 */
public class SessionManager {

    private static final String PREF_NAME    = "CustomerSession";

    // ── Existing keys (unchanged) ──────────────────────────────────────────
    private static final String KEY_CUST_ID  = "custId";
    private static final String KEY_LOGGED_IN = "loggedIn";

    // ── New profile-cache keys ─────────────────────────────────────────────
    private static final String KEY_NAME     = "custName";
    private static final String KEY_EMAIL    = "custEmail";
    private static final String KEY_PHONE    = "custPhone";
    private static final String KEY_ADDRESS  = "custAddress";
    private static final String KEY_IMAGE_URL = "custImageUrl";

    private final SharedPreferences preferences;

    public SessionManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // ── Existing methods (unchanged) ──────────────────────────────────────

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

    // ── New: cache full profile after successful API load/update ──────────

    /** Call this after every successful GET /api/customers/{id} response. */
    public void saveCustomerProfile(String name, String email,
                                    String phone, String address,
                                    String imageUrl) {
        preferences.edit()
                .putString(KEY_NAME,      name      != null ? name      : "")
                .putString(KEY_EMAIL,     email     != null ? email     : "")
                .putString(KEY_PHONE,     phone     != null ? phone     : "")
                .putString(KEY_ADDRESS,   address   != null ? address   : "")
                .putString(KEY_IMAGE_URL, imageUrl  != null ? imageUrl  : "")
                .apply();
    }

    /** Returns cached name, or empty string if never loaded. */
    public String getCachedName()     { return preferences.getString(KEY_NAME,     ""); }
    public String getCachedEmail()    { return preferences.getString(KEY_EMAIL,    ""); }
    public String getCachedPhone()    { return preferences.getString(KEY_PHONE,    ""); }
    public String getCachedAddress()  { return preferences.getString(KEY_ADDRESS,  ""); }
    public String getCachedImageUrl() { return preferences.getString(KEY_IMAGE_URL,""); }

    /** Returns true if we have at least a name cached (i.e. profile was loaded before). */
    public boolean hasCachedProfile() {
        return !preferences.getString(KEY_NAME, "").isEmpty();
    }
}