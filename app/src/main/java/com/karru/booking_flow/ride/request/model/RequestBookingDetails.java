package com.karru.booking_flow.ride.request.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * ?<h1>RequestBookingDetails</h1>
 * used to hold the requesting details model
 * @author 3Embed
 * @since on 25-04-2018.
 */
public class RequestBookingDetails implements Serializable
{
    /*"bookingId":"31524654509",
"vehicleTypeName":"Car",
"pickupAddress":"Creative Villa Apartment, 44 RBI Colony, Vishveshvaraiah Nagar, Ganga Nagar, Bengaluru, Karnataka 560024, India",
"pickupLatitude":"13.028616993868635",
"pickupLongitude":"77.58940882980824",
"dropAddress":"",
"dropLatitude":"0",
"dropLongitude":"0",
"totalTime":"120",
"elapsedTime":4*/
    @SerializedName("bookingId")
    @Expose
    private String bookingId;
    @SerializedName("vehicleTypeName")
    @Expose
    private String vehicleTypeName;
    @SerializedName("pickupAddress")
    @Expose
    private String pickupAddress;
    @SerializedName("dropAddress")
    @Expose
    private String dropAddress;
    @SerializedName("totalTime")
    @Expose
    private int totalTime;
    @SerializedName("elapsedTime")
    @Expose
    private int elapsedTime;

    public String getBookingId() {
        return bookingId;
    }
    public String getVehicleTypeName() {
        return vehicleTypeName;
    }
    public String getPickupAddress() {
        return pickupAddress;
    }
    public String getDropAddress() {
        return dropAddress;
    }
    public int getTotalTime() {
        return totalTime;
    }
    public int getElapsedTime() {
        return elapsedTime;
    }
}
