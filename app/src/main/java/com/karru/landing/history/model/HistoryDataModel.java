package com.karru.landing.history.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>HistoryDataModel</h1>
 * used to hold the data of history row
 * @author  3Embed
 * @since on 2/22/2018.
 */
public class HistoryDataModel implements Serializable
{
    /*"bookingId":41519283049,
"status":12,
"businessName":"Karru Me",
"cancellationFee":35,
"statusText":"Completed",
"bookingDate":"2018-Feb-22 07:04 AM",
"pickupAddress":"Creative Villa Apartment, 44 RBI Colony, Vishveshvaraiah Nagar, Ganga Nagar, Bengaluru, Karnataka 560024, India",
"dropAddress":"Creative Villa Apartment, 44 RBI Colony, Vishveshvaraiah Nagar, Ganga Nagar, Bengaluru, Karnataka 560024, India",
"amount":"₹ 50.00"*/
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("bookingId")
    @Expose
    private String bookingId;
    @SerializedName("businessName")
    @Expose
    private String businessName;
    @SerializedName("statusText")
    @Expose
    private String statusText;
    @SerializedName("bookingDate")
    @Expose
    private String bookingDate;
    @SerializedName("pickupAddress")
    @Expose
    private String pickupAddress;
    @SerializedName("dropAddress")
    @Expose
    private String dropAddress;
    @SerializedName("bookingTime")
    @Expose
    private String bookingTime;
    @SerializedName("vehicleTypeName")
    @Expose
    private String vehicleTypeName;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("pickupLatitude")
    @Expose
    private double pickupLatitude;
    @SerializedName("pickupLongitude")
    @Expose
    private double pickupLongitude;
    @SerializedName("cancellationFee")
    @Expose
    private double cancellationFee;
    @SerializedName("currencySymbol")
    @Expose
    private String currencySymbol;
    @SerializedName("currencyAbbr")
    @Expose
    private int currencyAbbr;

    public String getCurrencySymbol() {
        return currencySymbol;
    }
    public int getCurrencyAbbr() {
        return currencyAbbr;
    }
    public double getPickupLatitude() {
        return pickupLatitude;
    }
    public double getPickupLongitude() {
        return pickupLongitude;
    }
    public String getVehicleTypeName() {
        return vehicleTypeName;
    }
    public String getBookingTime() {
        return bookingTime;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }
    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }
    public void setCurrencyAbbr(int currencyAbbr) {
        this.currencyAbbr = currencyAbbr;
    }
    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }
    public String getBookingId() {
        return bookingId;
    }
    public String getBusinessName() {
        return businessName;
    }
    public double getCancellationFee() {
        return cancellationFee;
    }
    public String getStatusText() {
        return statusText;
    }
    public String getBookingDate() {
        return bookingDate;
    }
    public String getPickupAddress() {
        return pickupAddress;
    }
    public String getDropAddress() {
        return dropAddress;
    }
    public void setDropAddress(String dropAddress) {
        this.dropAddress = dropAddress;
    }
    public String getAmount() {
        return amount;
    }
}
