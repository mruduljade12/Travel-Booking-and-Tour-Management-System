package com.mrudul.tourandtravel.user.user_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.user.user_adapters.RecentTourAdapter;
import com.mrudul.tourandtravel.user.user_adapters.TourPackageAdapter;
import com.mrudul.tourandtravel.user.user_adapters.UserTourPackageAdapter;
import com.mrudul.tourandtravel.user.user_models.TourPackageModel;
import com.mrudul.tourandtravel.user.user_models.UserTourPackages;

import java.util.ArrayList;

/**
 * Home Dashboard — SmartTour360
 *
 * Category tab behaviour:
 *   All / Adventure / Beaches  → homeSection VISIBLE, packagesSection GONE
 *   Packages                   → homeSection GONE,    packagesSection VISIBLE
 *
 * Existing IDs preserved: rvPopularTours, rvFeaturedTours
 * New IDs added: rvPackages, homeSection, packagesSection
 */
public class HomeFragment extends Fragment {

    // ── Existing fields (preserved) ──────────────────────────────────────────
    RecyclerView popRecyclerView, recentTourRecyclerView;
    ArrayList<UserTourPackages> popList, recentList;
    UserTourPackageAdapter adapter;
    RecentTourAdapter      recentAdapter;

    // ── New fields ────────────────────────────────────────────────────────────
    private RecyclerView         rvPackages;
    private TourPackageAdapter   packageAdapter;
    private ArrayList<TourPackageModel> packageList;

    private LinearLayout homeSection, packagesSection;
    private View         homeSearchBar;
    private TextView     homeCatAll, homeCatPackages, homeCatAdventure, homeCatBeaches;
    private TextView     pkgSubPopular, pkgSubFestival, pkgSubHoliday,
                         pkgSubAdventure, pkgSubBeach;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1, mParam2;

    public HomeFragment() {}

