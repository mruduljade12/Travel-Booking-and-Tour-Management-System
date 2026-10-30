package com.mrudul.tourandtravel.driver.driver_activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mrudul.tourandtravel.R;

/**
 * SmartTour360 Driver App — Navigate to Pickup Screen
 *
 * Receives the accepted request's pickup data via Intent extras:
 *   EXTRA_PICKUP      — e.g. "Pune Airport"
 *   EXTRA_DESTINATION — e.g. "Lonavala, Maharashtra"
 *
 * Displays a Google Map with:
 *   - Driver's current location marker (blue)
 *   - Customer pickup marker (green)
 *   - A static illustrative polyline route between the two points
 *
 * "Start Navigation" launches Google Maps turn-by-turn navigation.
 */
public class NavigateToPickupActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    // ── Intent extra keys ─────────────────────────────────────
    public static final String EXTRA_PICKUP      = "nav_pickup";
    public static final String EXTRA_DESTINATION = "nav_destination";

    // ── Reference coordinates (Pune area) ────────────────────
    // Driver location: Koregaon Park, Pune
    private static final LatLng DRIVER_LOCATION = new LatLng(18.5362, 73.8943);
    // Customer pickup: Pune Airport
    private static final LatLng PICKUP_LOCATION = new LatLng(18.5793, 73.9089);

    // ── State ─────────────────────────────────────────────────
    private String pickupName      = "Pune Airport";
    private String destinationName = "Lonavala, Maharashtra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_navigate_to_pickup);

        // Edge-to-edge insets
        View root = findViewById(R.id.main);
        if (root != null) {
            ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
                Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                return insets;
            });
        }

        // Read intent extras (fallback to defaults)
        Intent incoming = getIntent();
        if (incoming != null) {
            String p = incoming.getStringExtra(EXTRA_PICKUP);
            String d = incoming.getStringExtra(EXTRA_DESTINATION);
            if (p != null && !p.isEmpty()) pickupName      = p;
            if (d != null && !d.isEmpty()) destinationName = d;
        }

        bindViews();
        initMap();
    }

    // ─────────────────────────────────────────────────────────
    //  View binding + listeners
    // ─────────────────────────────────────────────────────────

    private void bindViews() {
        // Back button
        View ivBack = findViewById(R.id.iv_nav_back);
        if (ivBack != null) ivBack.setOnClickListener(v -> finish());

        // Populate bottom panel
        TextView tvTitle    = findViewById(R.id.tv_nav_pickup_title);
        TextView tvSubtitle = findViewById(R.id.tv_nav_pickup_subtitle);
        if (tvTitle    != null) tvTitle.setText(pickupName);
        if (tvSubtitle != null) tvSubtitle.setText(destinationName);

        // Start Navigation button
        AppCompatButton btnStart = findViewById(R.id.btn_start_navigation);
        if (btnStart != null) {
            btnStart.setOnClickListener(v -> startNavigation());
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Google Maps initialisation
    // ─────────────────────────────────────────────────────────

    private void initMap() {
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.map_fragment_container, mapFragment)
                .commit();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        // ── Driver location marker (blue) ──
        googleMap.addMarker(new MarkerOptions()
                .position(DRIVER_LOCATION)
                .title(getString(R.string.nav_your_location))
                .icon(BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_AZURE)));

        // ── Customer pickup marker (green) ──
        googleMap.addMarker(new MarkerOptions()
                .position(PICKUP_LOCATION)
                .title(pickupName)
                .snippet(destinationName)
                .icon(BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_GREEN)));

        // ── Illustrative route polyline ──
        // Intermediate waypoints trace a realistic road path
        // (Koregaon Park → Nagar Rd → Airport Rd → Pune Airport)
        googleMap.addPolyline(new PolylineOptions()
                .add(DRIVER_LOCATION)
                .add(new LatLng(18.5380, 73.9010))   // Nagar Rd
                .add(new LatLng(18.5510, 73.9060))   // mid-route
                .add(new LatLng(18.5650, 73.9075))   // Airport Rd approach
                .add(PICKUP_LOCATION)
                .color(0xFF1976D2)                   // SmartTour blue
                .width(14f)
                .geodesic(true));

        // ── Camera — show both markers ──
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(18.5580, 73.9020), 13f));
    }

    // ─────────────────────────────────────────────────────────
    //  Start Navigation — launches Google Maps turn-by-turn
    // ─────────────────────────────────────────────────────────

    private void startNavigation() {
        // Try to open Google Maps navigation to the pickup coordinates
        Uri gmmIntentUri = Uri.parse(
                "google.navigation:q=" + PICKUP_LOCATION.latitude
                        + "," + PICKUP_LOCATION.longitude
                        + "&mode=d");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            // Fallback: open in browser / any map app
            Uri fallback = Uri.parse(
                    "https://maps.google.com/?daddr="
                            + PICKUP_LOCATION.latitude + ","
                            + PICKUP_LOCATION.longitude);
            startActivity(new Intent(Intent.ACTION_VIEW, fallback));
            Toast.makeText(this,
                    "Google Maps not installed. Opening in browser.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}