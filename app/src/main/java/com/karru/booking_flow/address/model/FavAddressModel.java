package com.karru.booking_flow.address.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * <h1>FavAddressModel</h1>
 * This class is used to parse the data for the fav address
 * @author 3Embed
 * @since on 28/07/17.
 */

public class FavAddressModel implements Serializable
{
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<FavAddressDataModel> data;

    public ArrayList<FavAddressDataModel> getData() {
        return data;
    }
    public void setData(ArrayList<FavAddressDataModel> data) {
        this.data = data;
    }
    public String getMessage() {
        return message;
    }
}
