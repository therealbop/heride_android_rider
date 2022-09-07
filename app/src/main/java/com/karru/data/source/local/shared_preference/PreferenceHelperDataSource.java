package com.karru.data.source.local.shared_preference;

import com.karru.landing.home.model.HotelDataModel;
import com.karru.landing.home.model.WalletDataModel;
import com.karru.landing.my_vehicles.MyVehiclesDataModel;
import com.karru.landing.my_vehicles.add_new_vehicle.MakesModelsDataModel;
import com.karru.landing.payment.model.CardDetails;
import com.karru.splash.first.LanguagesList;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>PreferenceHelperDataSource</h1>
 * This interface is used to store and retrieve the data
 * @author Akbar.
 * @since 07-11-2017
 */
public interface PreferenceHelperDataSource
{
    /**
     * <h2>.setFCMRegistrationId</h2>
     * This method is used to set the FCM push token
     * @param regId FCM push token
     */
    void setFCMRegistrationId(String regId);

    /**
     * <h2>setIsLogin</h2>
     * This method is used to set if the user is logged in
     * @param isLogin true if logged in else false
     */
    void setIsLogin(boolean isLogin);
    /**
     * <h2>isLoggedIn</h2>
     * This method is used to get if the user is logged in
     * @return  true if logged in else false
     */
    boolean isLoggedIn();

    /**
     * <h2>setCurrLatitude</h2>
     * This method is used to set the curr latitude
     */
    void setCurrLatitude(String latitude);

    /**
     * <h2>getCurrLatitude</h2>
     * This method is used to get the curr latitude
     */
    String getCurrLatitude();

    /**
     * <h2>setCurrLongitude</h2>
     * This method is used to set the curr longitude
     */
    void setCurrLongitude(String longitude);

    /**
     * <h2>getCurrLongitude</h2>
     * This method is used to get the curr longitude
     */
    String getCurrLongitude();

    /**
     * <h2>setPassword</h2>
     * This method is used to set the password
     * @param password password from the user
     */
    void setPassword(String password);
    /**
     * <h2>getPassword</h2>
     * This method is used to get the password
     */
    String getPassword();

    /**
     * <h2>setPassword</h2>
     * This method is used to set the customerEmail
     * @param customerEmail customerEmail for the user
     */
    void setCustomerEmail(String customerEmail);
    /**
     * <h2>getCustEmail</h2>
     * This method is used to get the getCustEmail
     */
    String getCustomerEmail();
    /**
     * <h2>setImageUrl</h2>
     * This method is used to set the getImageUrl
     * @param imageUrl getImageUrl of the user
     */
    void setImageUrl(String imageUrl);
    /**
     * <h2>getCustEmail</h2>
     * This method is used to get the getCustEmail
     */
    String getImageUrl();
    /**
     * <h2>setLoginType</h2>
     * This method is used to set the login type
     * @param loginType getImageUrl of the user
     *              loginType : 0 (Normal Login)
     *                          1 (Hotel Partner Login)
     *                          2 (Agent)
     */
    void setLoginType(int loginType);
    /**
     * <h2>getLoginType</h2>
     * This method is used to get the login type
     */
    int getLoginType();
    /**
     * <h2>setLoginType</h2>
     * This method is used to set the login type
     * @param defaultCardDetails getImageUrl of the user
     *              loginType : 0 (Normal Login)
     *                          1 (Hotel Partner Login)
     *                          2 (Agent)
     */
    void setHotelDataModel(HotelDataModel defaultCardDetails);
    /**
     * <h2>getLoginType</h2>
     * This method is used to get the login type
     */
    HotelDataModel getHotelDataModel();
    /**
     * <h2>setUsername</h2>
     * This method is used to set the getUsername
     * @param username getUsername of the user
     */
    void setUsername(String username);
    /**
     * <h2>getUserName</h2>
     * This method is used to get the getUsername
     */
    String getUsername();
    /**
     * <h2>setUsername</h2>
     * This method is used to set the mobileNo
     * @param mobileNo mobileNo of the user
     */
    void setMobileNo(String mobileNo);
    /**
     * <h2>getMobileNo</h2>
     * This method is used to get the mobileNo
     */
    String getMobileNo();
    /**
     * <h2>setUsername</h2>
     * This method is used to set the mobileNo
     * @param mobileNo mobileNo of the user
     */
    void setSid(String mobileNo);
    /**
     * <h2>getMobileNo</h2>
     * This method is used to get the mobileNo
     */
    String getSid();

