package com.mrudul.tourandtravel.user.user_activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.mrudul.tourandtravel.R;

/**
 * Forgot Password screen — SmartTour360
 *
 * Validates email or phone input locally.
 * TODO: Connect to Spring Boot forgot-password endpoint when available.
 *       Expected endpoint: POST /api/customer/forgot-password
 *       Request body: { "emailOrPhone": "..." }
 */
public class ForgotPassword extends AppCompatActivity {

    private EditText fpEmailInput;
    private Button   fpSendBtn;
    private TextView fpLoginLink;
    private TextView fpBrandName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupStatusBar();
        setContentView(R.layout.activity_forgot_password);

        fpEmailInput = findViewById(R.id.fpEmailInput);
        fpSendBtn    = findViewById(R.id.fpSendBtn);
        fpLoginLink  = findViewById(R.id.fpLoginLink);
        fpBrandName  = findViewById(R.id.fpBrandName);

        // Two-tone brand name: "SmartTour" dark blue + "360" bright blue
        applyTwoToneBrand(fpBrandName);

        // Send Reset Link
        fpSendBtn.setOnClickListener(v -> handleSendReset());

        // Navigate back to Login
        fpLoginLink.setOnClickListener(v -> {
            startActivity(new Intent(ForgotPassword.this, Login.class));
            finish();
        });
    }

    private void handleSendReset() {
        String input = fpEmailInput.getText().toString().trim();

        // Validation 1: empty
        if (TextUtils.isEmpty(input)) {
            fpEmailInput.setError(getString(R.string.fp_error_empty));
            fpEmailInput.requestFocus();
            return;
        }

        // Validation 2: email or phone
        boolean looksLikeEmail = input.contains("@");
        if (looksLikeEmail) {
            if (!Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
                fpEmailInput.setError(getString(R.string.fp_error_invalid));
                fpEmailInput.requestFocus();
                return;
            }
        } else {
            // Phone number: digits only, 10-15 chars
            if (!input.matches("[+]?[0-9]{10,15}")) {
                fpEmailInput.setError(getString(R.string.fp_error_invalid));
                fpEmailInput.requestFocus();
                return;
            }
        }

        // Input is valid — disable button while "processing"
        fpSendBtn.setEnabled(false);

        // TODO: Replace this with actual API call when backend endpoint is ready.
        //       POST /api/customer/forgot-password  { emailOrPhone: input }
        //       On success: show success Toast and navigate to Login.
        //       On failure: re-enable button and show error.

        // Simulated success for now (UI-ready placeholder)
        Toast.makeText(this, getString(R.string.fp_success_toast), Toast.LENGTH_LONG).show();
        fpSendBtn.setEnabled(true);

        // Navigate back to login after sending
        startActivity(new Intent(ForgotPassword.this, Login.class));
        finish();
    }

    private void applyTwoToneBrand(TextView tv) {
        // "SmartTour" (0-9) → dark navy  "#0B3B78"
        // "360"       (9-12) → bright blue "#1899E8"
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