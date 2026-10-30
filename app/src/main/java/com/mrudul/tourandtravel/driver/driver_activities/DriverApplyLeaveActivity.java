package com.mrudul.tourandtravel.driver.driver_activities;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.driver.driver_fragments.DriverLeaveFragment;

/**
 * Thin host Activity for DriverLeaveFragment.
 * Opened from Profile screen -> Apply For Leave row.
 * Allows the leave form to run as a standalone screen
 * independently of the bottom nav Schedule tab.
 */
public class DriverApplyLeaveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_driver_apply_leave);

        View root = findViewById(R.id.leave_activity_root);
        if (root != null) {
            ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
                Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                return insets;
            });
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.leave_fragment_container, new DriverLeaveFragment())
                    .commit();
        }
    }
}