    /**
     * <h2>setCustomerApiInterval</h2>
     * This method is used to set the pubnub interval
     * @param customerApiInterval customerApiInterval stored in  backend
     */
    void setCustomerApiInterval(long customerApiInterval);
    /**
     * <h2>getCustomerApiInterval</h2>
     * This method is used to get the customer pubnub Api Interval
     */
    long getCustomerApiInterval();
    /**
     * <h2>setLaterBookingBufferTime</h2>
     * This method is used to set the later booking buffer timer
     * @param laterBookingTimeInterval customerApiInterval buffer time for later booking
     */
    void setLaterBookingBufferTime(long laterBookingTimeInterval);
    /**
     * <h2>getLaterBookingBufferTime</h2>
     * This method is used to get the the later booking buffer timer
     */
    long getLaterBookingBufferTime();
    /**
     * <h2>setStripeKey</h2>
     * This method is used to set the stripeKey for Stripe payment
     * @param stripeKey stripeKey for Stripe payment
     */
    void setStripeKey(String stripeKey);
    /**
     * <h2>getStripeKey</h2>
     * This method is used to get the the stripeKey for Stripe payment
     */
    String getStripeKey();
    /**
     * <h2>setGoogleServerKeys</h2>
     * This method is used to set the googleServerKeys for distance matrix
     * @param distanceMatrixKeys<String> distanceMatrixKeys list for googleServerKeys for distance matrix
     */
    void setGoogleServerKeys(List<String> distanceMatrixKeys);

    /**
     * <h2>setFcmTopics</h2>
     * This method is used to set the fcm topics for push notifications
     * @param fcmTopics<String> fcm topics list for FCM pushes
     */
    void setFcmTopics(ArrayList<String>  fcmTopics);

    /**
     * <h2>getFcmTopics</h2>
     * This method is used to get FCM topics list for push notifications
     */
    ArrayList<String>  getFcmTopics();

    /**
     * <h2>getGoogleServerKeys</h2>
     * This method is used to get distanceMatrixKeys list for googleServerKeys for distance matrix
     */
    ArrayList<String> getGoogleServerKeys();
    /**
     * <h2>setGoogleServerKey</h2>
     * This method is used for setting the google server key .
     * @param googleServerKey googleServerKey for google server key .
     */
    void setGoogleServerKey(String googleServerKey);
    /**
     * <h2>getGoogleServerKey</h2>
     *  This method is used for getting the google server key .
     */
    String getGoogleServerKey();

    /**
     * <h2>setPushTopics</h2>
     * This method is used for setting push topics from admin
     * @param pushTopics pushTopics topics from FCM For push
     */
    void setPushTopics(ArrayList<String> pushTopics);
    /**
     * <h2>getPushTopics</h2>
     *  This method is used for getting push topics from admin
     */
    ArrayList<String> getPushTopics();
    /**
     * <h2>setUserEmail</h2>
     * This method is used for set user Email
     * @param email email user email Id
     */
    void setUserEmail(String email);
    /**
     * <h2>getUserEmail</h2>
     *  This method is used for getting email for the user
     */
    String getUserEmail();

    /**
     * <h2>setCityId</h2>
     * This method is used for set user Email
     * @param cityId email user email Id
     */
    void setCityId(String cityId);
    /**
     * <h2>getCityId</h2>
     *  This method is used for getting cityId for the user
     */
    String getCityId();

    /**
     * <h2>setOTP</h2>
     * This method is used to set the OTP
     * @param OTP OTP to be stored
     */
    void setOTP(String OTP);
    /**
     * <h2>getOTP</h2>
     * This method is used to get the OTP
     * @return  OTP to be retrieved
     */
    String getOTP();

    /**
     * <h2>setPickUpAddress</h2>
     * This method is used to set the pick up address
     * @param pickAddress pick up address to be stored
     */
    void setPickUpAddress(String pickAddress);
    /**
     * <h2>getPickUpAddress</h2>
     * This method is used to get the pick up address
     * @return  Pick upa address to be retrieved
     */
    String getPickUpAddress();

