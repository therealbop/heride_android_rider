package com.karru.landing.rate.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class RateCardRide implements Serializable
{
    @SerializedName("vehicleImgOn")
    @Expose
    private String vehicleImgOn;
    @SerializedName("typeName")
    @Expose
    private String typeName;
    @SerializedName("timeFare")
    @Expose
    private String timeFare;
    @SerializedName("mileagePriceAfter")
    @Expose
    private String mileagePriceAfter;
    @SerializedName("bookingType")
    @Expose
    private String bookingType;
    @SerializedName("minimumFare")
    @Expose
    private String minimumFare;
    @SerializedName("mileagePrice")
    @Expose
    private String mileagePrice;
    @SerializedName("vehicleDimension")
    @Expose
    private String vehicleDimension;
    @SerializedName("seatingCapacity")
    @Expose
    private int seatingCapacity;
    @SerializedName("timeFareAfter")
    @Expose
    private String timeFareAfter;
    @SerializedName("typeDesc")
    @Expose
    private String typeDesc;
    @SerializedName("waitingFee")
    @Expose
    private String waitingFee;
    @SerializedName("waitingTimeXMinute")
    @Expose
    private String waitingTimeXMinute;
    @SerializedName("baseFee")
    @Expose
    private String baseFee;

    public String getBaseFee() {
        return baseFee;
    }
    public String getWaitingFee() {
        return waitingFee;
    }
    public String getWaitingTimeXMinute() {
        return waitingTimeXMinute;
    }
    public String getVehicleImgOn ()
    {
        return vehicleImgOn;
    }
    public String getTypeName ()
    {
        return typeName;
    }
    public String getTypeDesc() {
        return typeDesc;
    }
    public String getTimeFare ()
    {
        return timeFare;
    }
    public String getMileagePriceAfter ()
    {
        return mileagePriceAfter;
    }
    public String getBookingType ()
    {
        return bookingType;
    }
    public void setBookingType (String bookingType)
    {
        this.bookingType = bookingType;
    }
    public String getMinimumFare ()
    {
        return minimumFare;
    }
    public String getMileagePrice ()
    {
        return mileagePrice;
    }
    public String getVehicleDimension ()
    {
        return vehicleDimension;
    }
    public int getSeatingCapacity ()
    {
        return seatingCapacity;
    }
    public String getTimeFareAfter ()
    {
        return timeFareAfter;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [vehicleImgOn = "+vehicleImgOn+", typeName = "+typeName+", timeFare = "+timeFare+", mileagePriceAfter = "+mileagePriceAfter+", bookingType = "+bookingType+", minimumFare = "+minimumFare+", mileagePrice = "+mileagePrice+", vehicleDimension = "+vehicleDimension+", seatingCapacity = "+seatingCapacity+", timeFareAfter = "+timeFareAfter+"]";
    }
}
