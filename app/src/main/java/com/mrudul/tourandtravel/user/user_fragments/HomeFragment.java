package com.mrudul.tourandtravel.user.user_fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.network.DirectionsResponse;
import com.mrudul.tourandtravel.network.DirectionsRetrofitClient;
import com.mrudul.tourandtravel.network.RetrofitClient;
import com.mrudul.tourandtravel.user.user_adapters.DestinationAdapter;
import com.mrudul.tourandtravel.user.user_adapters.EventListAdapter;
import com.mrudul.tourandtravel.user.user_adapters.RecentTourAdapter;
import com.mrudul.tourandtravel.user.user_adapters.UserTourPackageAdapter;
import com.mrudul.tourandtravel.user.user_interfaces.HomeTourClicked;
import com.mrudul.tourandtravel.user.user_models.BookingResponse;
import com.mrudul.tourandtravel.user.user_models.PackageDestinationResponse;
import com.mrudul.tourandtravel.user.user_models.TourPackageResponse;
import com.mrudul.tourandtravel.user.user_models.UserTourPackages;
import com.mrudul.tourandtravel.utils.SessionManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements HomeTourClicked {

    RecyclerView popRecyclerView,recentTourRecyclerView;
    ArrayList<TourPackageResponse> popList;
    ArrayList<TourPackageResponse> recentList;
    ArrayList<PackageDestinationResponse> destinationList;
    UserTourPackageAdapter adapter;
    RecentTourAdapter recentAdapter;
    DestinationAdapter destinationAdapter;


    MapView mapView;
    GoogleMap googleMap;
    FusedLocationProviderClient fusedLocationClient;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        popRecyclerView = view.findViewById(R.id.rvPopularTours);
        recentTourRecyclerView = view.findViewById(R.id.rvFeaturedTours);

        popRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        popList = new ArrayList<>();
        adapter = new UserTourPackageAdapter(view.getContext(),popList,this);
        popRecyclerView.setAdapter(adapter);

        recentTourRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recentList = new ArrayList<>();
        recentAdapter = new RecentTourAdapter(view.getContext(),recentList,this);
        recentTourRecyclerView.setAdapter(recentAdapter);


        loadMyTour();
        loadPopularTour();
    }

    private void loadPopularTour() {
        Call<List<TourPackageResponse>> call = RetrofitClient.getApiService().getTourPackage();
        call.enqueue(new Callback<List<TourPackageResponse>>() {
            @Override
            public void onResponse(Call<List<TourPackageResponse>> call, Response<List<TourPackageResponse>> response) {
                if (response.isSuccessful() && response != null){
                    int count = 0;
                    popList.clear();
                    List<TourPackageResponse> list = response.body();

                    for (TourPackageResponse tourPackageResponse:list){
                        if (list.size() < 5){
                            popList.addAll(list);
                            adapter.notifyDataSetChanged();
                            break;
                        } else if (count < 5){
                            popList.add(tourPackageResponse);
                            adapter.notifyDataSetChanged();
                            count++;
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

    private void loadMyTour() {
        Call<List<BookingResponse>> call = RetrofitClient.getApiService().getMyTour();
        call.enqueue(new Callback<List<BookingResponse>>() {
            @Override
            public void onResponse(Call<List<BookingResponse>> call, Response<List<BookingResponse>> response) {
                if (response.isSuccessful() && response != null){

                    SessionManager sessionManager =  new SessionManager(requireContext());
                    recentList.clear();

                    for (BookingResponse bookingResponse : response.body()){

                        if (bookingResponse.getCustomerEmail().equals(sessionManager.getKeyCustEmail())){


                            Call<TourPackageResponse> responseCall = RetrofitClient.getApiService().getTourPackageById(bookingResponse.getPackageId());
                            responseCall.enqueue(new Callback<TourPackageResponse>() {
                                @Override
                                public void onResponse(Call<TourPackageResponse> call, Response<TourPackageResponse> response) {
                                    if (response.isSuccessful() && response != null){
                                        recentList.add(response.body());
                                        recentAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onFailure(Call<TourPackageResponse> call, Throwable t) {
                                    Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<BookingResponse>> call, Throwable t) {
                Toast.makeText(requireContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onHomeTourClicked(TourPackageResponse response) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_home_tour_package);

// Initialize views
        ImageView closeBtn = dialog.findViewById(R.id.memberDialogCloseBtn);
        ImageView dialogImage = dialog.findViewById(R.id.memberDialogImage);
        TextView title = dialog.findViewById(R.id.memberDialogTitle);
        TextView duration = dialog.findViewById(R.id.memberDialogDuration);
        TextView rating = dialog.findViewById(R.id.memberDialogRating);
        TextView price = dialog.findViewById(R.id.memberDialogPrice);
        TextView status = dialog.findViewById(R.id.memberDialogStatus);
        TextView days = dialog.findViewById(R.id.memberDialogDays);
        TextView nights = dialog.findViewById(R.id.memberDialogNights);
        TextView remainingSeats = dialog.findViewById(R.id.memberDialogRemainingSeats);
        TextView description = dialog.findViewById(R.id.memberDialogDescription);


        Glide.with(requireContext())
                .load(response.getPackThumbnail())
                .placeholder(R.drawable.ic_default_image)
                .error(R.drawable.ic_default_image)
                .into(dialogImage);

        title.setText(response.getPackName());
        duration.setText(String.valueOf(response.getPackDurationDay()));
        rating.setText(String.valueOf(response.getPackRating()));
        price.setText(String.valueOf(response.getPackPrice()));
        status.setText(response.getPackStatus());
        days.setText(String.valueOf(response.getPackDurationDay()));
        nights.setText(String.valueOf(response.getPackDurationNight()));
        remainingSeats.setText(String.valueOf(response.getPackMaxCapacity()));
        description.setText(response.getPackDescription());


        RecyclerView recyclerView = dialog.findViewById(R.id.memberDialogRecyclerView);
        destinationList = new ArrayList<>();
        destinationList.clear();
        destinationList.addAll(response.getDestinations());
        destinationAdapter = new DestinationAdapter(requireContext(),destinationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(destinationAdapter);


        dialog.show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }


        closeBtn.setOnClickListener(v -> dialog.dismiss());
    }

    @Override
    public void onRecentHomeItemClicked(TourPackageResponse response) {
        Dialog dialog = new Dialog(requireContext());

        dialog.setContentView(R.layout.dialog_tour_event);

        mapView = dialog.findViewById(R.id.customerMapView);
        ImageView cancelBtn = dialog.findViewById(R.id.homeDialogMapCancelBtn);


        RecyclerView eventRecyclerView = dialog.findViewById(R.id.eventRecyclerView);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        ArrayList<PackageDestinationResponse> destinationList = new ArrayList<>();
        for (PackageDestinationResponse data: response.getDestinations()){
            LocalDate date = LocalDate.parse(data.getVisitDate());
            if (!date.isBefore(LocalDate.now())){
                destinationList.add(data);
            }
        }
        EventListAdapter eventListAdapter = new EventListAdapter(requireContext(),destinationList,this);
        eventRecyclerView.setAdapter(eventListAdapter);




        dialog.show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
        }


        mapView = dialog.findViewById(R.id.customerMapView);

        mapView.onCreate(null);
        mapView.onResume();

        mapView.getMapAsync(map -> {
            googleMap = map;
        });



        cancelBtn.setOnClickListener(v->{
            dialog.dismiss();
        });
    }


    /*
    @Override
    public void onMapEventClicked(PackageDestinationResponse response) {

        if (googleMap == null) {
            Toast.makeText(requireContext(), "Map is not ready", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(requireContext(), "Location permission required", Toast.LENGTH_SHORT).show();
            return;
        }

        double destinationLat = Double.parseDouble(response.getLatitude());

        double destinationLng = Double.parseDouble(response.getLongitude());

        LatLng destination = new LatLng(destinationLat, destinationLng);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {

                    if (location == null) {
                        Toast.makeText(requireContext(), "Unable to get current location", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double userLat = location.getLatitude();
                    double userLng = location.getLongitude();

                    LatLng userLocation = new LatLng(userLat, userLng);
                    googleMap.clear();
                    googleMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));

                    googleMap.addMarker(new MarkerOptions().position(destination).title(response.getName()));

                    // Draw road route
                    getRoute(userLat, userLng, destinationLat, destinationLng);
                });

    }
    navigation code for mobile
     */


// navigation code for emulator
@Override
public void onMapEventClicked(PackageDestinationResponse response) {

    SessionManager sessionManager = new SessionManager(requireContext());

    if (googleMap == null) {
        Toast.makeText(requireContext(),
                "Map is not ready",
                Toast.LENGTH_SHORT).show();
        return;
    }

    double destinationLat = Double.parseDouble(response.getLatitude());
    double destinationLng = Double.parseDouble(response.getLongitude());

    // Emulator starting location
    double userLat = 18.5204;
    double userLng = 73.8567;

    LatLng userLocation = new LatLng(userLat, userLng);
    LatLng destination = new LatLng(destinationLat, destinationLng);

    googleMap.clear();

    String userImageUrl = sessionManager.getKeyCustImage();

    Glide.with(requireContext())
            .asBitmap()
            .load(userImageUrl)
            .into(new CustomTarget<Bitmap>() {

                @Override
                public void onResourceReady(
                        @NonNull Bitmap bitmap,
                        Transition<? super Bitmap> transition) {

                    Bitmap userMarker = createMarkerBitmap(
                            bitmap,
                            Color.rgb(50, 180, 90)
                    );

                    googleMap.addMarker(
                            new MarkerOptions()
                                    .position(userLocation)
                                    .title("Your Location")
                                    .icon(BitmapDescriptorFactory.fromBitmap(userMarker))
                    );
                }

                @Override
                public void onLoadCleared(Drawable placeholder) {
                }
            });

    Log.d("IMAGE_TEST", "Destination image = " + response.getImage());

    Glide.with(requireContext())
            .asBitmap()
            .load(response.getImage())
            .placeholder(R.drawable.ic_default_image)
            .error(R.drawable.ic_default_image)
            .into(new CustomTarget<Bitmap>() {

                @Override
                public void onResourceReady(
                        @NonNull Bitmap bitmap,
                        Transition<? super Bitmap> transition) {

                    Bitmap destinationMarker = createMarkerBitmap(bitmap, Color.RED);

                    googleMap.addMarker(
                            new MarkerOptions()
                                    .position(destination)
                                    .title(response.getName())
                                    .icon(BitmapDescriptorFactory.fromBitmap(destinationMarker))
                                    .anchor(0.5f, 1f)
                    );
                }

                @Override
                public void onLoadCleared(Drawable placeholder) {
                }
            });

    getRoute(userLat, userLng, destinationLat, destinationLng);
}

    private void getRoute(double startLat, double startLng,
                          double endLat, double endLng) {

        String origin = startLat + "," + startLng;
        String destination = endLat + "," + endLng;
        String apiKey = "AIzaSyDc-B2qS8MdTNBM1Hl1ACGz2jDJshBAOnU";

        Call<DirectionsResponse> call = DirectionsRetrofitClient
                .getApiService()
                .getDirections(origin, destination, "driving", apiKey);

        call.enqueue(new Callback<DirectionsResponse>() {

            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {

                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().getRoutes() != null
                        && !response.body().getRoutes().isEmpty()) {

                    DirectionsResponse.Route route = response.body().getRoutes().get(0);
                    String encodedPoints = route.getOverview_polyline().getPoints();
                    List<LatLng> routePoints = decodePolyline(encodedPoints);

                    googleMap.addPolyline(new PolylineOptions().addAll(routePoints).width(10).color(Color.BLUE).geodesic(true));
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();

                    for (LatLng point : routePoints) {
                        builder.include(point);
                    }

                    builder.include(new LatLng(startLat, startLng));
                    builder.include(new LatLng(endLat, endLng));
                    LatLngBounds bounds = builder.build();
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100)
                    );

                } else {
                    Toast.makeText(requireContext(), "Route not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Route error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private List<LatLng> decodePolyline(String encoded) {

        List<LatLng> poly = new ArrayList<>();

        int index = 0;
        int lat = 0;
        int lng = 0;

        while (index < encoded.length()) {

            int b;
            int shift = 0;
            int result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dLat = ((result & 1) != 0)
                    ? ~(result >> 1)
                    : (result >> 1);

            lat += dLat;

            shift = 0;
            result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dLng = ((result & 1) != 0)
                    ? ~(result >> 1)
                    : (result >> 1);

            lng += dLng;

            LatLng point = new LatLng(
                    lat / 1E5,
                    lng / 1E5
            );

            poly.add(point);
        }

        return poly;
    }


    @Override
    public void onResume() {
        super.onResume();

        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        if (mapView != null) {
            mapView.onPause();
        }

        super.onPause();
    }

    @Override
    public void onDestroyView() {
        if (mapView != null) {
            mapView.onDestroy();
            mapView = null;
        }

        super.onDestroyView();
    }


    private Bitmap createMarkerBitmap(Bitmap image, int borderColor) {

        int size = 120;
        Bitmap output = Bitmap.createBitmap(size, size + 40, Bitmap.Config.ARGB_8888);
        android.graphics.Canvas canvas = new android.graphics.Canvas(output);
        android.graphics.Paint paint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);

        paint.setColor(borderColor);
        paint.setStrokeWidth(8);

        canvas.drawLine(size / 2f, 80, size / 2f, size + 20, paint);

        float centerX = size / 2f;
        float centerY = 55;
        float radius = 48;

        paint.setColor(borderColor);
        canvas.drawCircle(centerX, centerY, radius + 5, paint);


        android.graphics.Path path = new android.graphics.Path();
        path.addCircle(centerX, centerY, radius, android.graphics.Path.Direction.CW);
        canvas.save();
        canvas.clipPath(path);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(image, size, size, true);
        canvas.drawBitmap(scaledBitmap, 0, 5, paint);
        canvas.restore();
        return output;
    }

}