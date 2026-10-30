package com.mrudul.tourandtravel.user.user_activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.network.ApiService;
import com.mrudul.tourandtravel.network.RetrofitClient;
import com.mrudul.tourandtravel.user.user_models.CustomerRegisterRequest;
import com.mrudul.tourandtravel.user.user_models.CustomerResponse;
import com.mrudul.tourandtravel.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Registration screen — SmartTour360
 *
 * New UI fields:
 *   regEmailMobile    — combined email/mobile field (replaces separate etEmail + etPhone visually)
 *   regPasswordToggle — password show/hide
 *   regConfirmPassword — confirm password
 *   regConfirmToggle  — confirm password show/hide
 *   regTermsCheck     — terms & conditions checkbox
 *   regLoginLink      — navigate to Login
 *
 * Legacy IDs preserved (used by existing API call):
 *   etName, etEmail, etPhone, etAddress, etPassword,
 *   rgGender, rbMale, rbFemale, btnRegister, btnCancel, goToLogin
 */
public class UserRegisteration extends AppCompatActivity {

    // ── Existing fields (API still uses these) ───────────────────────────────
    private EditText    etName;
    private EditText    etEmail;      // hidden — populated from regEmailMobile when valid email
    private EditText    etPhone;      // hidden — populated from regEmailMobile when valid phone
    private EditText    etAddress;    // hidden — defaulted to "N/A" for API compat
    private EditText    etPassword;
    private RadioGroup  rgGender;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private Button      btnRegister;
    private Button      btnCancel;
    private TextView    goToLogin;

    // ── New UI fields ─────────────────────────────────────────────────────────
    private EditText    regEmailMobile;
    private ImageView   regPasswordToggle;
    private EditText    regConfirmPassword;
    private ImageView   regConfirmToggle;
    private CheckBox    regTermsCheck;
    private TextView    regLoginLink;
    private TextView    regBrandName;

    private boolean passwordVisible = false;
    private boolean confirmVisible  = false;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupStatusBar();
        setContentView(R.layout.activity_user_registeration);

        // ── Bind existing views ───────────────────────────────────────────────
        etName       = findViewById(R.id.etName);
        etEmail      = findViewById(R.id.etEmail);
        etPhone      = findViewById(R.id.etPhone);
        etAddress    = findViewById(R.id.etAddress);
        etPassword   = findViewById(R.id.etPassword);
        rgGender     = findViewById(R.id.rgGender);
        rbMale       = findViewById(R.id.rbMale);
        rbFemale     = findViewById(R.id.rbFemale);
        btnRegister  = findViewById(R.id.btnRegister);
        btnCancel    = findViewById(R.id.btnCancel);
        goToLogin    = findViewById(R.id.goToLogin);

        // ── Bind new views ────────────────────────────────────────────────────
        regEmailMobile    = findViewById(R.id.regEmailMobile);
        regPasswordToggle = findViewById(R.id.regPasswordToggle);
        regConfirmPassword= findViewById(R.id.regConfirmPassword);
        regConfirmToggle  = findViewById(R.id.regConfirmToggle);
        regTermsCheck     = findViewById(R.id.regTermsCheck);
        regLoginLink      = findViewById(R.id.regLoginLink);
        regBrandName      = findViewById(R.id.regBrandName);

        apiService = RetrofitClient.getApiService();

        // Two-tone brand name
        applyTwoToneBrand(regBrandName);

        // Register button
        btnRegister.setOnClickListener(v -> registerCustomer());

        // Password visibility toggle
        if (regPasswordToggle != null) {
            regPasswordToggle.setOnClickListener(v -> {
                passwordVisible = !passwordVisible;
                if (passwordVisible) {
                    etPassword.setTransformationMethod(
                            HideReturnsTransformationMethod.getInstance());
                    regPasswordToggle.setImageResource(R.drawable.ic_eye_on);
                } else {
                    etPassword.setTransformationMethod(
                            PasswordTransformationMethod.getInstance());
                    regPasswordToggle.setImageResource(R.drawable.ic_eye_off);
                }
                etPassword.setSelection(etPassword.getText().length());
            });
        }

        // Confirm password visibility toggle
        if (regConfirmToggle != null) {
            regConfirmToggle.setOnClickListener(v -> {
                confirmVisible = !confirmVisible;
                if (confirmVisible) {
                    regConfirmPassword.setTransformationMethod(
                            HideReturnsTransformationMethod.getInstance());
                    regConfirmToggle.setImageResource(R.drawable.ic_eye_on);
                } else {
                    regConfirmPassword.setTransformationMethod(
                            PasswordTransformationMethod.getInstance());
                    regConfirmToggle.setImageResource(R.drawable.ic_eye_off);
                }
                regConfirmPassword.setSelection(regConfirmPassword.getText().length());
            });
        }

