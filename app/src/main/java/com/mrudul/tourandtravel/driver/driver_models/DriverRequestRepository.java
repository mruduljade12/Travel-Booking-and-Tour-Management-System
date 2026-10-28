package com.mrudul.tourandtravel.driver.driver_models;

import java.util.ArrayList;

/**
 * Singleton repository — single source of truth for all driver requests.
 * Both DriverScheduleFragment and DriverRequestsActivity use this instance.
 */
public class DriverRequestRepository {

    private static DriverRequestRepository instance;

    public static DriverRequestRepository getInstance() {
        if (instance == null) {
            instance = new DriverRequestRepository();
            instance.seed();
        }
        return instance;
    }

    private DriverRequestRepository() {}

    private final ArrayList<DriverRequestModel> requests = new ArrayList<>();

    private void seed() {
        requests.clear();
        // Pending
        requests.add(new DriverRequestModel("R1", "10:30 AM",
                "Pune Airport",        "Lonavala, Maharashtra",
                2, "\u20b9 650",  "45 km",  DriverRequestModel.STATUS_PENDING));
        requests.add(new DriverRequestModel("R4", "09:00 AM",
                "Hinjewadi, Pune",     "Mumbai Airport",
                2, "\u20b9 1,800","150 km", DriverRequestModel.STATUS_PENDING));
        requests.add(new DriverRequestModel("R6", "02:00 PM",
                "Kothrud, Pune",       "Alibag",
                1, "\u20b9 950",  "80 km",  DriverRequestModel.STATUS_PENDING));
        // Accepted
        requests.add(new DriverRequestModel("R2", "11:15 AM",
                "Shivaji Nagar, Pune", "Khandala, Lonavala",
                4, "\u20b9 850",  "60 km",  DriverRequestModel.STATUS_ACCEPTED));
        requests.add(new DriverRequestModel("R5", "08:30 AM",
                "Pune Station",        "Nashik",
                3, "\u20b9 1,100","100 km", DriverRequestModel.STATUS_ACCEPTED));
        // Completed
        requests.add(new DriverRequestModel("R3", "12:45 PM",
                "Pune",                "Mahabaleshwar",
                3, "\u20b9 1,200","120 km", DriverRequestModel.STATUS_COMPLETED));
    }

    public ArrayList<DriverRequestModel> getAll() {
        return requests;
    }

    public ArrayList<DriverRequestModel> getByStatus(int status) {
        ArrayList<DriverRequestModel> result = new ArrayList<>();
        for (DriverRequestModel r : requests) {
            if (r.getStatus() == status) result.add(r);
        }
        return result;
    }

    public int countByStatus(int status) {
        int count = 0;
        for (DriverRequestModel r : requests) {
            if (r.getStatus() == status) count++;
        }
        return count;
    }
}