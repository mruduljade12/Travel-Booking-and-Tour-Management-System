package com.mrudul.tourandtravel.user.user_activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

    private EditText userLoginEmail;
    private EditText userLoginPassword;

    private Button userLoginBtn;
    private Button cancelBtn;

    private TextView regPageLink;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userLoginEmail = findViewById(R.id.userLoginEmail);
        userLoginPassword = findViewById(R.id.userLoginPassword);
        userLoginBtn = findViewById(R.id.userLoginBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        regPageLink = findViewById(R.id.regPageLink);
        apiService = RetrofitClient.getApiService();

        //login button logic
        userLoginBtn.setOnClickListener(v -> loginCustomer());


        //cancel button logic
        cancelBtn.setOnClickListener(v -> finish());


        //link logic
        regPageLink.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, UserRegisteration.class);
            startActivity(intent);
            finish();
        });
    }

    private void loginCustomer() {

        String email = userLoginEmail.getText().toString().trim();
        String password = userLoginPassword.getText().toString().trim();

        // Email validation
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

        // Password validation
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

        // Create login request
        CustomerLoginRequest request = new CustomerLoginRequest(email, password);

        // Disable button while request is running
        userLoginBtn.setEnabled(false);

        // Send request to Spring Boot
        Call<CustomerResponse> call = apiService.loginCustomer(request);

        call.enqueue(new Callback<CustomerResponse>() {

            @Override
            public void onResponse(
                    Call<CustomerResponse> call,
                    Response<CustomerResponse> response) {

                userLoginBtn.setEnabled(true);

                if (response.isSuccessful()
                        && response.body() != null) {

                    CustomerResponse customer = response.body();

                    SessionManager sessionManager = new SessionManager(Login.this);
                    sessionManager.saveCustomerId(customer.getCustId());
                    sessionManager.saveCustomerEmail(customer.getCustEmail());
                    sessionManager.saveCustomerPhone(customer.getCustPhone());
                    sessionManager.setKeyCustName(customer.getCustName());
                    sessionManager.saveCustomerImage(customer.getImageUrl());

                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {

                    Toast.makeText(
                            Login.this,
                            "Invalid email or password",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    Call<CustomerResponse> call,
                    Throwable t) {

                userLoginBtn.setEnabled(true);

                Toast.makeText(
                        Login.this,
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}