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

import com.bumptech.glide.Glide;
import com.mrudul.tourandtravel.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TourGuideProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TourGuideProfileFragment extends Fragment {

    private ImageView guideProfilePic;
    private TextView tvGuideProfileName, tvGuideBadge;
    private TextView tvGuideExperience, tvLanguages, tvSpecialization;


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
        tvGuideBadge = view.findViewById(R.id.tvGuideBadge);
        tvGuideExperience = view.findViewById(R.id.tvGuideExperience);
        tvLanguages = view.findViewById(R.id.tvLanguages);
        tvSpecialization = view.findViewById(R.id.tvSpecialization);



        // Set profile image
        Glide.with(this)
                .load(R.drawable.tour_guide_ic)
                .placeholder(R.drawable.tour_guide_ic)
                .error(R.drawable.tour_guide_ic)
                .into(guideProfilePic);

        // Set text
        tvGuideProfileName.setText("Mrudul Jade");
        tvGuideBadge.setText("Certified Professional Tour Guide");
        tvGuideExperience.setText("Experience: 3 Years");
        tvLanguages.setText("Languages: English, Hindi, Marathi");
        tvSpecialization.setText("Specialization: Historical & Eco Tours");
    }
}