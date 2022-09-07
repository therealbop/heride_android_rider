package com.karru.landing.emergency_contact.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

public class EmergencyContactDetail implements Serializable
{
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<UserContactInfo> data;

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public ArrayList<UserContactInfo> getData() {
        return data;
    }
    public void setData(ArrayList<UserContactInfo> data) {
        this.data = data;
    }
}
