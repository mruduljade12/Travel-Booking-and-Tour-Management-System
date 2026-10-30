package com.mrudul.tourandtravel.network;

import com.mrudul.tourandtravel.app_common_activities.LeaveRequest;
import com.mrudul.tourandtravel.app_common_activities.LeaveResponse;
import com.mrudul.tourandtravel.app_common_activities.LeaveStatusRequest;
import com.mrudul.tourandtravel.driver.driver_models.DriverLoginRequest;
import com.mrudul.tourandtravel.driver.driver_models.DriverLoginResponse;
import com.mrudul.tourandtravel.tour_guide.tour_guide_models.TourGuideLoginRequest;
import com.mrudul.tourandtravel.tour_guide.tour_guide_models.TourGuideLoginResponse;
import com.mrudul.tourandtravel.user.user_models.BookingRequest;
import com.mrudul.tourandtravel.user.user_models.BookingResponse;
import com.mrudul.tourandtravel.user.user_models.CustomerLoginRequest;
import com.mrudul.tourandtravel.user.user_models.CustomerRegisterRequest;
import com.mrudul.tourandtravel.user.user_models.CustomerResponse;
import com.mrudul.tourandtravel.user.user_models.CustomerUpdateRequest;
import com.mrudul.tourandtravel.user.user_models.TourPackageResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    // customer api
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


    // driver api
    @POST("api/drivers/login")
    Call<DriverLoginResponse> loginDriver(
            @Body DriverLoginRequest request
    );

    @GET("api/drivers/{driverId}")
    Call<DriverLoginResponse> getDriver(
            @Path("driverId") int driverId
    );


    // tour guide api
    @POST("api/tour_guides/login")
    Call<TourGuideLoginResponse> loginTourGuide(
            @Body TourGuideLoginRequest request
            );

    @GET("api/tour_guides/{guideId}")
    Call<TourGuideLoginResponse> getTourGuide(
            @Path("guideId") int guideId
    );


    // Tour package APIs

    @GET("api/packages")
    Call<List<TourPackageResponse>> getTourPackage();

    @GET("api/packages/{packageId}")
    Call<TourPackageResponse> getTourPackageById(
            @Path("packageId") int packageId
    );


    // booking api
    @POST("api/bookings")
    Call<BookingResponse> bookTour(
            @Body BookingRequest request
    );

    @GET("api/bookings")
    Call<List<BookingResponse>> getMyTour();



    // leave api
    @POST("api/leaves")
    Call<LeaveResponse> applyLeave(@Body LeaveRequest request);

    @GET("api/leaves/driver/{driverId}")
    Call<List<LeaveResponse>> getDriverLeaves(
            @Path("driverId") int driverId
    );

    @GET("api/leaves/guide/{guideId}")
    Call<List<LeaveResponse>> getGuideLeaves(
            @Path("guideId") int guideId
    );

    @GET("api/leaves/pending")
    Call<List<LeaveResponse>> getPendingLeaves();

    @PUT("api/leaves/{leaveId}/status")
    Call<LeaveResponse> updateLeaveStatus(
            @Path("leaveId") int leaveId,
            @Body LeaveStatusRequest request
    );
}
