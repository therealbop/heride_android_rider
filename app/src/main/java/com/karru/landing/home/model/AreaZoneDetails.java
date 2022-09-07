package com.karru.landing.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <h1>AreaZoneDetails</h1>
 * This class is used to hold zone details
 * @author 3Embed
 * @since on 03-01-2018.
 */
public class AreaZoneDetails implements Serializable
{
    /*"id":"5a9f7f5cf9a60e201d7d6082",
       "city":"Bengaluru",
         "title":"Orion Mall",
       "pointsProps":{},
       "specialPickup":true,
         pickups":[]*/
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("pointsProps")
    @Expose
    private ZonesPoints pointsProps;
    @SerializedName("pickups")
    @Expose
    private ArrayList<PickUpGates> pickups;

    public String getTitle() {
        return title;
    }
    public String getCity() {
        return city;
    }
    public ArrayList<PickUpGates> getPickups() {
        return pickups;
    }
    public String getId() {
        return id;
    }
    public ZonesPoints getPointsProps() {
        return pointsProps;
    }
}
