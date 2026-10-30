package com.mrudul.tourandtravel.driver.driver_activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mrudul.tourandtravel.R;

/**
 * SmartTour360 Driver App - Vehicle Details Screen
 *
 * Displays vehicle information (Toyota Innova Crysta) and
 * three verified document rows: RC Book, Driving License, Insurance.
 *
 * Opened from: Profile screen -> Vehicle Details row.
 */
public class DriverVehicleDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_driver_vehicle_details);

        View root = findViewById(R.id.main);
        if (root != null) {
            ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
                Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                return insets;
            });
        }

        setupBack();
        setupDocumentRows();
    }

    // ── Back navigation ────────────────────────────────────────

    private void setupBack() {
        View ivBack = findViewById(R.id.iv_veh_back);
        if (ivBack != null) ivBack.setOnClickListener(v -> finish());
    }

    // ── Document row click handlers ─────────────────────────────

    private void setupDocumentRows() {
        bindDocRow(R.id.row_rc_book,        "RC Book - coming soon");
        bindDocRow(R.id.row_driving_license, "Driving License - coming soon");
        bindDocRow(R.id.row_insurance,      "Insurance - coming soon");
    }

    private void bindDocRow(int rowId, String message) {
        View row = findViewById(rowId);
        if (row != null) {
            row.setOnClickListener(v ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
        }
    }
}