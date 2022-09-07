package com.karru.data.source.local.shared_preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karru.landing.home.model.HotelDataModel;
import com.karru.landing.home.model.WalletDataModel;
import com.karru.landing.my_vehicles.MyVehiclesDataModel;
import com.karru.landing.payment.model.CardDetails;
import com.karru.splash.first.LanguagesList;
import com.karru.utility.Utility;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import static com.karru.utility.Constants.CARD;

/**
 * <h>PreferencesHelper</h>
 * <p>
 * Class to create shared preferences
 * </p>
 *
 * @since 13/5/15.
 */
public class PreferencesHelper implements PreferenceHelperDataSource
{
    private static final String PREF_NAME = "iDeliver_AndroidPref";
    private static final String FCM_TOKEN = "getRegistrationId";
    private static final String IS_LOGIN = "is_LOGIN";
    private static final String CURR_LAT = "curr_latitude";
    private static final String CURR_LONG = "curr_longitude";
    private static final String PASSWORD = "Password";
    private static final String CUST_PUBNUB_INTERVAL = "customerApiInterval";
    private static final String LATER_BUFFER_TIME = "laterBookingTimeInterval";
    private static final String STRIPE_KEY = "stripeKey";
    private static final String VEHICLE_DETAILS_RESPONSE = "vehcile_details_response";
    private static final String DROP_ADDRESS = "drop_adr";
    private static final String DROP_LATITUDE = "dropLatitude";
    private static final String DROP_LONGITUDE = "dropLongitude";
    private static final String DEFAULT_VEHICLE = "default_vehicle";
    private static final String GOOGLE_SERVER_KEYS = "google_server_keys";
    private static final String FCM_TOPICS = "facm_topics";
    private static final Type GOOGLE_SERVER_KEYS_TYPE = new TypeToken<List<String>>() {
    }.getType();
    private static final Type FCM_TOPICS_KEYS_TYPE = new TypeToken<List<String>>() {
    }.getType();
    private static final String GOOGLE_SERVER_KEY = "google_server_key";
    private static final String SOMEONE_BOOKING_RANGE = "some_one_booking_range";
    private static final String HOTEL_EXISTS = "hotel_exists";
    private static final String HOTEL_NAME = "hotel_name";
    private static final String TIME_STAMP = "time_stamp";
    private static final String EMERGENCY_ENABLE = "emergency_enable";
    private static final String WALLET_SETTINGS = "WALLET_SETTINGS";
    private static final String LANGUAGE_SETTINGS = "language_SETTINGS";
    private static final String PAYMENT_METHOD = "payment_method";
    private static final String EMERGENCY_LIMIT = "emergency_limit";
    private static final String DEFAULT_CARD = "default_card";
    private static final String HOTEL_DATA = "hotel_data";
    private static final String BUSINESS_TYPE = "business_type";
    private static final String PUSH_TOPICS = "pushTopic";
    private static final String USER_EMAIL = "EMail";
    private static final String CITY_ID = "city_id";
    private static final String OTP = "OTP";
    private static final String PICK_UP_ADDRESS = "pick_up_addr";
    private static final String PICK_UP_LAT = "pickLt";
    private static final String PICK_UP_LONG = "pickLg";
    private static final String VEHICLE_ID = "DeliveredId";
    private static final String VEHICLE_NAME = "VehicleName";
    private static final String VEHICLE_IMAGE = "VehicleUrl";
    private static final String BOOKING_TYPE = "ApptType";
    private static final String MQTT_TOPIC = "mqtt_topic";
    private static final String FCM_TOPIC = "fcm_topic";
    private static final String CASH_ENABLE = "cash_enable";
    private static final String TOWING_ENABLE = "towing_enable";
    private static final String CARD_ENABLE = "card_enable";
    private static final String CORPORATE_ENABLE = "corporate_enable";
    private static final String REQUESTER_ID = "requester_id";
    private static final String FAV_DRIVER_ENABLE = "fav_driver_enable";
    private static final String TWILIO_CALL_ENABLE = "twilio_call_enable";
    private static final String IS_REFERRAL_ENABLE = "isReferralCodeEnable";
    private static final String CHAT_ENABLE = "isChatEnable";
    private static final String HELP_ENABLE = "isHelpEnable";
    private static final String LOGIN_TYPE = "login_type";
    private static final String TAG = "PreferencesHelper";
    private static final String CUST_EMAIL = "customer_email";
    private static final String CUST_IMAGE = "getImageUrl";
    private static String USER_NAME = "getUsername";
    private static String MOBILE_NUMBER = "MobileNo";
    private static String SID = "SID";
    // Shared Preferences
    private SharedPreferences pref;
    // Editor for Shared preferences
    private SharedPreferences.Editor editor;
    private ArrayList<String> googleServerKeys = new ArrayList<>();
    private ArrayList<String> fcmTopics = new ArrayList<>();
    private Gson gson;


