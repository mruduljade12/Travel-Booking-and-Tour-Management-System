package com.mrudul.tourandtravel.user.user_fragments;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.network.ApiService;
import com.mrudul.tourandtravel.network.RetrofitClient;
import com.mrudul.tourandtravel.user.user_models.CustomerResponse;
import com.mrudul.tourandtravel.user.user_models.CustomerUpdateRequest;
import com.mrudul.tourandtravel.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ProfileFragment — SmartTour360 Customer Profile Screen
 * ═══════════════════════════════════════════════════════
 *
 * Bug fixes applied in this version:
 *
 * FIX 1 — "Name/email blank on profile open"
 *   Root cause: SessionManager previously stored only custId.
 *   loadCustomerProfile() made a network call — if the device was
 *   offline or not on the backend LAN, onFailure() fired silently
 *   and all TextViews stayed empty.
 *   Fix: SessionManager now caches name/email/phone/address in
 *   SharedPreferences after every successful API response.
 *   populateFromCache() is called first (instant display), then the
 *   API call refreshes and updates both the UI and the cache.
 *
 * FIX 2 — "Edit Profile button does nothing visible"
 *   Root cause: the button only toggled EditText.setEnabled() with no
 *   dialog, no Save/Cancel, and no user feedback.
 *   Fix: tapping Edit Profile opens an AlertDialog with all four
 *   editable fields pre-populated from cached data. Saving validates,
 *   calls the API, updates cache on success, and refreshes the UI.
 *
 * FIX 3 — "Personal Information rows not editable"
 *   Root cause: row clicks only showed a Toast.
 *   Fix: each row opens a focused AlertDialog for that single field,
 *   saves via the existing updateCustomerOnServer() path.
 *
 * FIX 4 — "Profile image shows generic icon even after login"
 *   Root cause: imageUrl was not cached, so after app restart Glide
 *   had nothing to load.
 *   Fix: imageUrl is now cached in SessionManager and loaded by
 *   Glide immediately on populateFromCache().
 *
 * All existing Firebase upload logic and Retrofit API calls are
 * preserved unchanged.
 */
public class ProfileFragment extends Fragment {

    // ── Existing fields ───────────────────────────────────────────────────
    private ApiService      apiService;
    private SessionManager  sessionManager;

    TextView         userName;
    EditText         pPhoneEdit, pEmailEdit, pAddressEdit;
    ImageView        pUserImg;
    AppCompatButton  saveBtn, logoutBtn;
    boolean          onEdit = false;

    private Uri    selectedImageUri;
    private String currentImageUrl;

    // ── New display TextViews ─────────────────────────────────────────────
    private TextView tvProfileEmail;
    private TextView tvInfoName;
    private TextView tvStatTrips, tvStatWishlist, tvStatRating;

