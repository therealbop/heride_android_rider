package com.karru.rental.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 This is a modal class for Package API
 @author Anurag
 */
public class DataModel implements Serializable {
    @SerializedName("packageId")
    @Expose
    private String packageId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("distance")
    @Expose
    private Integer distance;
    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("distanceMatrix")
    @Expose
    private String distanceMatrix;

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getDistanceMatrix() {
        return distanceMatrix;
    }

    public void setDistanceMatrix(String distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
    }

}
