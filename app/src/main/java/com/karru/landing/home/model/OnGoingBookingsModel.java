package com.karru.landing.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * <h1>OnGoingBookingsModel</h1>
 * This class is used to hold the on going bookings data
 * @author 3Embed
 * @since on 29-01-2018.
 */
public class OnGoingBookingsModel implements Serializable
{
    /*"bookingId":451516859017,
"bookingStatus":9,
"bookingStatusText":"Journey Started",
"pickupLocation":{},
"dropLocation":{},
"driverLocation":"13.02865123748779,77.58949279785156"*/
    @SerializedName("bookingId")
    @Expose
    private String bookingId ;
    @SerializedName("bookingStatusText")
    @Expose
    private String bookingStatusText;
    @SerializedName("driverName")
    @Expose
    private String driverName;
    @SerializedName("bookingStatus")
    @Expose
    private int bookingStatus;

    public String getDriverName() {
        return driverName;
    }
    public String getBookingId() {
        return bookingId;
    }
    public int getBookingStatus() {
        return bookingStatus;
    }
    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }
    public void setBookingStatusText(String bookingStatusText) { this.bookingStatusText = bookingStatusText; }
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
    public void setBookingStatus(int bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}