    // Constructor
    @Inject
    PreferencesHelper(Context context) {
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
        gson = new Gson();
    }

    /**
     * This method is used to getting the currency.
     *
     * @return, returns currency.
     */
    public String getCurrency() {
        return pref.getString("currency", "");
    }

    /**
     * This method is used to store the currency
     *
     * @param currency, containing currency.
     */
    public void setCurrency(String currency) {
        editor.putString("currency", currency);
        editor.commit();
    }

    @Override
    public void setFCMRegistrationId(String regId) {
        if (regId != null)
        {
            Utility.printLog(TAG+" push token if "+regId);
            editor.putString(FCM_TOKEN, regId);
            editor.commit();
        }
        else {
            Utility.printLog(TAG+" push token else "+regId);
            FirebaseInstanceIdService firebaseInstanceIdService=new FirebaseInstanceIdService();
            firebaseInstanceIdService.onTokenRefresh();
        }
    }

    @Override
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    @Override
    public void setIsLogin(boolean loggedIn) {
        editor.putBoolean(IS_LOGIN, loggedIn);
        editor.commit();
    }

    @Override
    public void setCurrLatitude(String latitude) {
        editor.putString(CURR_LAT, latitude);
        editor.commit();
    }

    @Override
    public String getCurrLatitude() {
        return pref.getString(CURR_LAT, "");
    }

    @Override
    public void setCurrLongitude(String longitude) {
        editor.putString(CURR_LONG, longitude);
        editor.commit();
    }

    @Override
    public String getCurrLongitude() {
        return pref.getString(CURR_LONG, "");
    }

    @Override
    public String getPassword() {
        return pref.getString(PASSWORD, "");
    }

    @Override
    public void setPassword(String Password) {
        editor.putString(PASSWORD, Password);
        editor.commit();
    }

    @Override
    public void setCustomerEmail(String customerEmail) {
        editor.putString(CUST_EMAIL, customerEmail);
        editor.commit();
    }

    @Override
    public String getCustomerEmail() {
        return pref.getString(CUST_EMAIL, "");
    }

    @Override
    public String getImageUrl() {
        return pref.getString(CUST_IMAGE, "");
    }

    @Override
    public void setImageUrl(String got_ImageUrl) {
        editor.putString(CUST_IMAGE, got_ImageUrl);
        editor.commit();
    }

    @Override
    public String getUsername() {
        return pref.getString(USER_NAME, "");
    }
    @Override
    public void setUsername(String got_Username) {
        editor.putString(USER_NAME, got_Username);
        editor.commit();
    }

    @Override
    public int getLoginType() {
        return pref.getInt(LOGIN_TYPE, 0);
    }
    @Override
    public void setLoginType(int loginType) {
        editor.putInt(LOGIN_TYPE, loginType);
        editor.commit();
    }

    @Override
    public String getMobileNo() {
        return pref.getString(MOBILE_NUMBER, "");
    }

    @Override
    public void setMobileNo(String MobileNo) {
        editor.putString(MOBILE_NUMBER, MobileNo);
        editor.commit();
    }

    @Override
    public String getSid() {
        return pref.getString(SID, "");
    }

