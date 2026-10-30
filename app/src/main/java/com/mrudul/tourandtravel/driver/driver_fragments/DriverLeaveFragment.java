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
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

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

/**
 * SmartTour360 Driver App - Apply For Leave Fragment
 *
 * Submits leave requests via the backend API (LeaveRequest -> LeaveResponse).
 * Button cast uses AppCompatButton (matches fragment_driver_leave.xml).
 */
public class DriverLeaveFragment extends Fragment {

    private TextInputEditText startDate, endDate, subject, description;
    private AppCompatButton   applyLeaveBtn;
    private DriverSessionManager sessionManager;

    public DriverLeaveFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_driver_leave, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        startDate     = view.findViewById(R.id.driverLeaveStartDate);
        endDate       = view.findViewById(R.id.driverLeaveEndDate);
        subject       = view.findViewById(R.id.driverLeaveSubject);
        description   = view.findViewById(R.id.driverLeaveDescription);
        applyLeaveBtn = view.findViewById(R.id.driverApplyLeaveBtn);

        sessionManager = new DriverSessionManager(requireContext());

        // Back arrow: finish the host Activity
        View ivBack = view.findViewById(R.id.iv_leave_back);
        if (ivBack != null) {
            ivBack.setOnClickListener(v -> { if (getActivity() != null) getActivity().finish(); });
        }

        if (startDate != null)
            startDate.setOnClickListener(v -> showDatePicker(startDate));
        if (endDate != null)
            endDate.setOnClickListener(v -> showDatePicker(endDate));
        if (applyLeaveBtn != null)
            applyLeaveBtn.setOnClickListener(v -> applyLeave());
    }

    private void applyLeave() {
        String s    = startDate   != null ? startDate.getText().toString().trim()   : "";
        String e    = endDate     != null ? endDate.getText().toString().trim()      : "";
        String sub  = subject     != null ? subject.getText().toString().trim()      : "";
        String desc = description != null ? description.getText().toString().trim()  : "";

        if (s.isEmpty())    { startDate.setError("Select start date");   return; }
        if (e.isEmpty())    { endDate.setError("Select end date");       return; }
        if (sub.isEmpty())  { subject.setError("Enter subject");         return; }
        if (desc.isEmpty()) { description.setError("Enter description"); return; }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        sdf.setLenient(false);
        try {
            if (sdf.parse(e).before(sdf.parse(s))) {
                endDate.setError("End date cannot be before start date");
                return;
            }
        } catch (Exception ex) {
            Toast.makeText(requireContext(), "Invalid date", Toast.LENGTH_SHORT).show();
            return;
        }

        int driverId = sessionManager.getDriverId();
        if (driverId == -1) {
            Toast.makeText(requireContext(), "Driver not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String backendStart = convertDate(s);
        String backendEnd   = convertDate(e);
        if (backendStart == null || backendEnd == null) {
            Toast.makeText(requireContext(), "Invalid date", Toast.LENGTH_SHORT).show();
            return;
        }

        LeaveRequest request = new LeaveRequest(driverId, null, backendStart, backendEnd, sub, desc);

        Log.d("LEAVE_TEST", "Driver ID: " + driverId + " | " + backendStart + " -> " + backendEnd);

        submitLeave(request);
    }

    private void submitLeave(LeaveRequest request) {
        if (applyLeaveBtn != null) applyLeaveBtn.setEnabled(false);

        RetrofitClient.getApiService().applyLeave(request)
                .enqueue(new Callback<LeaveResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<LeaveResponse> call,
                                           @NonNull Response<LeaveResponse> response) {
                        if (applyLeaveBtn != null) applyLeaveBtn.setEnabled(true);
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(requireContext(),
                                    "Leave application submitted", Toast.LENGTH_SHORT).show();
                            clearForm();
                        } else {
                            String error = "";
                            try {
                                if (response.errorBody() != null) error = response.errorBody().string();
                            } catch (Exception ex) { error = ex.getMessage(); }
                            Toast.makeText(requireContext(),
                                    "Failed: " + response.code() + "\n" + error, Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<LeaveResponse> call, Throwable t) {
                        if (applyLeaveBtn != null) applyLeaveBtn.setEnabled(true);
                        Log.d("LEAVE_FAIL", t.getMessage());
                        Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String convertDate(String date) {
        try {
            SimpleDateFormat in  = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat out = new SimpleDateFormat("yyyy-MM-dd",  Locale.getDefault());
            return out.format(in.parse(date));
        } catch (Exception e) { return null; }
    }

    private void showDatePicker(TextInputEditText field) {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(requireContext(),
                (v, year, month, day) -> field.setText(
                        String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month + 1, year)),
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void clearForm() {
        if (startDate   != null) startDate.setText("");
        if (endDate     != null) endDate.setText("");
        if (subject     != null) subject.setText("");
        if (description != null) description.setText("");
    }
}
