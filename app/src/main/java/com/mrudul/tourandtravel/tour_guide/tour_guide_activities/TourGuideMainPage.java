package com.mrudul.tourandtravel.tour_guide.tour_guide_activities;

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
import androidx.fragment.app.FragmentContainerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.network.RetrofitClient;
import com.mrudul.tourandtravel.tour_guide.tour_guide_fragments.TourGuideHomeFragment;
import com.mrudul.tourandtravel.tour_guide.tour_guide_fragments.TourGuideLeaveFragment;
import com.mrudul.tourandtravel.tour_guide.tour_guide_fragments.TourGuideProfileFragment;
import com.mrudul.tourandtravel.tour_guide.tour_guide_models.TourGuideLoginResponse;
import com.mrudul.tourandtravel.utils.TourGuideSessionManger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TourGuideMainPage extends AppCompatActivity {
    FragmentContainerView fragmentView;
    BottomNavigationView bottomNavigation;
    ImageView tourGuideImage;
    TextView tourGuideName;

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
        tourGuideImage =  findViewById(R.id.tourGuideImage);
        tourGuideName = findViewById(R.id.tourGuideName);


        TourGuideSessionManger sessionManger = new TourGuideSessionManger(TourGuideMainPage.this);
        int guideId = sessionManger.getGuideId();

        if (guideId == -1){
            Toast.makeText(this, "Guide is not loggin", Toast.LENGTH_SHORT).show();
        } else {

            Call<TourGuideLoginResponse> call = RetrofitClient.getApiService().getTourGuide(guideId);
            call.enqueue(new Callback<TourGuideLoginResponse>() {
                @Override
                public void onResponse(Call<TourGuideLoginResponse> call, Response<TourGuideLoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null){
                        TourGuideLoginResponse loginResponse = response.body();

                        Glide.with(TourGuideMainPage.this)
                                .load(loginResponse.getProfileImageUrl())
                                .placeholder(R.drawable.ic_default_image)
                                .error(R.drawable.ic_default_image)
                                .into(tourGuideImage);
                        tourGuideName.setText(loginResponse.getGuideName());
                    }
                }

                @Override
                public void onFailure(Call<TourGuideLoginResponse> call, Throwable t) {
                    Toast.makeText(TourGuideMainPage.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }



        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.tourGuideHome){
                    navigate("tour guide home page",new TourGuideHomeFragment());
                    return true;
                } else if (menuItem.getItemId() == R.id.tourGuideLeave){
                    navigate("tour guide schedule page",new TourGuideLeaveFragment());
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