package com.karru.rental.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * This is a modal class for Car Type API
 * @author Anurag
 */

public class CarDataModel implements Serializable {
    @SerializedName("typeId")
    @Expose
    private String typeId;
    @SerializedName("typeName")
    @Expose
    private String typeName;
    @SerializedName("vehicleImgOn")
    @Expose
    private String vehicleImgOn;
    @SerializedName("vehicleImgOff")
    @Expose
    private String vehicleImgOff;
    @SerializedName("vehicleMapIcon")
    @Expose
    private String vehicleMapIcon;
    @SerializedName("rental")
    @Expose
    private Rental rental;
    @SerializedName("currencySymbol")
    @Expose
    private String currencySymbol;
    @SerializedName("currencyAbbr")
    @Expose
    private Integer currencyAbbr;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("typeDesc")
    @Expose
    private String typeDesc;
    @SerializedName("drivers")
    @Expose
    private List<CarDriver> carDriver = null;
    @SerializedName("eta")
    @Expose
    private String eta = "---";

    public void setEta(String eta) {
        this.eta = eta;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public String getTypeId() {
        return typeId;
    }

    public String getEta() {
        return eta;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getVehicleImgOn() {
        return vehicleImgOn;
    }

    public void setVehicleImgOn(String vehicleImgOn) {
        this.vehicleImgOn = vehicleImgOn;
    }

    public String getVehicleImgOff() {
        return vehicleImgOff;
    }

    public void setVehicleImgOff(String vehicleImgOff) {
        this.vehicleImgOff = vehicleImgOff;
    }

    public String getVehicleMapIcon() {
        return vehicleMapIcon;
    }

    public void setVehicleMapIcon(String vehicleMapIcon) {
        this.vehicleMapIcon = vehicleMapIcon;
    }

    public Rental getRental() {
        return rental;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public Integer getCurrencyAbbr() {
        return currencyAbbr;
    }

    public void setCurrencyAbbr(Integer currencyAbbr) {
        this.currencyAbbr = currencyAbbr;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<CarDriver> getDrivers() {
        return carDriver;
    }

    public void setDrivers(List<CarDriver> carDriver) {
        this.carDriver = carDriver;
    }

}
