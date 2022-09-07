package com.karru.landing.history.history_details.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.karru.landing.home.model.InvoiceDetailsModel;

import java.io.Serializable;

/**
 * <h1>HistoryModel</h1>
 * used to holed the data for history details
 * @author 3EMbed
 * @since on 2/23/2018.
 */
public class HistoryModel implements Serializable
{
    /*"bookingId":51519311248,
"status":12,
"statusText":"Completed",
"bookingDate":"2018-02-22 14:54:10",
"businessName":"Karru Me",
"amount":"₹ 300.00",
"driver":{},
"vehicle":{},
"address":{},
"distanceAndTime":{},
"payment":{},
"receipt":{}*/
    @SerializedName("bookingId")
    @Expose
    private String bookingId;
    @SerializedName("statusText")
    @Expose
    private String statusText;
    @SerializedName("bookingDate")
    @Expose
    private String bookingDate;
    @SerializedName("businessName")
    @Expose
    private String businessName;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("cancellationReason")
    @Expose
    private String cancellationReason;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("driver")
    @Expose
    private DriverDetails driver;
    @SerializedName("isCancelationFeesApplied")
    @Expose
    private boolean isCancelationFeesApplied;
    @SerializedName("receipt")
    @Expose
    private InvoiceDetailsModel receipt;

    public boolean isCancelationFeesApplied() {
        return isCancelationFeesApplied;
    }
    public String getCancellationReason() {
        return cancellationReason;
    }
    public String getBookingId() {
        return bookingId;
    }
    public String getStatusText() {
        return statusText;
    }
    public String getBookingDate() {
        return bookingDate;
    }
    public String getBusinessName() {
        return businessName;
    }
    public String getAmount() {
        return amount;
    }
    public int getStatus() {
        return status;
    }
    public DriverDetails getDriver() {
        return driver;
    }
    public InvoiceDetailsModel getReceipt() {
        return receipt;
    }
}


