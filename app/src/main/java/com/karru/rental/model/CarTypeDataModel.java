package com.karru.rental.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * This is the modal class for Car Type API
 * @author Anurag
 */
public class CarTypeDataModel implements Serializable {
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<CarDataModel> carDataModel = null;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CarDataModel> getData() {
        return carDataModel;
    }

    public void setData(List<CarDataModel> carDataModel) {
        this.carDataModel = carDataModel;
    }
}
