package com.mrudul.tourandtravel.app_common_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.driver.driver_activities.DriverLoginActivity;
import com.mrudul.tourandtravel.driver.driver_activities.DriverRegistrationActivity;

/**
 * SmartTour360 Driver App — Splash Screen
 *
 * Entry point: user lands here on launch and taps a CTA to proceed.
 * No auto-navigation timer; all navigation is user-driven.
 */
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        // Push content clear of status bar + gesture navigation bar
        View root = findViewById(R.id.main);
        if (root != null) {
            ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // "Let's Get Started" → Driver Login
        View btnGetStarted = findViewById(R.id.btn_get_started);
        if (btnGetStarted != null) {
            btnGetStarted.setOnClickListener(v -> openDriverLogin());
        }

        // "Driver Login" text link → Driver Login
        View tvLoginLink = findViewById(R.id.tv_driver_login_link);
        if (tvLoginLink != null) {
            tvLoginLink.setOnClickListener(v -> openDriverLogin());
        }

        // "Register" text link → Driver Registration
        View tvRegisterLink = findViewById(R.id.tv_register_link);
        if (tvRegisterLink != null) {
            tvRegisterLink.setOnClickListener(v -> openDriverRegistration());
        }
    }

    private void openDriverLogin() {
        startActivity(new Intent(this, DriverLoginActivity.class));
    }

    private void openDriverRegistration() {
        startActivity(new Intent(this, DriverRegistrationActivity.class));
    }
}