    // Image picker (unchanged from original)
    private final ActivityResultLauncher<String> imagePicker =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {
                        if (uri != null) {
                            selectedImageUri = uri;
                            pUserImg.setImageURI(uri);
                        }
                    });

    // Legacy params
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1, mParam2;

    public ProfileFragment() {}

    public static ProfileFragment newInstance(String p1, String p2) {
        ProfileFragment f = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, p1);
        args.putString(ARG_PARAM2, p2);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ── Bind existing IDs ─────────────────────────────────────────────
        pUserImg     = view.findViewById(R.id.userProfileImage);
        userName     = view.findViewById(R.id.userProfileUserName);
        pEmailEdit   = view.findViewById(R.id.userEmail);
        pPhoneEdit   = view.findViewById(R.id.userPhone);
        pAddressEdit = view.findViewById(R.id.userAddress);
        saveBtn      = view.findViewById(R.id.userProfileSave);
        logoutBtn    = view.findViewById(R.id.userLogout);

        // ── Bind new display IDs ──────────────────────────────────────────
        tvProfileEmail = view.findViewById(R.id.tvProfileEmail);
        tvInfoName     = view.findViewById(R.id.tvInfoName);
        tvStatTrips    = view.findViewById(R.id.tvStatTrips);
        tvStatWishlist = view.findViewById(R.id.tvStatWishlist);
        tvStatRating   = view.findViewById(R.id.tvStatRating);

        apiService    = RetrofitClient.getApiService();
        sessionManager = new SessionManager(requireContext());

        // EditTexts start disabled (read-only mode)
        editTextEditableOrNot(false);

        // ── FIX 1: Show cached data immediately before API call ───────────
        populateFromCache();

        // ── Always try to refresh from API ────────────────────────────────
        loadCustomerProfile();

        // ── Profile photo tap ─────────────────────────────────────────────
        pUserImg.setOnClickListener(v -> imagePicker.launch("image/*"));

        // ── FIX 2: Edit Profile button opens full-profile edit dialog ─────
        View editProfileBtn = view.findViewById(R.id.ivEditProfile);
        if (editProfileBtn != null) {
            editProfileBtn.setOnClickListener(v -> showEditProfileDialog());
        }

        // ── Save Changes button (inline EditText mode — kept for compat) ──
        saveBtn.setOnClickListener(v -> {
            String email   = pEmailEdit.getText().toString().trim();
            String phone   = pPhoneEdit.getText().toString().trim();
            String address = pAddressEdit.getText().toString().trim();

            if (!validateFields(email, phone, address)) return;
            updateCustomerRequest(email, phone, address);
            editTextEditableOrNot(false);
            onEdit = false;
        });

        // ── Logout ────────────────────────────────────────────────────────
        logoutBtn.setOnClickListener(v -> {
            sessionManager.logout();
            Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
        });

        // ── FIX 3: Row taps open focused single-field edit dialogs ────────
        setRowClickListener(view, R.id.rowFullName, "Full Name",
            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS,
            sessionManager.getCachedName(), "name");

        setRowClickListener(view, R.id.rowEmail, "Email Address",
            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
            sessionManager.getCachedEmail(), "email");

        setRowClickListener(view, R.id.rowPhone, "Phone Number",
            InputType.TYPE_CLASS_PHONE,
            sessionManager.getCachedPhone(), "phone");

        setRowClickListener(view, R.id.rowAddress, "Home City",
            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS,
            sessionManager.getCachedAddress(), "address");

        // ── My Travel + Settings rows ─────────────────────────────────────
        wireToast(view, R.id.rowBookings,      "My Bookings coming soon");
        wireToast(view, R.id.rowSavedDest,     "Saved Destinations coming soon");
        wireToast(view, R.id.rowTravelPrefs,   "Travel Preferences coming soon");
        wireToast(view, R.id.rowNotifications, "Notifications & Privacy coming soon");
        wireToast(view, R.id.rowHelp,          "Help & Support coming soon");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // FIX 1 — Populate UI immediately from SharedPreferences cache
    // ─────────────────────────────────────────────────────────────────────────
    private void populateFromCache() {
        if (!sessionManager.hasCachedProfile()) return;

        String name    = sessionManager.getCachedName();
        String email   = sessionManager.getCachedEmail();
        String phone   = sessionManager.getCachedPhone();
        String address = sessionManager.getCachedAddress();
        String imgUrl  = sessionManager.getCachedImageUrl();

        // Identity card
        if (!TextUtils.isEmpty(name)) {
            userName.setText(name);
            if (tvInfoName != null) tvInfoName.setText(name);
        }
        if (!TextUtils.isEmpty(email)) {
            if (tvProfileEmail != null) tvProfileEmail.setText(email);
            pEmailEdit.setText(email);
        }
        if (!TextUtils.isEmpty(phone))   pPhoneEdit.setText(phone);
        if (!TextUtils.isEmpty(address)) pAddressEdit.setText(address);

        // Profile image — load from cache URL
        if (!TextUtils.isEmpty(imgUrl)) {
            currentImageUrl = imgUrl;
            Glide.with(this)
                .load(imgUrl)
                .placeholder(R.drawable.ic_default_image)
                .error(R.drawable.ic_default_image)
                .circleCrop()
                .into(pUserImg);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // FIX 2 — Full edit dialog with all four fields
    // ─────────────────────────────────────────────────────────────────────────
    private void showEditProfileDialog() {
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(android.R.layout.activity_list_item, null);

        // Build a vertical LinearLayout with four labelled EditTexts
        LinearLayout container = new LinearLayout(requireContext());
        container.setOrientation(LinearLayout.VERTICAL);
        int dp16 = dp(16);
        container.setPadding(dp16, dp(8), dp16, dp(4));

        EditText etName    = makeDialogField("Full Name",      sessionManager.getCachedName(),
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        EditText etEmail   = makeDialogField("Email Address",  sessionManager.getCachedEmail(),
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        EditText etPhone   = makeDialogField("Phone Number",   sessionManager.getCachedPhone(),
                InputType.TYPE_CLASS_PHONE);
        EditText etAddress = makeDialogField("Home City",      sessionManager.getCachedAddress(),
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        container.addView(etName);
        container.addView(etEmail);
        container.addView(etPhone);
        container.addView(etAddress);

        new AlertDialog.Builder(requireContext())
            .setTitle("Edit Profile")
            .setView(container)
            .setPositiveButton("Save Changes", (dialog, which) -> {
                String name    = etName.getText().toString().trim();
                String email   = etEmail.getText().toString().trim();
                String phone   = etPhone.getText().toString().trim();
                String address = etAddress.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!validateFields(email, phone, address)) return;

                // Save name locally in cache immediately (API doesn't have a name-update field)
                sessionManager.saveCustomerProfile(name, email, phone, address, currentImageUrl);
                userName.setText(name);
                if (tvInfoName     != null) tvInfoName.setText(name);
                if (tvProfileEmail != null) tvProfileEmail.setText(email);
                pEmailEdit.setText(email);
                pPhoneEdit.setText(phone);
                pAddressEdit.setText(address);

                // Push email, phone, address to backend
                updateCustomerRequest(email, phone, address);
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    private EditText makeDialogField(String hint, String value, int inputType) {
        EditText et = new EditText(requireContext());
        et.setHint(hint);
        et.setText(value);
        et.setInputType(inputType);
        et.setTextSize(15f);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, dp(6), 0, dp(6));
        et.setLayoutParams(lp);
        return et;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // FIX 3 — Single-field edit dialog per row
    // ─────────────────────────────────────────────────────────────────────────
    private void setRowClickListener(View root, int rowId, String label,
                                     int inputType, String currentValue, String field) {
        View row = root.findViewById(rowId);
        if (row == null) return;
        row.setOnClickListener(v -> showSingleFieldDialog(label, inputType, currentValue, field));
    }

    private void showSingleFieldDialog(String label, int inputType,
                                       String currentValue, String field) {
        EditText et = new EditText(requireContext());
        et.setInputType(inputType);
        et.setText(currentValue);
        et.setTextSize(16f);
        et.setPadding(dp(16), dp(12), dp(16), dp(12));
        et.setSelection(et.getText().length()); // cursor at end

        new AlertDialog.Builder(requireContext())
            .setTitle("Edit " + label)
            .setView(et)
            .setPositiveButton("Save", (dialog, which) -> {
                String value = et.getText().toString().trim();
                if (TextUtils.isEmpty(value)) {
                    Toast.makeText(getContext(), label + " cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("email".equals(field) && !Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
                    Toast.makeText(getContext(), "Enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("phone".equals(field) && value.length() != 10) {
                    Toast.makeText(getContext(), "Enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Update local cache and UI
                String name    = "name".equals(field)    ? value : sessionManager.getCachedName();
                String email   = "email".equals(field)   ? value : sessionManager.getCachedEmail();
                String phone   = "phone".equals(field)   ? value : sessionManager.getCachedPhone();
                String address = "address".equals(field) ? value : sessionManager.getCachedAddress();

                sessionManager.saveCustomerProfile(name, email, phone, address, currentImageUrl);

                // Refresh UI
                userName.setText(name);
                if (tvInfoName     != null) tvInfoName.setText(name);
                if (tvProfileEmail != null) tvProfileEmail.setText(email);
                pEmailEdit.setText(email);
                pPhoneEdit.setText(phone);
                pAddressEdit.setText(address);

                // Push to backend (name not pushed — no API field for it yet)
                if (!"name".equals(field)) {
                    updateCustomerRequest(email, phone, address);
                } else {
                    Toast.makeText(getContext(), "Name updated", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void wireToast(View root, int id, String msg) {
        View v = root.findViewById(id);
        if (v != null) v.setOnClickListener(x ->
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show());
    }

    private int dp(int dp) {
        return (int) (dp * requireContext().getResources().getDisplayMetrics().density);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Validation helper
    // ─────────────────────────────────────────────────────────────────────────
    private boolean validateFields(String email, String phone, String address) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Enter email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(), "Enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getContext(), "Enter phone number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (phone.length() != 10) {
            Toast.makeText(getContext(), "Enter valid 10-digit phone number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(getContext(), "Enter home city", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // API: update customer — UNCHANGED from original + cache update on success
    // ─────────────────────────────────────────────────────────────────────────
    private void updateCustomerRequest(String email, String phone, String address) {
        int custId = sessionManager.getCustomerId();
        if (custId == -1) {
            Toast.makeText(requireContext(), "Customer not logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        saveBtn.setEnabled(false);
        if (selectedImageUri != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference ref = storage.getReference()
                    .child("customer_profiles")
                    .child("customer_" + custId + ".jpg");
            ref.putFile(selectedImageUri)
                .addOnSuccessListener(task -> ref.getDownloadUrl()
                    .addOnSuccessListener(uri ->
                        updateCustomerOnServer(custId, email, phone, address, uri.toString()))
                    .addOnFailureListener(e -> {
                        saveBtn.setEnabled(true);
                        Toast.makeText(requireContext(), "Failed to get image URL", Toast.LENGTH_LONG).show();
                    }))
                .addOnFailureListener(e -> {
                    saveBtn.setEnabled(true);
                    Toast.makeText(requireContext(), "Image upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
        } else {
            updateCustomerOnServer(custId, email, phone, address, currentImageUrl);
        }
    }

    private void updateCustomerOnServer(int custId, String email, String phone,
                                        String address, String imageUrl) {
        CustomerUpdateRequest request = new CustomerUpdateRequest(email, phone, address, imageUrl);
        Call<CustomerResponse> call = apiService.updateCustomer(custId, request);
        call.enqueue(new Callback<CustomerResponse>() {
            @Override
            public void onResponse(@NonNull Call<CustomerResponse> call,
                                   @NonNull Response<CustomerResponse> response) {
                saveBtn.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    CustomerResponse c = response.body();
                    // Update cache with server-confirmed data
                    sessionManager.saveCustomerProfile(
                            c.getCustName(), c.getCustEmail(),
                            c.getCustPhone(), c.getCustAddress(), c.getImageUrl());
                    // Refresh UI
                    refreshUIFromResponse(c);
                    selectedImageUri = null;
                    editTextEditableOrNot(false);
                    onEdit = false;
                    Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Failed to update profile", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<CustomerResponse> call, @NonNull Throwable t) {
                saveBtn.setEnabled(true);
                Toast.makeText(requireContext(),
                    "Network error: " + t.getMessage() + "\nChanges saved locally.", Toast.LENGTH_LONG).show();
            }
        });
    }

    // ─────────────────────────────────────────────────────────────────────────
    // API: load customer — refresh + update cache on success
    // ─────────────────────────────────────────────────────────────────────────
    private void loadCustomerProfile() {
        int custId = sessionManager.getCustomerId();
        if (custId == -1) {
            Toast.makeText(requireContext(), "Customer not logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        Call<CustomerResponse> call = apiService.getCustomer(custId);
        call.enqueue(new Callback<CustomerResponse>() {
            @Override
            public void onResponse(@NonNull Call<CustomerResponse> call,
                                   @NonNull Response<CustomerResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CustomerResponse c = response.body();
                    // Update cache with fresh server data
                    sessionManager.saveCustomerProfile(
                            c.getCustName(), c.getCustEmail(),
                            c.getCustPhone(), c.getCustAddress(), c.getImageUrl());
                    refreshUIFromResponse(c);
                }
                // If not successful, the cached data shown by populateFromCache() remains
            }
            @Override
            public void onFailure(@NonNull Call<CustomerResponse> call, @NonNull Throwable t) {
                // Offline / wrong network — cached data is already showing, no-op
            }
        });
    }

    /** Update all UI views from a fresh CustomerResponse. */
    private void refreshUIFromResponse(CustomerResponse c) {
        if (!isAdded()) return; // fragment may have been removed
        String name    = c.getCustName()    != null ? c.getCustName()    : "";
        String email   = c.getCustEmail()   != null ? c.getCustEmail()   : "";
        String phone   = c.getCustPhone()   != null ? c.getCustPhone()   : "";
        String address = c.getCustAddress() != null ? c.getCustAddress() : "";
        String imgUrl  = c.getImageUrl()    != null ? c.getImageUrl()    : "";

        userName.setText(name);
        if (tvInfoName     != null) tvInfoName.setText(name);
        if (tvProfileEmail != null) tvProfileEmail.setText(email);
        pEmailEdit.setText(email);
        pPhoneEdit.setText(phone);
        pAddressEdit.setText(address);

        currentImageUrl = imgUrl;
        if (!TextUtils.isEmpty(imgUrl)) {
            Glide.with(this).load(imgUrl)
                .placeholder(R.drawable.ic_default_image)
                .error(R.drawable.ic_default_image)
                .circleCrop()
                .into(pUserImg);
        }
    }

    private void editTextEditableOrNot(boolean isEditable) {
        pAddressEdit.setEnabled(isEditable);
        pEmailEdit.setEnabled(isEditable);
        pPhoneEdit.setEnabled(isEditable);
    }
}