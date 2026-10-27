package com.mrudul.tourandtravel.tour_guide.tour_guide_fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
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
import com.mrudul.tourandtravel.utils.TourGuideSessionManger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TourGuideLeaveFragment extends Fragment {

    private TextInputEditText startDate;
    private TextInputEditText endDate;
    private TextInputEditText subject;
    private TextInputEditText description;

    private MaterialButton applyLeaveBtn;

    private TourGuideSessionManger sessionManager;

    public TourGuideLeaveFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_tour_guide_leave,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        startDate = view.findViewById(R.id.tourGuideLeaveStartDate);
        endDate = view.findViewById(R.id.tourGuideLeaveEndDate);

        subject = view.findViewById(R.id.tourGuideLeaveSubject);
        description = view.findViewById(R.id.tourGuideLeaveDescription);

        applyLeaveBtn =
                view.findViewById(R.id.tourGuideApplyLeaveBtn);

        sessionManager =
                new TourGuideSessionManger(requireContext());

        // Date pickers
        startDate.setOnClickListener(v ->
                showDatePicker(startDate));

        endDate.setOnClickListener(v ->
                showDatePicker(endDate));

        // Apply leave
        applyLeaveBtn.setOnClickListener(v ->
                applyLeave());
    }

    private void applyLeave() {

        String startDateText =
                startDate.getText().toString().trim();

        String endDateText =
                endDate.getText().toString().trim();

        String subjectText =
                subject.getText().toString().trim();

        String descriptionText =
                description.getText().toString().trim();


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

        // -----------------------------
        // Validate dates
        // -----------------------------

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


        int guideId = sessionManager.getGuideId();

        if (guideId == -1) {

            Toast.makeText(
                    requireContext(),
                    "Tour guide is not logged in",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }



        String backendStartDate =
                convertDate(startDateText);

        String backendEndDate =
                convertDate(endDateText);

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
                        null,              // driverId
                        guideId,           // guideId
                        backendStartDate,
                        backendEndDate,
                        subjectText,
                        descriptionText
                );

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

                if (response.isSuccessful()
                        && response.body() != null) {

                    Toast.makeText(
                            requireContext(),
                            "Leave application submitted",
                            Toast.LENGTH_SHORT
                    ).show();

                    clearForm();

                } else {

                    Toast.makeText(
                            requireContext(),
                            "Failed to submit leave",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<LeaveResponse> call,
                    @NonNull Throwable t) {

                applyLeaveBtn.setEnabled(true);

                Toast.makeText(
                        requireContext(),
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
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