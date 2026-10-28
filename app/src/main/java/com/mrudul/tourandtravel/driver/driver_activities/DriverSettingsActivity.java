package com.mrudul.tourandtravel.driver.driver_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mrudul.tourandtravel.R;

/**
 * SmartTour360 Driver App - Screen 9: Settings
 *
 * Opened from the gear icon on DriverProfileFragment.
 * Contains: Driver mini card, Preferences, About, Log Out.
 */
public class DriverSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_driver_settings);

        View root = findViewById(R.id.main);
        if (root != null) {
            ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
                Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                return insets;
            });
        }

        setupBack();
        setupRows();
        setupBottomNav();
    }

    private void setupBack() {
        View ivBack = findViewById(R.id.iv_set_back);
        if (ivBack != null) ivBack.setOnClickListener(v -> finish());
    }

    private void setupRows() {
        bindRow(R.id.row_notifications,    "Notifications - coming soon");
        bindRow(R.id.row_language,         "Language - coming soon");
        bindRow(R.id.row_change_password,  "Change Password - coming soon");
        bindRow(R.id.row_privacy,          "Privacy & Security - coming soon");
        bindRow(R.id.row_help,             "Help & Support - coming soon");
        bindRow(R.id.row_terms,            "Terms & Conditions - coming soon");

        View rowLogout = findViewById(R.id.row_logout);
        if (rowLogout != null) {
            rowLogout.setOnClickListener(v -> confirmLogout());
        }
    }

    private void bindRow(int rowId, String message) {
        View row = findViewById(rowId);
        if (row != null) {
            row.setOnClickListener(v ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
        }
    }

    private void confirmLogout() {
        // Navigate back to SplashScreen and clear back stack
        Intent intent = new Intent(this,
            com.mrudul.tourandtravel.app_common_activities.SplashScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setupBottomNav() {
        BottomNavigationView nav = findViewById(R.id.set_bottom_nav);
        if (nav == null) return;
        nav.setSelectedItemId(R.id.driverProfile);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.driverHome) {
                Intent intent = new Intent(this, DriverMainPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                return true;
            } else if (id == R.id.driverProfile) {
                finish(); // go back to Profile
                return true;
            }
            return false;
        });
    }
}