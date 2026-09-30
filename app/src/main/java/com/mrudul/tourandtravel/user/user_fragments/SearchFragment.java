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
        tourList.add(new SearchTourModel(
                "TOUR001", R.drawable.site1, "Goa Beach Tour",
                "Available", 4.5f, 15000, 4, 3, 20,
                "Enjoy beautiful beaches and nightlife in Goa."
        ));

        tourList.add(new SearchTourModel(
                "TOUR002", R.drawable.site1, "Manali Adventure",
                "Available", 4.8f, 25000, 6, 5, 15,
                "Explore the mountains and enjoy adventure activities."
        ));

        tourList.add(new SearchTourModel(
                "TOUR003", R.drawable.site1, "Kerala Backwaters",
                "Available", 4.7f, 18000, 5, 4, 12,
                "Experience the beauty of Kerala's backwaters."
        ));

        tourList.add(new SearchTourModel(
                "TOUR004", R.drawable.site1, "Mumbai City Tour",
                "Unavailable", 4.2f, 8000, 2, 1, 25,
                "Discover famous landmarks and attractions in Mumbai."
        ));

        tourList.add(new SearchTourModel(
                "TOUR005", R.drawable.site1, "Kashmir Valley Tour",
                "Available", 4.9f, 35000, 7, 6, 10,
                "Explore the beautiful valleys and lakes of Kashmir."
        ));
        adapter = new SearchToutAdapter(view.getContext(),tourList, this);
        searchItemRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onSearchItemClick(SearchTourModel tourModel) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.search_item_dialog);

        ImageView img = dialog.findViewById(R.id.dialogPackageThumbnail);
        ImageView closeBtn = dialog.findViewById(R.id.dialogCancelBtn);
        TextView packageName = dialog.findViewById(R.id.dialogPackageName);
        TextView packageStatus = dialog.findViewById(R.id.dialogPackageStatus);
        TextView duration = dialog.findViewById(R.id.dialogPackageDuration);
        TextView rating = dialog.findViewById(R.id.dialogPackageRating);
        TextView price = dialog.findViewById(R.id.dialogPackagePrice);
        TextView days = dialog.findViewById(R.id.dialogPackageDays);
        TextView nights = dialog.findViewById(R.id.dialogPackageNights);
        TextView remainingSeats = dialog.findViewById(R.id.dialogPackageRemainingSeats);
        TextView description = dialog.findViewById(R.id.dialogPackageDescription);
        AppCompatButton bookTourBtn = dialog.findViewById(R.id.dialogPackageBookBtn);


        //setting image
        Glide.with(dialog.getContext())
                .load(tourModel.getPack_thumbnail())
                .error(R.drawable.ic_default_image)
                .placeholder(R.drawable.ic_default_image)
                .into(img);


        //set window
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );


        packageName.setText(tourModel.getPack_name());
        packageStatus.setText(tourModel.getPack_status());
        duration.setText(String.valueOf(tourModel.getPack_duration_day()));
        rating.setText(String.valueOf(tourModel.getTourPackageRating()));
        price.setText(String.valueOf(tourModel.getPack_price()));
        days.setText(String.valueOf(tourModel.getPack_duration_day()));
        nights.setText(String.valueOf(tourModel.getPack_duration_night()));
        remainingSeats.setText(String.valueOf(tourModel.getPack_max_capacity()-5));
        description.setText(tourModel.getPack_description());


        bookTourBtn.setOnClickListener(v->{
            dialog.dismiss();
        });


        closeBtn.setOnClickListener(v->{
            dialog.dismiss();
        });


        dialog.show();

    }
}