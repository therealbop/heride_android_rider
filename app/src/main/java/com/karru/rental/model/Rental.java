package com.karru.rental.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * This is the Modal class for Car Type API
 * @author Anurag
 */

public class Rental implements Serializable {
    @SerializedName("baseFare")
    @Expose
    private String baseFare;
    @SerializedName("distanceFare")
    @Expose
    private String distanceFare;
    @SerializedName("durationFare")
    @Expose
    private String durationFare;
    @SerializedName("advanceFare")
    @Expose
    private String advanceFare;

    public String getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(String baseFare) {
        this.baseFare = baseFare;
    }

    public String getDistanceFare() {
        return distanceFare;
    }

    public void setDistanceFare(String distanceFare) {
        this.distanceFare = distanceFare;
    }

    public String getDurationFare() {
        return durationFare;
    }

    public void setDurationFare(String durationFare) {
        this.durationFare = durationFare;
    }

    public String getAdvanceFare() {
        return advanceFare;
    }

    public void setAdvanceFare(String advanceFare) {
        this.advanceFare = advanceFare;
    }



}
