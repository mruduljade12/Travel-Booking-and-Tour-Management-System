package com.mrudul.tourandtravel.user.user_fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.user.user_adapters.SearchToutAdapter;
import com.mrudul.tourandtravel.user.user_interfaces.SearchItemClick;
import com.mrudul.tourandtravel.user.user_models.SearchTourModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements SearchItemClick {

    EditText searchEditText;
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
        searchEditText = view.findViewById(R.id.etSearchTourQuery);
        searchItemRecyclerView = view.findViewById(R.id.rvSearchTourList);
        searchItemRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(),2));

        tourList = new ArrayList<>();
        tourList.add(new SearchTourModel("TP001",R.drawable.tour_travel_logo, "Goa Sun & Beach Retreat", 12500, 4.5f, 4.8f));
        tourList.add(new SearchTourModel("TP002",R.drawable.tour_travel_logo, "Manali Snow Peak Adventure", 18000, 6.0f, 4.6f));
        tourList.add(new SearchTourModel("TP003",R.drawable.tour_travel_logo, "Kerala Houseboat & Spice Hills", 22000, 5.0f, 4.9f));
        tourList.add(new SearchTourModel("TP004",R.drawable.tour_travel_logo, "Jaipur Royal Heritage Tour", 14500, 3.5f, 4.5f));
        tourList.add(new SearchTourModel("TP005",R.drawable.tour_travel_logo, "Leh Ladakh Bike Expedition", 32000, 8.0f, 4.7f));
        tourList.add(new SearchTourModel("TP006",R.drawable.tour_travel_logo, "Ooty & Coorg Nature Escape", 11000, 4.0f, 4.4f));
        adapter = new SearchToutAdapter(view.getContext(),tourList, this);
        searchItemRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onSearchItemClick(SearchTourModel tourModel) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.search_item_dialog);

        ImageView img = dialog.findViewById(R.id.ivPackageIcon);
        ImageView closeBtn = dialog.findViewById(R.id.ivCloseDetails);
        TextView packageName = dialog.findViewById(R.id.tvDetailPackageName);
        TextView packageId = dialog.findViewById(R.id.tvDetailPackageId);
        TextView duration = dialog.findViewById(R.id.tvDetailDuration);
        TextView rating = dialog.findViewById(R.id.tvDetailRating);
        TextView price = dialog.findViewById(R.id.tvDetailPrice);
        AppCompatButton bookTourBtn = dialog.findViewById(R.id.btnBookTour);


        //setting image
        Glide.with(dialog.getContext())
                .load(tourModel.getTourImageUri())
                .error(R.drawable.ic_default_image)
                .placeholder(R.drawable.ic_default_image)
                .into(img);


        //set window
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );


        packageName.setText(tourModel.getToutPackageName());
        packageId.setText(tourModel.getTourPackageId());
        duration.setText(String.valueOf(tourModel.getTourPackageDuration()));
        rating.setText(String.valueOf(tourModel.getTourPackageRating()));
        price.setText(String.valueOf(tourModel.getTourPackagePrice()));


        bookTourBtn.setOnClickListener(v->{
            dialog.dismiss();
        });


        closeBtn.setOnClickListener(v->{
            dialog.dismiss();
        });


        dialog.show();

    }
}