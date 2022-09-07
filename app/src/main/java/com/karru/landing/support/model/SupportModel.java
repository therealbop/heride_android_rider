package com.karru.landing.support.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <h2>FavoritesModel</h2>
 * <p>
 *     Pojo class to parse support data list
 * </p>
 * @since  24/11/15.
 */
public class SupportModel implements Serializable
{
    @SerializedName("data")
    @Expose
    private ArrayList <SupportDataModel> data;

    public ArrayList<SupportDataModel> getData() {
        return data;
    }
    public void setData(ArrayList<SupportDataModel> data) {
        this.data = data;
    }
}