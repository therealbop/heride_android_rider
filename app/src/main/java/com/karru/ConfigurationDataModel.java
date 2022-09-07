package com.karru;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

/**
 * <h2>ConfigurationDataModel</h2>
 * <p>
 * Pojo class to parse retrieved app config data from application class
 * </p>
 *
 * @author embed on 30/6/17.
 */
class ConfigurationDataModel implements Serializable {
  /*"stripePublishKey":"pk_test_IBYk0hnidox7CDA3doY6KQGi",
"customerGooglePlaceKeys":[
"AIzaSyAaC6n3v0fzqbNrT5blAgd3NiEUPwccevA",
"AIzaSyDL2wgiwROpc6VIct4JpsU_lLmDnjC83X8"
],
"customerApiInterval":8,
"shipmentLaterBookingAppBuffer":5400,
"rideLaterBookingAppBuffer":19800,
"customerEmergencyContactLimit":10
}
*/
  @SerializedName("appVersion")
  @Expose
  private String appVersion;
  @SerializedName("isMandatoryUpdateEnable")
  @Expose
  private Boolean isMandatoryUpdateEnable;
  @SerializedName("stripePublishKey")
  @Expose
  private String stripePublishKey;
  @SerializedName("customerApiInterval")
  @Expose
  private Integer customerApiInterval;
  @SerializedName("shipmentLaterBookingAppBuffer")
  @Expose
  private Integer shipmentLaterBookingAppBuffer;
  @SerializedName("rideLaterBookingAppBuffer")
  @Expose
  private Integer rideLaterBookingAppBuffer;
  @SerializedName("customerEmergencyContactLimit")
  @Expose
  private Integer customerEmergencyContactLimit;
  @SerializedName("isTwillioCallingEnable")
  @Expose
  private Boolean isTwillioCallingEnable;
  @SerializedName("customerGooglePlaceKeys")
  @Expose
  private List<String> customerGooglePlaceKeys = null;
  @SerializedName("isChatModuleEnable")
  @Expose
  private Boolean isChatModuleEnable;
  @SerializedName("isPromoCodeEnable")
  @Expose
  private Boolean isPromoCodeEnable;
  @SerializedName("isBookingForSomeoneElseEnable")
  @Expose
  private Boolean isBookingForSomeoneElseEnable;
  @SerializedName("isDriverPreferencesEnable")
  @Expose
  private Boolean isDriverPreferencesEnable;
  @SerializedName("isMultipleBookingsEnable")
  @Expose
  private Boolean isMultipleBookingsEnable;
  @SerializedName("isReferralCodeEnable")
  @Expose
  private Boolean isReferralCodeEnable;

  @SerializedName("isHelpCenterEnable")
  @Expose
  private Boolean isHelpCenterEnable;

  @Expose
  @SerializedName("googleMapKey")
  private String googleMapKey;

  /*  @SerializedName("refferalShareCodeTextEnable")
    @Expose
    private Boolean refferalShareCodeTextEnable;*/
  @SerializedName("weightMetrics")
  @Expose
  private List<String> weightMetrics = null;

  public String getAppVersion() {
    return appVersion;
  }

  public void setAppVersion(String appVersion) {
    this.appVersion = appVersion;
  }

  public Boolean isMandatoryUpdateEnable() {
    return isMandatoryUpdateEnable;
  }

  public void setIsMandatoryUpdateEnable(Boolean isMandatoryUpdateEnable) {
    this.isMandatoryUpdateEnable = isMandatoryUpdateEnable;
  }

  public Boolean getHelpCenterEnable() {
    return isHelpCenterEnable;
  }

  public void setHelpCenterEnable(Boolean helpCenterEnable) {
    isHelpCenterEnable = helpCenterEnable;
  }

  public String getGoogleMapKey() {
    return googleMapKey;
  }

  public String getStripePublishKey() {
    return stripePublishKey;
  }

  public void setStripePublishKey(String stripePublishKey) {
    this.stripePublishKey = stripePublishKey;
  }

  public Integer getCustomerApiInterval() {
    return customerApiInterval;
  }

  public void setCustomerApiInterval(Integer customerApiInterval) {
    this.customerApiInterval = customerApiInterval;
  }

  public Integer getShipmentLaterBookingAppBuffer() {
    return shipmentLaterBookingAppBuffer;
  }

  public void setShipmentLaterBookingAppBuffer(Integer shipmentLaterBookingAppBuffer) {
    this.shipmentLaterBookingAppBuffer = shipmentLaterBookingAppBuffer;
  }

  public Integer getRideLaterBookingAppBuffer() {
    return rideLaterBookingAppBuffer;
  }

  public void setRideLaterBookingAppBuffer(Integer rideLaterBookingAppBuffer) {
    this.rideLaterBookingAppBuffer = rideLaterBookingAppBuffer;
  }

  public Integer getCustomerEmergencyContactLimit() {
    return customerEmergencyContactLimit;
  }

  public void setCustomerEmergencyContactLimit(Integer customerEmergencyContactLimit) {
    this.customerEmergencyContactLimit = customerEmergencyContactLimit;
  }

  public Boolean isTwillioCallingEnable() {
    return isTwillioCallingEnable;
  }

  public void setIsTwillioCallingEnable(Boolean isTwillioCallingEnable) {
    this.isTwillioCallingEnable = isTwillioCallingEnable;
  }

  public List<String> getCustomerGooglePlaceKeys() {
    return customerGooglePlaceKeys;
  }

  public void setCustomerGooglePlaceKeys(List<String> customerGooglePlaceKeys) {
    this.customerGooglePlaceKeys = customerGooglePlaceKeys;
  }

  public Boolean isChatModuleEnable() {
    return isChatModuleEnable;
  }

  public void setIsChatModuleEnable(Boolean isChatModuleEnable) {
    this.isChatModuleEnable = isChatModuleEnable;
  }

  public Boolean getIsPromoCodeEnable() {
    return isPromoCodeEnable;
  }

  public void setIsPromoCodeEnable(Boolean isPromoCodeEnable) {
    this.isPromoCodeEnable = isPromoCodeEnable;
  }

  public Boolean getIsBookingForSomeoneElseEnable() {
    return isBookingForSomeoneElseEnable;
  }

  public void setIsBookingForSomeoneElseEnable(Boolean isBookingForSomeoneElseEnable) {
    this.isBookingForSomeoneElseEnable = isBookingForSomeoneElseEnable;
  }

  public Boolean getIsDriverPreferencesEnable() {
    return isDriverPreferencesEnable;
  }

  public void setIsDriverPreferencesEnable(Boolean isDriverPreferencesEnable) {
    this.isDriverPreferencesEnable = isDriverPreferencesEnable;
  }

  public Boolean getIsMultipleBookingsEnable() {
    return isMultipleBookingsEnable;
  }

  public void setIsMultipleBookingsEnable(Boolean isMultipleBookingsEnable) {
    this.isMultipleBookingsEnable = isMultipleBookingsEnable;
  }

  public Boolean isReferralCodeEnable() {
    return isReferralCodeEnable;
  }

  public void setRefferalShareCodeTextEnable(Boolean refferalShareCodeTextEnable) {
    this.isReferralCodeEnable = refferalShareCodeTextEnable;
  }

  public List<String> getWeightMetrics() {
    return weightMetrics;
  }

  public void setWeightMetrics(List<String> weightMetrics) {
    this.weightMetrics = weightMetrics;
  }
}
