package com.karru.landing.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * <h1>DriverDetailsModel</h1>
 * This class is used to hole the driver details data
 * @author  embed
 * @since on 2/12/15.
 */
public class DriverDetailsModel implements Serializable
{
    /*"name":"Raghu V",
"phone":"+918123666326",
"profilePic":"https://s3-ap-southeast-2.amazonaws.com/karru/Drivers/ProfilePics/1516719285883_0_01.png",
"latitude":77.58943207813148,
"longitude":77.58943207813148,
"averageRating":1,
"mqttTopic":"masterm_5a674ce5cad8ff011ab61aff_13AB10F9-4880-4E75-A7F4-C74267688EDE"*/

    /*"status":4,
"location_Heading":"289.000702",
"latitude":13.028639558883958,
"longitude":77.589458722796863,
"driverId":"5a7174c86d99ba39de130051"
"vehicleTypeIds":[
"5c108e65f9a60e328453f769"
]*/
    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    @SerializedName("mqttTopic")
    @Expose
    private String mqttTopic;
    @SerializedName("masterId")
    @Expose
    private String masterId;
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("averageRating")
    @Expose
    private double averageRating;
    @SerializedName("location_Heading")
    @Expose
    private String location_Heading;
    @SerializedName("isFavouriteDriver")
    @Expose
    private boolean isFavouriteDriver;
    @SerializedName("distance")
    @Expose
    private double distance;
    @SerializedName("vehicleTypeIds")
    @Expose
    private ArrayList<String> vehicleTypeIds;

    public ArrayList<String> getVehicleTypeIds() {
        return vehicleTypeIds;
    }

    public double getDistance() {
        return distance;
    }
    public boolean isFavouriteDriver() {
        return isFavouriteDriver;
    }
    public String getFullName() {
        return fullName;
    }
    public String getMasterId() {
        return masterId;
    }
    public String getLocation_Heading() {
        return location_Heading;
    }
    public String getName() {
        return name;
    }
    public String getPhone() {
        return phone;
    }
    public String getProfilePic() {
        return profilePic;
    }
    public double getAverageRating() {
        return averageRating;
    }
    public String getMqttTopic() {
        return mqttTopic;
    }
    public double getLongitude()
    {
        return longitude;
    }
    public double getLatitude() {return latitude;}
}
