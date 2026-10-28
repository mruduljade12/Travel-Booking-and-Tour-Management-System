package com.mrudul.tourandtravel.driver.driver_fragments;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.driver.driver_adapter.DriverAssignPackageAdapter;
import com.mrudul.tourandtravel.driver.driver_interfaces.OnDriverHomeClick;
import com.mrudul.tourandtravel.driver.driver_models.DriverAssignPackageModel;
import com.mrudul.tourandtravel.network.DirectionsResponse;
import com.mrudul.tourandtravel.network.DirectionsRetrofitClient;
import com.mrudul.tourandtravel.network.RetrofitClient;
import com.mrudul.tourandtravel.user.user_adapters.EventListAdapter;
import com.mrudul.tourandtravel.user.user_interfaces.HomeTourClicked;
import com.mrudul.tourandtravel.user.user_models.PackageDestinationResponse;
import com.mrudul.tourandtravel.user.user_models.TourPackageResponse;
import com.mrudul.tourandtravel.utils.DriverSessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * SmartTour360 Driver App — Home Fragment
 *
 * Shows the list of tour packages assigned to the logged-in driver.
 * Tapping a package opens a dialog with a map and destination events.
 */
public class DriverHomeFragment extends Fragment implements HomeTourClicked {

    RecyclerView rvAssignedPackages;
    ArrayList<TourPackageResponse> packageList;
    DriverAssignPackageAdapter packageAdapter;

    MapView mapView;
    GoogleMap googleMap;

    public DriverHomeFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_driver_assign_package, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvAssignedPackages = view.findViewById(R.id.rvAssignedPackages);
        packageList = new ArrayList<>();
        packageAdapter = new DriverAssignPackageAdapter(view.getContext(), packageList, this);
        rvAssignedPackages.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvAssignedPackages.setAdapter(packageAdapter);

        RetrofitClient.getApiService().getTourPackage()
                .enqueue(new Callback<List<TourPackageResponse>>() {
                    @Override
                    public void onResponse(Call<List<TourPackageResponse>> call,
                                           Response<List<TourPackageResponse>> response) {
                        DriverSessionManager manager = new DriverSessionManager(requireContext());
                        packageList.clear();
                        if (response.isSuccessful() && response.body() != null) {
                            for (TourPackageResponse data : response.body()) {
                                if (data.getDriver() != null
                                        && Integer.valueOf(data.getDriver().getDriverId())
                                                  .equals(manager.getDriverId())) {
                                    packageList.add(data);
                                }
                            }
                            packageAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<TourPackageResponse>> call, Throwable t) {
                        Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onHomeTourClicked(TourPackageResponse response) {
        // no-op for driver home
    }

    @Override
    public void onRecentHomeItemClicked(TourPackageResponse response) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_tour_event);

        mapView = dialog.findViewById(R.id.customerMapView);
        ImageView cancelBtn = dialog.findViewById(R.id.homeDialogMapCancelBtn);

        RecyclerView eventRecyclerView = dialog.findViewById(R.id.eventRecyclerView);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        ArrayList<PackageDestinationResponse> destinationList = new ArrayList<>(response.getDestinations());
        EventListAdapter eventListAdapter = new EventListAdapter(requireContext(), destinationList, this);
        eventRecyclerView.setAdapter(eventListAdapter);

        dialog.show();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        }

        mapView.onCreate(null);
        mapView.onResume();
        mapView.getMapAsync(map -> googleMap = map);

        cancelBtn.setOnClickListener(v -> dialog.dismiss());
    }

    @Override
    public void onMapEventClicked(PackageDestinationResponse response) {
        DriverSessionManager sessionManager = new DriverSessionManager(requireContext());

        if (googleMap == null) {
            Toast.makeText(requireContext(), "Map is not ready", Toast.LENGTH_SHORT).show();
            return;
        }

        double destinationLat = Double.parseDouble(response.getLatitude());
        double destinationLng = Double.parseDouble(response.getLongitude());

        // Emulator starting location (Pune)
        double userLat = 18.5204;
        double userLng = 73.8567;

        LatLng userLocation  = new LatLng(userLat, userLng);
        LatLng destination   = new LatLng(destinationLat, destinationLng);

        googleMap.clear();

        String userImageUrl = sessionManager.getKeyDriverImage();

        Glide.with(requireContext()).asBitmap().load(userImageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap,
                                                Transition<? super Bitmap> transition) {
                        Bitmap marker = createMarkerBitmap(bitmap, Color.rgb(50, 180, 90));
                        googleMap.addMarker(new MarkerOptions()
                                .position(userLocation)
                                .title("Your Location")
                                .icon(BitmapDescriptorFactory.fromBitmap(marker)));
                    }
                    @Override public void onLoadCleared(Drawable placeholder) {}
                });

