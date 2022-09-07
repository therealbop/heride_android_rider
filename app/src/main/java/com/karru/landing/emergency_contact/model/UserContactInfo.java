package com.karru.landing.emergency_contact.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserContactInfo implements Serializable
{
    @SerializedName("contactNumber")
    @Expose
    private String contactNumber;
    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("contactName")
    @Expose
    private String contactName;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getContactNumber ()
    {
        return contactNumber;
    }
    public void setContactNumber (String contactNumber)
    {
        this.contactNumber = contactNumber;
    }
    public String get_id ()
    {
        return _id;
    }
    public String getStatus ()
    {
        return status;
    }
    public void setStatus (String status)
    {
        this.status = status;
    }
    public String getContactName ()
    {
        return contactName;
    }
    public void setContactName (String contactName)
    {
        this.contactName = contactName;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [contactNumber = "+contactNumber+", _id = "+_id+", status = "+status+", contactName = "+contactName+", userId = "+userId+"]";
    }
}
