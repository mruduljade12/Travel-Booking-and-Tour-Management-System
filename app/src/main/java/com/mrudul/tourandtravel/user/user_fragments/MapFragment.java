package com.mrudul.tourandtravel.user.user_fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mrudul.tourandtravel.R;

/**
 * MapFragment — SmartTour360 Live Tracking Screen
 * ══════════════════════════════════════════════════
 * Fully offline / UI-only. No Google Maps SDK. No API key. No GPS.
 *
 * The map is rendered by GoapMapView (custom Canvas drawing).
 * All data is local mock data — no network calls.
 *
 * Interactions:
 *  • Back button → popBackStack / finish
 *  • Map View button → Toast (future: toggle satellite/terrain view)
 *  • My Location control → highlights mock location, Toast
 *  • Zoom In / Zoom Out → scale GoapMapView
 *  • Phone button → dialog (call feature placeholder)
 *  • Send button → dialog (navigation feature placeholder)
 *  • Start Navigation → dialog with mock next-stop info
 *
 * Package/folder structure is unchanged.
 */
public class MapFragment extends Fragment {

    // ── Mock zoom state ────────────────────────────────────────────────────
    private float currentScale = 1.0f;
    private static final float ZOOM_STEP = 0.15f;
    private static final float ZOOM_MIN  = 0.70f;
    private static final float ZOOM_MAX  = 2.20f;

    private GoapMapView goapMapView;

    public MapFragment() { /* required empty constructor */ }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        goapMapView = view.findViewById(R.id.navGoapMap);

        // ── Back button ────────────────────────────────────────────────────
        ImageView backBtn = view.findViewById(R.id.navBackBtn);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> {
                if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                    getParentFragmentManager().popBackStack();
                } else {
                    requireActivity().onBackPressed();
                }
            });
        }

        // ── Map View toggle button ─────────────────────────────────────────
        View mapViewBtn = view.findViewById(R.id.navMapViewBtn);
        if (mapViewBtn != null) {
            mapViewBtn.setOnClickListener(v ->
                Toast.makeText(requireContext(),
                    "Satellite / terrain view coming soon",
                    Toast.LENGTH_SHORT).show());
        }

        // ── Map Controls ───────────────────────────────────────────────────
        FrameLayout btnZoomIn  = view.findViewById(R.id.navBtnZoomIn);
        FrameLayout btnZoomOut = view.findViewById(R.id.navBtnZoomOut);
        FrameLayout btnMyLoc   = view.findViewById(R.id.navBtnMyLocation);

        if (btnZoomIn != null) {
            btnZoomIn.setOnClickListener(v -> {
                currentScale = Math.min(currentScale + ZOOM_STEP, ZOOM_MAX);
                applyMapScale();
                Toast.makeText(requireContext(), "Zoom In", Toast.LENGTH_SHORT).show();
            });
        }

        if (btnZoomOut != null) {
            btnZoomOut.setOnClickListener(v -> {
                currentScale = Math.max(currentScale - ZOOM_STEP, ZOOM_MIN);
                applyMapScale();
                Toast.makeText(requireContext(), "Zoom Out", Toast.LENGTH_SHORT).show();
            });
        }

        if (btnMyLoc != null) {
            btnMyLoc.setOnClickListener(v -> {
                // Reset zoom to default, visual feedback for "center on location"
                currentScale = 1.0f;
                applyMapScale();
                Toast.makeText(requireContext(),
                    "Centred on Your Location (mock)",
                    Toast.LENGTH_SHORT).show();
            });
        }

        // ── Phone button ───────────────────────────────────────────────────
        FrameLayout phoneBtn = view.findViewById(R.id.navPhoneBtn);
        if (phoneBtn != null) {
            phoneBtn.setOnClickListener(v -> showPhoneDialog());
        }

        // ── Send / Navigate button ─────────────────────────────────────────
        FrameLayout sendBtn = view.findViewById(R.id.navSendBtn);
        if (sendBtn != null) {
            sendBtn.setOnClickListener(v -> showNavigationDialog("send"));
        }

        // ── Start Navigation button ────────────────────────────────────────
        Button startNavBtn = view.findViewById(R.id.navStartBtn);
        if (startNavBtn != null) {
            startNavBtn.setOnClickListener(v -> showNavigationDialog("start"));
        }
    }

    // ── Zoom helper ──────────────────────────────────────────────────────────
    private void applyMapScale() {
        if (goapMapView != null) {
            goapMapView.setScaleX(currentScale);
            goapMapView.setScaleY(currentScale);
        }
    }

    // ── Dialogs ──────────────────────────────────────────────────────────────
    private void showPhoneDialog() {
        new AlertDialog.Builder(requireContext())
            .setTitle("Call Driver")
            .setMessage("Driver: Sanika Patil\nVehicle: MH-14-CD-5678\n\n" +
                        "Live call feature will be enabled in a future update.\n" +
                        "Please contact your tour coordinator.")
            .setPositiveButton("OK", null)
            .show();
    }

    private void showNavigationDialog(String trigger) {
        String title   = trigger.equals("start") ? "Start Navigation" : "Navigation";
        String message = "Mock Navigation Preview\n\n" +
                         "Next Stop: Baga Beach\n" +
                         "Route: Your Location \u2192 Candolim \u2192 Calangute \u2192 Baga Beach\n" +
                         "Estimated: ~25 min (demo)\n\n" +
                         "Live turn-by-turn navigation will be enabled\n" +
                         "in a future integration. This is a local preview.";
        new AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Got it", null)
            .show();
    }
}