    /**
     * <h2>getPickUpLatitude</h2>
     * This method is used for setting the pickup latitude.
     * @param pickLat pickup latitude.
     */
    void setPickUpLatitude(String pickLat);
    /**
     * <h2>getPickUpLatitude</h2>
     * This method is used for getting the pick up latitude.
     * @return pickup latitude
     */
    String getPickUpLatitude();

    /**
     * <h2>setPickUpLongitude</h2>
     * This method is used for setting the pickup longitudes.
     * @param pickUpLongitude pickup longitudes.
     */
    void setPickUpLongitude(String pickUpLongitude);
    /**
     * <h2>getPickUpLongitude</h2>
     * This method is used for getting the pick up longitudes.
     * @return pickup longitude.
     */
    String getPickUpLongitude();

    /**
     * <h2>setVehicleID</h2>
     * This method is used for setting the DeliveredId.
     * @param vehicleID DeliveredId.
     */
    void setVehicleID(String vehicleID);
    /**
     * <h2>getVehicleID</h2>
     * This method is used for getting the DeliveredId.
     * @return DeliveredId.
     */
    String getVehicleID();

    /**
     * <h2>setVehicleName</h2>
     * This method is used for setting the VehicleName.
     * @param vehicleName VehicleName.
     */
    void setVehicleName(String vehicleName);
    /**
     * <h2>getVehicleName</h2>
     * This method is used for getting the VehicleName.
     * @return VehicleName.
     */
    String getVehicleName();

    /**
     * <h2>setVehicleImage</h2>
     * This method is used for setting the VehicleUrl.
     * @param vehicleImage vehicle image url.
     */
    void setVehicleImage(String vehicleImage);
    /**
     * <h2>getVehicleImage</h2>
     * This method is used for getting the VehicleUrl.
     * @return VehicleUrl.
     */
    String getVehicleImage();

    /**
     * <h2>setBookingType</h2>
     * This method is used for setting the ApptType.
     * @param bookingType bookingType.  //bookingType - 1 - for now, and 2 - for later.
     */
    void setBookingType(int bookingType);
    /**
     * <h2>getBookingType</h2>
     * This method is used for getting the ApptType.
     * @return ApptType.  //appt_type - 1 - for now, and 2 - for later.
     */
    int getBookingType();

    /**
     * <h2>setMqttTopic</h2>
     * This method is used for setting the mqtt topic to receive the events .
     * @param mqttTopic topic name to be subscribed to
     */
    void setMqttTopic(String mqttTopic);
    /**
     * <h2>getMqttTopic</h2>
     * This method is used for getting the mqtt topic to subscribe
     * @return language code .
     */
    String getMqttTopic();

    /**
     * <h2>setFCMTopic</h2>
     * This method is used for setting the FCM topic to receive the events .
     * @param FCMTopic topic name to be subscribed to
     */
    void setFCMTopic(String FCMTopic);
    /**
     * <h2>getFCMTopic</h2>
     * This method is used for getting the FCM topic to subscribe
     * @return FCM topic to be subscribed
     */
    String getFCMTopic();

    /**
     * <h2>setBusinessType</h2>
     * This method is used to set the business type
     * @param businessType business type
     */
    void setBusinessType(int businessType);
    /**
     * <h2>getBusinessType</h2>
     * This method is used to get the business type
     * @return  business type
     */
    int getBusinessType();

    /**
     * <h2>setBusinessType</h2>
     * This method is used to set the vehicle details response
     * @param vehicleDetailsResponse vehicle details response
     */
    void setVehicleDetailsResponse(String vehicleDetailsResponse);
    /**
     * <h2>getVehicleDetailsResponse</h2>
     * This method is used to get the vehicle details response
     * @return  vehicle details response
     */
    String getVehicleDetailsResponse();

    /**
     * <h2>getDropAddress</h2>
     * This method is used to getting the Drop address.
     * @return returns Drop address.
     */
    String getDropAddress() ;

    /**
     * <h2>setDropAddress</h2>
     * This method is used to store the Drop address
     * @param dropAddress, containing pick up address.
     */
    void setDropAddress(String dropAddress) ;

