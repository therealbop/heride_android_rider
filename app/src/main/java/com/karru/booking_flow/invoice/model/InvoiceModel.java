package com.karru.booking_flow.invoice.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.karru.landing.home.model.InvoiceDetailsModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <h1>InvoiceModel</h1>
 * This class is used to store the invoice models
 * @author 3Embed
 * @since on 06-02-2018.
 */
public class InvoiceModel implements Serializable
{
    @SerializedName("ratingPoints")
    @Expose
    private int ratingPoints= 5;
    @SerializedName("reasonsList")
    @Expose
    private ArrayList<String> reasonsList = new ArrayList<>();
    @SerializedName("driverId")
    @Expose
    private String driverId;
    @SerializedName("isDriverFavorite")
    @Expose
    private boolean isDriverFavorite;
    @SerializedName("tipAdded")
    @Expose
    private double tipAdded = 0;
    @SerializedName("driverFeedbackData")
    @Expose
    private String driverFeedbackData;
    @SerializedName("userSelectedReasons")
    @Expose
    private StringBuilder userSelectedReasons;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("currencyAbbreviation")
    @Expose
    private int currencyAbbreviation;
    @SerializedName("invoiceDetailsModel")
    @Expose
    private InvoiceDetailsModel invoiceDetailsModel;

    public InvoiceDetailsModel getInvoiceDetailsModel() {
        return invoiceDetailsModel;
    }
    public void setInvoiceDetailsModel(InvoiceDetailsModel invoiceDetailsModel) { this.invoiceDetailsModel = invoiceDetailsModel; }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public int getCurrencyAbbreviation() {
        return currencyAbbreviation;
    }
    public void setCurrencyAbbreviation(int currencyAbbreviation) { this.currencyAbbreviation = currencyAbbreviation; }
    public StringBuilder getUserSelectedReasons() {
        return userSelectedReasons;
    }
    public void setUserSelectedReasons(StringBuilder userSelectedReasons) { this.userSelectedReasons = userSelectedReasons; }
    public String getDriverFeedbackData() {
        return driverFeedbackData;
    }
    public void setDriverFeedbackData(String driverFeedbackData) { this.driverFeedbackData = driverFeedbackData; }
    public double getTipAdded() {
        return tipAdded;
    }
    public void setTipAdded(double tipAdded) {
        this.tipAdded = tipAdded;
    }
    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }
    public void setDriverFavorite(boolean driverFavorite) {
        isDriverFavorite = driverFavorite;
    }
    public String getDriverId() {
        return driverId;
    }
    public boolean isDriverFavorite() {
        return isDriverFavorite;
    }
    public int getRatingPoints() {
        return ratingPoints;
    }
    public void setRatingPoints(int ratingPoints) {
        this.ratingPoints = ratingPoints;
    }
    public ArrayList<String> getReasonsList() {
        return reasonsList;
    }
    public void setReasonsList(ArrayList<String> reasonsList) {
        this.reasonsList = reasonsList;
    }
}
