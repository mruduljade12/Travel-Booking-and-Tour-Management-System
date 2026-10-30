package com.mrudul.tourandtravel.network;

import java.util.List;

public class DirectionsResponse {

    private String status;
    private List<Route> routes;

    public String getStatus() {
        return status;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public static class Route {

        private OverviewPolyline overview_polyline;

        public OverviewPolyline getOverview_polyline() {
            return overview_polyline;
        }
    }

    public static class OverviewPolyline {

        private String points;

        public String getPoints() {
            return points;
        }
    }
}
