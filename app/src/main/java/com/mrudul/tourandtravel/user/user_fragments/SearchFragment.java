package com.mrudul.tourandtravel.user.user_fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.network.RetrofitClient;
import com.mrudul.tourandtravel.user.user_adapters.DestinationAdapter;
import com.mrudul.tourandtravel.user.user_adapters.SearchToutAdapter;
import com.mrudul.tourandtravel.user.user_interfaces.SearchItemClick;
import com.mrudul.tourandtravel.user.user_models.BookingRequest;
import com.mrudul.tourandtravel.user.user_models.BookingResponse;
import com.mrudul.tourandtravel.user.user_models.PackageDestinationResponse;
import com.mrudul.tourandtravel.user.user_models.TourPackageResponse;
import com.mrudul.tourandtravel.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements SearchItemClick {

    EditText searchEditText;
    ImageView searchBtn;
    RecyclerView searchItemRecyclerView;
    ArrayList<TourPackageResponse> tourList;
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
        searchBtn = view.findViewById(R.id.userPackageSearchBtn);

        searchItemRecyclerView = view.findViewById(R.id.rvSearchTourList);
        searchItemRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        tourList = new ArrayList<>();
        adapter = new SearchToutAdapter(view.getContext(), tourList, this);
        searchItemRecyclerView.setAdapter(adapter);


        loadSearchTour();


        searchBtn.setOnClickListener(v->{
            filterTourPackage();
        });
    }

    private void filterTourPackage() {
        String query = searchEditText.getText().toString().trim().toLowerCase();

        Call<List<TourPackageResponse>> call = RetrofitClient.getApiService().getTourPackage();
        call.enqueue(new Callback<List<TourPackageResponse>>() {
            @Override
            public void onResponse(Call<List<TourPackageResponse>> call, Response<List<TourPackageResponse>> response) {
                tourList.clear();

                if (response.isSuccessful() && response != null){
                    for (TourPackageResponse packageResponse: response.body()){
                        if (packageResponse.getPackName().toLowerCase().contains(query)
                        || packageResponse.getPackDescription().toLowerCase().contains(query)
                        || packageResponse.getPackStatus().toLowerCase().contains(query)){

                            tourList.add(packageResponse);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TourPackageResponse>> call, Throwable t) {
                Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSearchTour() {
        Call<List<TourPackageResponse>> listCall = RetrofitClient.getApiService().getTourPackage();
        listCall.enqueue(new Callback<List<TourPackageResponse>>() {
            @Override
            public void onResponse(Call<List<TourPackageResponse>> call, Response<List<TourPackageResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tourList.clear();
                    tourList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(requireContext(), "Packages failed to load", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TourPackageResponse>> call, Throwable t) {
                t.printStackTrace();

                Toast.makeText(
                        requireContext(),
                        t.getClass().getSimpleName() + ": " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();

                Log.e("API_ERROR", "Error: ", t);
            }
        });
    }

    @Override
    public void onSearchItemClick(TourPackageResponse tourModel) {
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
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        ArrayList<PackageDestinationResponse> list = (ArrayList<PackageDestinationResponse>) tourModel.getDestinations();
        DestinationAdapter destinationAdapter = new DestinationAdapter(requireContext(),list);
        recyclerView.setAdapter(destinationAdapter);


        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }


        //setting image
        Glide.with(dialog.getContext())
                .load(tourModel.getPackThumbnail())
                .error(R.drawable.ic_default_image)
                .placeholder(R.drawable.ic_default_image)
                .into(img);


        packageName.setText(tourModel.getPackName());
        packageStatus.setText(tourModel.getPackStatus());
        duration.setText(String.valueOf(tourModel.getPackDurationDay()));
        rating.setText(String.valueOf(tourModel.getPackRating()));
        price.setText(String.valueOf(tourModel.getPackPrice()));
        days.setText(String.valueOf(tourModel.getPackDurationDay()));
        nights.setText(String.valueOf(tourModel.getPackDurationNight()));
        remainingSeats.setText(String.valueOf(tourModel.getPackMaxCapacity() - 5));
        description.setText(tourModel.getPackDescription());


        bookTourBtn.setOnClickListener(v -> {
            bookingPackage(tourModel.getPackageId(),tourModel.getPackPrice(),tourModel.getPackName());
            dialog.dismiss();
        });


        closeBtn.setOnClickListener(v -> {
            dialog.dismiss();
        });


        dialog.show();

        if (dialog.getWindow() != null) dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
    }

    private void bookingPackage(Integer packageId, Integer packPrice, String packName) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_booking);

        EditText numberOfMemberInput = dialog.findViewById(R.id.etNumberOfMembers);
        AppCompatButton confirmBtn = dialog.findViewById(R.id.btnConfirmBooking);
        ImageView cancelBtn = dialog.findViewById(R.id.btnCloseBookingDialog);
        TextView bookingPrice = dialog.findViewById(R.id.bookingPrice);
        numberOfMemberInput.setText("1");
        int members = Integer.parseInt(numberOfMemberInput.getText().toString().trim());
        int totalPrice = members*packPrice;
        bookingPrice.setText("Rs."+totalPrice*members);





        confirmBtn.setOnClickListener(v->{
            confirmBtn.setEnabled(false);

            int newMembers = Integer.parseInt(numberOfMemberInput.getText().toString().trim());
            int totalPackPrice = newMembers*packPrice;

            SessionManager sessionManager = new SessionManager(requireContext());
            BookingRequest request = new BookingRequest(packageId,sessionManager.getKeyCustName(),sessionManager.getKeyCustEmail(),sessionManager.getKeyCustPhone(),newMembers,packName,totalPackPrice);
            Call<BookingResponse> call = RetrofitClient.getApiService().bookTour(request);

            call.enqueue(new Callback<BookingResponse>() {
                @Override
                public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
                    if (response.isSuccessful() && response.body() != null){
                        dialog.dismiss();
                    } else {
                        Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<BookingResponse> call, Throwable t) {
                    Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });


        cancelBtn.setOnClickListener(v->{
            dialog.dismiss();
        });


        dialog.show();

        if (dialog.getWindow() != null) dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
    }
}