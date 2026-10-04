package com.mrudul.tourandtravel.driver.driver_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.driver.driver_activities.DriverLoginActivity;
import com.mrudul.tourandtravel.driver.driver_activities.DriverMainPage;
import com.mrudul.tourandtravel.driver.driver_models.DriverLoginResponse;
import com.mrudul.tourandtravel.network.ApiService;
import com.mrudul.tourandtravel.network.RetrofitClient;
import com.mrudul.tourandtravel.user.user_models.CustomerResponse;
import com.mrudul.tourandtravel.utils.DriverSessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverProfileFragment extends Fragment {

    ImageView driverImage;
    TextView driverName,driverPhone,driverEmail,licenseNumber;
    AppCompatButton logoutBtn;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DriverProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DriverProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DriverProfileFragment newInstance(String param1, String param2) {
        DriverProfileFragment fragment = new DriverProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        driverImage = view.findViewById(R.id.driverProfilePic);
        driverName = view.findViewById(R.id.driverProfileName);
        driverPhone = view.findViewById(R.id.driverPhone);
        driverEmail = view.findViewById(R.id.driverEmail);
        licenseNumber = view.findViewById(R.id.driverLicenseNumber);
        logoutBtn = view.findViewById(R.id.driverLogoutBtn);

        loadDriverProfile();


        logoutBtn.setOnClickListener(v->{
            logoutDriver();
        });

    }


    private void loadDriverProfile() {

        DriverSessionManager driverSessionManager = new DriverSessionManager(requireContext());
        ApiService apiService = RetrofitClient.getApiService();

        int driverId = driverSessionManager.getDriverId();

        if (driverId == -1) {

            Toast.makeText(
                    requireContext(),
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

                    DriverLoginResponse driverLoginResponse = response.body();

                    driverName.setText(driverLoginResponse.getDriverName());
                    driverEmail.setText(driverLoginResponse.getDriverEmail());
                    driverPhone.setText(driverLoginResponse.getDriverPhone());
                    licenseNumber.setText(driverLoginResponse.getLicenseNumber());



                    if (driverLoginResponse.getProfileImageUrl() != null
                            && !driverLoginResponse.getProfileImageUrl().isEmpty()) {

                        Glide.with(requireContext())
                                .load(driverLoginResponse.getProfileImageUrl())
                                .placeholder(R.drawable.ic_default_image)
                                .error(R.drawable.ic_default_image)
                                .into(driverImage);
                    }

                } else {

                    Toast.makeText(
                            requireContext(),
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
                        requireContext(),
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void logoutDriver() {

        DriverSessionManager driverSessionManager = new DriverSessionManager(requireContext());

        // Clear driver session
        driverSessionManager.logout();

        Toast.makeText(
                requireContext(),
                "Logged out successfully",
                Toast.LENGTH_SHORT
        ).show();

        // Go back to driver login
        Intent intent = new Intent(
                requireContext(),
                DriverLoginActivity.class
        );

        // Prevent going back to MainPage using back button
        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);
        requireActivity().finish();
    }
}