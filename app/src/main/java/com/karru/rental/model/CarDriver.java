package com.karru.rental.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *This is a modal class for Car Type API
 * @author Anurag
 */

public class CarDriver {
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("masterId")
    @Expose
    private String masterId;
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("distance")
    @Expose
    private Double distance;
    @SerializedName("isFavouriteDriver")
    @Expose
    private Boolean isFavouriteDriver;
    @SerializedName("vehicleTypeIds")
    @Expose
    private List<String> vehicleTypeIds = null;
    @SerializedName("mqttTopic")
    @Expose
    private String mqttTopic;
    @SerializedName("locationHeading")
    @Expose
    private Integer locationHeading;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getMasterId() {
        return masterId;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Boolean getIsFavouriteDriver() {
        return isFavouriteDriver;
    }

    public void setIsFavouriteDriver(Boolean isFavouriteDriver) {
        this.isFavouriteDriver = isFavouriteDriver;
    }

    public List<String> getVehicleTypeIds() {
        return vehicleTypeIds;
    }

    public void setVehicleTypeIds(List<String> vehicleTypeIds) {
        this.vehicleTypeIds = vehicleTypeIds;
    }

    public String getMqttTopic() {
        return mqttTopic;
    }

    public void setMqttTopic(String mqttTopic) {
        this.mqttTopic = mqttTopic;
    }

    public Integer getLocationHeading() {
        return locationHeading;
    }

    public void setLocationHeading(Integer locationHeading) {
        this.locationHeading = locationHeading;
    }

}
