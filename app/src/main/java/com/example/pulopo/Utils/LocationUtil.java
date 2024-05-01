package com.example.pulopo.Utils;

public class LocationUtil {
    public  static int senderId;
    public  static  int reciverId;
    public  static  double latLocation;
    public  static  double longLocation;

    public LocationUtil() {
    }

    public static int getSenderId() {
        return senderId;
    }

    public static void setSenderId(int senderId) {
        LocationUtil.senderId = senderId;
    }

    public static int getReciverId() {
        return reciverId;
    }

    public static void setReciverId(int reciverId) {
        LocationUtil.reciverId = reciverId;
    }

    public static double getLatLocation() {
        return latLocation;
    }

    public static void setLatLocation(double latLocation) {
        LocationUtil.latLocation = latLocation;
    }

    public static double getLongLocation() {
        return longLocation;
    }

    public static void setLongLocation(double longLocation) {
        LocationUtil.longLocation = longLocation;
    }
}