package com.karru.splash.first;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <h2>LanguagesListModel</h2>
 * used to store the languages model
 */
public class LanguagesListModel implements Serializable
{
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<LanguagesList> data = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<LanguagesList> getData() {
        return data;
    }

    public void setData(List<LanguagesList> data) {
        this.data = data;
    }
}
