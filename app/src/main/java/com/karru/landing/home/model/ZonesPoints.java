package com.karru.landing.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <h1>ZonesPoints</h1>
 * This class is used to hold zone latlongs
 * @author 3Embed
 * @since on 03-01-2018.
 */
public class ZonesPoints implements Serializable
{
    /*"strokeColor":"#521977",
"strokeOpacity":0.4,
"strokeWeight":2,
"fillColor":"#521977",
"fillOpacity":0.55,
"paths":[],*/
    @SerializedName("strokeColor")
    @Expose
    private String strokeColor;
    @SerializedName("fillColor")
    @Expose
    private String fillColor;
    @SerializedName("strokeOpacity")
    @Expose
    private float strokeOpacity;
    @SerializedName("fillOpacity")
    @Expose
    private float fillOpacity;
    @SerializedName("strokeWeight")
    @Expose
    private int strokeWeight;
    @SerializedName("paths")
    @Expose
    private ArrayList<Location> paths;

    public String getStrokeColor() {
        return strokeColor;
    }
    public String getFillColor() {
        return fillColor;
    }
    public int getStrokeWeight() {
        return strokeWeight;
    }
    public ArrayList<Location> getPaths() {
        return paths;
    }
}
