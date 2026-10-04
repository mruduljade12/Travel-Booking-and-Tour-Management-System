package com.mrudul.tourandtravel.network;

import com.mrudul.tourandtravel.driver.driver_models.DriverLoginRequest;
import com.mrudul.tourandtravel.driver.driver_models.DriverLoginResponse;
import com.mrudul.tourandtravel.user.user_models.CustomerLoginRequest;
import com.mrudul.tourandtravel.user.user_models.CustomerRegisterRequest;
import com.mrudul.tourandtravel.user.user_models.CustomerResponse;
import com.mrudul.tourandtravel.user.user_models.CustomerUpdateRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @POST("api/customers/register")
    Call<CustomerResponse> registerCustomer(
            @Body CustomerRegisterRequest request
    );

    @POST("api/customers/login")
    Call<CustomerResponse> loginCustomer(
            @Body CustomerLoginRequest request
    );

    @GET("api/customers/{custId}")
    Call<CustomerResponse> getCustomer(
            @Path("custId") int custId
    );

    @PUT("api/customers/{custId}")
    Call<CustomerResponse> updateCustomer(
            @Path("custId") int custId,
            @Body CustomerUpdateRequest request
    );


    @POST("api/drivers/login")
    Call<DriverLoginResponse> loginDriver(
            @Body DriverLoginRequest request
    );

    @GET("api/drivers/{driverId}")
    Call<DriverLoginResponse> getDriver(
            @Path("driverId") int driverId
    );
}
