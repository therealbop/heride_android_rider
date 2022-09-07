package com.karru.landing.history.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <h1>HistoryModel</h1>
 * used to hold the data of history
 * @author 3EMbed
 * @since on 2/22/2018.
 */
public class HistoryModel implements Serializable
{
    @SerializedName("assigned")
    @Expose
    private ArrayList<HistoryDataModel> assigned;
    @SerializedName("unassigned")
    @Expose
    private ArrayList<HistoryDataModel> unassigned;
    @SerializedName("past")
    @Expose
    private ArrayList<HistoryDataModel> past;

    public ArrayList<HistoryDataModel> getAssigned() {
        return assigned;
    }
    public ArrayList<HistoryDataModel> getUnassigned() {
        return unassigned;
    }
    public ArrayList<HistoryDataModel> getPast() {
        return past;
    }
}