    @Override
    public void setSid(String sid) {
        editor.putString(SID, sid);
        editor.commit();
    }

    @Override
    public long getCustomerApiInterval() {
        return pref.getLong(CUST_PUBNUB_INTERVAL, 10);
    }

    @Override
    public void setCustomerApiInterval(long customerApiInterval) {
        editor.putLong(CUST_PUBNUB_INTERVAL, customerApiInterval);
        editor.commit();
    }

    @Override
    public long getLaterBookingBufferTime() {
        return pref.getLong(LATER_BUFFER_TIME, 0);
    }
    @Override
    public void setLaterBookingBufferTime(long laterBookingTimeInterval) {
        editor.putLong(LATER_BUFFER_TIME, laterBookingTimeInterval);
        editor.commit();
    }

    @Override
    public String getStripeKey() {
        return pref.getString(STRIPE_KEY, "");
    }
    @Override
    public void setStripeKey(String stripeKey)
    {
        editor.putString(STRIPE_KEY, stripeKey);
        editor.commit();
    }

    @Override
    public void setGoogleServerKeys(List<String> distanceMatrixKeys)
    {
        this.googleServerKeys = new ArrayList<>(distanceMatrixKeys);
        String jsonString = gson.toJson(this.googleServerKeys);
        editor.putString(GOOGLE_SERVER_KEYS, jsonString);
        editor.commit();
    }
    @Override
    public ArrayList<String> getGoogleServerKeys()
    {
        String jsonString;
        if (googleServerKeys == null) {
            googleServerKeys = new ArrayList<>();
        }
        jsonString = pref.getString(GOOGLE_SERVER_KEYS, "");
        googleServerKeys = new Gson().fromJson(jsonString, GOOGLE_SERVER_KEYS_TYPE);
        return googleServerKeys;
    }

    @Override
    public void setFcmTopics(ArrayList<String> fcmTopics)
    {
        this.fcmTopics = fcmTopics;
        String jsonString = gson.toJson(this.fcmTopics);
        editor.putString(FCM_TOPICS, jsonString);
        editor.commit();
    }
    @Override
    public ArrayList<String> getFcmTopics()
    {
        String jsonString;
        if (fcmTopics == null)
        {
            fcmTopics = new ArrayList<>();
        }
        jsonString = pref.getString(FCM_TOPICS, "");
        fcmTopics = new Gson().fromJson(jsonString, FCM_TOPICS_KEYS_TYPE);
        return fcmTopics;
    }

    @Override
    public void setGoogleServerKey(String googleServerKey) {
        editor.putString(GOOGLE_SERVER_KEY, googleServerKey);
        editor.commit();
    }
    @Override
    public String getGoogleServerKey() {
        return pref.getString(GOOGLE_SERVER_KEY, "");
    }

    @Override
    public CardDetails getDefaultCardDetails()
    {
        String jsonString = pref.getString(DEFAULT_CARD, null);
        return new Gson().fromJson(jsonString, CardDetails.class);
    }
    @Override
    public void setDefaultCardDetails(CardDetails defaultCardDetails)
    {
        if (defaultCardDetails != null)
        {
            String jsonString = new Gson().toJson(defaultCardDetails);
            editor.putString(DEFAULT_CARD, jsonString);
            editor.commit();
        }
        else
        {
            editor.putString(DEFAULT_CARD, null);
            editor.commit();
        }
    }

    @Override
    public HotelDataModel getHotelDataModel()
    {
        String jsonString = pref.getString(HOTEL_DATA, null);
        return new Gson().fromJson(jsonString, HotelDataModel.class);
    }
    @Override
    public void setHotelDataModel(HotelDataModel defaultCardDetails)
    {
        if (defaultCardDetails != null)
        {
            String jsonString = new Gson().toJson(defaultCardDetails);
            editor.putString(HOTEL_DATA, jsonString);
            editor.commit();
        }
        else
        {
            editor.putString(HOTEL_DATA, null);
            editor.commit();
        }
    }

