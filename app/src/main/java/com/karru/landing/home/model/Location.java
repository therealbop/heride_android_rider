package com.karru.landing.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>Location</h1>
 * This class is used to hold the location object
 * @author embed
 * @since  on 14/9/16.
 */
public class Location implements Serializable
{
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lng")
    @Expose
    private String lng;
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("address")
    @Expose
    private String address;

    public double getLatitude() { return latitude; }
    public String getAddress() {
        return address;
    }
    public double getLongitude() {
        return longitude;
    }
    public String getLat ()
    {
        return lat;
    }
    public void setLat (String lat)
    {
        this.lat = lat;
    }
    public String getLng() {
        return lng;
    }
    public void setLng(String lng) {
        this.lng = lng;
    }
}