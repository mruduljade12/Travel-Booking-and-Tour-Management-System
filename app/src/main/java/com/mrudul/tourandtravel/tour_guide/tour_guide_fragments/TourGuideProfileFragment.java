package com.mrudul.tourandtravel.tour_guide.tour_guide_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.network.RetrofitClient;
import com.mrudul.tourandtravel.tour_guide.tour_guide_models.TourGuideLoginResponse;
import com.mrudul.tourandtravel.utils.TourGuideSessionManger;

import java.time.LocalDate;
import java.time.Period;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TourGuideProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TourGuideProfileFragment extends Fragment {

    private ImageView guideProfilePic;
    private TextView tvGuideProfileName;
    private TextView tvGuideExperience, tvLanguages,tvPhone;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TourGuideProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TourGuideProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TourGuideProfileFragment newInstance(String param1, String param2) {
        TourGuideProfileFragment fragment = new TourGuideProfileFragment();
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
        return inflater.inflate(R.layout.fragment_tour_guide_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        guideProfilePic = view.findViewById(R.id.guideProfilePic);
        tvGuideProfileName = view.findViewById(R.id.tvGuideProfileName);
        tvGuideExperience = view.findViewById(R.id.tvGuideExperience);
        tvLanguages = view.findViewById(R.id.tvLanguages);
        tvPhone = view.findViewById(R.id.tvTourGuidePhone);

        loadGuideData();
    }

    private void loadGuideData() {
        TourGuideSessionManger sessionManger = new TourGuideSessionManger(requireContext());
        int guideId = sessionManger.getGuideId();

        if (guideId == -1){
            Toast.makeText(requireContext(), "Guide Not Login", Toast.LENGTH_SHORT).show();
        }

        Call<TourGuideLoginResponse> responseCall = RetrofitClient.getApiService().getTourGuide(guideId);

        responseCall.enqueue(new Callback<TourGuideLoginResponse>() {
            @Override
            public void onResponse(Call<TourGuideLoginResponse> call, Response<TourGuideLoginResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    TourGuideLoginResponse loginResponse = response.body();

                    Glide.with(requireContext())
                            .load(loginResponse.getProfileImageUrl())
                            .error(R.drawable.ic_default_image)
                            .placeholder(R.drawable.ic_default_image)
                            .into(guideProfilePic);

                    tvGuideProfileName.setText(loginResponse.getGuideName());
                    tvGuideExperience.setText(Period.between(LocalDate.now(),LocalDate.parse(loginResponse.getJoiningDate())).getYears() + "years");
                    tvLanguages.setText(loginResponse.getLanguage());
                    tvPhone.setText(loginResponse.getGuidePhone());

                } else {
                    Toast.makeText(requireContext(), "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TourGuideLoginResponse> call, Throwable t) {
                Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}