package com.karru.landing.favorite.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h2>FavoritesDriversDetails</h2>
 * <p>
 *     usd to get the favorites drivers data
 * </p>
 * @since  24/11/15.
 */
public class FavoritesDriversDetails implements Serializable
{
    /*"driverId":"5a9e40d36d99ba39de130565",
"name":"Baby Baby",
"profilePic":"https://s3-ap-southeast-2.amazonaws.com/karru/Drivers/ProfilePics/1524570921510_0_01.png",
"isOnline":true*/
    @SerializedName("driverId")
    @Expose
    private String driverId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    @SerializedName("isOnline")
    @Expose
    private boolean isOnline;

    public String getDriverId() {
        return driverId;
    }
    public String getName() {
        return name;
    }
    public String getProfilePic() {
        return profilePic;
    }
    public boolean isOnline() {
        return isOnline;
    }
}
