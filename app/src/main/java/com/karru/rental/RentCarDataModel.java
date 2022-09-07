package com.karru.rental;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.karru.rental.model.DataModel;

import java.io.Serializable;
import java.util.List;

/**
 * This is a modal class for Package API
 *
 * @author Anurag
 */

public class RentCarDataModel implements Serializable {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("packagesDetails")
    @Expose
    private List<DataModel> packagesDetails = null;
    @SerializedName("fareDetailsRules")
    @Expose
    private List<String> fareDetailsRules = null;
    @SerializedName("guidanceNotes")
    @Expose
    private List<String> guidanceNotes = null;

    public List<String> getFareDetailsRules() {
        return fareDetailsRules;
    }

    public void setFareDetailsRules(List<String> fareDetailsRules) {
        this.fareDetailsRules = fareDetailsRules;
    }

    public List<String> getGuidanceNotes() {
        return guidanceNotes;
    }

    public void setGuidanceNotes(List<String> guidanceNotes) {
        this.guidanceNotes = guidanceNotes;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataModel> getData() {
        return packagesDetails;
    }

    public void setData(List<DataModel> data) {
        this.packagesDetails = data;
    }
}
