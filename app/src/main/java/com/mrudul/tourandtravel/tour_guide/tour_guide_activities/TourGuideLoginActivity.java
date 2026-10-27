package com.mrudul.tourandtravel.tour_guide.tour_guide_activities;

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
import com.mrudul.tourandtravel.driver.driver_activities.DriverLoginActivity;
import com.mrudul.tourandtravel.network.RetrofitClient;
import com.mrudul.tourandtravel.tour_guide.tour_guide_models.TourGuideLoginRequest;
import com.mrudul.tourandtravel.tour_guide.tour_guide_models.TourGuideLoginResponse;
import com.mrudul.tourandtravel.utils.TourGuideSessionManger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TourGuideLoginActivity extends AppCompatActivity {

    AppCompatButton btnGuideLogin,cancelBtn;
    EditText tourGuideEmail,tourGuideAccessCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tour_guide_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnGuideLogin = findViewById(R.id.btnGuideLogin);
        cancelBtn = findViewById(R.id.btnGuideCancel);
        tourGuideEmail = findViewById(R.id.etGuideEmail);
        tourGuideAccessCode = findViewById(R.id.etGuidePassword);

        btnGuideLogin.setOnClickListener(v->{
            loginDriver();
        });

        cancelBtn.setOnClickListener(v->{
            finish();
        });
    }

    private void loginDriver() {
        String email = tourGuideEmail.getText().toString().trim();
        String accessCode = tourGuideAccessCode.getText().toString().trim();

        // Email validation
        if (TextUtils.isEmpty(email)) {

            tourGuideEmail.setError(
                    "Enter email"
            );

            tourGuideEmail.requestFocus();

            return;
        }

        // Access code validation
        if (TextUtils.isEmpty(accessCode)) {

            tourGuideAccessCode.setError(
                    "Enter access code"
            );

            tourGuideAccessCode.requestFocus();

            return;
        }


        btnGuideLogin.setEnabled(false);

        TourGuideLoginRequest request = new TourGuideLoginRequest(email,accessCode);

        Call<TourGuideLoginResponse> call = RetrofitClient.getApiService().loginTourGuide(request);

        call.enqueue(new Callback<TourGuideLoginResponse>() {
            @Override
            public void onResponse(Call<TourGuideLoginResponse> call, Response<TourGuideLoginResponse> response) {
                btnGuideLogin.setEnabled(true);

                if (response.body() != null && response.isSuccessful()){

                    TourGuideLoginResponse loginResponse = response.body();
                    TourGuideSessionManger sessionManger = new TourGuideSessionManger(TourGuideLoginActivity.this);
                    sessionManger.saveGuideId(loginResponse.getGuideId());
                    sessionManger.setKeyGuideImage(loginResponse.getProfileImageUrl());

                    Intent intent =  new Intent(TourGuideLoginActivity.this,TourGuideMainPage.class);
                    startActivity(intent);
                    finish();
                } else {

                    Toast.makeText(
                            TourGuideLoginActivity.this,
                            "Invalid email or access code",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<TourGuideLoginResponse> call, Throwable t) {
                btnGuideLogin.setEnabled(true);
                Toast.makeText(TourGuideLoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}