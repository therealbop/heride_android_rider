package com.karru.landing.support.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <h1>SupportDataModel</h1>
 * <pojo class to parse and handle support list data
 * @since  31/5/17.
 */

public class SupportDataModel implements Serializable
{
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("hasSubCatExpanded")
    @Expose
    private boolean hasSubCatExpanded = false;
    @SerializedName("subcat")
    @Expose
    private ArrayList<SupportSubCatModel> subcat;

    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public String getName ()
    {
        return name;
    }
    public void setName (String Name)
    {
        this.name = Name;
    }
    public ArrayList<SupportSubCatModel> getSubcat() {
        return subcat;
    }
    public boolean getHasSubCatExpanded() {
        return hasSubCatExpanded;
    }
    public void setHasSubCatExpanded(boolean hasSubCatExpanded) { this.hasSubCatExpanded = hasSubCatExpanded; }
}
