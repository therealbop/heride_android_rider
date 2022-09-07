package com.karru.landing.home.model.fare_estimate_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.karru.landing.home.model.PromoCodeModel;
import java.io.Serializable;
import java.util.ArrayList;
import javax.inject.Singleton;

/**
 * <h1>FareEstimateModel</h1>
 * used to hold the fare estimation data
 * @author 3Embed
 * @since on 23-03-2018.
 */
@Singleton
public class FareEstimateModel implements Serializable
{
    /*"surgeApplied":false,
"surgePriceText":"",
"laterBookingAdvanceFee":"₹ 20",
"currencyAbbr":"1",
"currencySymbol":"₹",
"duration":"19.05",
"timeFee":"19.05",
"distance":"4.98",
"distanceFee":"49.76",
"finalAmount":"158.81",
"estimateId":"227",
"distanceMetrics":"KM",
"timeMetrics":"Mins",
"extraFees":[]
"towTruckServices":[
],
"towTruckBookingService":2,
"towTruckBooking":true*/

    private static FareEstimateModel instance;
    public FareEstimateModel()
    {
        instance = this ;
    }
    public static FareEstimateModel getInstance()
    {
        return instance;
    }

    @SerializedName("surgeApplied")
    @Expose
    private boolean surgeApplied;
    @SerializedName("isPromoCodeApplied")
    @Expose
    private boolean isPromoCodeApplied;
    @SerializedName("surgePriceText")
    @Expose
    private String surgePriceText;
    @SerializedName("currencySymbol")
    @Expose
    private String currencySymbol;
    @SerializedName("baseFare")
    @Expose
    private String baseFare;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("timeFee")
    @Expose
    private String timeFee;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("distanceFee")
    @Expose
    private String distanceFee;
    @SerializedName("finalAmount")
    @Expose
    private String finalAmount;
    @SerializedName("estimateId")
    @Expose
    private String estimateId;
    @SerializedName("distanceMetrics")
    @Expose
    private String distanceMetrics;
    @SerializedName("timeMetrics")
    @Expose
    private String timeMetrics;
    @SerializedName("minFee")
    @Expose
    private String minFee;
    @SerializedName("extraFees")
    @Expose
    private ArrayList<ExtraFeesModel> extraFees;
    @SerializedName("towTruckServices")
    @Expose
    private ArrayList<ExtraFeesModel> towTruckServices;
    @SerializedName("towTruckBookingService")
    @Expose
    private int towTruckBookingService;
    @SerializedName("towTruckBooking")
    @Expose
    private boolean towTruckBooking;
    @SerializedName("currencyAbbr")
    @Expose
    private int currencyAbbr;
    @SerializedName("isMinFeeApplied")
    @Expose
    private int isMinFeeApplied;
    @SerializedName("freeTime")
    @Expose
    private int freeTime;
    @SerializedName("laterBookingAdvanceFee")
    @Expose
    private double laterBookingAdvanceFee;
    @SerializedName("subTotal")
    @Expose
    private double subTotal;
    @SerializedName("freeDistance")
    @Expose
    private double freeDistance;
    @SerializedName("promoCodeData")
    @Expose
    private PromoCodeModel promoCodeData;

    public ArrayList<ExtraFeesModel> getTowTruckServices() {
        return towTruckServices;
    }

    public int getTowTruckBookingService() {
        return towTruckBookingService;
    }

    public boolean isTowTruckBooking() {
        return towTruckBooking;
    }

    public int getFreeTime() {
        return freeTime;
    }
    public double getFreeDistance() {
        return freeDistance;
    }
    public void setPromoCodeApplied(boolean promoCodeApplied) { isPromoCodeApplied = promoCodeApplied; }
    public void setPromoCodeData(PromoCodeModel promoCodeData) { this.promoCodeData = promoCodeData; }
    public PromoCodeModel getPromoCodeData() {
        return promoCodeData;
    }
    public double getSubTotal() {
        return subTotal;
    }
    public String getMinFee() {
        return minFee;
    }
    public int getIsMinFeeApplied() {
        return isMinFeeApplied;
    }
    public boolean isPromoCodeApplied() {
        return isPromoCodeApplied;
    }
    public boolean isSurgeApplied() {
        return surgeApplied;
    }
    public String getDistanceMetrics() {
        return distanceMetrics;
    }
    public String getTimeMetrics() {
        return timeMetrics;
    }
    public String getSurgePriceText() {
        return surgePriceText;
    }
    public String getCurrencySymbol() {
        return currencySymbol;
    }
    public double getLaterBookingAdvanceFee() {
        return laterBookingAdvanceFee;
    }
    public String getBaseFare() {
        return baseFare;
    }
    public int getCurrencyAbbr() {
        return currencyAbbr;
    }
    public String getDuration() {
        return duration;
    }
    public String getTimeFee() {
        return timeFee;
    }
    public String getDistance() {
        return distance;
    }
    public String getDistanceFee() {
        return distanceFee;
    }
    public String getFinalAmount() {
        return finalAmount;
    }
    public String getEstimateId() {
        return estimateId;
    }
    public ArrayList<ExtraFeesModel> getExtraFees() {
        return extraFees;
    }
}
