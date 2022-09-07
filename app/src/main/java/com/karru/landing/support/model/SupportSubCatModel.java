package com.karru.landing.support.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>SupportSubCatModel</h1>
 * <p> pojo class to parse support sub category</p>
 * @since 31/5/17.
 */

public class SupportSubCatModel implements Serializable
{
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("link")
    @Expose
    private String link;

    public String getName ()
    {
        return name;
    }
    public void setName (String Name)
    {
        this.name = Name;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
}
