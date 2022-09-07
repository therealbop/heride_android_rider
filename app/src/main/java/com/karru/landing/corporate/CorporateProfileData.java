package com.karru.landing.corporate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * @author 3Embed
 * @since on 11-04-2018.
 */
public class CorporateProfileData implements Serializable
{
    @SerializedName("instituteName")
    @Expose
    private String instituteName;
    @SerializedName("corporateEmail")
    @Expose
    private String corporateEmail;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("isSelected")
    @Expose
    private boolean isSelected;
    @SerializedName("userWalletBalance")
    @Expose
    private double userWalletBalance;
    @SerializedName("currencySymbol")
    @Expose
    private String currencySymbol;
    @SerializedName("currencyAbbr")
    @Expose
    private int currencyAbbr;

    public int getCurrencyAbbr() {
        return currencyAbbr;
    }
    public double getUserWalletBalance() {
        return userWalletBalance;
    }
    public String getCurrencySymbol() {
        return currencySymbol;
    }
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    public String getInstituteName() {
        return instituteName;
    }
    public String getCorporateEmail() {
        return corporateEmail;
    }
    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
