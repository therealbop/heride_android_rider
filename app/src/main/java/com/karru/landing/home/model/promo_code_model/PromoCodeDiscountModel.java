package com.karru.landing.home.model.promo_code_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/*
* <h1> PromoCodeDiscountModel</h1>
* this class is use to hold the value of discount for promo code.
* @author 3Embed
*@since on 25-06-2018.
* */
public class PromoCodeDiscountModel implements Serializable
{
    @SerializedName("typeName")
    @Expose
    private String typeName;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("typeId")
    @Expose
    private String typeId;

    public String getValue ()
    {
        return value;
    }
    public void setValue (String value)
    {
        this.value = value;
    }
    @Override
    public String toString()
    {
        return "ClassPojo [typeName = "+typeName+", value = "+value+", typeId = "+typeId+"]";
    }
}
