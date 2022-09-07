package com.karru.booking_flow.address.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * <h1>AddressDataModel</h1>
 * This class is used to hold the address data
 * @author embed
 * @since on 17/3/17.
 */
public class AddressDataModel implements Serializable
{
    @SerializedName("addressId")
    @Expose
    private int addressId;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lng")
    @Expose
    private String lng;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("isToAddAsFav")
    @Expose
    private boolean isToAddAsFav = false;
    @SerializedName("isItAFavAdrs")
    @Expose
    private boolean isItAFavAdrs =false;
    @SerializedName("type")
    @Expose
    private int type ;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAddressId() {
        return addressId;
    }
    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
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
    public void setLng(String lng) { this.lng = lng; }
    public void set_id(String _id) { this._id = _id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public void setIsToAddAsFav(boolean toAddAsFav) { isToAddAsFav = toAddAsFav; }
    public void setIsItAFavAdrs(boolean itAFavAdrs) { isItAFavAdrs = itAFavAdrs; }
}
