package com.karru.landing.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <h1>VehicleTypesDetails</h1>
 * This class is used to hold the vehicle types data
 * @author  embed
 * @since on 23/3/17.
 */
public class VehicleTypesDetails implements Serializable
{
    /*"typeId":"5a30d8e7f9a60e517273663d",
"typeName":"Sedan",
"vehicleDimension":"1.00Mx1.00Mx1.00M",
"vehicleCapacity":"100.00 Kg",
"isSpecialType":true,
"specialTypes":[
"5a30d8e7f9a60e517273663d",
"5a3e0d7cf9a60e049b2b2a9e"
],
"vehicleImgOn":"https://s3.amazonaws.com/uberforallv1/VehicleTypes/vehicleOnImages/file20171225162135.png",
"vehicleImgOff":"https://s3.amazonaws.com/uberforallv1/VehicleTypes/vehicleOffImages/file20171225162143.png",
"vehicleMapIcon":"https://s3.amazonaws.com/uberforallv1/VehicleTypes/vehicleMapImages/file20171225162148.png",
"bookingType":"1",
"ride":{},
"delivery":{},
"cityName":"Bengaluru"*/

    @SerializedName("typeId")
    @Expose
    private String typeId;
    @SerializedName("typeName")
    @Expose
    private String typeName;
    @SerializedName("vehicleDimension")
    @Expose
    private String vehicleDimension;
    @SerializedName("vehicleCapacity")
    @Expose
    private String vehicleCapacity;
    @SerializedName("vehicleImgOn")
    @Expose
    private String vehicleImgOn;
    @SerializedName("vehicleImgOff")
    @Expose
    private String vehicleImgOff;
    @SerializedName("vehicleMapIcon")
    @Expose
    private String vehicleMapIcon;
    @SerializedName("isSpecialType")
    @Expose
    private boolean isSpecialType;
    @SerializedName("specialTypes")
    @Expose
    private ArrayList<String> specialTypes;
    @SerializedName("ride")
    @Expose
    private VehicleTypeDetailsModel ride;
    @SerializedName("towTruck")
    @Expose
    private VehicleTypeDetailsModel towTruck;
    @SerializedName("delivery")
    @Expose
    private VehicleTypeDetailsModel delivery;
    @SerializedName("drivers")
    @Expose
    private ArrayList<DriverDetailsModel> drivers;
    @SerializedName("businessType")
    @Expose
    private int businessType;
    @SerializedName("isTowTruck")
    @Expose
    private boolean isTowTruck;
    @SerializedName("isRental")
    @Expose
    private boolean isRental;

    public boolean isRental() {
        return isRental;
    }
    public int getBusinessType() {
        return businessType;
    }
    public boolean isSpecialType() {
        return isSpecialType;
    }
    public ArrayList<String> getSpecialTypes() {
        return specialTypes;
    }
    public ArrayList<DriverDetailsModel> getDrivers() {
        return drivers;
    }
    public VehicleTypeDetailsModel getDelivery() {
        return delivery;
    }

    public VehicleTypeDetailsModel getTowTruck() {
        return towTruck;
    }

    public boolean isTowTruck() {
        return isTowTruck;
    }

    public VehicleTypeDetailsModel getRide() {return ride;}
    public String getTypeId() {
        return typeId;
    }
    public String getTypeName() {
        return typeName;
    }
    public String getVehicleDimension() {
        return vehicleDimension;
    }
    public String getVehicleCapacity() {
        return vehicleCapacity;
    }
    public String getVehicleImgOn() {
        return vehicleImgOn;
    }
    public String getVehicleImgOff() {
        return vehicleImgOff;
    }
    public String getVehicleMapIcon() {
        return vehicleMapIcon;
    }
}

