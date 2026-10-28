package com.mrudul.tourandtravel.driver.driver_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.driver.driver_activities.DriverApplyLeaveActivity;
import com.mrudul.tourandtravel.driver.driver_activities.DriverLoginActivity;
import com.mrudul.tourandtravel.driver.driver_activities.DriverSettingsActivity;
import com.mrudul.tourandtravel.driver.driver_activities.DriverVehicleDetailsActivity;
import com.mrudul.tourandtravel.driver.driver_models.DriverLoginResponse;
import com.mrudul.tourandtravel.network.RetrofitClient;
import com.mrudul.tourandtravel.utils.DriverSessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * SmartTour360 Driver App - Profile Fragment
 *
 * Loads driver profile from the backend API and populates the redesigned UI.
 *
 * Option row navigation:
 *   Settings gear    -> DriverSettingsActivity
 *   Vehicle Details  -> DriverVehicleDetailsActivity
 *   Apply For Leave  -> DriverApplyLeaveActivity
 *   Help & Support   -> Toast placeholder
 *   Logout           -> clears session, goes to DriverLoginActivity
 */
public class DriverProfileFragment extends Fragment {

    private ImageView driverImage;
    private TextView  driverName, driverPhone, driverEmail, licenseNumber;

    public DriverProfileFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_driver_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        driverImage   = view.findViewById(R.id.driverProfilePic);
        driverName    = view.findViewById(R.id.driverProfileName);
        driverPhone   = view.findViewById(R.id.driverPhone);
        driverEmail   = view.findViewById(R.id.driverEmail);
        licenseNumber = view.findViewById(R.id.driverLicenseNumber);

        loadDriverProfile();

        // Settings gear
        View ivSettings = view.findViewById(R.id.iv_prof_settings);
        if (ivSettings != null) ivSettings.setOnClickListener(v -> launch(DriverSettingsActivity.class));

        // Vehicle Details row
        View rowVehicle = view.findViewById(R.id.row_vehicle_details);
        if (rowVehicle != null) rowVehicle.setOnClickListener(v -> launch(DriverVehicleDetailsActivity.class));

        // Apply For Leave row
        View rowLeave = view.findViewById(R.id.row_apply_leave);
        if (rowLeave != null) rowLeave.setOnClickListener(v -> launch(DriverApplyLeaveActivity.class));

        // Help & Support row
        View rowHelp = view.findViewById(R.id.row_help_support);
        if (rowHelp != null) rowHelp.setOnClickListener(v ->
                Toast.makeText(requireContext(), "Help & Support - coming soon", Toast.LENGTH_SHORT).show());

        // Logout row (optional - if present in layout)
        View rowLogout = view.findViewById(R.id.row_logout);
        if (rowLogout != null) rowLogout.setOnClickListener(v -> logoutDriver());
    }

    private void loadDriverProfile() {
        DriverSessionManager driverSessionManager = new DriverSessionManager(requireContext());
        int driverId = driverSessionManager.getDriverId();

        if (driverId == -1) {
            Toast.makeText(requireContext(), "Driver not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        RetrofitClient.getApiService().getDriver(driverId)
                .enqueue(new Callback<DriverLoginResponse>() {
                    @Override
                    public void onResponse(Call<DriverLoginResponse> call,
                                           Response<DriverLoginResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            DriverLoginResponse driver = response.body();
                            if (driverName    != null) driverName.setText(driver.getDriverName());
                            if (driverEmail   != null) driverEmail.setText(driver.getDriverEmail());
                            if (driverPhone   != null) driverPhone.setText(driver.getDriverPhone());
                            if (licenseNumber != null) licenseNumber.setText(driver.getLicenseNumber());
                            if (driverImage   != null
                                    && driver.getProfileImageUrl() != null
                                    && !driver.getProfileImageUrl().isEmpty()) {
                                Glide.with(requireContext())
                                        .load(driver.getProfileImageUrl())
                                        .placeholder(R.drawable.ic_default_image)
                                        .error(R.drawable.ic_default_image)
                                        .into(driverImage);
                            }
                        } else {
                            Toast.makeText(requireContext(),
                                    "Failed to load profile", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<DriverLoginResponse> call, Throwable t) {
                        Toast.makeText(requireContext(),
                                "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void logoutDriver() {
        new DriverSessionManager(requireContext()).logout();
        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(requireContext(), DriverLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    private void launch(Class<?> activityClass) {
        if (getContext() != null) startActivity(new Intent(getContext(), activityClass));
    }
}
