package com.mrudul.tourandtravel.driver_activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.driver_fragments.DriverAssignPackageFragment;
import com.mrudul.tourandtravel.driver_fragments.DriverHomeFragment;
import com.mrudul.tourandtravel.driver_fragments.DriverProfileFragment;

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





        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.driverHome){
                    navigate("Home",new DriverHomeFragment());
                    return true;
                } else if (menuItem.getItemId() == R.id.driverAssignPackage) {
                    navigate("Package Assign",new DriverAssignPackageFragment());
                    return true;
                } else if (menuItem.getItemId() == R.id.driverProfile) {
                    navigate("Profile",new DriverProfileFragment());
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