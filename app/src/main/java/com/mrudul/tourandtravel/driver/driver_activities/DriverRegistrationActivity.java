package com.mrudul.tourandtravel.driver.driver_activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mrudul.tourandtravel.R;

/**
 * SmartTour360 Driver App — Screen 3: Driver Register
 *
 * Provides full registration UI with:
 *  - Full Name, Email/Mobile, Password, Confirm Password
 *  - Client-side validation
 *  - Password visibility toggles
 *  - Navigation to Driver Login (already have an account)
 *
 * Backend registration logic to be connected in a future sprint
 * once the API layer is implemented.
 */
public class DriverRegistrationActivity extends AppCompatActivity {

    private EditText etFullName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private ImageView ivTogglePassword;
    private ImageView ivToggleConfirmPassword;
    private AppCompatButton btnRegister;
    private View tvLoginLink;

    private boolean isPasswordVisible        = false;
    private boolean isConfirmPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_driver_registration);

        // Edge-to-edge insets
        View root = findViewById(R.id.main);
        if (root != null) {
            ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        bindViews();
        setListeners();
    }

    private void bindViews() {
        etFullName             = findViewById(R.id.et_reg_full_name);
        etEmail                = findViewById(R.id.et_reg_email);
        etPassword             = findViewById(R.id.et_reg_password);
        etConfirmPassword      = findViewById(R.id.et_reg_confirm_password);
        ivTogglePassword       = findViewById(R.id.iv_toggle_reg_password);
        ivToggleConfirmPassword= findViewById(R.id.iv_toggle_confirm_password);
        btnRegister            = findViewById(R.id.btn_register);
        tvLoginLink            = findViewById(R.id.tv_reg_login_link);
    }

    private void setListeners() {

        // ── Register button ──
        if (btnRegister != null) {
            btnRegister.setOnClickListener(v -> attemptRegistration());
        }

        // ── Password visibility toggle ──
        if (ivTogglePassword != null) {
            ivTogglePassword.setOnClickListener(v -> {
                isPasswordVisible = toggleVisibility(etPassword, ivTogglePassword, isPasswordVisible);
            });
        }

        // ── Confirm password visibility toggle ──
        if (ivToggleConfirmPassword != null) {
            ivToggleConfirmPassword.setOnClickListener(v -> {
                isConfirmPasswordVisible = toggleVisibility(
                        etConfirmPassword, ivToggleConfirmPassword, isConfirmPasswordVisible);
            });
        }

        // ── "Already have an account? Login" ──
        if (tvLoginLink != null) {
            tvLoginLink.setOnClickListener(v -> {
                // Navigate back to Driver Login; clear this from back stack
                Intent intent = new Intent(DriverRegistrationActivity.this, DriverLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }
    }

    /**
     * Validates all fields then proceeds with registration.
     * Backend call to be wired here once the API layer is ready.
     */
    private void attemptRegistration() {
        if (etFullName == null || etEmail == null
                || etPassword == null || etConfirmPassword == null) return;

        String name            = etFullName.getText().toString().trim();
        String email           = etEmail.getText().toString().trim();
        String password        = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        // ── Validation ──
        if (TextUtils.isEmpty(name)) {
            etFullName.setError(getString(R.string.err_name_required));
            etFullName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            etEmail.setError(getString(R.string.err_email_required));
            etEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError(getString(R.string.err_password_required));
            etPassword.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError(getString(R.string.err_confirm_password_required));
            etConfirmPassword.requestFocus();
            return;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError(getString(R.string.err_passwords_no_match));
            etConfirmPassword.requestFocus();
            return;
        }

        // ── Registration logic placeholder ──
        // TODO: Replace with actual API call once backend is available.
        Toast.makeText(this, "Registration successful — connect API", Toast.LENGTH_SHORT).show();
    }

    /**
     * Toggles an EditText password field between visible and hidden.
     *
     * @return the new visibility state (true = visible)
     */
    private boolean toggleVisibility(EditText field, ImageView icon, boolean currentlyVisible) {
        if (field == null || icon == null) return currentlyVisible;

        if (currentlyVisible) {
            field.setTransformationMethod(PasswordTransformationMethod.getInstance());
            icon.setImageResource(R.drawable.ic_visibility_off);
        } else {
            field.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            icon.setImageResource(R.drawable.ic_visibility);
        }
        field.setSelection(field.getText().length());
        return !currentlyVisible;
    }
}