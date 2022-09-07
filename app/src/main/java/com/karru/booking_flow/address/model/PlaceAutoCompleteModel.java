package com.karru.booking_flow.address.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * <h1>PlaceAutoCompleteModel</h1>
 * used to store the auto search address
 * @author 3embed
 * @author on 27/3/17.
 */
public class PlaceAutoCompleteModel implements Serializable
{
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("ref_key")
    @Expose
    private String ref_key;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lng")
    @Expose
    private String lng;

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getRef_key() {
        return ref_key;
    }
    public void setRef_key(String ref_key) {
        this.ref_key = ref_key;
    }
    public String getLat() {
        return lat;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }
    public String getLng() {
        return lng;
    }
    public void setLng(String lng) {
        this.lng = lng;
    }
}
