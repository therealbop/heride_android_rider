package com.karru.landing.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * <h1>MQTTResponseDataModel</h1>
 * This class is used to hold the MQTT response
 * @author embed
 * @since on 19/1/16.
 */
public class MQTTResponseDataModel implements Serializable
{
    @SerializedName("flag")
    @Expose
    private String flag;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("vehicleTypes")
    @Expose
    private ArrayList<VehicleTypesDetails> vehicleTypes = new ArrayList<>();
    @SerializedName("ongoingBookings")
    @Expose
    private ArrayList<OnGoingBookingsModel> ongoingBookings;
    @SerializedName("areaZone")
    @Expose
    private AreaZoneDetails areaZone;

    public ArrayList<OnGoingBookingsModel> getOngoingBookings() {
        return ongoingBookings;
    }
    public AreaZoneDetails getAreaZone() { return areaZone; }
    public void setOngoingBookings(ArrayList<OnGoingBookingsModel> ongoingBookings) { this.ongoingBookings = ongoingBookings;}
    public void setAreaZone(AreaZoneDetails areaZone) {
        this.areaZone = areaZone;
    }
    public int getStatus() {
        return status;
    }
    public ArrayList<VehicleTypesDetails> getVehicleTypes() {return vehicleTypes;}
    public void setVehicleTypes(ArrayList<VehicleTypesDetails> vehicleTypes) { this.vehicleTypes = vehicleTypes; }
    public String getFlag ()
    {
        return flag;
    }
    public void setFlag (String flag)
    {
        this.flag = flag;
    }
}
