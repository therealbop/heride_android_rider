package com.karru.landing.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1></h1>
 * This class is used to hold the booking details
 * @author 3Embed
 * @since on 30-01-2018.
 */
public class BookingDetailsDataModel implements Serializable
{
    /*"message":"Got The Details.",
"data":{
"bookingId":401517378291,
"status":6,
"statusText":"Driver is on the way.",
"serviceType":2,
"receivers":{},
"bookingDate":"2018-01-31 05:58:16",
"trackingUrl":"",
"driver":{},
"vehicle":{},
"pickup":{},
"drop":{}
}*/
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("statusText")
    @Expose
    private String statusText;
    @SerializedName("bookingId")
    @Expose
    private String bookingId;
    @SerializedName("trackingUrl")
    @Expose
    private String trackingUrl;
    @SerializedName("bid")
    @Expose
    private String bid;

    @SerializedName("isRental")
    @Expose
    private boolean isRental;

    @SerializedName("packageTitle")
    @Expose
    private String packageTitle;

    @SerializedName("fromID")
    @Expose
    private String fromID;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("driver")
    @Expose
    private DriverDetailsModel driver;
    @SerializedName("pickup")
    @Expose
    private Location pickup;
    @SerializedName("drop")
    @Expose
    private Location drop;
    @SerializedName("vehicle")
    @Expose
    private VehicleTypeDetailsModel vehicle;

    public String getName() {
        return name;
    }
    public String getBid() {
        return bid;
    }
    public String getFromID() {
        return fromID;
    }
    public String getTrackingUrl() {
        return trackingUrl;
    }
    public DriverDetailsModel getDriver() {
        return driver;
    }
    public String getBookingId() {
        return bookingId;
    }
    public int getStatus() {
        return status;
    }
    public String getStatusText() {
        return statusText;
    }
    public Location getPickup() {
        return pickup;
    }
    public Location getDrop() {
        return drop;
    }
    public VehicleTypeDetailsModel getVehicle() {
        return vehicle;
    }

    public boolean isRental() {
        return isRental;
    }

    public void setRental(boolean rental) {
        isRental = rental;
    }

    public String getPackageTitle() {
        return packageTitle;
    }

    public void setPackageTitle(String packageTitle) {
        this.packageTitle = packageTitle;
    }
}
