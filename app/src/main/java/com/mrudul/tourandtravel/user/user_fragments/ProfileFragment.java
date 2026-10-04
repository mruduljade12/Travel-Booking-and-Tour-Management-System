package com.mrudul.tourandtravel.user.user_fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.network.ApiService;
import com.mrudul.tourandtravel.network.RetrofitClient;
import com.mrudul.tourandtravel.user.user_models.CustomerResponse;
import com.mrudul.tourandtravel.user.user_models.CustomerUpdateRequest;
import com.mrudul.tourandtravel.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private ApiService apiService;
    private SessionManager sessionManager;


    TextView userName;
    EditText pPhoneEdit,pEmailEdit,pAddressEdit;
    ImageView pUserImg,editBtn;
    AppCompatButton saveBtn,logoutBtn;
    boolean onEdit = false;


    private Uri selectedImageUri;
    private String currentImageUrl;
    private final ActivityResultLauncher<String> imagePicker =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {
                        if (uri != null) {
                            selectedImageUri = uri;
                            pUserImg.setImageURI(uri);
                        }
                    });


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pUserImg = view.findViewById(R.id.userProfileImage);
        userName = view.findViewById(R.id.userProfileUserName);
        pEmailEdit = view.findViewById(R.id.userEmail);
        pPhoneEdit = view.findViewById(R.id.userPhone);
        pAddressEdit = view.findViewById(R.id.userAddress);
        saveBtn = view.findViewById(R.id.userProfileSave);
        editBtn = view.findViewById(R.id.ivEditProfile);
        logoutBtn = view.findViewById(R.id.userLogout);
        apiService = RetrofitClient.getApiService();
        sessionManager = new SessionManager(requireContext());
        editTextEditableOrNot(false);
        loadCustomerProfile();



        pUserImg.setOnClickListener(v->{
            imagePicker.launch("image/*");
        });



        saveBtn.setOnClickListener(v->{

            String email = pEmailEdit.getText().toString().trim();
            String phone = pPhoneEdit.getText().toString().trim();
            String address = pAddressEdit.getText().toString().trim();

            if (email.isEmpty()) {
                pEmailEdit.setError("Enter email");
                pEmailEdit.requestFocus();
                return;
            }

            if (phone.isEmpty()) {
                pPhoneEdit.setError("Enter phone number");
                pPhoneEdit.requestFocus();
                return;
            }

            if (phone.length() != 10) {
                pPhoneEdit.setError("Enter valid 10 digit phone number");
                pPhoneEdit.requestFocus();
                return;
            }

            if (address.isEmpty()) {
                pAddressEdit.setError("Enter address");
                pAddressEdit.requestFocus();
                return;
            }

            updateCustomerRequest(email, phone, address);



            editTextEditableOrNot(false);
            onEdit = false;
        });




        editBtn.setOnClickListener(v->{

            if (onEdit){
                editTextEditableOrNot(false);
                onEdit = false;
            }else {
                editTextEditableOrNot(true);
                onEdit = true;
            }

        });

    }

    private void updateCustomerRequest(
            String email,
            String phone,
            String address) {

        int custId = sessionManager.getCustomerId();

        if (custId == -1) {

            Toast.makeText(
                    requireContext(),
                    "Customer not logged in",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        saveBtn.setEnabled(false);

        // New image selected
        if (selectedImageUri != null) {

            FirebaseStorage storage =
                    FirebaseStorage.getInstance();

            StorageReference imageReference =
                    storage.getReference()
                            .child("customer_profiles")
                            .child("customer_" + custId + ".jpg");

            imageReference.putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot -> {

                        imageReference.getDownloadUrl()
                                .addOnSuccessListener(uri -> {

                                    String imageUrl =
                                            uri.toString();

                                    updateCustomerOnServer(
                                            custId,
                                            email,
                                            phone,
                                            address,
                                            imageUrl
                                    );
                                })
                                .addOnFailureListener(e -> {

                                    saveBtn.setEnabled(true);

                                    Toast.makeText(
                                            requireContext(),
                                            "Failed to get image URL",
                                            Toast.LENGTH_LONG
                                    ).show();
                                });
                    })
                    .addOnFailureListener(e -> {

                        saveBtn.setEnabled(true);

                        Toast.makeText(
                                requireContext(),
                                "Image upload failed: "
                                        + e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    });

        } else {

            // No new image selected.
            // Keep the existing Firebase image URL.

            updateCustomerOnServer(
                    custId,
                    email,
                    phone,
                    address,
                    currentImageUrl
            );
        }
    }


    private void updateCustomerOnServer(
            int custId,
            String email,
            String phone,
            String address,
            String imageUrl) {

        CustomerUpdateRequest request =
                new CustomerUpdateRequest(
                        email,
                        phone,
                        address,
                        imageUrl
                );

        Call<CustomerResponse> call =
                apiService.updateCustomer(
                        custId,
                        request
                );

        call.enqueue(new Callback<CustomerResponse>() {

            @Override
            public void onResponse(
                    Call<CustomerResponse> call,
                    Response<CustomerResponse> response) {

                saveBtn.setEnabled(true);

                if (response.isSuccessful()
                        && response.body() != null) {

                    CustomerResponse customer =
                            response.body();

                    pEmailEdit.setText(
                            customer.getCustEmail()
                    );

                    pPhoneEdit.setText(
                            customer.getCustPhone()
                    );

                    pAddressEdit.setText(
                            customer.getCustAddress()
                    );

                    currentImageUrl = customer.getImageUrl();

                    Glide.with(requireContext())
                            .load(customer.getImageUrl())
                            .placeholder(
                                    R.drawable.ic_default_image
                            )
                            .error(
                                    R.drawable.ic_default_image
                            )
                            .into(pUserImg);

                    // Clear selected image because
                    // it has already been uploaded.
                    selectedImageUri = null;

                    editTextEditableOrNot(false);
                    onEdit = false;

                    Toast.makeText(
                            requireContext(),
                            "Profile updated successfully",
                            Toast.LENGTH_SHORT
                    ).show();

                } else {

                    Toast.makeText(
                            requireContext(),
                            "Failed to update profile",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    Call<CustomerResponse> call,
                    Throwable t) {

                saveBtn.setEnabled(true);

                Toast.makeText(
                        requireContext(),
                        "Network error: "
                                + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }


    private void loadCustomerProfile() {

        int custId = sessionManager.getCustomerId();

        if (custId == -1) {

            Toast.makeText(
                    requireContext(),
                    "Customer not logged in",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        Call<CustomerResponse> call = apiService.getCustomer(custId);

        call.enqueue(new Callback<CustomerResponse>() {

            @Override
            public void onResponse(
                    Call<CustomerResponse> call,
                    Response<CustomerResponse> response) {

                if (response.isSuccessful()
                        && response.body() != null) {

                    CustomerResponse customer = response.body();

                    userName.setText(customer.getCustName());
                    pEmailEdit.setText(customer.getCustEmail());
                    pPhoneEdit.setText(customer.getCustPhone());
                    pAddressEdit.setText(customer.getCustAddress());
                    if (customer.getImageUrl() != null
                            && !customer.getImageUrl().isEmpty()) {

                        Glide.with(requireContext())
                                .load(customer.getImageUrl())
                                .placeholder(R.drawable.ic_default_image)
                                .error(R.drawable.ic_default_image)
                                .into(pUserImg);
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
                    Call<CustomerResponse> call,
                    Throwable t) {

                Toast.makeText(
                        requireContext(),
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }


    private void editTextEditableOrNot(boolean isEditable){
        pAddressEdit.setEnabled(isEditable);
        pEmailEdit.setEnabled(isEditable);
        pPhoneEdit.setEnabled(isEditable);
    }
}