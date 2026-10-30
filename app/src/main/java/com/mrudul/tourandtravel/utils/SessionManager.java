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

<<<<<<< HEAD
    private static final String PREF_NAME    = "CustomerSession";

    // ── Existing keys (unchanged) ──────────────────────────────────────────
    private static final String KEY_CUST_ID  = "custId";
=======
    private static final String PREF_NAME = "CustomerSession";

    private static final String KEY_CUST_ID = "custId";
    private static final String KEY_CUST_NAME = "custName";
    private static final String KEY_CUST_EMAIL = "email";
    private static final String KEY_CUST_PHONE = "phone";
    private static final String KEY_CUST_IMAGE = "image";
>>>>>>> 5705a3faf7ae0ac8091c7aab87b2b459c72910b2
    private static final String KEY_LOGGED_IN = "loggedIn";

    // ── New profile-cache keys ─────────────────────────────────────────────
    private static final String KEY_NAME     = "custName";
    private static final String KEY_EMAIL    = "custEmail";
    private static final String KEY_PHONE    = "custPhone";
    private static final String KEY_ADDRESS  = "custAddress";
    private static final String KEY_IMAGE_URL = "custImageUrl";

    private final SharedPreferences preferences;

    public SessionManager(Context context) {
<<<<<<< HEAD
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // ── Existing methods (unchanged) ──────────────────────────────────────

=======
        preferences = context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
        );
    }

>>>>>>> 5705a3faf7ae0ac8091c7aab87b2b459c72910b2
    public void saveCustomerId(int custId) {
        preferences.edit()
                .putInt(KEY_CUST_ID, custId)
                .putBoolean(KEY_LOGGED_IN, true)
                .apply();
    }

<<<<<<< HEAD
=======
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

>>>>>>> 5705a3faf7ae0ac8091c7aab87b2b459c72910b2
    public int getCustomerId() {
        return preferences.getInt(KEY_CUST_ID, -1);
    }

<<<<<<< HEAD
=======
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

>>>>>>> 5705a3faf7ae0ac8091c7aab87b2b459c72910b2
    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_LOGGED_IN, false);
    }

    public void logout() {
        preferences.edit().clear().apply();
    }
<<<<<<< HEAD

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
=======
>>>>>>> 5705a3faf7ae0ac8091c7aab87b2b459c72910b2
}