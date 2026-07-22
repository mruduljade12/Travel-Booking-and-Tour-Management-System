package com.mrudul.tourandtravel.user_activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.user_fragments.AssistantFragment;
import com.mrudul.tourandtravel.user_fragments.HomeFragment;
import com.mrudul.tourandtravel.user_fragments.MapFragment;
import com.mrudul.tourandtravel.user_fragments.ProfileFragment;
import com.mrudul.tourandtravel.user_fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                // id declare
                int id = menuItem.getItemId();
                int home = R.id.user_home;
                int search = R.id.user_search;
                int map = R.id.user_map;
                int ai = R.id.ai_assistant;
                int profile = R.id.user_profile;

                if (id == home){

                    //navigate to home fragment.
                    changeFragment(new HomeFragment());
                    return true;
                } else if (id == search){

                    //navigate to home fragment.
                    changeFragment(new SearchFragment());
                    return true;
                } else if (id == map){

                    //navigate to home fragment.
                    changeFragment(new MapFragment());
                    return true;
                } else if (id == ai){

                    //navigate to home fragment.
                    changeFragment(new AssistantFragment());
                    return true;
                } else if (id == profile){

                    //navigate to home fragment.
                    changeFragment(new ProfileFragment());
                    return true;
                }
                return false;
            }
        });

    }

    private void changeFragment(Fragment fragment){

        //code which replace the fragment when an item is clicked on navigation bar
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragmentContainerView,fragment)
                .commit();
    }
}