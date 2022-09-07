package com.karru.landing.my_vehicles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <h2>MyVehiclesModel</h2>
 * used to hold the my vehicles model
 */
public class MyVehiclesModel implements Serializable {
    /*
"message":"Success.",
"data":[]*/
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("data")
    private ArrayList<MyVehiclesDataModel> data;

    public String getMessage() {
        return message;
    }

    public ArrayList<MyVehiclesDataModel> getData() {
        return data;
    }
}
