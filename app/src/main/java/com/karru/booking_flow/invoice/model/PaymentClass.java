package com.karru.booking_flow.invoice.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>PaymentClass</h1>
 * @author 3Embed
 * @since on 28-03-2018.
 */
public class PaymentClass implements Serializable
{
    /*"cardLastDigits":"",
"cardType":"Visa",
"cashCollected":0,
"cardDeduct":0,
"walletTransaction":515*/
    @SerializedName("cardLastDigits")
    @Expose
    private String cardLastDigits;
    @SerializedName("cardType")
    @Expose
    private String cardType;
    @SerializedName("isCorporateBooking")
    @Expose
    private boolean isCorporateBooking;
    @SerializedName("cashCollected")
    @Expose
    private double cashCollected;
    @SerializedName("cardDeduct")
    @Expose
    private double cardDeduct;
    @SerializedName("walletTransaction")
    @Expose
    private double walletTransaction;

    public boolean isCorporateBooking() {
        return isCorporateBooking;
    }
    public double getCashCollected() {
        return cashCollected;
    }
    public double getCardDeduct() {
        return cardDeduct;
    }
    public double getWalletTransaction() {
        return walletTransaction;
    }
    public String getCardLastDigits() {
        return cardLastDigits;
    }
    public String getCardType() {
        return cardType;
    }
}
