package com.example.pulopo.model;

public class Users {
    String UID;
    String userName;
    double latLocation;
    double longLocation;

    public Users(String UID, String userName, double latLocation, double longLocation) {
        this.UID = UID;
        this.userName = userName;
        this.latLocation = latLocation;
        this.longLocation = longLocation;
    }


    public Users() {
    }



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public double getLatLocation() {
        return latLocation;
    }

    public void setLatLocation(double latLocation) {
        this.latLocation = latLocation;
    }

    public double getLongLocation() {
        return longLocation;
    }

    public void setLongLocation(double longLocation) {
        this.longLocation = longLocation;
    }
}
