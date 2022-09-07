package com.karru.landing.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * <h1>PickUpGates</h1>
 * used to hold the pick up gates in zones
 * @author 3EMbed
 * @since on 07-03-2018.
 */

public class PickUpGates implements Serializable
{
    /*"id":"5a9f7f5cf9a60e201d7d607e",
"title":"Mall Entry Gate",
"location":{}*/
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("selected")
    @Expose
    private boolean selected;
    @SerializedName("location")
    @Expose
    private Location location;

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public Location getLocation() {
        return location;
    }
}
