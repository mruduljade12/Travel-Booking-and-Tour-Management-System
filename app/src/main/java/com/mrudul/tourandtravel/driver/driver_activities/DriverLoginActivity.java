package com.mrudul.tourandtravel.driver.driver_activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
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
import com.mrudul.tourandtravel.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverLoginActivity extends AppCompatActivity {

    AppCompatButton driverLoginBtn;
    EditText emailEditText,passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_driver_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        driverLoginBtn = findViewById(R.id.driverLoginBtn);
        emailEditText = findViewById(R.id.driverEmailInput);
        passwordEditText = findViewById(R.id.driverPassword);

        driverLoginBtn.setOnClickListener(v->{
            loginDriver();
        });
    }


    private void loginDriver() {

        String email = emailEditText.getText().toString().trim();

        String accessCode = passwordEditText.getText().toString().trim();

        // Email validation
        if (TextUtils.isEmpty(email)) {

            emailEditText.setError(
                    "Enter email"
            );

            emailEditText.requestFocus();

            return;
        }

        // Access code validation
        if (TextUtils.isEmpty(accessCode)) {

            passwordEditText.setError(
                    "Enter access code"
            );

            passwordEditText.requestFocus();

            return;
        }

        driverLoginBtn.setEnabled(false);

        DriverLoginRequest request =
                new DriverLoginRequest(
                        email,
                        accessCode
                );

        Call<DriverLoginResponse> call =
                RetrofitClient
                        .getApiService()
                        .loginDriver(request);

        call.enqueue(new Callback<DriverLoginResponse>() {

            @Override
            public void onResponse(
                    Call<DriverLoginResponse> call,
                    Response<DriverLoginResponse> response) {

                driverLoginBtn.setEnabled(true);

                if (response.isSuccessful()
                        && response.body() != null) {

                    DriverLoginResponse driver = response.body();
                    DriverSessionManager sessionManager = new DriverSessionManager(DriverLoginActivity.this);

                    sessionManager.saveDriverId(driver.getDriverId());


                   Intent intent = new Intent(DriverLoginActivity.this,DriverMainPage.class);
                   startActivity(intent);
                   finish();

                } else {

                    Toast.makeText(
                            DriverLoginActivity.this,
                            "Invalid email or access code",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    Call<DriverLoginResponse> call,
                    Throwable t) {

                driverLoginBtn.setEnabled(true);

                Toast.makeText(
                        DriverLoginActivity.this,
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}