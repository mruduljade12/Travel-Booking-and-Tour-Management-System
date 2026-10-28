package com.mrudul.tourandtravel.driver.driver_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.driver.driver_activities.NavigateToPickupActivity;
import com.mrudul.tourandtravel.driver.driver_adapter.DriverRequestAdapter;
import com.mrudul.tourandtravel.driver.driver_interfaces.DriverRequestClickListener;
import com.mrudul.tourandtravel.driver.driver_models.DriverRequestModel;
import com.mrudul.tourandtravel.driver.driver_models.DriverRequestRepository;

import java.util.ArrayList;

/**
 * SmartTour360 Driver App — Schedule Fragment (unified request management)
 *
 * 4 tabs: All Requests | Pending | Accepted | Completed
 *
 * Data source: DriverRequestRepository singleton — same data as
 * DriverRequestsActivity so both screens always reflect the latest status.
 *
 * Pending cards show Accept / Reject buttons.
 * Accepted cards navigate to NavigateToPickupActivity on tap.
 */
public class DriverScheduleFragment extends Fragment
        implements DriverRequestClickListener {

    private static final int TAB_ALL       = 0;
    private static final int TAB_PENDING   = 1;
    private static final int TAB_ACCEPTED  = 2;
    private static final int TAB_COMPLETED = 3;

    private TextView     tabAll, tabPending, tabAccepted, tabCompleted;
    private RecyclerView rvRequests;
    private DriverRequestAdapter adapter;

    private final ArrayList<DriverRequestModel> displayList = new ArrayList<>();
    private int currentTab = TAB_ALL;

    public DriverScheduleFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_driver_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabAll       = view.findViewById(R.id.tab_all);
        tabPending   = view.findViewById(R.id.tab_pending);
        tabAccepted  = view.findViewById(R.id.tab_accepted);
        tabCompleted = view.findViewById(R.id.tab_completed);
        rvRequests   = view.findViewById(R.id.rv_schedule_requests);

        setupRecyclerView();
        setupTabs();
        selectTab(TAB_ALL);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh when returning from NavigateToPickup or other screens
        filterAndDisplay(currentTab);
    }

    // ─────────────────────────────────────────────────────────
    //  RecyclerView
    // ─────────────────────────────────────────────────────────

    private void setupRecyclerView() {
        adapter = new DriverRequestAdapter(requireContext(), displayList, this);
        rvRequests.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvRequests.setAdapter(adapter);
    }

    // ─────────────────────────────────────────────────────────
    //  Tab switching
    // ─────────────────────────────────────────────────────────

    private void setupTabs() {
        tabAll.setOnClickListener(v       -> selectTab(TAB_ALL));
        tabPending.setOnClickListener(v   -> selectTab(TAB_PENDING));
        tabAccepted.setOnClickListener(v  -> selectTab(TAB_ACCEPTED));
        tabCompleted.setOnClickListener(v -> selectTab(TAB_COMPLETED));
    }

    private void selectTab(int tab) {
        currentTab = tab;
        applyTabStyle(tabAll,       tab == TAB_ALL);
        applyTabStyle(tabPending,   tab == TAB_PENDING);
        applyTabStyle(tabAccepted,  tab == TAB_ACCEPTED);
        applyTabStyle(tabCompleted, tab == TAB_COMPLETED);
        filterAndDisplay(tab);
    }

    private void applyTabStyle(TextView tv, boolean selected) {
        if (selected) {
            tv.setBackground(ContextCompat.getDrawable(requireContext(),
                    R.drawable.bg_sched_tab_selected));
            tv.setTextColor(ContextCompat.getColor(requireContext(),
                    R.color.sched_tab_selected_text));
            tv.setTypeface(null, android.graphics.Typeface.BOLD);
        } else {
            tv.setBackground(null);
            tv.setTextColor(ContextCompat.getColor(requireContext(),
                    R.color.sched_tab_unselected_text));
            tv.setTypeface(null, android.graphics.Typeface.NORMAL);
        }
    }

    private void filterAndDisplay(int tab) {
        DriverRequestRepository repo = DriverRequestRepository.getInstance();
        displayList.clear();
        switch (tab) {
            case TAB_PENDING:
                displayList.addAll(repo.getByStatus(DriverRequestModel.STATUS_PENDING));
                break;
            case TAB_ACCEPTED:
                displayList.addAll(repo.getByStatus(DriverRequestModel.STATUS_ACCEPTED));
                break;
            case TAB_COMPLETED:
                displayList.addAll(repo.getByStatus(DriverRequestModel.STATUS_COMPLETED));
                break;
            default: // ALL — show all except rejected
                for (DriverRequestModel r : repo.getAll()) {
                    if (r.getStatus() != DriverRequestModel.STATUS_REJECTED) {
                        displayList.add(r);
                    }
                }
                break;
        }
        adapter.notifyDataSetChanged();
    }

    // ─────────────────────────────────────────────────────────
    //  DriverRequestClickListener
    // ─────────────────────────────────────────────────────────

    @Override
    public void onAcceptRequest(DriverRequestModel model, int position) {
        model.setStatus(DriverRequestModel.STATUS_ACCEPTED);
        filterAndDisplay(currentTab);
    }

    @Override
    public void onRejectRequest(DriverRequestModel model, int position) {
        model.setStatus(DriverRequestModel.STATUS_REJECTED);
        filterAndDisplay(currentTab);
    }
}