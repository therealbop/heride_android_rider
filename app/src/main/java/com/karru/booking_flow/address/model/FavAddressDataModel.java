package com.karru.booking_flow.address.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>FavAddressDataModel</h1>
 * This class is used to hold the fav address data
 * @author  3Embed
 * @since on 28/07/17.
 */
public class FavAddressDataModel implements Serializable
{
    /*"data" : [
      {
        "longitude" : 77.58942,
        "addressId" : "5979e8cb0ba296af45e65be4",
        "latitude" : 13.02868,
        "userId" : "594d27924296c76c228db0df",
        "name" : "home",
        "address" : "19, 1st Main Road, RBI Colony, Hebbal, Bengaluru, Karnataka 560024"
      }
    ]
      }*/
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("addressId")
    @Expose
    private String addressId;
    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("latitude")
    @Expose
    private double latitude = 0.0;
    @SerializedName("longitude")
    @Expose
    private double longitude = 0.0;

    public String getName() {
        return name;
    }
    public String get_id() {
        return _id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getAddressId() {
        return addressId;
    }
    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
