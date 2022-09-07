package com.karru.landing.my_vehicles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class MyVehiclesDataModel implements Serializable {
    /*"id":"5c0e75693878a72799a28d3f",
"year":"1938",
"make":"make",
"model":"model",
"color":"red",
"isDefault":true*/
    @Expose
    @SerializedName("id")
    private String id;
    @Expose
    @SerializedName("year")
    private String year;
    @Expose
    @SerializedName("make")
    private String make;
    @Expose
    @SerializedName("model")
    private String model;
    @Expose
    @SerializedName("color")
    private String color;
    @Expose
    @SerializedName("isVehicledefault")
    private boolean isVehicleDefault;

    public void setVehicleDefault(boolean vehicledefault) {
        isVehicleDefault = vehicledefault;
    }

    public String getId() {
        return id;
    }

    public String getYear() {
        return year;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public boolean isDefault() {
        return isVehicleDefault;
    }
}
