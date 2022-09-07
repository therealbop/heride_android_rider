package com.karru.landing.home.model.eta_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author Akbar on 26/11/16.
 */
public class ElementsForEtaModel implements Serializable
{
    /*
    "distance":{
        "text":"48 m",
        "value":48
    },
    "duration":{
        "text":"1 min",
        "value":25
    */
    @SerializedName("duration")
    @Expose
    private DurationForEtaModel duration;
    @SerializedName("distance")
    @Expose
    private DurationForEtaModel distance;
    @SerializedName("status")
    @Expose
    private String status;

    public DurationForEtaModel getDistance() {
        return distance;
    }
    public String getStatus() {
        return status;
    }
    public DurationForEtaModel getDuration() {
        return duration;
    }
}
