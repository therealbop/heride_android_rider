package com.karru.landing.home.model.promo_code_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>PromoCodeDataModel</h1>
 * This class is used to hold promo code data
 * @author 3Embed
 * @since on 25-06-2018.
 */
public class PromoCodeDataModel implements Serializable
{
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("minimumPurchaseValue")
    @Expose
    private String minimumPurchaseValue;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("ab")
    @Expose
    private String ab;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("howItWorks")
    @Expose
    private String howItWorks;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("termsAndConditions")
    @Expose
    private String termsAndConditions;
    @SerializedName("discount")
    @Expose
    private PromoCodeDiscountModel discount;

    public String getTitle ()
    {
        return title;
    }
    public void setTitle (String title)
    {
        this.title = title;
    }
    public String getDescription ()
    {
        return description;
    }
    public void setDescription (String description)
    {
        this.description = description;
    }
    public String getCode ()
    {
        return code;
    }
    public void setCode (String code)
    {
        this.code = code;
    }
    public String getEndTime ()
    {
        return endTime;
    }
    public String getTermsAndConditions ()
    {
        return termsAndConditions;
    }
    public PromoCodeDiscountModel getDiscount ()
    {
        return discount;
    }
    public void setDiscount (PromoCodeDiscountModel discount)
    {
        this.discount = discount;
    }
    @Override
    public String toString()
    {
        return "ClassPojo [startTime = "+startTime+", title = "+title+", _id = "+_id+", minimumPurchaseValue = "+minimumPurchaseValue+", description = "+description+", ab = "+ab+", code = "+code+", howItWorks = "+howItWorks+", endTime = "+endTime+", termsAndConditions = "+termsAndConditions+", discount = "+discount+"]";
    }
}
