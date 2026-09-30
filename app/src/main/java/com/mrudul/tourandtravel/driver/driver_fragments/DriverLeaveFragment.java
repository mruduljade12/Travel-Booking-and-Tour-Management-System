package com.mrudul.tourandtravel.driver.driver_fragments;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverLeaveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverLeaveFragment extends Fragment {

    private TextInputEditText startDate, endDate;
    private TextInputEditText subject, description;
    private MaterialButton applyLeaveBtn;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DriverLeaveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DriverLeaveFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DriverLeaveFragment newInstance(String param1, String param2) {
        DriverLeaveFragment fragment = new DriverLeaveFragment();
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
        return inflater.inflate(R.layout.fragment_driver_leave, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize input fields
        startDate = view.findViewById(R.id.driverLeaveStartDate);
        endDate = view.findViewById(R.id.driverLeaveEndDate);
        subject = view.findViewById(R.id.driverLeaveSubject);
        description = view.findViewById(R.id.driverLeaveDescription);

        // Initialize button
        applyLeaveBtn = view.findViewById(R.id.driverApplyLeaveBtn);

        // Date picker listeners
        startDate.setOnClickListener(v -> showDatePicker(startDate));
        endDate.setOnClickListener(v -> showDatePicker(endDate));

        // Apply button listener
        applyLeaveBtn.setOnClickListener(v -> applyLeave());
    }

    private void applyLeave() {

        // Get input values
        String start_date = startDate.getText().toString().trim();
        String end_date = endDate.getText().toString().trim();
        String leave_subject = subject.getText().toString().trim();
        String leave_description = description.getText().toString().trim();

        // Validate empty fields
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
            if (sdf.parse(end_date).before(sdf.parse(start_date))) {
                endDate.setError("End date cannot be before start date");
                return;
            }
        } catch (Exception e) {
            Toast.makeText(requireContext(),
                    "Invalid date",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // All validations passed
        Toast.makeText(requireContext(),
                "Leave application validated successfully",
                Toast.LENGTH_SHORT).show();
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