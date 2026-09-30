package com.mrudul.tourandtravel.tour_guide.tour_guide_fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mrudul.tourandtravel.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TourGuideLeaveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TourGuideLeaveFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TourGuideLeaveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TourGuideLeaveFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TourGuideLeaveFragment newInstance(String param1, String param2) {
        TourGuideLeaveFragment fragment = new TourGuideLeaveFragment();
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
        return inflater.inflate(R.layout.fragment_tour_guide_leave, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize input fields
        TextInputEditText startDate = view.findViewById(R.id.tourGuideLeaveStartDate);
        TextInputEditText endDate = view.findViewById(R.id.tourGuideLeaveEndDate);
        TextInputEditText subject = view.findViewById(R.id.tourGuideLeaveSubject);
        TextInputEditText description = view.findViewById(R.id.tourGuideLeaveDescription);

// Initialize Apply button
        MaterialButton applyLeaveBtn = view.findViewById(R.id.tourGuideApplyLeaveBtn);

// Start date picker
        startDate.setOnClickListener(v -> showDatePicker(startDate));

// End date picker
        endDate.setOnClickListener(v -> showDatePicker(endDate));

// Apply button click
        applyLeaveBtn.setOnClickListener(v -> {

            // Get input values
            String start_date = startDate.getText().toString().trim();
            String end_date = endDate.getText().toString().trim();
            String leave_subject = subject.getText().toString().trim();
            String leave_description = description.getText().toString().trim();

            // Validate inputs
            if (start_date.isEmpty()) {
                startDate.setError("Select start date");
                return;
            }

            if (end_date.isEmpty()) {
                endDate.setError("Select end date");
                return;
            }

            if (leave_subject.isEmpty()) {
                subject.setError("Enter subject");
                return;
            }

            if (leave_description.isEmpty()) {
                description.setError("Enter description");
                return;
            }

            // Validate date range
            SimpleDateFormat sdf = new SimpleDateFormat(
                    "dd/MM/yyyy", Locale.getDefault()
            );
            sdf.setLenient(false);

            try {
                Date start = sdf.parse(start_date);
                Date end = sdf.parse(end_date);

                if (end.before(start)) {
                    endDate.setError("End date must be after start date");
                    return;
                }

            } catch (ParseException e) {
                Toast.makeText(requireContext(),
                        "Invalid date", Toast.LENGTH_SHORT).show();
                return;
            }

            // All inputs are valid
            Toast.makeText(requireContext(),
                    "Leave application submitted",
                    Toast.LENGTH_SHORT).show();

            // Send these variables to your Spring Boot API
        });
    }

    private void showDatePicker(TextInputEditText editText) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {

                    String date = String.format(
                            Locale.getDefault(),
                            "%02d/%02d/%04d",
                            dayOfMonth,
                            month + 1,
                            year
                    );

                    editText.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }
}