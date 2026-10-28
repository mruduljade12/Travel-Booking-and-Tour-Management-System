package com.mrudul.tourandtravel.driver.driver_activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.driver.driver_fragments.DriverEarningsFragment;
import com.mrudul.tourandtravel.driver.driver_fragments.DriverHomeFragment;
import com.mrudul.tourandtravel.driver.driver_fragments.DriverScheduleFragment;
import com.mrudul.tourandtravel.driver.driver_fragments.DriverProfileFragment;
import com.mrudul.tourandtravel.driver.driver_models.DriverLoginResponse;
import com.mrudul.tourandtravel.network.RetrofitClient;
import com.mrudul.tourandtravel.utils.DriverSessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * SmartTour360 Driver App — Driver Main Page (host Activity)
 *
 * Bottom nav tabs:
 *   Home     -> DriverHomeFragment
 *   Schedule -> DriverScheduleFragment
 *   Earnings -> DriverEarningsFragment
 *   Profile  -> DriverProfileFragment
 */
public class DriverMainPage extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ImageView driverImage;
    private TextView  driverName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_driver_main_page);

        View root = findViewById(R.id.main);
        if (root != null) {
            ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        bottomNavigationView = findViewById(R.id.driverBottomNavigation);
        driverImage          = findViewById(R.id.driverImage);
        driverName           = findViewById(R.id.driverName);

        if (savedInstanceState == null) {
            navigate("Home", new DriverHomeFragment());
            if (bottomNavigationView != null) {
                bottomNavigationView.setSelectedItemId(R.id.driverHome);
            }
        }

        // Load driver profile into the header
        DriverSessionManager driverSessionManager = new DriverSessionManager(DriverMainPage.this);
        int driverId = driverSessionManager.getDriverId();

        if (driverId == -1) {
            Toast.makeText(this, "Driver not logged in", Toast.LENGTH_SHORT).show();
        } else {
            RetrofitClient.getApiService().getDriver(driverId)
                    .enqueue(new Callback<DriverLoginResponse>() {
                        @Override
                        public void onResponse(Call<DriverLoginResponse> call,
                                               Response<DriverLoginResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                DriverLoginResponse driver = response.body();
                                if (driverName != null) driverName.setText(driver.getDriverName());
                                if (driverImage != null
                                        && driver.getProfileImageUrl() != null
                                        && !driver.getProfileImageUrl().isEmpty()) {
                                    Glide.with(DriverMainPage.this)
                                            .load(driver.getProfileImageUrl())
                                            .placeholder(R.drawable.ic_default_image)
                                            .error(R.drawable.ic_default_image)
                                            .into(driverImage);
                                }
                            } else {
                                Toast.makeText(DriverMainPage.this,
                                        "Failed to load profile", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<DriverLoginResponse> call, Throwable t) {
                            Toast.makeText(DriverMainPage.this,
                                    "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }

        if (bottomNavigationView != null) {
            bottomNavigationView.setOnItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.driverHome) {
                    navigate("Home", new DriverHomeFragment());
                    return true;
                } else if (id == R.id.driverLeave) {
                    navigate("Schedule", new DriverScheduleFragment());
                    return true;
                } else if (id == R.id.driverEarnings) {
                    navigate("Earnings", new DriverEarningsFragment());
                    return true;
                } else if (id == R.id.driverProfile) {
                    navigate("Profile", new DriverProfileFragment());
                    return true;
                }
                return false;
            });
        }
    }

    private void navigate(String tag, Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.driverFragmentContainer, fragment, tag)
                .commit();
    }
}
