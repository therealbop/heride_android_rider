package com.karru.landing.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>PromoCodeModel</h1>
 * used to store promo details
 */
public class PromoCodeModel implements Serializable
{
    /*"promoPaymentMothod":2,
"isApplicableWithWallet":1,
"discountAmount":0,
"discountType":1,
"discountValue":30,
"promoId":"5b1677fd2345873d4f39977c"*/
    @SerializedName("paymentType")
    @Expose
    private int paymentType;
    @SerializedName("payByWallet")
    @Expose
    private int payByWallet;
    @SerializedName("promoId")
    @Expose
    private String promoId;
    @SerializedName("couponCode")
    @Expose
    private String couponCode;
    @SerializedName("discountAmount")
    @Expose
    private double discountAmount;
    @SerializedName("finalAmount")
    @Expose
    private double finalAmount;
    @SerializedName("discountType")
    @Expose
    private int discountType;
    @SerializedName("discountValue")
    @Expose
    private int discountValue;

    public double getFinalAmount() {
        return finalAmount;
    }
    public double getDiscountAmount() {
        return discountAmount;
    }
    public int getDiscountValue() {
        return discountValue;
    }
    public int getDiscountType() {
        return discountType;
    }
    public String getCouponCode() {
        return couponCode;
    }
    public int getPromoPaymentMothod() {
        return paymentType;
    }
    public int getIsApplicableWithWallet() {
        return payByWallet;
    }
    public String getPromoId() {
        return promoId;
    }
}
