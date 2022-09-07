package com.karru.authentication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>UserDetailsDataModel</h1>
 * This class is used to hold the user details
 * @author  3Embed
 * @since on 18-12-2017.
 */

public class UserDetailsDataModel implements Serializable
{
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("emailOrPhone")
    @Expose
    private String emailOrPhone;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("countryCode")
    @Expose
    private String countryCode;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("profilePic")
    @Expose
    private String profilePic ;
    @SerializedName("loginType")
    @Expose
    private int loginType ;
    @SerializedName("facebookId")
    @Expose
    private String facebookId ;
    @SerializedName("googleId")
    @Expose
    private String googleId ;
    @SerializedName("referralCode")
    @Expose
    private String referralCode ;
    @SerializedName("businessName")
    @Expose
    private String businessName ;
    @SerializedName("latitude")
    @Expose
    private String latitude = "" ;
    @SerializedName("longitude")
    @Expose
    private String longitude = "" ;
    @SerializedName("devType")
    @Expose
    private int devType  ;
    @SerializedName("deviceId")
    @Expose
    private String deviceId = "" ;
    @SerializedName("appVersion")
    @Expose
    private String appVersion = "" ;
    @SerializedName("devMake")
    @Expose
    private String devMake = "" ;
    @SerializedName("devModel")
    @Expose
    private String devModel = "" ;
    @SerializedName("billingAddress1")
    @Expose
    private String billingAddress1  ;
    @SerializedName("ipAddress")
    @Expose
    private String ipAddress;
    @SerializedName("isPartnerUser")
    @Expose
    private String isPartnerUser; // isPartnerUser 1 for partner ,0 for normal

    public String getIsPartnerUser() {
        return isPartnerUser;
    }

    public void setIsPartnerUser(String isPartnerUser) {
        this.isPartnerUser = isPartnerUser;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getEmailOrPhone()
    {
        return emailOrPhone;
    }

    public void setEmailOrPhone(String emailOrPhone) {
        this.emailOrPhone = emailOrPhone;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getDevType() {
        return devType;
    }

    public void setDevType(int devType) {
        this.devType = devType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDevMake() {
        return devMake;
    }

    public void setDevMake(String devMake) {
        this.devMake = devMake;
    }

    public String getDevModel() {
        return devModel;
    }

    public void setDevModel(String devModel) {
        this.devModel = devModel;
    }

    public String getBillingAddress1() {
        return billingAddress1;
    }

    public void setBillingAddress1(String billingAddress1) {
        this.billingAddress1 = billingAddress1;
    }
}