        Glide.with(requireContext()).asBitmap().load(response.getImage())
                .placeholder(R.drawable.ic_default_image)
                .error(R.drawable.ic_default_image)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap,
                                                Transition<? super Bitmap> transition) {
                        Bitmap marker = createMarkerBitmap(bitmap, Color.RED);
                        googleMap.addMarker(new MarkerOptions()
                                .position(destination)
                                .title(response.getName())
                                .icon(BitmapDescriptorFactory.fromBitmap(marker))
                                .anchor(0.5f, 1f));
                    }
                    @Override public void onLoadCleared(Drawable placeholder) {}
                });

        getRoute(userLat, userLng, destinationLat, destinationLng);
    }

    private void getRoute(double startLat, double startLng, double endLat, double endLng) {
        String origin      = startLat + "," + startLng;
        String destination = endLat + "," + endLng;
        String apiKey      = "AIzaSyDc-B2qS8MdTNBM1Hl1ACGz2jDJshBAOnU";

        DirectionsRetrofitClient.getApiService()
                .getDirections(origin, destination, "driving", apiKey)
                .enqueue(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call,
                                           Response<DirectionsResponse> response) {
                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().getRoutes() != null
                                && !response.body().getRoutes().isEmpty()) {

                            String encodedPoints = response.body().getRoutes().get(0)
                                    .getOverview_polyline().getPoints();
                            List<LatLng> routePoints = decodePolyline(encodedPoints);

                            googleMap.addPolyline(new PolylineOptions()
                                    .addAll(routePoints).width(10).color(Color.BLUE).geodesic(true));

                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            for (LatLng point : routePoints) builder.include(point);
                            builder.include(new LatLng(startLat, startLng));
                            builder.include(new LatLng(endLat, endLng));
                            googleMap.animateCamera(
                                    CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
                        } else {
                            Toast.makeText(requireContext(), "Route not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        Toast.makeText(requireContext(),
                                "Route error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private List<LatLng> decodePolyline(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, lat = 0, lng = 0;
        while (index < encoded.length()) {
            int b, shift = 0, result = 0;
            do { b = encoded.charAt(index++) - 63; result |= (b & 0x1f) << shift; shift += 5; } while (b >= 0x20);
            lat += ((result & 1) != 0) ? ~(result >> 1) : (result >> 1);
            shift = 0; result = 0;
            do { b = encoded.charAt(index++) - 63; result |= (b & 0x1f) << shift; shift += 5; } while (b >= 0x20);
            lng += ((result & 1) != 0) ? ~(result >> 1) : (result >> 1);
            poly.add(new LatLng(lat / 1E5, lng / 1E5));
        }
        return poly;
    }

    @Override public void onResume()      { super.onResume();      if (mapView != null) mapView.onResume(); }
    @Override public void onPause()       { if (mapView != null) mapView.onPause(); super.onPause(); }
    @Override public void onDestroyView() { if (mapView != null) { mapView.onDestroy(); mapView = null; } super.onDestroyView(); }

    private Bitmap createMarkerBitmap(Bitmap image, int borderColor) {
        int size   = 120;
        Bitmap output = Bitmap.createBitmap(size, size + 40, Bitmap.Config.ARGB_8888);
        android.graphics.Canvas canvas = new android.graphics.Canvas(output);
        android.graphics.Paint paint   = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);
        paint.setColor(borderColor); paint.setStrokeWidth(8);
        canvas.drawLine(size / 2f, 80, size / 2f, size + 20, paint);
        float cx = size / 2f, cy = 55, r = 48;
        paint.setColor(borderColor);
        canvas.drawCircle(cx, cy, r + 5, paint);
        android.graphics.Path path = new android.graphics.Path();
        path.addCircle(cx, cy, r, android.graphics.Path.Direction.CW);
        canvas.save(); canvas.clipPath(path);
        canvas.drawBitmap(Bitmap.createScaledBitmap(image, size, size, true), 0, 5, paint);
        canvas.restore();
        return output;
    }
}