    /**
     * <h2>setDropLatitude</h2>
     * This method is used for setting the Drop latitude.
     * @param dropLatitude Drop latitude.
     */
    void setDropLatitude(String dropLatitude) ;
    /**
     * <h2>getDropLatitude</h2>
     * This method is used for getting the Drop latitude.
     * @return Drop latitude
     */
    String getDropLatitude() ;

    /**
     * <h2>setDropLongitude</h2>
     * This method is used for setting the Drop longitudes.
     * @param dropLongitude Drop longitudes.
     */
    void setDropLongitude(String dropLongitude) ;
    /**
     * <h2>getDropLongitude</h2>
     * This method is used for getting the Drop longitudes.
     * @return Drop longitude.
     */
    String getDropLongitude();

    /**
     * <h2>setCashEnable</h2>
     * method is used to set whether cash is enabled for the city
     * @param enabled enabled or disabled
     */
    void setCashEnable(boolean enabled);
    /**
     * <h2>getCashEnable</h2>
     * method is used to get whether cash is enabled for the city
     * @return:  boolean enabled or disabled
     */
    boolean isCashEnable();

    /**
     * <h2>setTowingEnable</h2>
     * method is used to set whether towing is enabled for the city
     * @param enabled enabled or disabled
     */
    void setTowingEnable(boolean enabled);
    /**
     * <h2>getCashEnable</h2>
     * method is used to get whether towing is enabled for the city
     * @return:  boolean enabled or disabled
     */
    boolean isTowingEnable();

    /**
     * <h2>setCardEnable</h2>
     * method is used to set whether card is enabled for the city
     * @param enabled enabled or disabled
     */
    void setCardEnable(boolean enabled);
    /**
     * <h2>getCardEnable</h2>
     * method is used to get whether cash is enabled for the city
     * @return: returns enabled or disabled
     */
    boolean isCardEnable();

    /**
     * <h2>setCorporateEnable</h2>
     * method is used to set whether Corporate is enabled for the city
     * @param enabled enabled or disabled
     */
    void setCorporateEnable(boolean enabled);
    /**
     * <h2>isCorporateEnable</h2>
     * method is used to get whether Corporate is enabled for the city
     * @return: returns enabled or disabled
     */
    boolean isCorporateEnable();

    /**
     * <h2>getWalletSettings</h2>
     * method is used to get wallet details
     * @return  wallet data
     */
    WalletDataModel getWalletSettings();

    /**
     * <h2>setWalletSettings</h2>
     * method is used to set wallet details
     * @param walletDataModel   wallet data
     */
    void setWalletSettings(WalletDataModel walletDataModel);

    /**
     * <h2>getLanguageSettings</h2>
     * method is used to get language details
     * @return  language data
     */
    LanguagesList getLanguageSettings();

    /**
     * <h2>setLanguageSettings</h2>
     * method is used to set language details
     * @param languageSettings   language data
     */
    void setLanguageSettings(LanguagesList languageSettings);

    /**
     * <h2>setDefaultVehicle</h2>
     * This method is used to set the default vehicle Id
     * @param defaultVehicle getImageUrl of the user
     */
    void setDefaultVehicle(MyVehiclesDataModel defaultVehicle);

    /**
     * <h2>getDefaultVehicle</h2>
     * This method is used to get the default vehicle Id
     */
    MyVehiclesDataModel getDefaultVehicle();

    /**
     * <h2>getDefaultCardDetails</h2>
     * used to get the default card details
     * @return returns the default card details
     */
    CardDetails getDefaultCardDetails();

    /**
     * <h2>setDefaultCardDetails</h2>
     * used to set the default card details
     * @param defaultCardDetails default card details model
     */
    void setDefaultCardDetails(CardDetails defaultCardDetails);

    /**
     * <h2>setSomeoneBookingRange</h2>
     * used to set the some one booking range
     * @param range range for some one
     */
    void setSomeoneBookingRange(float range) ;

    /**
     * <h2>getSomeoneBookingRange</h2>
     * used to get the some one booking range
     * @return range range for some one
     */
    float getSomeoneBookingRange() ;

    /**
     * <h2>setHotelExists</h2>
     * used to set the hotel exists
     * @param exists exists
     */
    void setHotelExists(boolean exists) ;

