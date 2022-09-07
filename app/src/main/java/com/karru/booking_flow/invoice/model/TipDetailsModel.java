package com.karru.booking_flow.invoice.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <h1>TipDetailsModel</h1>
 * used to hold the tip details model
 * @author 3Embed
 * @since on 16-03-2018.
 */
public class TipDetailsModel implements Serializable
{
    /*"isTipEnable":true,
"tipType":1,
"tipValues":[]*/
    @SerializedName("isTipEnable")
    @Expose
    private boolean isTipEnable;
    @SerializedName("tipType")
    @Expose
    private int tipType;
    @SerializedName("tipValues")
    @Expose
    private ArrayList<Integer> tipValues;

    public boolean isTipEnable() {
        return isTipEnable;
    }
    public int getTipType() {
        return tipType;
    }
    public ArrayList<Integer> getTipValues() {
        return tipValues;
    }
}
