package com.karru.landing.rate.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

public class RateCardDetail implements Serializable
{
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<RateCardCityDetails> data;

    public String getMessage ()
    {
        return message;
    }
    public void setMessage (String message)
    {
        this.message = message;
    }
    public ArrayList<RateCardCityDetails> getData ()
    {
        return data;
    }
    public void setData (ArrayList<RateCardCityDetails> data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", data = "+data+"]";
    }
}
