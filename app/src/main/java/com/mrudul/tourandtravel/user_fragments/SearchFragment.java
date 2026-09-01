package com.mrudul.tourandtravel.user_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.user_adapters.SearchToutAdapter;
import com.mrudul.tourandtravel.user_models.SearchTourModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    EditText searchEditText;
    ImageButton searchBtn;
    RecyclerView searchItemRecyclerView;
    ArrayList<SearchTourModel> tourList;
    SearchToutAdapter adapter;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchBtn = view.findViewById(R.id.searchBtn);
        searchEditText = view.findViewById(R.id.searchEditText);
        searchItemRecyclerView = view.findViewById(R.id.searchItemRecyclerView);
        searchItemRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(),2));

        tourList = new ArrayList<>();
        tourList.add(new SearchTourModel("TP001", "Goa Sun & Beach Retreat", 12500, 4.5f, 4.8f));
        tourList.add(new SearchTourModel("TP002", "Manali Snow Peak Adventure", 18000, 6.0f, 4.6f));
        tourList.add(new SearchTourModel("TP003", "Kerala Houseboat & Spice Hills", 22000, 5.0f, 4.9f));
        tourList.add(new SearchTourModel("TP004", "Jaipur Royal Heritage Tour", 14500, 3.5f, 4.5f));
        tourList.add(new SearchTourModel("TP005", "Leh Ladakh Bike Expedition", 32000, 8.0f, 4.7f));
        tourList.add(new SearchTourModel("TP006", "Ooty & Coorg Nature Escape", 11000, 4.0f, 4.4f));
        adapter = new SearchToutAdapter(view.getContext(),tourList);
        searchItemRecyclerView.setAdapter(adapter);
    }
}