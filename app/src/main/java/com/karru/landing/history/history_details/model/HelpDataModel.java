package com.karru.landing.history.history_details.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

public class HelpDataModel implements Serializable
{
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("bookingId")
    @Expose
    private String bookingId;
    @SerializedName("subcat")
    @Expose
    private ArrayList<String> subcat;
    @SerializedName("hasSubCatExpanded")
    @Expose
    private boolean hasSubCatExpanded = false;

    public boolean isHasSubCatExpanded() { return hasSubCatExpanded; }
    public void setHasSubCatExpanded(boolean hasSubCatExpanded) { this.hasSubCatExpanded = hasSubCatExpanded; }
    public ArrayList<String> getSubcat() {
        return subcat;
    }
    public String getBookingId() {
        return bookingId;
    }
    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }
    public String getLink ()
    {
        return link;
    }
    public void setLink (String link)
    {
        this.link = link;
    }
    public String getName ()
    {
        return name;
    }
    public void setName (String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [  link = "+link+", name = "+name+"]";
    }
}
