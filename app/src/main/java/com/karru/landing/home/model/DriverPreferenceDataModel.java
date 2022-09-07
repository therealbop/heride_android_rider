package com.karru.landing.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>DriverPreferenceDataModel</h1>
 * used to hold the preference model
 * @author 3Embed
 * @since on 03-04-2018.
 */
public class DriverPreferenceDataModel implements Serializable
{
    /*"id":"5b853ff1f9a60e5e6d04fc41",
"name":"Female Driver",
"fees":30,
"icon":"https://s3.amazonaws.com/uberforallv1/customers/ProfilePics/file2018828122829.png"*/
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("fees")
    @Expose
    private String fees;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("isSelected")
    @Expose
    private boolean isSelected ;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getFees() {
        return fees;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
