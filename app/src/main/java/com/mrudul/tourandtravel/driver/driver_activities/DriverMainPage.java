package com.mrudul.tourandtravel.driver.driver_activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.driver.driver_fragments.DriverHomeFragment;
import com.mrudul.tourandtravel.driver.driver_fragments.DriverLeaveFragment;
import com.mrudul.tourandtravel.driver.driver_fragments.DriverProfileFragment;
import com.mrudul.tourandtravel.driver.driver_models.DriverLoginResponse;
import com.mrudul.tourandtravel.network.ApiService;
import com.mrudul.tourandtravel.network.RetrofitClient;
import com.mrudul.tourandtravel.user.user_activities.MainActivity;
import com.mrudul.tourandtravel.user.user_models.CustomerResponse;
import com.mrudul.tourandtravel.utils.DriverSessionManager;
import com.mrudul.tourandtravel.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverMainPage extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageView driverImage;
    TextView driverName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_driver_main_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.driverBottomNavigation);
        driverImage = findViewById(R.id.driverImage);
        driverName = findViewById(R.id.driverName);


        DriverSessionManager driverSessionManager = new DriverSessionManager(DriverMainPage.this);
        ApiService apiService = RetrofitClient.getApiService();

        int driverId = driverSessionManager.getDriverId();

        if (driverId == -1) {

            Toast.makeText(
                    this,
                    "Driver not logged in",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        Call<DriverLoginResponse> call = apiService.getDriver(driverId);

        call.enqueue(new Callback<DriverLoginResponse>() {

            @Override
            public void onResponse(
                    Call<DriverLoginResponse> call,
                    Response<DriverLoginResponse> response) {

                if (response.isSuccessful()
                        && response.body() != null) {

                    DriverLoginResponse driver = response.body();

                    driverName.setText(driver.getDriverName());
                    if (driver.getProfileImageUrl() != null && !driver.getProfileImageUrl().isEmpty()) {

                        Glide.with(DriverMainPage.this)
                                .load(driver.getProfileImageUrl())
                                .placeholder(R.drawable.ic_default_image)
                                .error(R.drawable.ic_default_image)
                                .into(driverImage);
                    }

                } else {

                    Toast.makeText(
                            DriverMainPage.this,
                            "Failed to load profile",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    Call<DriverLoginResponse> call,
                    Throwable t) {

                Toast.makeText(
                        DriverMainPage.this,
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });




        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.driverHome){
                    navigate("Home",new DriverHomeFragment());
                    return true;
                } else if (menuItem.getItemId() == R.id.driverProfile) {
                    navigate("Profile",new DriverProfileFragment());
                    return true;
                } else if (menuItem.getItemId() == R.id.driverAssignPackage){
                    navigate("Leave",new DriverLeaveFragment());
                    return true;
                }

                return false;
            }
        });
    }


    private void navigate(String name, Fragment fragment){

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.driverFragmentContainer,fragment)
                .addToBackStack(name)
                .commit();
    }
}