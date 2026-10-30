package com.mrudul.tourandtravel.user.user_fragments;

import android.app.AlertDialog;
import android.os.Bundle;
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
import com.mrudul.tourandtravel.user.user_adapters.PopularPackageAdapter;
import com.mrudul.tourandtravel.user.user_models.TourPackageModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

    // ── Views ──────────────────────────────────────────────────────────────
    RecyclerView searchItemRecyclerView;
    private TextView     tvSearchDest, tvCheckinDate, tvCheckoutDate, tvTravellers;
    private Button       btnSearchPackages;
    private View         layoutEmptyState;

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

        popularList.add(new TourPackageModel(
            R.drawable.dest_goa, null,
            "Goa Beach Escape",
            "Sun, Sand & Endless Vibes",
            "3 Days \u2022 2 Nights",
            "Goa",
            "5+ People",
            "12,999",
            4.5f, "632", "Popular"));

        popularList.add(new TourPackageModel(
            R.drawable.dest_manali, null,
            "Manali Getaway",
            "Mountains, Snow & Serenity",
            "4 Days \u2022 3 Nights",
            "Manali, Himachal Pradesh",
            "5+ People",
            "18,999",
            4.6f, "856", "Popular"));

        popularList.add(new TourPackageModel(
            R.drawable.dest_kashmir, null,
            "Kashmir Winter Holiday",
            "Snowy Peaks & Serene Lakes",
            "6 Days \u2022 5 Nights",
            "Srinagar, Kashmir",
            "5+ People",
            "24,999",
            4.7f, "1.1k", "Festival Special"));

        popularList.add(new TourPackageModel(
            R.drawable.dest_rajasthan, null,
            "Rajasthan Cultural Tour",
            "Royal Heritage & Timeless Beauty",
            "5 Days \u2022 4 Nights",
            "Jaipur - Jodhpur - Udaipur",
            "5+ People",
            "18,999",
            4.6f, "742", "Holiday Package"));

        popularList.add(new TourPackageModel(
            R.drawable.dest_kerala, null,
            "Kerala Backwater Retreat",
            "Relax, Explore & Experience Kerala",
            "4 Days \u2022 3 Nights",
            "Alleppey - Munnar",
            "5+ People",
            "19,999",
            4.7f, "918", "Holiday Package"));

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
    }
}