    @Override
    public ArrayList<String> getPushTopics()
    {
        String pushTopics = pref.getString("PUSH_TOPICS","");
        Log.d("SessionMgr", "getPushTopics() pushTopic: "+pushTopics);
        if(pushTopics != null && !pushTopics.isEmpty())
        {
            pushTopics = pushTopics.substring(1,pushTopics.length()-1);
            pushTopics = pushTopics.replaceAll(" ","");
            Log.d("SessionMgr", "getPushTopics() pushTopic: inside "+pushTopics);
            return new ArrayList<>(Arrays.asList(pushTopics.split(",")));
        }
        else
        {
            return new ArrayList<>();
        }
    }
    @Override
    public void setPushTopics(ArrayList<String> pushTopics) {
        editor.putString(PUSH_TOPICS, String.valueOf(pushTopics));
        editor.commit();
    }

    @Override
    public String getUserEmail() {
        return pref.getString(USER_EMAIL, "");
    }
    @Override
    public void setUserEmail(String EMail) {
        editor.putString(USER_EMAIL, EMail);
        editor.commit();
    }

    @Override
    public String getCityId() {
        return pref.getString(CITY_ID, "");
    }
    @Override
    public void setCityId(String EMail) {
        editor.putString(CITY_ID, EMail);
        editor.commit();
    }

    @Override
    public String getOTP() {return pref.getString(OTP, "1111");}
    @Override
    public void setOTP(String otp)
    {
        editor.putString(OTP, otp);
        editor.commit();
    }

    @Override
    public String getPickUpAddress() {
        return pref.getString(PICK_UP_ADDRESS, "");
    }
    @Override
    public void setPickUpAddress(String pick_up_addr) {
        editor.putString(PICK_UP_ADDRESS, pick_up_addr);
        editor.commit();
    }

    @Override
    public String getPickUpLatitude() {
        return pref.getString(PICK_UP_LAT, "");
    }
    @Override
    public void setPickUpLatitude(String pickLt) {
        editor.putString(PICK_UP_LAT, pickLt);
        editor.commit();
    }

    @Override
    public String getPickUpLongitude() {
        return pref.getString(PICK_UP_LONG, "");
    }
    @Override
    public void setPickUpLongitude(String pickLg) {
        editor.putString(PICK_UP_LONG, pickLg);
        editor.commit();
    }

    @Override
    public String getVehicleID() {
        return pref.getString(VEHICLE_ID, "");
    }
    @Override
    public void setVehicleID(String vehicleID) {
        editor.putString(VEHICLE_ID, vehicleID);
        editor.commit();
    }

    @Override
    public String getVehicleName() {
        return pref.getString(VEHICLE_NAME, "");
    }
    @Override
    public void setVehicleName(String VehicleName) {
        editor.putString(VEHICLE_NAME, VehicleName);
        editor.commit();
    }

    @Override
    public String getVehicleImage() {
        return pref.getString(VEHICLE_IMAGE, "");
    }
    @Override
    public void setVehicleImage(String VehicleUrl) {
        editor.putString(VEHICLE_IMAGE, VehicleUrl);
        editor.commit();
    }

    @Override
    public int getBookingType() {
        return pref.getInt(BOOKING_TYPE, 1);
    }
    @Override
    public void setBookingType(int bookingType) {
        editor.putInt(BOOKING_TYPE, bookingType);
        editor.commit();
    }

    @Override
    public void setMqttTopic(String mqttTopic) {
        editor.putString(MQTT_TOPIC, mqttTopic);
        editor.commit();
    }
    @Override
    public String getMqttTopic()
    {
        return pref.getString(MQTT_TOPIC, "0");
    }

    @Override
    public void setFCMTopic(String FCMTopic) {
        editor.putString(FCM_TOPIC, FCMTopic);
        editor.commit();
    }

    @Override
    public String getFCMTopic()
    {
        return pref.getString(FCM_TOPIC, "");
    }

    @Override
    public void setBusinessType(int businessType) {
        editor.putInt(BUSINESS_TYPE, businessType);
        editor.commit();
    }
    @Override
    public int getBusinessType()
    {
        return pref.getInt(BUSINESS_TYPE, 2);
    }

