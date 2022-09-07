package com.karru.landing.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>VehicleTypeDetailsModel</h1>
 * This class is used to hold the vehicle type details
 * @author  3Embed
 * @since  on 02-01-2018.
 */
public class VehicleTypeDetailsModel implements Serializable
{
    @SerializedName("minimumFare")
    @Expose
    private String minimumFare;
    @SerializedName("baseFare")
    @Expose
    private String baseFare;
    @SerializedName("mileagePrice")
    @Expose
    private String mileagePrice;
    @SerializedName("timeFare")
    @Expose
    private String timeFare;
    @SerializedName("timeFareAfter")
    @Expose
    private String timeFareAfter;
    @SerializedName("mileagePriceAfter")
    @Expose
    private String mileagePriceAfter;
    @SerializedName("laterBookingAdvanceFee")
    @Expose
    private String laterBookingAdvanceFee;
    @SerializedName("bookingType")
    @Expose
    private int bookingType;
    @SerializedName("isDropLocationMandatory")
    @Expose
    private boolean isDropLocationMandatory;
    @SerializedName("isPreferenceEnabled")
    @Expose
    private boolean isPreferenceEnabled;
    @SerializedName("mapIcon")
    @Expose
    private String mapIcon;
    @SerializedName("plateNo")
    @Expose
    private String plateNo;
    @SerializedName("colour")
    @Expose
    private String colour;
    @SerializedName("typeName")
    @Expose
    private String typeName;
    @SerializedName("makeModel")
    @Expose
    private String makeModel;
    @SerializedName("vehicleImage")
    @Expose
    private String vehicleImage;
    @SerializedName("seatingCapacity")
    @Expose
    private int seatingCapacity;

    public boolean isPreferenceEnabled() {
        return isPreferenceEnabled;
    }
    public String getVehicleImage() {
        return vehicleImage;
    }
    public int getSeatingCapacity() {
        return seatingCapacity;
    }
    public String getLaterBookingAdvanceFee() {
        return laterBookingAdvanceFee;
    }
    public String getMapIcon() {
        return mapIcon;
    }
    public String getPlateNo() {
        return plateNo;
    }
    public String getColour() {
        return colour;
    }
    public String getTypeName() {
        return typeName;
    }
    public String getMakeModel() {
        return makeModel;
    }
    public String getMileagePriceAfter() {
        return mileagePriceAfter;
    }
    public String getTimeFareAfter() {
        return timeFareAfter;
    }
    public String getTimeFare() {
        return timeFare;
    }
    public String getMileagePrice() {
        return mileagePrice;
    }
    public String getMinimumFare() {
        return minimumFare;
    }
    public String getBaseFare() {
        return baseFare;
    }
    public int getBookingType() {
        return bookingType;
    }
    public boolean isDropLocationMandatory() {
        return isDropLocationMandatory;
    }
}
