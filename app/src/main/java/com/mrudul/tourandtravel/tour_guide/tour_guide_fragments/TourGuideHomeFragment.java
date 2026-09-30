package com.mrudul.tourandtravel.tour_guide.tour_guide_fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.tour_guide.tour_guide_interfaces.TourGuidePackageClickListener;
import com.mrudul.tourandtravel.tour_guide.tour_guide_models.TourGuideScheduleModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TourGuideHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TourGuideHomeFragment extends Fragment implements TourGuidePackageClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TourGuideHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TourGuideHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TourGuideHomeFragment newInstance(String param1, String param2) {
        TourGuideHomeFragment fragment = new TourGuideHomeFragment();
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
        return inflater.inflate(R.layout.fragment_tour_guide_home, container, false);
    }

    @Override
    public void onTourGuidePackageClick(TourGuideScheduleModel model) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.tour_guide_package_dialog);

        dialog.setCancelable(false);

        // Initialize views
        SwitchCompat guideDutySwitch =
                dialog.findViewById(R.id.guideDutySwitch);

        TextView guideStatusText =
                dialog.findViewById(R.id.guideStatusText);

        TextView currentStop =
                dialog.findViewById(R.id.tvCurrentStop);

        TextView assignedDriver =
                dialog.findViewById(R.id.tvAssignedDriver);

        AppCompatButton nextDestination =
                dialog.findViewById(R.id.btnNextDestination);

        // Set initial data
        currentStop.setText("Current Spot: Historic Fort Ruins");
        assignedDriver.setText("Driver: John Doe (Innova MH-08)");

        // Duty switch listener
        guideDutySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                guideStatusText.setText("Available");
            } else {
                guideStatusText.setText("Unavailable");
            }
        });

        // Next destination button
        nextDestination.setOnClickListener(v -> {
            Toast.makeText(requireContext(),
                    "Proceeding to next destination",
                    Toast.LENGTH_SHORT).show();

            // Add your next destination logic here.
        });

        // Show dialog
        dialog.show();

        // Set dialog size
        Window window = dialog.getWindow();

        if (window != null) {
            window.setBackgroundDrawableResource(
                    android.R.color.transparent
            );

            window.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
        }
    }
}