    @Override
    public void setVehicleDetailsResponse(String vehicleDetailsResponse) {
        editor.putString(VEHICLE_DETAILS_RESPONSE, vehicleDetailsResponse);
        editor.commit();
    }
    @Override
    public String getVehicleDetailsResponse()
    {
        return pref.getString(VEHICLE_DETAILS_RESPONSE, "");
    }

    @Override
    public String getDropAddress() {
        return pref.getString(DROP_ADDRESS, "");
    }

    @Override
    public void setDropAddress(String dropAddress) {
        editor.putString(DROP_ADDRESS, dropAddress);
        editor.commit();
    }

    @Override
    public void setDropLatitude(String dropLatitude) {
        editor.putString(DROP_LATITUDE, dropLatitude);
        editor.commit();
    }
    @Override
    public String getDropLatitude() {
        return pref.getString(DROP_LATITUDE, "");
    }

    @Override
    public void setDropLongitude(String dropLongitude) {
        editor.putString(DROP_LONGITUDE, dropLongitude);
        editor.commit();
    }
    @Override
    public String getDropLongitude() {
        return pref.getString(DROP_LONGITUDE, "");
    }

    @Override
    public void setCashEnable(boolean enabled) {
        editor.putBoolean(CASH_ENABLE, enabled);
        editor.commit();
    }
    @Override
    public boolean isCashEnable() {
        return pref.getBoolean(CASH_ENABLE, false);
    }

    @Override
    public void setTowingEnable(boolean enabled) {
        editor.putBoolean(TOWING_ENABLE, enabled);
        editor.commit();
    }
    @Override
    public boolean isTowingEnable() {
        return pref.getBoolean(TOWING_ENABLE, false);
    }

    @Override
    public void setCardEnable(boolean enabled) {
        editor.putBoolean(CARD_ENABLE, enabled);
        editor.commit();
    }

    @Override
    public boolean isCardEnable() {
        return pref.getBoolean(CARD_ENABLE, false);
    }

    @Override
    public void setCorporateEnable(boolean enabled) {
        editor.putBoolean(CORPORATE_ENABLE, enabled);
        editor.commit();
    }

    @Override
    public boolean isCorporateEnable() {
        return pref.getBoolean(CORPORATE_ENABLE, false);
    }

    @Override
    public void setSomeoneBookingRange(float range) {
        editor.putFloat(SOMEONE_BOOKING_RANGE, range);
        editor.commit();
    }

    @Override
    public void setCurrentTimeStamp(float range) {
        editor.putFloat(TIME_STAMP, range);
        editor.commit();
    }

    @Override
    public float getCurrentTimeStamp() {
        return pref.getFloat(TIME_STAMP, 0);
    }

    @Override
    public float getSomeoneBookingRange() {
        return pref.getFloat(SOMEONE_BOOKING_RANGE, 0);
    }

    @Override
    public void setEmergencyContactEnable(boolean enabled) {
        editor.putBoolean(EMERGENCY_ENABLE, enabled);
        editor.commit();
    }

    @Override
    public boolean isHotelExists() {
        return pref.getBoolean(HOTEL_EXISTS,false);
    }
    @Override
    public void setHotelExists(boolean enabled) {
        editor.putBoolean(HOTEL_EXISTS, enabled);
        editor.commit();
    }

    @Override
    public String getHotelName() {
        return pref.getString(HOTEL_NAME,"");
    }
    @Override
    public void setHotelName(String enabled) {
        editor.putString(HOTEL_NAME, enabled);
        editor.commit();
    }

    @Override
    public boolean isEmergencyContactEnable() {
        return pref.getBoolean(EMERGENCY_ENABLE, false);
    }

    @Override
    public void setFavDriverEnable(boolean enabled) {
        editor.putBoolean(FAV_DRIVER_ENABLE, enabled);
        editor.commit();
    }

    @Override
    public boolean isFavDriverEnable()
    {
        return pref.getBoolean(FAV_DRIVER_ENABLE, false);
    }

