package com.mrudul.tourandtravel.tour_guide.tour_guide_activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.tour_guide.tour_guide_fragments.TourGuideHomeFragment;
import com.mrudul.tourandtravel.tour_guide.tour_guide_fragments.TourGuideProfileFragment;
import com.mrudul.tourandtravel.tour_guide.tour_guide_fragments.TourGuideScheduleFragment;

public class TourGuidePage extends AppCompatActivity {
    FragmentContainerView fragmentView;
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tour_guide_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fragmentView = findViewById(R.id.tourGuideFragmentContainer);
        bottomNavigation = findViewById(R.id.tourGuideBottomNavigation);

        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.tourGuideHome){
                    navigate("tour guide home page",new TourGuideHomeFragment());
                    return true;
                } else if (menuItem.getItemId() == R.id.tourGuideSchedule){
                    navigate("tour guide schedule page",new TourGuideScheduleFragment());
                    return true;
                } else if (menuItem.getItemId() == R.id.tourGuideProfile) {
                    navigate("toue guide profile page",new TourGuideProfileFragment());
                    return true;
                }

                return false;
            }
        });
    }

    private void navigate(String name, Fragment fragment){

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.tourGuideFragmentContainer,fragment)
                .addToBackStack(name)
                .commit();
    }
}