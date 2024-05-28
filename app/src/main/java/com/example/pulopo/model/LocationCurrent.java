package com.example.pulopo.model;

public class LocationCurrent {
    public static double Lat;
    public static double Long;

    public LocationCurrent(double lat, double aLong) {
        Lat = lat;
        Long = aLong;
    }

    public static double getLat() {
        return Lat;
    }

    public static void setLat(double lat) {
        Lat = lat;
    }

    public static double getLong() {
        return Long;
    }

    public static void setLong(double aLong) {
        Long = aLong;
    }
}