    @Override
    public void setTWILIOCallEnable(boolean enabled) {
        editor.putBoolean(TWILIO_CALL_ENABLE, enabled);
        editor.commit();
    }
    @Override
    public boolean isTWILIOCallEnable()
    {
        return pref.getBoolean(TWILIO_CALL_ENABLE, false);
    }

    @Override
    public void setReferralCodeEnabled(boolean enabled) {
        editor.putBoolean(IS_REFERRAL_ENABLE, enabled);
        editor.commit();
    }
    @Override
    public boolean isReferralCodeEnabled()
    {
        return pref.getBoolean(IS_REFERRAL_ENABLE, false);
    }

    @Override
    public void setChatModuleEnable(boolean enabled) {
        editor.putBoolean(CHAT_ENABLE, enabled);
        editor.commit();
    }
    @Override
    public boolean isChatModuleEnable()
    {
        return pref.getBoolean(CHAT_ENABLE, false);
    }

    @Override
    public void setHelpModuleEnable(boolean enabled) {
        editor.putBoolean(HELP_ENABLE, enabled);
        editor.commit();
    }

    @Override
    public boolean isHelpModuleEnable()
    {
        return pref.getBoolean(HELP_ENABLE, false);
    }

    @Override
    public void setDefaultPaymentMethod(int paymentMethod)
    {
        editor.putInt(PAYMENT_METHOD, paymentMethod);
        editor.commit();
    }

    public int getDefaultPaymentMethod()
    {
        return pref.getInt(PAYMENT_METHOD, CARD);
    }

    @Override
    public void setRequesterId(String requesterId) {
        editor.putString(REQUESTER_ID, requesterId);
        editor.commit();
    }

    @Override
    public String getRequesterId() {
        return pref.getString(REQUESTER_ID, "");
    }

    @Override
    public void setEmergencyContactLimit(int paymentMethod)
    {
        editor.putInt(EMERGENCY_LIMIT, paymentMethod);
        editor.commit();
    }

    @Override
    public int getEmergencyContactLimit()
    {
        return pref.getInt(EMERGENCY_LIMIT, 0);
    }

    @Override
    public WalletDataModel getWalletSettings()
    {
        String jsonString = pref.getString(WALLET_SETTINGS, "");
        return new Gson().fromJson(jsonString, WalletDataModel.class);
    }
    @Override
    public void setWalletSettings(WalletDataModel walletDataModel)
    {
        if (walletDataModel != null)
        {
            String jsonString = new Gson().toJson(walletDataModel);
            editor.putString(WALLET_SETTINGS, jsonString);
            editor.commit();
        }
        else
        {
            editor.putString(WALLET_SETTINGS, "");
            editor.commit();
        }
    }

    @Override
    public LanguagesList getLanguageSettings()
    {
        String jsonString = pref.getString(LANGUAGE_SETTINGS, "");
        return new Gson().fromJson(jsonString, LanguagesList.class);
    }
    @Override
    public void setLanguageSettings(LanguagesList languageSettings)
    {
        if (languageSettings != null)
        {
            String jsonString = new Gson().toJson(languageSettings);
            editor.putString(LANGUAGE_SETTINGS, jsonString);
            editor.commit();
        }
        else
        {
            editor.putString(LANGUAGE_SETTINGS, "");
            editor.commit();
        }
    }

    @Override
    public MyVehiclesDataModel getDefaultVehicle()
    {
        String jsonString = pref.getString(DEFAULT_VEHICLE, "");
        return new Gson().fromJson(jsonString, MyVehiclesDataModel.class);
    }
    @Override
    public void setDefaultVehicle(MyVehiclesDataModel defaultVehicle)
    {
        if (defaultVehicle != null)
        {
            String jsonString = new Gson().toJson(defaultVehicle);
            editor.putString(DEFAULT_VEHICLE, jsonString);
            editor.commit();
        }
        else
        {
            editor.putString(DEFAULT_VEHICLE, "");
            editor.commit();
        }
    }
}

