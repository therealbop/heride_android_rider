package com.karru.landing.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>WalletDataModel</h1>
 * @author 3Embed
 * @since on 31-03-2018.
 * used to hold wallet data
 */
public class WalletDataModel implements Serializable
{
    @SerializedName("isWalletEnable")
    @Expose
    private boolean isWalletEnable;
    @SerializedName("walletBalance")
    @Expose
    private double walletBalance;
    @SerializedName("softLimit")
    @Expose
    private double softLimit;
    @SerializedName("hardLimit")
    @Expose
    private double hardLimit;
    @SerializedName("currencySymbol")
    @Expose
    private String currencySymbol;
    @SerializedName("currencyAbbr")
    @Expose
    private int currencyAbbr;

    public String getCurrencySymbol() {
        return currencySymbol;
    }
    public int getCurrencyAbbr() {
        return currencyAbbr;
    }
    public boolean isWalletEnable() {
        return isWalletEnable;
    }
    public double getWalletBalance() {
        return walletBalance;
    }
    public double getSoftLimit() {
        return softLimit;
    }
    public double getHardLimit() {
        return hardLimit;
    }
}
