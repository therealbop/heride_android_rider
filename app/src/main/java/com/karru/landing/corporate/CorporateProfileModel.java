package com.karru.landing.corporate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

public class CorporateProfileModel implements Serializable
{
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<CorporateProfileData> data;

    public String getMessage() {
        return message;
    }
    public ArrayList<CorporateProfileData> getData() {
        return data;
    }
}
