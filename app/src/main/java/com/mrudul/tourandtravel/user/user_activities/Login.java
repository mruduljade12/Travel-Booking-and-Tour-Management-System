package com.mrudul.tourandtravel.user.user_activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.network.ApiService;
import com.mrudul.tourandtravel.network.RetrofitClient;
import com.mrudul.tourandtravel.user.user_models.CustomerLoginRequest;
import com.mrudul.tourandtravel.user.user_models.CustomerResponse;
import com.mrudul.tourandtravel.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private EditText     userLoginEmail;
    private EditText     userLoginPassword;
    private Button       userLoginBtn;
    private TextView     regPageLink;

    private ImageView    passwordToggleIcon;
    private CheckBox     rememberMeCheck;
    private TextView     forgotPasswordLink;
    private LinearLayout googleLoginBtn;
    private LinearLayout guestLoginBtn;

    private boolean passwordVisible = false;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupStatusBar();
        setContentView(R.layout.activity_login);

        userLoginEmail     = findViewById(R.id.userLoginEmail);
        userLoginPassword  = findViewById(R.id.userLoginPassword);
        userLoginBtn       = findViewById(R.id.userLoginBtn);
        regPageLink        = findViewById(R.id.regPageLink);
        passwordToggleIcon = findViewById(R.id.passwordToggleIcon);
        rememberMeCheck    = findViewById(R.id.rememberMeCheck);
        forgotPasswordLink = findViewById(R.id.forgotPasswordLink);
        googleLoginBtn     = findViewById(R.id.googleLoginBtn);
        guestLoginBtn      = findViewById(R.id.guestLoginBtn);

        apiService = RetrofitClient.getApiService();

        userLoginBtn.setOnClickListener(v -> loginCustomer());

        if (passwordToggleIcon != null) {
            passwordToggleIcon.setOnClickListener(v -> {
                passwordVisible = !passwordVisible;
                if (passwordVisible) {
                    userLoginPassword.setTransformationMethod(
                            HideReturnsTransformationMethod.getInstance());
                    passwordToggleIcon.setImageResource(R.drawable.ic_eye_on);
                } else {
                    userLoginPassword.setTransformationMethod(
                            PasswordTransformationMethod.getInstance());
                    passwordToggleIcon.setImageResource(R.drawable.ic_eye_off);
                }
                userLoginPassword.setSelection(userLoginPassword.getText().length());
            });
        }

        if (forgotPasswordLink != null) {
            forgotPasswordLink.setOnClickListener(v -> {
                startActivity(new Intent(Login.this, ForgotPassword.class));
            });
        }

        if (googleLoginBtn != null) {
            googleLoginBtn.setOnClickListener(v ->
                Toast.makeText(this, "Google sign-in coming soon", Toast.LENGTH_SHORT).show());
        }

        if (guestLoginBtn != null) {
            guestLoginBtn.setOnClickListener(v -> {
                startActivity(new Intent(Login.this, MainActivity.class));
                finish();
            });
        }

        regPageLink.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, UserRegisteration.class));
            finish();
        });
    }

    private void loginCustomer() {
        String email    = userLoginEmail.getText().toString().trim();
        String password = userLoginPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            userLoginEmail.setError("Enter email");
            userLoginEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userLoginEmail.setError("Enter a valid email");
            userLoginEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            userLoginPassword.setError("Enter password");
            userLoginPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            userLoginPassword.setError("Password must be at least 6 characters");
            userLoginPassword.requestFocus();
            return;
        }

        CustomerLoginRequest request = new CustomerLoginRequest(email, password);
        userLoginBtn.setEnabled(false);

        Call<CustomerResponse> call = apiService.loginCustomer(request);
        call.enqueue(new Callback<CustomerResponse>() {
            @Override
            public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                userLoginBtn.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    CustomerResponse customer = response.body();
                    SessionManager sessionManager = new SessionManager(Login.this);
                    sessionManager.saveCustomerId(customer.getCustId());
                    // Cache profile fields so ProfileFragment shows data even offline
                    sessionManager.saveCustomerProfile(
                            customer.getCustName(),
                            customer.getCustEmail(),
                            customer.getCustPhone(),
                            customer.getCustAddress(),
                            customer.getImageUrl());
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(Login.this, "Invalid email or password", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<CustomerResponse> call, Throwable t) {
                userLoginBtn.setEnabled(true);
                Toast.makeText(Login.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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