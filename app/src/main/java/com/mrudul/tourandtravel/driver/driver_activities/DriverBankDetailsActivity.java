package com.mrudul.tourandtravel.driver.driver_activities;

import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mrudul.tourandtravel.R;

public class DriverBankDetailsActivity extends AppCompatActivity {

    private EditText etHolderName, etBankName, etAccountNumber, etIfscCode;
    private TextView tvAccountType;
    private View llAccountType;
    private AppCompatButton btnSave;

    private static final String[] ACCOUNT_TYPES = { "Savings", "Current" };
    private String selectedAccountType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_driver_bank_details);

        View root = findViewById(R.id.main);
        if (root != null) {
            ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
                Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                return insets;
            });
        }

        bindViews();
        applyInputFilters();
        setListeners();
    }

    private void bindViews() {
        etHolderName    = findViewById(R.id.et_holder_name);
        etBankName      = findViewById(R.id.et_bank_name);
        etAccountNumber = findViewById(R.id.et_account_number);
        etIfscCode      = findViewById(R.id.et_ifsc_code);
        tvAccountType   = findViewById(R.id.tv_account_type);
        llAccountType   = findViewById(R.id.ll_account_type);
        btnSave         = findViewById(R.id.btn_save_bank);
    }

    /** Force IFSC to uppercase as user types. */
    private void applyInputFilters() {
        if (etIfscCode != null) {
            etIfscCode.setFilters(new InputFilter[]{ new InputFilter.AllCaps(), new InputFilter.LengthFilter(11) });
        }
        if (etAccountNumber != null) {
            etAccountNumber.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(18) });
        }
    }

    private void setListeners() {
        View ivBack = findViewById(R.id.iv_bank_back);
        if (ivBack != null) ivBack.setOnClickListener(v -> finish());

        if (llAccountType != null) llAccountType.setOnClickListener(v -> showAccountTypeDialog());
        if (btnSave != null)       btnSave.setOnClickListener(v -> saveBankDetails());
    }

    private void showAccountTypeDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Select Account Type")
            .setItems(ACCOUNT_TYPES, (d, which) -> {
                selectedAccountType = ACCOUNT_TYPES[which];
                tvAccountType.setText(selectedAccountType);
                tvAccountType.setTextColor(ContextCompat.getColor(this, R.color.bank_field_text));
            }).show();
    }

    private void saveBankDetails() {
        String holder  = etHolderName.getText().toString().trim();
        String bank    = etBankName.getText().toString().trim();
        String accNum  = etAccountNumber.getText().toString().trim();
        String ifsc    = etIfscCode.getText().toString().trim();

        if (TextUtils.isEmpty(holder))  { etHolderName.setError("Required");    etHolderName.requestFocus(); return; }
        if (TextUtils.isEmpty(bank))    { etBankName.setError("Required");      etBankName.requestFocus();   return; }
        if (TextUtils.isEmpty(accNum))  { etAccountNumber.setError("Required"); etAccountNumber.requestFocus(); return; }
        if (accNum.length() < 9)        { etAccountNumber.setError("Enter valid account number"); etAccountNumber.requestFocus(); return; }
        if (TextUtils.isEmpty(ifsc))    { etIfscCode.setError("Required");      etIfscCode.requestFocus();   return; }
        if (ifsc.length() != 11)        { etIfscCode.setError("Must be 11 characters"); etIfscCode.requestFocus(); return; }
        if (TextUtils.isEmpty(selectedAccountType)) { Toast.makeText(this, "Select account type", Toast.LENGTH_SHORT).show(); return; }

        // Hide keyboard on success
        hideKeyboard();
        Toast.makeText(this, "Bank details saved successfully!", Toast.LENGTH_SHORT).show();
    }

    private void hideKeyboard() {
        View focus = getCurrentFocus();
        if (focus != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(focus.getWindowToken(), 0);
        }
    }
}