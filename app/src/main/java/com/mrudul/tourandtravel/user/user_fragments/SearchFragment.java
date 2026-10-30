package com.mrudul.tourandtravel.user.user_fragments;

import android.app.AlertDialog;
import android.os.Bundle;
<<<<<<< HEAD
=======

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
>>>>>>> 5705a3faf7ae0ac8091c7aab87b2b459c72910b2
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.mrudul.tourandtravel.R;
<<<<<<< HEAD
import com.mrudul.tourandtravel.user.user_adapters.PopularPackageAdapter;
import com.mrudul.tourandtravel.user.user_models.TourPackageModel;
=======
import com.mrudul.tourandtravel.network.RetrofitClient;
import com.mrudul.tourandtravel.user.user_adapters.DestinationAdapter;
import com.mrudul.tourandtravel.user.user_adapters.SearchToutAdapter;
import com.mrudul.tourandtravel.user.user_interfaces.SearchItemClick;
import com.mrudul.tourandtravel.user.user_models.BookingRequest;
import com.mrudul.tourandtravel.user.user_models.BookingResponse;
import com.mrudul.tourandtravel.user.user_models.PackageDestinationResponse;
import com.mrudul.tourandtravel.user.user_models.TourPackageResponse;
import com.mrudul.tourandtravel.utils.SessionManager;
>>>>>>> 5705a3faf7ae0ac8091c7aab87b2b459c72910b2

import java.text.SimpleDateFormat;
import java.util.ArrayList;
<<<<<<< HEAD
import java.util.Date;
import java.util.Locale;
=======
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
>>>>>>> 5705a3faf7ae0ac8091c7aab87b2b459c72910b2

/**
 * SearchFragment — SmartTour360 Search Packages Screen
 *
 * Fixes applied:
 *  1. MaterialDatePicker now uses getChildFragmentManager() — fixes crash
 *     when tapping Check-In / Check-Out rows inside a Fragment host.
 *  2. Destination validation uses Toast only (setError only works on EditText).
 *  3. Button background rendering fixed via layout xml (AppCompat style).
 *  4. Travellers picker simplified to avoid unused import of activity_list_item.
 *
 * Existing IDs preserved: rvSearchTourList, tvSearchDest, tvCheckinDate,
 *                          tvCheckoutDate, tvTravellers, btnSearchPackages.
 */
