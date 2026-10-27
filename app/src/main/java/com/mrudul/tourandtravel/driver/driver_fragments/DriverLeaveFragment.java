package com.mrudul.tourandtravel.driver.driver_fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.app_common_activities.LeaveRequest;
import com.mrudul.tourandtravel.app_common_activities.LeaveResponse;
import com.mrudul.tourandtravel.network.RetrofitClient;
import com.mrudul.tourandtravel.utils.DriverSessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverLeaveFragment extends Fragment {

    private TextInputEditText startDate;
    private TextInputEditText endDate;
    private TextInputEditText subject;
    private TextInputEditText description;

    private MaterialButton applyLeaveBtn;

    private DriverSessionManager sessionManager;

    public DriverLeaveFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_driver_leave,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        startDate = view.findViewById(R.id.driverLeaveStartDate);
        endDate = view.findViewById(R.id.driverLeaveEndDate);

        subject = view.findViewById(R.id.driverLeaveSubject);
        description = view.findViewById(R.id.driverLeaveDescription);

        applyLeaveBtn = view.findViewById(R.id.driverApplyLeaveBtn);


        sessionManager = new DriverSessionManager(requireContext());


        startDate.setOnClickListener(v ->
                showDatePicker(startDate)
        );

        endDate.setOnClickListener(v ->
                showDatePicker(endDate)
        );


        applyLeaveBtn.setOnClickListener(v ->
                applyLeave()
        );
    }

    private void applyLeave() {

        String startDateText = startDate.getText().toString().trim();
        String endDateText = endDate.getText().toString().trim();
        String subjectText = subject.getText().toString().trim();
        String descriptionText = description.getText().toString().trim();

        if (startDateText.isEmpty()) {
            startDate.setError("Select start date");
            startDate.requestFocus();
            return;
        }

        if (endDateText.isEmpty()) {
            endDate.setError("Select end date");
            endDate.requestFocus();
            return;
        }

        if (subjectText.isEmpty()) {
            subject.setError("Enter subject");
            subject.requestFocus();
            return;
        }

        if (descriptionText.isEmpty()) {
            description.setError("Enter description");
            description.requestFocus();
            return;
        }


        SimpleDateFormat sdf =
                new SimpleDateFormat(
                        "dd/MM/yyyy",
                        Locale.getDefault()
                );

        sdf.setLenient(false);

        try {

            if (sdf.parse(endDateText)
                    .before(sdf.parse(startDateText))) {

                endDate.setError(
                        "End date cannot be before start date"
                );

                endDate.requestFocus();

                return;
            }

        } catch (Exception e) {

            Toast.makeText(
                    requireContext(),
                    "Invalid date",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        int driverId = sessionManager.getDriverId();

        if (driverId == -1) {

            Toast.makeText(
                    requireContext(),
                    "Driver is not logged in",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }



        String backendStartDate = convertDate(startDateText);
        String backendEndDate = convertDate(endDateText);
        if (backendStartDate == null ||
                backendEndDate == null) {

            Toast.makeText(
                    requireContext(),
                    "Invalid date",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        LeaveRequest request =
                new LeaveRequest(
                        driverId,
                        null,
                        backendStartDate,
                        backendEndDate,
                        subjectText,
                        descriptionText
                );

        Log.d("LEAVE_TEST", "Driver ID: " + driverId);
        Log.d("LEAVE_TEST", "Start: " + backendStartDate);
        Log.d("LEAVE_TEST", "End: " + backendEndDate);
        Log.d("LEAVE_TEST", "Subject: " + subjectText);
        Log.d("LEAVE_TEST", "Description: " + descriptionText);

        submitLeave(request);
    }


    private void submitLeave(LeaveRequest request) {

        applyLeaveBtn.setEnabled(false);

        Call<LeaveResponse> call =
                RetrofitClient
                        .getApiService()
                        .applyLeave(request);

        call.enqueue(new Callback<LeaveResponse>() {

            @Override
            public void onResponse(
                    @NonNull Call<LeaveResponse> call,
                    @NonNull Response<LeaveResponse> response) {

                applyLeaveBtn.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {

                    Toast.makeText(
                            requireContext(),
                            "Leave application submitted",
                            Toast.LENGTH_SHORT
                    ).show();

                    clearForm();

                } else {

                    String errorMessage = "";

                    try {
                        if (response.errorBody() != null) {
                            errorMessage = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        errorMessage = e.getMessage();
                    }

                    Toast.makeText(
                            requireContext(),
                            "Failed: " + response.code() + "\n" + errorMessage,
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<LeaveResponse> call, Throwable t) {
                Log.d("leave application failure",t.getMessage());
                Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String convertDate(String date) {

        try {

            SimpleDateFormat input =
                    new SimpleDateFormat(
                            "dd/MM/yyyy",
                            Locale.getDefault()
                    );

            SimpleDateFormat output =
                    new SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                    );

            return output.format(input.parse(date));

        } catch (Exception e) {

            return null;
        }
    }

    private void showDatePicker(TextInputEditText editText) {

        Calendar calendar =
                Calendar.getInstance();

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(
                        requireContext(),

                        (view, year, month, dayOfMonth) -> {

                            String date =
                                    String.format(
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


    private void clearForm() {

        startDate.setText("");
        endDate.setText("");
        subject.setText("");
        description.setText("");
    }
}