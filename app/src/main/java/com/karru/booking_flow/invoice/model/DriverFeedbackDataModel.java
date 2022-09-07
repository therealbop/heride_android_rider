package com.karru.booking_flow.invoice.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * <h1>DriverFeedbackDataModel</h1>
 * This class is used to hold the feedback list in the model
 * @since 23/08/17.
 */
public class DriverFeedbackDataModel implements Serializable
{
    @SerializedName("rating1")
    @Expose
    private ArrayList<String> rating1;
    @SerializedName("rating2")
    @Expose
    private ArrayList<String> rating2;
    @SerializedName("rating3")
    @Expose
    private ArrayList<String> rating3;
    @SerializedName("rating4")
    @Expose
    private ArrayList<String> rating4;
    @SerializedName("rating5")
    @Expose
    private ArrayList<String> rating5;

    public ArrayList<String> getRating1() {
        return rating1;
    }
    public ArrayList<String> getRating2() {
        return rating2;
    }
    public ArrayList<String> getRating3() {
        return rating3;
    }
    public ArrayList<String> getRating4() {
        return rating4;
    }
    public ArrayList<String> getRating5() {
        return rating5;
    }
}
