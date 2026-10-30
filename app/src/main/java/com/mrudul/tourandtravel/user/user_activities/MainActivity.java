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
import com.mrudul.tourandtravel.user.user_fragments.MapFragment;
import com.mrudul.tourandtravel.user.user_fragments.ProfileFragment;
import com.mrudul.tourandtravel.user.user_fragments.SearchFragment;
import com.mrudul.tourandtravel.user.user_models.CustomerResponse;
import com.mrudul.tourandtravel.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TextView  username;
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
        username  = findViewById(R.id.mainUserName);

        // ── FIX: Set up bottom-nav listener BEFORE the session guard so it is
        //         always registered regardless of login state.
        //         Previously the listener was placed after an early return, which
        //         meant no tab tap worked for unauthenticated / demo sessions.
        setupBottomNavigation();

        // ── Load initial fragment (Home) only once on first create ─────────────
        if (savedInstanceState == null) {
            changeFragment(new HomeFragment(), R.id.user_home);
        }

        // ── Load customer profile (best-effort — navigation already works) ─────
        SessionManager sessionManager = new SessionManager(this);
        int custId = sessionManager.getCustomerId();

        if (custId == -1) {
            // Not logged in — navigation still works; profile info won't load.
            Toast.makeText(this, "Customer not logged in", Toast.LENGTH_SHORT).show();
            return;   // Skip API call, but listener is already registered above.
        }

        ApiService apiService = RetrofitClient.getApiService();
        Call<CustomerResponse> call = apiService.getCustomer(custId);
        call.enqueue(new Callback<CustomerResponse>() {

            @Override
            public void onResponse(@NonNull Call<CustomerResponse> call,
                                   @NonNull Response<CustomerResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
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
                    Toast.makeText(MainActivity.this,
                            "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CustomerResponse> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this,
                        "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Bottom navigation — must be set up before any early return
    // ─────────────────────────────────────────────────────────────────────────
    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(
                new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.user_home) {
                    changeFragment(new HomeFragment(), R.id.user_home);
                    return true;
                } else if (id == R.id.user_search) {
                    changeFragment(new SearchFragment(), R.id.user_search);
                    return true;
                } else if (id == R.id.user_map) {
                    changeFragment(new MapFragment(), R.id.user_map);
                    return true;
                } else if (id == R.id.ai_assistant) {
                    changeFragment(new AssistantFragment(), R.id.ai_assistant);
                    return true;
                } else if (id == R.id.user_profile) {
                    changeFragment(new ProfileFragment(), R.id.user_profile);
                    return true;
                }
                return false;
            }
        });
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Fragment replace + highlight the correct bottom-nav tab
    //
    // FIX: The old changeFragment() never called setSelectedItemId(), so Home
    //      stayed visually selected even when another tab was active.
    //      Now every fragment swap also updates the visual selection.
    // ─────────────────────────────────────────────────────────────────────────
    private void changeFragment(Fragment fragment, int menuItemId) {
        // Update the visual selection first (safe even if id == currently selected)
        if (bottomNavigationView.getSelectedItemId() != menuItemId) {
            // Temporarily remove the listener to avoid recursive callback when we
            // call setSelectedItemId() programmatically.
            bottomNavigationView.setOnItemSelectedListener(null);
            bottomNavigationView.setSelectedItemId(menuItemId);
            setupBottomNavigation();   // Re-attach listener
        }

        FragmentManager fm = getSupportFragmentManager();

        // Avoid replacing with the same fragment type that is already visible
        Fragment current = fm.findFragmentById(R.id.fragmentContainerView);
        if (current != null && current.getClass().equals(fragment.getClass())) {
            return;  // Already showing this fragment — prevents duplicate instances
        }

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragmentContainerView, fragment)
                   .commit();
    }
}