        // New Login link
        if (regLoginLink != null) {
            regLoginLink.setOnClickListener(v -> {
                startActivity(new Intent(UserRegisteration.this, Login.class));
                finish();
            });
        }

        // Legacy cancel (hidden but kept for Java compat)
        if (btnCancel != null) {
            btnCancel.setOnClickListener(v -> finish());
        }

        // Legacy goToLogin (hidden but kept for Java compat)
        if (goToLogin != null) {
            goToLogin.setOnClickListener(v -> {
                startActivity(new Intent(UserRegisteration.this, Login.class));
                finish();
            });
        }
    }

    // ────────────────────────────────────────────────────────────────────────
    // Registration with existing API — unchanged logic, extended validation
    // ────────────────────────────────────────────────────────────────────────
    private void registerCustomer() {

        String name     = etName.getText().toString().trim();
        String input    = (regEmailMobile != null)
                          ? regEmailMobile.getText().toString().trim() : "";
        String password = etPassword.getText().toString().trim();
        String confirm  = (regConfirmPassword != null)
                          ? regConfirmPassword.getText().toString().trim() : "";

        // 1. Full Name
        if (TextUtils.isEmpty(name)) {
            etName.setError(getString(R.string.reg_err_name_empty));
            etName.requestFocus();
            return;
        }
        if (name.length() <= 6) {
            etName.setError(getString(R.string.reg_err_name_short));
            etName.requestFocus();
            return;
        }

        // 2. Email or Mobile
        if (TextUtils.isEmpty(input)) {
            if (regEmailMobile != null) {
                regEmailMobile.setError(getString(R.string.reg_err_email_empty));
                regEmailMobile.requestFocus();
            }
            return;
        }

        String email = "";
        String phone = "";

        if (input.contains("@")) {
            if (!Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
                if (regEmailMobile != null) {
                    regEmailMobile.setError(getString(R.string.reg_err_email_invalid));
                    regEmailMobile.requestFocus();
                }
                return;
            }
            email = input;
            phone = "0000000000"; // placeholder phone for API
        } else {
            if (!input.matches("[+]?[0-9]{10,15}")) {
                if (regEmailMobile != null) {
                    regEmailMobile.setError(getString(R.string.reg_err_email_invalid));
                    regEmailMobile.requestFocus();
                }
                return;
            }
            phone = input;
            email = input + "@mobile.local"; // placeholder email for API
        }

        // 3. Password
        if (TextUtils.isEmpty(password)) {
            etPassword.setError(getString(R.string.reg_err_pwd_empty));
            etPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            etPassword.setError(getString(R.string.reg_err_pwd_short));
            etPassword.requestFocus();
            return;
        }

        // 4. Confirm Password
        if (regConfirmPassword != null) {
            if (TextUtils.isEmpty(confirm)) {
                regConfirmPassword.setError(getString(R.string.reg_err_confirm_empty));
                regConfirmPassword.requestFocus();
                return;
            }
            if (!password.equals(confirm)) {
                regConfirmPassword.setError(getString(R.string.reg_err_pwd_mismatch));
                regConfirmPassword.requestFocus();
                return;
            }
        }

        // 5. Terms & Conditions
        if (regTermsCheck != null && !regTermsCheck.isChecked()) {
            Toast.makeText(this, getString(R.string.reg_err_terms), Toast.LENGTH_SHORT).show();
            return;
        }

        // ── Build request using existing API model ───────────────────────────
        // gender defaults to "male" (hidden field — not collected in new UI)
        CustomerRegisterRequest request = new CustomerRegisterRequest(
                name, email, phone, "N/A", "male", password, null
        );

        btnRegister.setEnabled(false);

        Call<CustomerResponse> call = apiService.registerCustomer(request);
        call.enqueue(new Callback<CustomerResponse>() {
            @Override
            public void onResponse(Call<CustomerResponse> call,
                                   Response<CustomerResponse> response) {
                btnRegister.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    CustomerResponse customer = response.body();
                    SessionManager sessionManager =
                            new SessionManager(UserRegisteration.this);
                    sessionManager.saveCustomerId(customer.getCustId());
                    startActivity(new Intent(UserRegisteration.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(UserRegisteration.this,
                            "Registration failed: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CustomerResponse> call, Throwable t) {
                btnRegister.setEnabled(true);
                Toast.makeText(UserRegisteration.this,
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void applyTwoToneBrand(TextView tv) {
        if (tv == null) return;
        SpannableString s = new SpannableString("SmartTour360");
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#0B3B78")),
                0, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#1899E8")),
                9, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(s);
    }

    private void setupStatusBar() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        WindowInsetsControllerCompat ctrl =
                WindowCompat.getInsetsController(window, window.getDecorView());
        if (ctrl != null) {
            ctrl.setAppearanceLightStatusBars(false);
        }
    }
}