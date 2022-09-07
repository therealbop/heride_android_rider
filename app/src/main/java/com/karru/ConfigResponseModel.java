package com.karru;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ConfigResponseModel implements Serializable
{
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ConfigurationDataModel data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ConfigurationDataModel getData() {
        return data;
    }

    public void setData(ConfigurationDataModel data) {
        this.data = data;
    }
}
