package com.karru.landing.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class HotelDataModel implements Serializable
{
    /*"hotelUserType":1,
"hotelLogo":"https:\/\/s3.amazonaws.com\/uberfortruck\/Hotel\/Logo\/file201926115156.png",
"hotelName":"sichi",*/
    @SerializedName("hotelUserType")
    @Expose
    private int hotelUserType=1;
    @SerializedName("hotelLogo")
    @Expose
    private String hotelLogo;
    @SerializedName("hotelName")
    @Expose
    private String hotelName;
    @SerializedName("partnerUserId")
    @Expose
    private String partnerUserId;

    public HotelDataModel(int hotelUserType, String hotelLogo, String hotelName,String partnerUserId) {
        this.hotelUserType = hotelUserType;
        this.partnerUserId = partnerUserId;
        this.hotelLogo = hotelLogo;
        this.hotelName = hotelName;
    }

    public String getPartnerUserId() {
        return partnerUserId;
    }

    public int getHotelUserType() {
        return hotelUserType;
    }

    public void setHotelUserType(int hotelUserType) {
        this.hotelUserType = hotelUserType;
    }

    public String getHotelLogo() {
        return hotelLogo;
    }

    public void setHotelLogo(String hotelLogo) {
        this.hotelLogo = hotelLogo;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }
}
