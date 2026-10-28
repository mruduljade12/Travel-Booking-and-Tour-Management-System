package com.mrudul.tourandtravel.driver.driver_models;

/**
 * Model for a scheduled driver trip shown in the Schedule screen.
 *
 * STATUS_ONGOING   — trip is currently in progress
 * STATUS_COMPLETED — trip has been completed
 */
public class DriverTripModel {

    public static final int STATUS_ONGOING   = 0;
    public static final int STATUS_COMPLETED = 1;

    private String tripId;
    private String time;
    private String pickupLocation;
    private String destination;
    private int    seats;
    private String fare;       // e.g. "₹ 650"
    private String distance;   // e.g. "45 km"
    private int    status;

    public DriverTripModel(String tripId, String time, String pickupLocation,
                           String destination, int seats,
                           String fare, String distance, int status) {
        this.tripId         = tripId;
        this.time           = time;
        this.pickupLocation = pickupLocation;
        this.destination    = destination;
        this.seats          = seats;
        this.fare           = fare;
        this.distance       = distance;
        this.status         = status;
    }

    public String getTripId()         { return tripId; }
    public String getTime()           { return time; }
    public String getPickupLocation() { return pickupLocation; }
    public String getDestination()    { return destination; }
    public int    getSeats()          { return seats; }
    public String getFare()           { return fare; }
    public String getDistance()       { return distance; }
    public int    getStatus()         { return status; }
}