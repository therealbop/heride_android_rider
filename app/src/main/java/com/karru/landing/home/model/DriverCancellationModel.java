package com.karru.landing.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * <h1>DriverCancellationModel</h1>
 * used to hold the cancellation of driver
 * @author 3Embed
 * @since on 2/13/2018.
 */
public class DriverCancellationModel implements Serializable
{
    /*"bookingId":141518514364,
"statusText":"Raghavendra has cancelled the trip with the reason My reason is not listed ",
"status":5,
"driverName":"Raghavendra V",
"phone":"8123666322"*/
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("bookingId")
    @Expose
    private String bookingId;
    @SerializedName("statusText")
    @Expose
    private String statusText;
    @SerializedName("amount")
    @Expose
    private String amount;

    public String getAmount() { return amount; }
    public String getBookingId() { return bookingId; }
    public String getStatusText() { return statusText; }
    public int getStatus() { return status; }
}
