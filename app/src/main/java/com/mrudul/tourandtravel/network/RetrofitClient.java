package com.mrudul.tourandtravel.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // url for mobile == http://10.231.5.200:8080/
    // url for emulator == http://10.0.2.2:8080/
    // url of railway server == "https://tour-app-server-production.up.railway.app/"

    private static final String BASE_URL =
            "https://tour-app-server-production.up.railway.app/";

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {

        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

    public static ApiService getApiService() {

        return getRetrofitInstance()
                .create(ApiService.class);
    }

}
