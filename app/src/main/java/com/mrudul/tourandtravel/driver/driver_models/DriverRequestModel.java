package com.mrudul.tourandtravel.driver.driver_models;

/**
 * Model representing a single driver ride/tour request.
 *
 * Status values:
 *   STATUS_PENDING   — waiting for driver action
 *   STATUS_ACCEPTED  — driver accepted
 *   STATUS_REJECTED  — driver rejected
 *   STATUS_COMPLETED — trip completed
 */
public class DriverRequestModel {

    public static final int STATUS_PENDING   = 0;
    public static final int STATUS_ACCEPTED  = 1;
    public static final int STATUS_REJECTED  = 2;
    public static final int STATUS_COMPLETED = 3;

    private String requestId;
    private String time;
    private String pickupLocation;
    private String destination;
    private int    seats;
    private String amount;
    private String distance;
    private int    status;

    public DriverRequestModel(String requestId, String time, String pickupLocation,
                              String destination, int seats, String amount,
                              String distance, int status) {
        this.requestId       = requestId;
        this.time            = time;
        this.pickupLocation  = pickupLocation;
        this.destination     = destination;
        this.seats           = seats;
        this.amount          = amount;
        this.distance        = distance;
        this.status          = status;
    }

    public String getRequestId()      { return requestId; }
    public String getTime()           { return time; }
    public String getPickupLocation() { return pickupLocation; }
    public String getDestination()    { return destination; }
    public int    getSeats()          { return seats; }
    public String getAmount()         { return amount; }
    public String getDistance()       { return distance; }
    public int    getStatus()         { return status; }
    public void   setStatus(int s)    { this.status = s; }
}