package com.karru.landing.payment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>CardDetails</h1>
 * Used to hold the cards details
 * @author embed
 * @since on 25/11/15.
 */
public class CardDetails implements Serializable
{
    /*"name":"",
"last4":"1111",
"expYear":2050,
"expMonth":11,
"id":"card_1Bo4eP2876tVKl2MbXxLlIZy",
"brand":"Visa",
"funding":"unknown",
"isDefault":true*/
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("last4")
    @Expose
    private String last4;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("expMonth")
    @Expose
    private String expMonth;
    @SerializedName("funding")
    @Expose
    private String funding;
    @SerializedName("expYear")
    @Expose
    private String expYear;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("isDefault")
    @Expose
    private boolean isDefault;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setLast4(String last4) {
        this.last4 = last4;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public void setExpMonth(String expMonth) {
        this.expMonth = expMonth;
    }
    public void setExpYear(String expYear) {
        this.expYear = expYear;
    }
    public String getLast4() {
        return last4;
    }
    public String getBrand() {
        return brand;
    }
    public String getExpMonth() {
        return expMonth;
    }
    public String getExpYear() {
        return expYear;
    }
    public boolean getDefault() {
        return isDefault;
    }
    public void setDefault(boolean aDefault) {
        this.isDefault = aDefault;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
