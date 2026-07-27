package com.mrudul.tourandtravel.user_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mrudul.tourandtravel.R;
import com.mrudul.tourandtravel.user_adapters.EventListAdapter;
import com.mrudul.tourandtravel.user_models.EventModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    ArrayList<EventModel> eventList;
    EventListAdapter eventAdapter;
    RecyclerView eventRecyclerView;

    MapView mapView;
    GoogleMap googleMap;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
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
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        eventRecyclerView = view.findViewById(R.id.eventRecyclerView);

        eventRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        eventList = new ArrayList<>();

        eventList.add(new EventModel("go to temple","03-jan-2026","03:45 pm"));
        eventList.add(new EventModel("go to mall","03-jan-2026","07:45 pm"));
        eventList.add(new EventModel("go to river basin","03-jan-2026","08:45 pm"));


        eventAdapter = new EventListAdapter(view.getContext(),eventList);
        eventRecyclerView.setAdapter(eventAdapter);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {

        googleMap = map;

        LatLng mahabaleshwar =
                new LatLng(17.9237,73.6586);

        googleMap.addMarker(new MarkerOptions().position(mahabaleshwar).title("mahabaleshwar"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mahabaleshwar,12));
    }
}