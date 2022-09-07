package com.karru.landing.home.model.eta_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author Akbar on 26/11/16.
 */
public class DurationForEtaModel implements Serializable
{
    /*    "text":"48 m",
    "value":48*/
    @SerializedName("value")
    @Expose
    private String value;

    @SerializedName("text")
    @Expose
    private String text;

    public String getText() {
        return text;
    }
    public String getValue() {
        return value;
    }
}