    public static HomeFragment newInstance(String p1, String p2) {
        HomeFragment f = new HomeFragment();
        Bundle b = new Bundle();
        b.putString(ARG_PARAM1, p1);
        b.putString(ARG_PARAM2, p2);
        f.setArguments(b);
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ── Bind content sections ─────────────────────────────────────────────
        homeSection     = view.findViewById(R.id.homeSection);
        packagesSection = view.findViewById(R.id.packagesSection);

        // ── Existing RecyclerViews ────────────────────────────────────────────
        popRecyclerView        = view.findViewById(R.id.rvPopularTours);
        recentTourRecyclerView = view.findViewById(R.id.rvFeaturedTours);

        popRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recentTourRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext()));

        // Popular Destinations — 6 UNIQUE destinations with REAL PHOTOS
        popList = new ArrayList<>();
        popList.add(new UserTourPackages(R.drawable.img_mahabaleshwar, "Mahabaleshwar", 5.5f, 4.5f));
        popList.add(new UserTourPackages(R.drawable.img_lonavala,      "Lonavala",      3.0f, 4.4f));
        popList.add(new UserTourPackages(R.drawable.img_goa,           "Goa",           4.0f, 4.6f));
        popList.add(new UserTourPackages(R.drawable.img_munnar,        "Munnar",        5.0f, 4.7f));
        popList.add(new UserTourPackages(R.drawable.img_kashmir,       "Kashmir",       6.0f, 4.8f));
        popList.add(new UserTourPackages(R.drawable.img_jaipur,        "Jaipur",        4.0f, 4.5f));

        adapter = new UserTourPackageAdapter(requireContext(), popList);
        popRecyclerView.setAdapter(adapter);

        // Trip Details — 4 UNIQUE destinations with REAL PHOTOS
        recentList = new ArrayList<>();
        recentList.add(new UserTourPackages(R.drawable.img_mumbai,    "Mumbai",    1.0f, 4.3f));
        recentList.add(new UserTourPackages(R.drawable.img_pune,      "Pune",      2.0f, 4.2f));
        recentList.add(new UserTourPackages(R.drawable.img_ratnagiri, "Ratnagiri", 3.0f, 4.4f));
        recentList.add(new UserTourPackages(R.drawable.img_goa,       "Goa",       4.0f, 4.6f));

        recentAdapter = new RecentTourAdapter(requireContext(), recentList);
        recentTourRecyclerView.setAdapter(recentAdapter);

        // ── Packages RecyclerView ─────────────────────────────────────────────
        rvPackages = view.findViewById(R.id.rvPackages);
        rvPackages.setLayoutManager(new LinearLayoutManager(requireContext()));
        buildPackageList();
        packageAdapter = new TourPackageAdapter(requireContext(), packageList);
        rvPackages.setAdapter(packageAdapter);

        // ── Search bar ────────────────────────────────────────────────────────
        homeSearchBar = view.findViewById(R.id.homeSearchBar);
        if (homeSearchBar != null) {
            homeSearchBar.setOnClickListener(v -> navigateToSearch());
        }

        // ── Category tabs ─────────────────────────────────────────────────────
        homeCatAll       = view.findViewById(R.id.homeCatAll);
        homeCatPackages  = view.findViewById(R.id.homeCatPackages);
        homeCatAdventure = view.findViewById(R.id.homeCatAdventure);
        homeCatBeaches   = view.findViewById(R.id.homeCatBeaches);

        homeCatAll.setOnClickListener(v -> selectHomeCategory(homeCatAll));
        homeCatPackages.setOnClickListener(v -> selectPackagesCategory());
        homeCatAdventure.setOnClickListener(v -> selectHomeCategory(homeCatAdventure));
        homeCatBeaches.setOnClickListener(v -> selectHomeCategory(homeCatBeaches));

        // ── Package sub-filters ───────────────────────────────────────────────
        pkgSubPopular  = view.findViewById(R.id.pkgSubPopular);
        pkgSubFestival = view.findViewById(R.id.pkgSubFestival);
        pkgSubHoliday  = view.findViewById(R.id.pkgSubHoliday);
        pkgSubAdventure= view.findViewById(R.id.pkgSubAdventure);
        pkgSubBeach    = view.findViewById(R.id.pkgSubBeach);

        View.OnClickListener subListener = sub -> {
            resetSubFilters();
            sub.setBackgroundResource(R.drawable.pkg_sub_active);
            if (sub instanceof TextView) {
                ((TextView) sub).setTextColor(android.graphics.Color.WHITE);
            }
        };
        if (pkgSubPopular  != null) pkgSubPopular.setOnClickListener(subListener);
        if (pkgSubFestival != null) pkgSubFestival.setOnClickListener(subListener);
        if (pkgSubHoliday  != null) pkgSubHoliday.setOnClickListener(subListener);
        if (pkgSubAdventure!= null) pkgSubAdventure.setOnClickListener(subListener);
        if (pkgSubBeach    != null) pkgSubBeach.setOnClickListener(subListener);

        // ── Notification + hamburger ──────────────────────────────────────────
        View notif = view.findViewById(R.id.homeNotifContainer);
        if (notif != null) {
            notif.setOnClickListener(v ->
                Toast.makeText(getContext(), "Notifications", Toast.LENGTH_SHORT).show());
        }
        View ham = view.findViewById(R.id.homeHamburger);
        if (ham != null) {
            ham.setOnClickListener(v ->
                Toast.makeText(getContext(), "Menu", Toast.LENGTH_SHORT).show());
        }
    }

    // ── Category selection helpers ────────────────────────────────────────────

    /** All/Adventure/Beaches → show home section, hide packages */
    private void selectHomeCategory(TextView selected) {
        resetMainCategories();
        selected.setBackgroundResource(R.drawable.home_cat_active);
        selected.setTextColor(android.graphics.Color.WHITE);
        homeSection.setVisibility(View.VISIBLE);
        packagesSection.setVisibility(View.GONE);
    }

    /** Packages → hide home section, show packages section */
    private void selectPackagesCategory() {
        resetMainCategories();
        homeCatPackages.setBackgroundResource(R.drawable.home_cat_active);
        homeCatPackages.setTextColor(android.graphics.Color.WHITE);
        homeSection.setVisibility(View.GONE);
        packagesSection.setVisibility(View.VISIBLE);
    }

    private void resetMainCategories() {
        int blue = android.graphics.Color.parseColor("#0D5BB5");
        for (TextView tv : new TextView[]{homeCatAll, homeCatPackages,
                                          homeCatAdventure, homeCatBeaches}) {
            if (tv != null) {
                tv.setBackgroundResource(R.drawable.home_cat_inactive);
                tv.setTextColor(blue);
            }
        }
    }

    private void resetSubFilters() {
        int blue = android.graphics.Color.parseColor("#0D5BB5");
        for (TextView tv : new TextView[]{pkgSubPopular, pkgSubFestival,
                pkgSubHoliday, pkgSubAdventure, pkgSubBeach}) {
            if (tv != null) {
                tv.setBackgroundResource(R.drawable.pkg_sub_inactive);
                tv.setTextColor(blue);
            }
        }
    }

    private void navigateToSearch() {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, new SearchFragment())
                    .commit();
        }
    }

    // ── Package data — 6 UNIQUE destinations, NO DUPLICATES ──────────────────
    private void buildPackageList() {
        packageList = new ArrayList<>();

        packageList.add(new TourPackageModel(
                R.drawable.img_goa,
                "Goa Beach Holiday",
                "Sun, Sand & Endless Vibes",
                "4 Days / 3 Nights",
                "Goa",
                "5+ People",
                "\u20B9 16,999",
                4.8f,
                "Popular"));

        packageList.add(new TourPackageModel(
                R.drawable.img_kashmir,
                "Kashmir Winter Holiday",
                "Snowy Peaks & Serene Lakes",
                "6 Days / 5 Nights",
                "Srinagar",
                "5+ People",
                "\u20B9 24,999",
                4.7f,
                "Festival Special"));

        packageList.add(new TourPackageModel(
                R.drawable.img_jaipur,
                "Rajasthan Cultural Tour",
                "Royal Heritage & Timeless Beauty",
                "5 Days / 4 Nights",
                "Jaipur - Jodhpur - Udaipur",
                "5+ People",
                "\u20B9 18,999",
                4.6f,
                "Holiday Package"));

        packageList.add(new TourPackageModel(
                R.drawable.img_mahabaleshwar,
                "Manali Adventure Escape",
                "Mountains, Snow & Adventure",
                "5 Days / 4 Nights",
                "Manali",
                "5+ People",
                "\u20B9 21,999",
                4.8f,
                "Adventure"));

        packageList.add(new TourPackageModel(
                R.drawable.img_munnar,
                "Kerala Backwater Retreat",
                "Relax, Explore & Experience Kerala",
                "4 Days / 3 Nights",
                "Alleppey - Munnar",
                "5+ People",
                "\u20B9 19,999",
                4.7f,
                "Holiday Package"));

        packageList.add(new TourPackageModel(
                R.drawable.img_ratnagiri,
                "Andaman Island Escape",
                "Crystal Waters & Tropical Bliss",
                "5 Days / 4 Nights",
                "Port Blair - Havelock",
                "5+ People",
                "\u20B9 22,999",
                4.9f,
                "Popular"));
    }
}