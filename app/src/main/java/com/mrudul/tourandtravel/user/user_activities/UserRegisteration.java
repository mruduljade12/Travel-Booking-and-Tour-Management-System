package com.mrudul.tourandtravel.user.user_activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.mrudul.tourandtravel.user.user_models.CustomerRegisterRequest;
import com.mrudul.tourandtravel.user.user_models.CustomerResponse;
import com.mrudul.tourandtravel.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegisteration extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etAddress;
    private EditText etPassword;

    private RadioGroup rgGender;
    private RadioButton rbMale;
    private RadioButton rbFemale;

    private Button btnRegister;
    private Button btnCancel;

    private TextView goToLogin;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_registeration);

        // Initialize views
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        etPassword = findViewById(R.id.etPassword);

        rgGender = findViewById(R.id.rgGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);

        btnRegister = findViewById(R.id.btnRegister);
        btnCancel = findViewById(R.id.btnCancel);

        goToLogin = findViewById(R.id.goToLogin);

        // Retrofit API
        apiService = RetrofitClient.getApiService();

        // Register button
        btnRegister.setOnClickListener(v -> registerCustomer());

        // Cancel button
        btnCancel.setOnClickListener(v -> finish());

        // Go to login
        goToLogin.setOnClickListener(v -> {
            // Change LoginActivity.class if your login activity
            // has a different package/class name.
            Intent intent = new Intent(
                    UserRegisteration.this,
                    Login.class
            );

            startActivity(intent);
            finish();
        });
    }

    private void registerCustomer() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String password = etPassword.getText().toString().trim();


        if (TextUtils.isEmpty(name)) {
            etName.setError("Enter your name");
            etName.requestFocus();
            return;
        }

        if (name.length() <= 6) {
            etName.setError("Name must be more than 6 characters");
            etName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Enter your email");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Enter your phone number");
            etPhone.requestFocus();
            return;
        }

        if (phone.length() != 10) {
            etPhone.setError("Phone number must contain 10 digits");
            etPhone.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(address)) {
            etAddress.setError("Enter your address");
            etAddress.requestFocus();
            return;
        }

        if (rgGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(
                    this,
                    "Please select gender",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Enter your password");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must contain at least 6 characters");
            etPassword.requestFocus();
            return;
        }

        String gender;

        int selectedGenderId = rgGender.getCheckedRadioButtonId();

        if (selectedGenderId == R.id.rbMale) {
            gender = "male";
        } else {
            gender = "female";
        }


        CustomerRegisterRequest request =
                new CustomerRegisterRequest(
                        name,
                        email,
                        phone,
                        address,
                        gender,
                        password,
                        null
                );

        // Disable button while request is running
        btnRegister.setEnabled(false);


        Call<CustomerResponse> call = apiService.registerCustomer(request);

        call.enqueue(new Callback<CustomerResponse>() {

            @Override
            public void onResponse(
                    Call<CustomerResponse> call,
                    Response<CustomerResponse> response) {

                btnRegister.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {

                    CustomerResponse customer = response.body();

                    SessionManager sessionManager = new SessionManager(UserRegisteration.this);
                    sessionManager.saveCustomerId(customer.getCustId());

                    Intent intent = new Intent(UserRegisteration.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {

                    Toast.makeText(
                            UserRegisteration.this,
                            "Registration failed: "
                                    + response.code(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    Call<CustomerResponse> call,
                    Throwable t) {

                btnRegister.setEnabled(true);

                Toast.makeText(
                        UserRegisteration.this,
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}