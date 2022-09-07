package com.karru.landing.home.model.fare_estimate_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * <h1>ExtraFeesModel</h1>
 * used to hold the extra fees data
 * @author 3Embed
 * @since on 23-03-2018.
 */
public class ExtraFeesModel implements Serializable
{
    /*"title":"3Embed Pick Fee",
"fee":60,*/
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("fee")
    @Expose
    private double fee;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public double getFee() {
        return fee;
    }
}