    /**
     * <h2>isHotelExists</h2>
     * used to get the the hotel exists
     * @return rexists exists
     */
    boolean isHotelExists() ;

    /**
     * <h2>setHotelExists</h2>
     * used to set the hotel name
     * @param name name
     */
    void setHotelName(String name) ;

    /**
     * <h2>getHotelName</h2>
     * used to get the the hotel name
     * @return exists exists
     */
    String getHotelName() ;

    /**
     * <h2>setCurrentTimeStamp</h2>
     * used to set the current time stamp
     * @param range current time stamp
     */
    void setCurrentTimeStamp(float range) ;

    /**
     * <h2>getSomeoneBookingRange</h2>
     * used to get the current time stamp
     * @return range current time stamp
     */
    float getCurrentTimeStamp() ;

    /**
     * <h2>setEmergencyContactEnable</h2>
     * method is used to set whether emergency contact is enabled for the city
     * @param enabled enabled or disabled
     */
    void setEmergencyContactEnable(boolean enabled);
    /**
     * <h2>isEmergencyContactEnable</h2>
     * method is used to get whether emergency contact is enabled for the city
     * @return  boolean enabled or disabled
     */
    boolean isEmergencyContactEnable();

    /**
     * <h2>setFavDriverEnable</h2>
     * method is used to set whether fav driver module is enabled for the city
     * @param enabled enabled or disabled
     */
    void setFavDriverEnable(boolean enabled);

    /**
     * <h2>isFavDriverEnable</h2>
     * method is used to get whether fav driver module is enabled for the city
     * @return  boolean enabled or disabled
     */
    boolean isFavDriverEnable();

    /**
     * <h2>setTWILIOCallEnable</h2>
     * method is used to set whether TWILLIO call enable
     * @param enabled enabled or disabled
     */
    void setTWILIOCallEnable(boolean enabled);

    /**
     * <h2>isTWILIOCallEnable</h2>
     * method is used to get whether TWILLIO call enable
     * @return  boolean enabled or disabled
     */
    boolean isTWILIOCallEnable();

    /**
     * <h2>setReferralCodeEnabled</h2>
     * method is used to set whether referral code enable
     * @param enabled enabled or disabled
     */
    void setReferralCodeEnabled(boolean enabled);

    /**
     * <h2>getReferralCodeEnabled</h2>
     * method is used to get whether referral code enable
     * @return  boolean enabled or disabled
     */
    boolean isReferralCodeEnabled();

    /**
     * <h2>setChatModuleEnable</h2>
     * method is used to set whether chat enable
     * @param enabled enabled or disabled
     */
    void setChatModuleEnable(boolean enabled);

    /**
     * <h2>setHelpModuleEnable</h2>
     * method is used to set whether chat enable
     * @param enabled enabled or disabled
     */
    void setHelpModuleEnable(boolean enabled);

    /**
     * <h2>isChatModuleEnable</h2>
     * method is used to get whether chat enable
     * @return  boolean enabled or disabled
     */
    boolean isChatModuleEnable();

    /**
     * <h2>isHelpModuleEnable</h2>
     * method is used to get whether chat enable
     * @return  boolean enabled or disabled
     */
    boolean isHelpModuleEnable();

    /**
     * <h2>setDefaultPaymentMethod</h2>
     * This method is used to set the default payment method
     * @param paymentMethod paymentMethod stored in  backend
     */
    void setDefaultPaymentMethod(int paymentMethod);
    /**
     * <h2>getDefaultPaymentMethod</h2>
     * This method is used to get the default payment method
     */
    int getDefaultPaymentMethod();

    /**
     * <h2>setEmergencyContactLimit</h2>
     * This method is used to set the emergency contact limit
     */
    void setEmergencyContactLimit(int paymentMethod);

    /**
     * <h2>getEmergencyContactLimit</h2>
     * This method is used to get the emergency contact limit
     */
    int getEmergencyContactLimit();

    /**
     * <h2>setRequesterId</h2>
     * used to set the requesterId for zen desk
     * @param requesterId  sets requesterId for zen desk
     */
    void setRequesterId(String requesterId);
    /**
     * <h2>getRequesterId</h2>
     * used to get the RequesterId
     * @return returns the RequesterId
     */
    String getRequesterId();
}
