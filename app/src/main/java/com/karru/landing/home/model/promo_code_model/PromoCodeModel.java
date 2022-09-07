package com.karru.landing.home.model.promo_code_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <h1>PromoCodeModel</h1>
 * This class is used to hold promo code details
 * @author 3Embed
 * @since on 25-06-2018.
 */
public class PromoCodeModel implements Serializable
{
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<PromoCodeDataModel> data;

    public String getMessage ()
    {
        return message;
    }
    public void setMessage (String message)
    {
        this.message = message;
    }
    public ArrayList<PromoCodeDataModel>  getData ()
    {
        return data;
    }
    public void setData (ArrayList<PromoCodeDataModel>  data)
    {
        this.data = data;
    }
    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", data = "+data+"]";
    }
}
