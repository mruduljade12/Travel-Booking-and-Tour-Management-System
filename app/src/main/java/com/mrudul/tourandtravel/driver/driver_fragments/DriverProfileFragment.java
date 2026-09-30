package com.mrudul.tourandtravel.driver.driver_fragments;

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

import com.mrudul.tourandtravel.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverProfileFragment extends Fragment {

    ImageView driverImage;
    TextView driverName,driverPhone,driverEmail,licenseNumber,vehicleModel,vehicleNumber;
    AppCompatButton editBtn,logoutBtn;


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
        vehicleModel = view.findViewById(R.id.driverVehicle);
        vehicleNumber = view.findViewById(R.id.driverVehicleNumber);
        editBtn = view.findViewById(R.id.driverProfileEditBtn);
        logoutBtn = view.findViewById(R.id.driverLogoutBtn);
    }
}