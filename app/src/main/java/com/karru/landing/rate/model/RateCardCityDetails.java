package com.karru.landing.rate.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class RateCardCityDetails implements Serializable
{
    @SerializedName("ride")
    @Expose
    private ArrayList<RateCardRide> ride;
    @SerializedName("cityDistanceMetrics")
    @Expose
    private String cityDistanceMetrics;
    @SerializedName("cityCurrencyAbbr")
    @Expose
    private int cityCurrencyAbbr;
    @SerializedName("cityId")
    @Expose
    private String cityId;
    @SerializedName("cityName")
    @Expose
    private String cityName;
    @SerializedName("cityCurrencySymbol")
    @Expose
    private String cityCurrencySymbol;
    @SerializedName("cityTimeMetrics")
    @Expose
    private String cityTimeMetrics;

    public String getCityTimeMetrics() {
        return cityTimeMetrics;
    }
    public ArrayList<RateCardRide> getRide ()
    {
        return ride;
    }
    public void setRide (ArrayList<RateCardRide> ride)
    {
        this.ride = ride;
    }
    public String getCityDistanceMetrics ()
    {
        return cityDistanceMetrics;
    }
    public int getCityCurrencyAbbr ()
    {
        return cityCurrencyAbbr;
    }
    public String getCityName ()
    {
        return cityName;
    }
    public String getCityCurrencySymbol ()
    {
        return cityCurrencySymbol;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ride = "+ride+", cityDistanceMetrics = "+ cityDistanceMetrics +", cityCurrencyAbbr = "+cityCurrencyAbbr+", cityId = "+cityId+", cityName = "+cityName+", cityCurrencySymbol = "+cityCurrencySymbol+"]";
    }
}
