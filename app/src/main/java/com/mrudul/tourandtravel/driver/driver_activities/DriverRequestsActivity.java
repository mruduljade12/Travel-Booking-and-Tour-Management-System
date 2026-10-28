package com.mrudul.tourandtravel.driver.driver_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.driver.driver_adapter.DriverRequestAdapter;
import com.mrudul.tourandtravel.driver.driver_interfaces.DriverRequestClickListener;
import com.mrudul.tourandtravel.driver.driver_models.DriverRequestModel;
import com.mrudul.tourandtravel.driver.driver_models.DriverRequestRepository;

import java.util.ArrayList;

/**
 * SmartTour360 Driver App — Screen 5: Driver Requests
 *
 * Uses DriverRequestRepository (singleton) — same data as
 * DriverScheduleFragment so both screens stay in sync.
 *
 * Tabs: All | Pending | Accepted
 */
public class DriverRequestsActivity extends AppCompatActivity
        implements DriverRequestClickListener {

    private static final int TAB_ALL      = 0;
    private static final int TAB_PENDING  = 1;
    private static final int TAB_ACCEPTED = 2;

    private TextView             tabAll, tabPending, tabAccepted;
    private RecyclerView         rvRequests;
    private DriverRequestAdapter adapter;

    private final ArrayList<DriverRequestModel> displayList = new ArrayList<>();
    private int currentTab = TAB_ALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_driver_requests);

        View root = findViewById(R.id.main);
        if (root != null) {
            ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
                Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                return insets;
            });
        }

        bindViews();
        setupRecyclerView();
        setupTabs();
        setupBottomNav();
        selectTab(TAB_ALL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        filterAndDisplay(currentTab);
    }

    // ─────────────────────────────────────────────────────────

    private void bindViews() {
        tabAll      = findViewById(R.id.tab_all);
        tabPending  = findViewById(R.id.tab_pending);
        tabAccepted = findViewById(R.id.tab_accepted);
        rvRequests  = findViewById(R.id.rv_driver_requests);

        View ivBack = findViewById(R.id.iv_req_back);
        if (ivBack != null) ivBack.setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        adapter = new DriverRequestAdapter(this, displayList, this);
        rvRequests.setLayoutManager(new LinearLayoutManager(this));
        rvRequests.setAdapter(adapter);
    }

    private void setupTabs() {
        tabAll.setOnClickListener(v      -> selectTab(TAB_ALL));
        tabPending.setOnClickListener(v  -> selectTab(TAB_PENDING));
        tabAccepted.setOnClickListener(v -> selectTab(TAB_ACCEPTED));
    }

    private void setupBottomNav() {
        BottomNavigationView nav = findViewById(R.id.req_bottom_nav);
        if (nav == null) return;
        nav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.driverHome) {
                finish();
                return true;
            }
            return false;
        });
    }

    private void selectTab(int tab) {
        currentTab = tab;
        refreshTabLabels();
        filterAndDisplay(tab);
    }

    private void refreshTabLabels() {
        DriverRequestRepository repo = DriverRequestRepository.getInstance();
        int pending  = repo.countByStatus(DriverRequestModel.STATUS_PENDING);
        int accepted = repo.countByStatus(DriverRequestModel.STATUS_ACCEPTED);
        int all = repo.getAll().size();

        tabAll.setText(getString(R.string.req_tab_all) + " (" + all + ")");
        tabPending.setText(getString(R.string.req_tab_pending) + " (" + pending + ")");
        tabAccepted.setText(getString(R.string.req_tab_accepted) + " (" + accepted + ")");

        applyTabStyle(tabAll,      currentTab == TAB_ALL);
        applyTabStyle(tabPending,  currentTab == TAB_PENDING);
        applyTabStyle(tabAccepted, currentTab == TAB_ACCEPTED);
    }

    private void applyTabStyle(TextView tab, boolean selected) {
        if (selected) {
            tab.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_tab_selected));
            tab.setTextColor(ContextCompat.getColor(this, R.color.req_tab_selected_text));
            tab.setTypeface(null, android.graphics.Typeface.BOLD);
        } else {
            tab.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_tab_unselected));
            tab.setTextColor(ContextCompat.getColor(this, R.color.req_tab_unselected_text));
            tab.setTypeface(null, android.graphics.Typeface.NORMAL);
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
            default:
                for (DriverRequestModel r : repo.getAll()) {
                    if (r.getStatus() != DriverRequestModel.STATUS_REJECTED)
                        displayList.add(r);
                }
                break;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAcceptRequest(DriverRequestModel model, int position) {
        model.setStatus(DriverRequestModel.STATUS_ACCEPTED);
        refreshTabLabels();
        filterAndDisplay(currentTab);
    }

    @Override
    public void onRejectRequest(DriverRequestModel model, int position) {
        model.setStatus(DriverRequestModel.STATUS_REJECTED);
        refreshTabLabels();
        filterAndDisplay(currentTab);
    }
}