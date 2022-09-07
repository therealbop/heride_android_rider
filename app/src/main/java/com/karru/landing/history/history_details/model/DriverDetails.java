package com.karru.landing.history.history_details.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class DriverDetails implements Serializable
{
    /*"name":"Raghavendra V",
"profilePic":"https://s3-ap-southeast-2.amazonaws.com/karru/Drivers/ProfilePics/1517336515153_0_01.png",
"rating":5*/
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    @SerializedName("rating")
    @Expose
    private double rating;

    public String getName() {
        return name;
    }
    public String getProfilePic() {
        return profilePic;
    }
    public double getRating() {
        return rating;
    }
}