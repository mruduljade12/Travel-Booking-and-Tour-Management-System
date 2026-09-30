package com.mrudul.tourandtravel.user.user_fragments;

import android.os.Bundle;

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

import com.mrudul.tourandtravel.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    TextView userName;
    EditText pPhoneEdit,pEmailEdit,pAddressEdit;
    ImageView pUserImg,editBtn;
    AppCompatButton saveBtn,logoutBtn;

    boolean onEdit = false;

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


        editTextEditableOrNot(false);




        saveBtn.setOnClickListener(v->{

            String email = pEmailEdit.getText().toString().trim();
            String phone = pPhoneEdit.getText().toString().trim();
            String address = pAddressEdit.getText().toString().trim();



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


    private void editTextEditableOrNot(boolean isEditable){
        pAddressEdit.setEnabled(isEditable);
        pEmailEdit.setEnabled(isEditable);
        pPhoneEdit.setEnabled(isEditable);
    }
}