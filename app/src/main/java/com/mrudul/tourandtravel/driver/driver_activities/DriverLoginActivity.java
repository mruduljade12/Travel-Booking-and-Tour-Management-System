package com.mrudul.tourandtravel.driver.driver_activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.driver.driver_models.DriverLoginRequest;
import com.mrudul.tourandtravel.driver.driver_models.DriverLoginResponse;
import com.mrudul.tourandtravel.network.RetrofitClient;
import com.mrudul.tourandtravel.utils.DriverSessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * SmartTour360 Driver App — Screen 2: Driver Login
 *
 * Connects to the backend via Retrofit (DriverLoginRequest -> DriverLoginResponse).
 * Also includes: validation, password visibility toggle, Register navigation.
 */
public class DriverLoginActivity extends AppCompatActivity {

    private AppCompatButton driverLoginBtn;
    private EditText        emailEditText;
    private EditText        passwordEditText;
    private ImageView       ivTogglePassword;
    private TextView        tvForgotPassword;
    private TextView        tvRegisterLink;
    private View            btnGoogleLogin;

    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_driver_login);

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
        driverLoginBtn   = findViewById(R.id.driverLoginBtn);
        emailEditText    = findViewById(R.id.driverLoginUserName);
        passwordEditText = findViewById(R.id.driverPassword);
        ivTogglePassword = findViewById(R.id.iv_toggle_password);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvRegisterLink   = findViewById(R.id.tv_driver_register_link);
        btnGoogleLogin   = findViewById(R.id.btn_google_login);
    }

    private void setListeners() {

        if (driverLoginBtn != null) {
            driverLoginBtn.setOnClickListener(v -> attemptLogin());
        }

        if (ivTogglePassword != null) {
            ivTogglePassword.setOnClickListener(v -> togglePasswordVisibility());
        }

        if (tvForgotPassword != null) {
            tvForgotPassword.setOnClickListener(v ->
                Toast.makeText(this, "Forgot Password — coming soon", Toast.LENGTH_SHORT).show()
            );
        }

        if (btnGoogleLogin != null) {
            btnGoogleLogin.setOnClickListener(v ->
                Toast.makeText(this, "Google login — coming soon", Toast.LENGTH_SHORT).show()
            );
        }

        if (tvRegisterLink != null) {
            tvRegisterLink.setOnClickListener(v ->
                startActivity(new Intent(DriverLoginActivity.this, DriverRegistrationActivity.class))
            );
        }
    }

    private void attemptLogin() {
        if (emailEditText == null || passwordEditText == null) return;

        String email      = emailEditText.getText().toString().trim();
        String accessCode = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError(getString(R.string.err_email_required));
            emailEditText.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(accessCode)) {
            passwordEditText.setError(getString(R.string.err_password_required));
            passwordEditText.requestFocus();
            return;
        }

        driverLoginBtn.setEnabled(false);

        DriverLoginRequest request = new DriverLoginRequest(email, accessCode);

        RetrofitClient.getApiService().loginDriver(request)
                .enqueue(new Callback<DriverLoginResponse>() {

                    @Override
                    public void onResponse(Call<DriverLoginResponse> call,
                                           Response<DriverLoginResponse> response) {

                        driverLoginBtn.setEnabled(true);

                        if (response.isSuccessful() && response.body() != null) {
                            DriverLoginResponse driver = response.body();
                            new DriverSessionManager(DriverLoginActivity.this)
                                    .saveDriverId(driver.getDriverId());

                            Intent intent = new Intent(DriverLoginActivity.this, DriverMainPage.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(DriverLoginActivity.this,
                                    "Invalid email or access code", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DriverLoginResponse> call, Throwable t) {
                        driverLoginBtn.setEnabled(true);
                        Toast.makeText(DriverLoginActivity.this,
                                "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void togglePasswordVisibility() {
        if (passwordEditText == null || ivTogglePassword == null) return;

        if (isPasswordVisible) {
            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivTogglePassword.setImageResource(R.drawable.ic_visibility_off);
            isPasswordVisible = false;
        } else {
            passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            ivTogglePassword.setImageResource(R.drawable.ic_visibility);
            isPasswordVisible = true;
        }
        passwordEditText.setSelection(passwordEditText.getText().length());
    }
}
