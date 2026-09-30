package com.mrudul.tourandtravel.driver.driver_fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.driver.driver_adapter.DriverAssignPackageAdapter;
import com.mrudul.tourandtravel.driver.driver_interfaces.DriverPackageClickListener;
import com.mrudul.tourandtravel.driver.driver_models.DriverAssignPackageModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverHomeFragment extends Fragment implements DriverPackageClickListener {

    RecyclerView rvAssignedPackages;
    ArrayList<DriverAssignPackageModel> packageList;
    DriverAssignPackageAdapter packageAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DriverHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DriverHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DriverHomeFragment newInstance(String param1, String param2) {
        DriverHomeFragment fragment = new DriverHomeFragment();
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
        return inflater.inflate(R.layout.fragment_driver_assign_package, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvAssignedPackages = view.findViewById(R.id.rvAssignedPackages);
        packageList = new ArrayList<>();
        packageList.add(new DriverAssignPackageModel("Golden Triangle Tour", 4, "Terminal 3, IGIA, Delhi", "Ramesh Kumar"));
        packageList.add(new DriverAssignPackageModel("Kerala Backwaters Escape", 2, "Cochin International Airport", "Anil Nair"));
        packageList.add(new DriverAssignPackageModel("Goa Beach & Heritage", 6, "Madgaon Railway Station", "Maria D'Souza"));
        packageList.add(new DriverAssignPackageModel("Himachal Mountain Trek", 3, "ISBT Kashmiri Gate, Delhi", "Vikram Singh"));
        packageList.add(new DriverAssignPackageModel("Rajasthan Royal Safari", 5, "Jaipur Junction Railway Station", "Sanjay Sharma"));
        packageAdapter = new DriverAssignPackageAdapter(view.getContext(),packageList);

        rvAssignedPackages.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvAssignedPackages.setAdapter(packageAdapter);
    }

    @Override
    public void onClickDriverPackage(DriverAssignPackageModel model) {
        Dialog dialog = new Dialog(requireContext());

        dialog.setContentView(R.layout.driver_package_event_dialog);

        dialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        dialog.setCancelable(false);

        // Initialize views
        SwitchCompat dutySwitch = dialog.findViewById(R.id.dutySwitch);
        TextView dutyStatusText = dialog.findViewById(R.id.dutyStatusText);

        TextView destinationName =
                dialog.findViewById(R.id.driverTourDestinationName);

        TextView guideName =
                dialog.findViewById(R.id.driverTourGuideName);

        AppCompatButton navigateBtn =
                dialog.findViewById(R.id.btnNavigate);

        // Set initial data
        destinationName.setText(model.getPickUpAddress());
        guideName.setText(model.getGuideName());

        // Duty switch listener
        dutySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                dutyStatusText.setText("Online");
            } else {
                dutyStatusText.setText("Offline");
            }
        });

        // Navigation button
        navigateBtn.setOnClickListener(v -> {
            Toast.makeText(requireContext(),
                    "Starting navigation...",
                    Toast.LENGTH_SHORT).show();
        });

        dialog.show();

        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
    }
}