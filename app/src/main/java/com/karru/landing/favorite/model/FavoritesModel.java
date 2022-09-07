package com.karru.landing.favorite.model;

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
public class FavoritesModel implements Serializable
{
    @SerializedName("data")
    @Expose
    private ArrayList<FavoritesDriversDetails> data;

    public ArrayList<FavoritesDriversDetails> getData() {
        return data;
    }
    public void setData(ArrayList<FavoritesDriversDetails> data) {
        this.data = data;
    }
}