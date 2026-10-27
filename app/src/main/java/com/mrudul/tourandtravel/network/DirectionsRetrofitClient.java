package com.mrudul.tourandtravel.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DirectionsRetrofitClient {

    private static DirectionsApi directionsApi;

    public static DirectionsApi getApiService() {

        if (directionsApi == null) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            directionsApi =
                    retrofit.create(DirectionsApi.class);
        }

        return directionsApi;
    }
}
