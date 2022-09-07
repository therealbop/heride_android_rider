package com.karru.splash.first;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h2>LanguagesList</h2>
 * used to store the languages list
 */
public class LanguagesList implements Serializable
{
    /*"_id":"5a81ac90f9a60e164d192602",
"languageId":5,
"name":"arabic",
"code":"ar",
"active":1,
"langDirection":true*/

    public LanguagesList(String code, String name, int isRTL)
    {
        this.code = code;
        this.name = name;
        this.langDirection = isRTL;
    }

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("langDirection")
    @Expose
    private int langDirection;
    @SerializedName("code")
    @Expose
    private String code;

    public String getName() {
        return name;
    }

    public int getLangDirection() {
        return langDirection;
    }

    public void setLangDirection(int langDirection) {
        this.langDirection = langDirection;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
