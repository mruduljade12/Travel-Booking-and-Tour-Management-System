package com.mrudul.tourandtravel.tour_guide_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.tour_guide_adapters.TourGuideScheduleAdapter;
import com.mrudul.tourandtravel.tour_guide_models.TourGuideScheduleModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TourGuideScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TourGuideScheduleFragment extends Fragment {

    RecyclerView rvGuideTours;
    TourGuideScheduleAdapter tourAdapter;
    ArrayList<TourGuideScheduleModel> packageList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TourGuideScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TourGuideScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TourGuideScheduleFragment newInstance(String param1, String param2) {
        TourGuideScheduleFragment fragment = new TourGuideScheduleFragment();
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
        return inflater.inflate(R.layout.fragment_tour_guide_schedule2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvGuideTours = view.findViewById(R.id.rvGuideTours);
        packageList = new ArrayList<>();
        packageList.add(new TourGuideScheduleModel("Golden Triangle Tour", "25 Oct 2026, 09:00 AM", "Group of 4 (Family)", "Rajesh Sharma"));
        packageList.add(new TourGuideScheduleModel("Kerala Backwaters Escape", "26 Oct 2026, 11:30 AM", "Group of 2 (Couple)", "Suresh Nair"));
        packageList.add(new TourGuideScheduleModel("Goa Heritage Walk", "27 Oct 2026, 04:00 PM", "Group of 8 (Friends)", "Anthony D'Souza"));
        packageList.add(new TourGuideScheduleModel("Himachal Adventure Trek", "29 Oct 2026, 06:00 AM", "Group of 5 (Solo Travelers)", "Preet Singh"));
        packageList.add(new TourGuideScheduleModel("Rajasthan Desert Safari", "02 Nov 2026, 02:00 PM", "Group of 3 (Corporate)", "Vikram Rathore"));
        tourAdapter = new TourGuideScheduleAdapter(view.getContext(),packageList);
        rvGuideTours.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvGuideTours.setAdapter(tourAdapter);
    }
}