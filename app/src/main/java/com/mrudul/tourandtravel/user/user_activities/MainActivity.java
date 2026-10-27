package com.mrudul.tourandtravel.user.user_activities;

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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.network.ApiService;
import com.mrudul.tourandtravel.network.RetrofitClient;
import com.mrudul.tourandtravel.user.user_fragments.AssistantFragment;
import com.mrudul.tourandtravel.user.user_fragments.HomeFragment;
import com.mrudul.tourandtravel.user.user_fragments.ProfileFragment;
import com.mrudul.tourandtravel.user.user_fragments.SearchFragment;
import com.mrudul.tourandtravel.user.user_models.CustomerResponse;
import com.mrudul.tourandtravel.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TextView username;
    ImageView userImage;

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
        userImage = findViewById(R.id.mainUserImage);
        username = findViewById(R.id.mainUserName);

        SessionManager sessionManager = new SessionManager(this);
        ApiService apiService = RetrofitClient.getApiService();

        int custId = sessionManager.getCustomerId();

        if (custId == -1) {

            Toast.makeText(
                    this,
                    "Customer not logged in",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        Call<CustomerResponse> call = apiService.getCustomer(custId);

        call.enqueue(new Callback<CustomerResponse>() {

            @Override
            public void onResponse(
                    Call<CustomerResponse> call,
                    Response<CustomerResponse> response) {

                if (response.isSuccessful()
                        && response.body() != null) {

                    CustomerResponse customer = response.body();

                    username.setText(customer.getCustName());
                    if (customer.getImageUrl() != null && !customer.getImageUrl().isEmpty()) {

                        Glide.with(MainActivity.this)
                                .load(customer.getImageUrl())
                                .placeholder(R.drawable.ic_default_image)
                                .error(R.drawable.ic_default_image)
                                .into(userImage);
                    }

                } else {

                    Toast.makeText(
                            MainActivity.this,
                            "Failed to load profile",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    Call<CustomerResponse> call,
                    Throwable t) {

                Toast.makeText(
                        MainActivity.this,
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                // id declare
                int id = menuItem.getItemId();
                int home = R.id.user_home;
                int search = R.id.user_search;
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