public class SearchFragment extends Fragment
        implements PopularPackageAdapter.OnPackageClickListener {

<<<<<<< HEAD
    // ── Views ──────────────────────────────────────────────────────────────
    RecyclerView searchItemRecyclerView;
    private TextView     tvSearchDest, tvCheckinDate, tvCheckoutDate, tvTravellers;
    private Button       btnSearchPackages;
    private View         layoutEmptyState;
=======
    EditText searchEditText;
    ImageView searchBtn;
    RecyclerView searchItemRecyclerView;
    ArrayList<TourPackageResponse> tourList;
    SearchToutAdapter adapter;
>>>>>>> 5705a3faf7ae0ac8091c7aab87b2b459c72910b2

    private PopularPackageAdapter       popularAdapter;
    private ArrayList<TourPackageModel> popularList;

    // ── State ──────────────────────────────────────────────────────────────
    private String selectedDestination = "";
    private long   checkinMs  = -1;
    private long   checkoutMs = -1;
    private int    adultCount = 1;
    private int    childCount = 0;

    // Legacy newInstance kept for compat
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1, mParam2;

    public SearchFragment() {}

    public static SearchFragment newInstance(String p1, String p2) {
        SearchFragment f = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, p1);
        args.putString(ARG_PARAM2, p2);
        f.setArguments(args);
        return f;
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
<<<<<<< HEAD

        // ── Bind ─────────────────────────────────────────────────────────────
        searchItemRecyclerView = view.findViewById(R.id.rvSearchTourList);
        tvSearchDest      = view.findViewById(R.id.tvSearchDest);
        tvCheckinDate     = view.findViewById(R.id.tvCheckinDate);
        tvCheckoutDate    = view.findViewById(R.id.tvCheckoutDate);
        tvTravellers      = view.findViewById(R.id.tvTravellers);
        btnSearchPackages = view.findViewById(R.id.btnSearchPackages);
        layoutEmptyState  = view.findViewById(R.id.layoutSearchEmptyState);

        // ── RecyclerView ──────────────────────────────────────────────────────
        searchItemRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        buildPopularList();
        popularAdapter = new PopularPackageAdapter(requireContext(), popularList, this);
        searchItemRecyclerView.setAdapter(popularAdapter);

        // ── Row listeners ──────────────────────────────────────────────────
        view.findViewById(R.id.searchDestRow)
            .setOnClickListener(v -> showDestinationPicker());

        // FIX 1: Use getChildFragmentManager() — NOT getParentFragmentManager()
        //        Using getParentFragmentManager() from inside a Fragment causes
        //        IllegalStateException: Fragment not attached to an activity.
        view.findViewById(R.id.searchCheckinRow)
            .setOnClickListener(v -> showDatePicker(true));

        view.findViewById(R.id.searchCheckoutRow)
            .setOnClickListener(v -> showDatePicker(false));

        view.findViewById(R.id.searchTravellersRow)
            .setOnClickListener(v -> showTravellersPicker());

        btnSearchPackages.setOnClickListener(v -> performSearch());

        // View All
        TextView tvViewAll = view.findViewById(R.id.tvViewAll);
        if (tvViewAll != null) {
            tvViewAll.setOnClickListener(v ->
                Toast.makeText(getContext(), "View All Packages", Toast.LENGTH_SHORT).show());
        }

        // Notification
        View notif = view.findViewById(R.id.searchNotifContainer);
        if (notif != null) {
            notif.setOnClickListener(v ->
                Toast.makeText(getContext(), "Notifications", Toast.LENGTH_SHORT).show());
        }
=======
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
>>>>>>> 5705a3faf7ae0ac8091c7aab87b2b459c72910b2
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Destination picker
    // ─────────────────────────────────────────────────────────────────────────
    private void showDestinationPicker() {
        final String[] destinations = {
            "Goa", "Mumbai", "Manali", "Kashmir",
            "Ratnagiri", "Jaipur", "Bali", "Kerala"
        };
        new AlertDialog.Builder(requireContext())
            .setTitle("Select Destination")
            .setItems(destinations, (dialog, which) -> {
                selectedDestination = destinations[which];
                tvSearchDest.setText(selectedDestination);
                tvSearchDest.setTextColor(android.graphics.Color.parseColor("#0D5BB5"));
            })
            .show();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Date pickers
    // FIX 1: show() must use getChildFragmentManager() when called from a Fragment.
    //        getParentFragmentManager() throws IllegalStateException on navigation.
    // ─────────────────────────────────────────────────────────────────────────
    private void showDatePicker(final boolean isCheckin) {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now());

        if (!isCheckin && checkinMs > 0) {
            constraintsBuilder.setValidator(DateValidatorPointForward.from(checkinMs));
        }

        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(isCheckin ? "Select Check-In Date" : "Select Check-Out Date")
                .setCalendarConstraints(constraintsBuilder.build())
                .build();

        picker.addOnPositiveButtonClickListener(selection -> {
            String formatted = formatDate(selection);
            if (isCheckin) {
                checkinMs = selection;
                tvCheckinDate.setText(formatted);
                tvCheckinDate.setTextColor(android.graphics.Color.parseColor("#0D5BB5"));
                // Reset check-out if it is now before check-in
                if (checkoutMs > 0 && checkoutMs <= checkinMs) {
                    checkoutMs = -1;
                    tvCheckoutDate.setText(R.string.search_date_hint);
                    tvCheckoutDate.setTextColor(android.graphics.Color.parseColor("#8AAAC8"));
                }
            } else {
                checkoutMs = selection;
                tvCheckoutDate.setText(formatted);
                tvCheckoutDate.setTextColor(android.graphics.Color.parseColor("#0D5BB5"));
            }
        });

        // *** KEY FIX: getChildFragmentManager() not getParentFragmentManager() ***
        picker.show(getChildFragmentManager(), "datePicker_" + isCheckin);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Travellers picker
    // ─────────────────────────────────────────────────────────────────────────
    private void showTravellersPicker() {
        final String[] options = {
            "1 Adult",
            "2 Adults",
            "3 Adults",
            "2 Adults \u2022 1 Child",
            "2 Adults \u2022 2 Children",
            "Family (2A + 2C)"
        };
        new AlertDialog.Builder(requireContext())
            .setTitle("Select Travelers")
            .setItems(options, (dialog, which) -> {
                switch (which) {
                    case 0: adultCount = 1; childCount = 0; break;
                    case 1: adultCount = 2; childCount = 0; break;
                    case 2: adultCount = 3; childCount = 0; break;
                    case 3: adultCount = 2; childCount = 1; break;
                    case 4: adultCount = 2; childCount = 2; break;
                    case 5: adultCount = 2; childCount = 2; break;
                }
                updateTravellersText();
            })
            .show();
    }

    private void updateTravellersText() {
        String text = adultCount + (adultCount == 1 ? " Adult" : " Adults");
        if (childCount > 0) {
            text += " \u2022 " + childCount + (childCount == 1 ? " Child" : " Children");
        }
        tvTravellers.setText(text);
        tvTravellers.setTextColor(android.graphics.Color.parseColor("#0D5BB5"));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Search validation
    // FIX 2: Removed tvSearchDest.setError() — setError() only works on EditText,
    //        not plain TextView. Using Toast for all validation feedback.
    // ─────────────────────────────────────────────────────────────────────────
    private void performSearch() {
        if (selectedDestination.isEmpty()) {
            // FIX: Use Toast only — setError() on TextView silently fails
            Toast.makeText(getContext(),
                    "Please select a destination", Toast.LENGTH_SHORT).show();
            // Visually highlight the destination row
            tvSearchDest.setTextColor(android.graphics.Color.parseColor("#E53935"));
            tvSearchDest.setText("Where do you want to go? \u2B05");
            return;
        }
        if (checkinMs < 0) {
            Toast.makeText(getContext(),
                    "Please select a check-in date", Toast.LENGTH_SHORT).show();
            tvCheckinDate.setTextColor(android.graphics.Color.parseColor("#E53935"));
            return;
        }
        if (checkoutMs < 0) {
            Toast.makeText(getContext(),
                    "Please select a check-out date", Toast.LENGTH_SHORT).show();
            tvCheckoutDate.setTextColor(android.graphics.Color.parseColor("#E53935"));
            return;
        }
        if (checkoutMs <= checkinMs) {
            Toast.makeText(getContext(),
                    "Check-out must be after check-in", Toast.LENGTH_SHORT).show();
            tvCheckoutDate.setTextColor(android.graphics.Color.parseColor("#E53935"));
            return;
        }

        // All valid — show result
        // TODO: Replace with Retrofit API call when backend is ready.
        String msg = "Searching " + selectedDestination
                + " \u2022 " + adultCount + " Adult"
                + (childCount > 0 ? " \u2022 " + childCount + " Child" : "")
                + "\n" + formatDate(checkinMs) + " \u2192 " + formatDate(checkoutMs);
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Package click callback
    // ─────────────────────────────────────────────────────────────────────────
    @Override
<<<<<<< HEAD
    public void onPackageClick(TourPackageModel pkg) {
        Toast.makeText(getContext(),
                "Opening: " + pkg.getTitle(), Toast.LENGTH_SHORT).show();
        // TODO: Navigate to PackageDetailsActivity with pkg details
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Popular packages local data
    // ─────────────────────────────────────────────────────────────────────────
    private void buildPopularList() {
        popularList = new ArrayList<>();
=======
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
>>>>>>> 5705a3faf7ae0ac8091c7aab87b2b459c72910b2

        popularList.add(new TourPackageModel(
            R.drawable.dest_goa, null,
            "Goa Beach Escape",
            "Sun, Sand & Endless Vibes",
            "3 Days \u2022 2 Nights",
            "Goa",
            "5+ People",
            "12,999",
            4.5f, "632", "Popular"));

<<<<<<< HEAD
        popularList.add(new TourPackageModel(
            R.drawable.dest_manali, null,
            "Manali Getaway",
            "Mountains, Snow & Serenity",
            "4 Days \u2022 3 Nights",
            "Manali, Himachal Pradesh",
            "5+ People",
            "18,999",
            4.6f, "856", "Popular"));
=======
        //setting image
        Glide.with(dialog.getContext())
                .load(tourModel.getPackThumbnail())
                .error(R.drawable.ic_default_image)
                .placeholder(R.drawable.ic_default_image)
                .into(img);
>>>>>>> 5705a3faf7ae0ac8091c7aab87b2b459c72910b2

        popularList.add(new TourPackageModel(
            R.drawable.dest_kashmir, null,
            "Kashmir Winter Holiday",
            "Snowy Peaks & Serene Lakes",
            "6 Days \u2022 5 Nights",
            "Srinagar, Kashmir",
            "5+ People",
            "24,999",
            4.7f, "1.1k", "Festival Special"));

<<<<<<< HEAD
        popularList.add(new TourPackageModel(
            R.drawable.dest_rajasthan, null,
            "Rajasthan Cultural Tour",
            "Royal Heritage & Timeless Beauty",
            "5 Days \u2022 4 Nights",
            "Jaipur - Jodhpur - Udaipur",
            "5+ People",
            "18,999",
            4.6f, "742", "Holiday Package"));
=======
        packageName.setText(tourModel.getPackName());
        packageStatus.setText(tourModel.getPackStatus());
        duration.setText(String.valueOf(tourModel.getPackDurationDay()));
        rating.setText(String.valueOf(tourModel.getPackRating()));
        price.setText(String.valueOf(tourModel.getPackPrice()));
        days.setText(String.valueOf(tourModel.getPackDurationDay()));
        nights.setText(String.valueOf(tourModel.getPackDurationNight()));
        remainingSeats.setText(String.valueOf(tourModel.getPackMaxCapacity() - 5));
        description.setText(tourModel.getPackDescription());
>>>>>>> 5705a3faf7ae0ac8091c7aab87b2b459c72910b2

        popularList.add(new TourPackageModel(
            R.drawable.dest_kerala, null,
            "Kerala Backwater Retreat",
            "Relax, Explore & Experience Kerala",
            "4 Days \u2022 3 Nights",
            "Alleppey - Munnar",
            "5+ People",
            "19,999",
            4.7f, "918", "Holiday Package"));

<<<<<<< HEAD
        popularList.add(new TourPackageModel(
            R.drawable.dest_andaman, null,
            "Andaman Island Escape",
            "Crystal Waters & Tropical Bliss",
            "5 Days \u2022 4 Nights",
            "Port Blair - Havelock",
            "5+ People",
            "22,999",
            4.9f, "1.2k", "Popular"));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────
    private String formatDate(long ms) {
        return new SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                .format(new Date(ms));
=======
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
>>>>>>> 5705a3faf7ae0ac8091c7aab87b2b459c72910b2
    }
}