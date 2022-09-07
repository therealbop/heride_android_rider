package com.karru.landing.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <h1>DriverPreferenceModel</h1>
 * used to hold the preference model
 * @author 3Embed
 * @since on 03-04-2018.
 */
public class DriverPreferenceModel implements Serializable
{
    @SerializedName("data")
    @Expose
    private ArrayList<DriverPreferenceDataModel> data = new ArrayList<>();
    @SerializedName("currencySymbol")
    @Expose
    private String currencySymbol;
    @SerializedName("currencyAbbr")
    @Expose
    private int currencyAbbr;
    private ArrayList<String> selectedDriverPref = new ArrayList<>();

    public ArrayList<String> getSelectedDriverPref() {
        return selectedDriverPref;
    }

    public void setSelectedDriverPref(ArrayList<String> selectedDriverPref) {
        this.selectedDriverPref = selectedDriverPref;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }
    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }
    public int getCurrencyAbbr() {
        return currencyAbbr;
    }
    public void setCurrencyAbbr(int currencyAbbr) {
        this.currencyAbbr = currencyAbbr;
    }
    public ArrayList<DriverPreferenceDataModel> getData() {
        return data;
    }

    public void setData(ArrayList<DriverPreferenceDataModel> data) {
        this.data = data;
    }
}
