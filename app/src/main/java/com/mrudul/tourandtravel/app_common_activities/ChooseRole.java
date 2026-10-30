package com.mrudul.tourandtravel.app_common_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.driver.driver_activities.DriverLoginActivity;
import com.mrudul.tourandtravel.tour_guide.tour_guide_activities.TourGuideLoginActivity;
import com.mrudul.tourandtravel.user.user_activities.Login;

/**
 * Choose Your Role — SmartTour360
 * Three role cards: Customer, Driver, Tour Guide.
 * All navigation destinations are the existing activities.
 */
public class ChooseRole extends AppCompatActivity {

    private CardView customer, driver, tourGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Keep status bar light with dark icons to match white background
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);

        setContentView(R.layout.activity_choose_role);

        customer  = findViewById(R.id.customerRole);
        driver    = findViewById(R.id.driverRole);
        tourGuide = findViewById(R.id.tourGuideRole);

        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseRole.this, Login.class));
                finish();
            }
        });

        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseRole.this, DriverLoginActivity.class));
                finish();
            }
        });

        tourGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseRole.this, TourGuideLoginActivity.class));
                finish();
            }
        });
    }
}