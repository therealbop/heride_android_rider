package com.karru.util;

import android.content.Context;
import android.location.Address;
import android.net.Uri;

import com.google.firebase.messaging.FirebaseMessaging;
import com.karru.ApplicationClass;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.landing.home.model.HotelDataModel;
import com.karru.landing.payment.model.CardDetails;
import com.karru.managers.user_vehicles.MQTTManager;
import com.karru.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * <h21>DataParser</h1>
 * This class is used to parse the data
 * @author  3Embed
 * @since on 18-12-2017.
 */

public class DataParser
{
    /**
     * <h2>fetchErrorMessage</h2>
     * This method is used to fetch the error response message from json
     * @param jsonStringResponse Json response to be parsed
     * @return returns the error body message
     */
    public static String fetchErrorMessage(Response<ResponseBody> jsonStringResponse)
    {
        try
        {
            String responseToBeParsed = jsonStringResponse.errorBody().string();
            JSONObject jsonResponse = new JSONObject(responseToBeParsed);
            return jsonResponse.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * <h2>fetchErrorMessage</h2>
     * This method is used to fetch the error response message from json
     * @param jsonStringResponse Json response to be parsed
     * @return returns the error body message
     */
    public static String fetchErrorMessage1(String jsonStringResponse)
    {
        try
        {
            JSONObject jsonResponse = new JSONObject(jsonStringResponse);
            return jsonResponse.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * <h2>fetchSuccessMessage</h2>
     * This method is used to fetch the success message from json
     * @param jsonStringResponse Json response to be parsed
     * @return returns the success response body message
     */
    public static String fetchSuccessMessage(Response<ResponseBody> jsonStringResponse)
    {
        try
        {
            String responseToBeParsed = jsonStringResponse.body().string();
            JSONObject jsonResponse = new JSONObject(responseToBeParsed);
            Utility.printLog(" response from add card "+responseToBeParsed);
            return jsonResponse.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * <h2>fetchSidFromData</h2>
     * This method is used to parse the sid from response
     * @param jsonStringResponse Json response
     * @return String response for sid
     */
    public static String[] fetchSidFromData(Response<ResponseBody> jsonStringResponse)
    {
        String[] signupData = new String[2];
        try {
            String responseToBeParsed = jsonStringResponse.body().string();
            JSONObject jsonResponse = new JSONObject(responseToBeParsed);
            Utility.printLog("SignUpData"+responseToBeParsed);
            JSONObject dataObject= jsonResponse.getJSONObject("data");
            signupData[0] = dataObject.getString("sid");
            signupData[1] = dataObject.getString("expireOtp");
            return signupData;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return signupData;
    }

    /**
     * <h2>fetchStringArrayFromData</h2>
     * This method is used to get cancellation reasons from response
     * @param jsonStringResponse Json response
     * @return cancellation reasons array
     */
    public static JSONArray fetchStringArrayFromData(Response<ResponseBody> jsonStringResponse)
    {
        try {
            String responseToBeParsed = jsonStringResponse.body().string();
            JSONObject jsonResponse = new JSONObject(responseToBeParsed);
            return jsonResponse.getJSONArray("data");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    /**
     * <h2>fetchIDFromData</h2>
     * This method is used to parse the id from response
     * @param jsonStringResponse Json response
     * @return String response for id
     */
    public static String fetchIDFromData(Response<ResponseBody> jsonStringResponse)
    {
        try {
            String responseToBeParsed = jsonStringResponse.body().string();
            JSONObject jsonResponse = new JSONObject(responseToBeParsed);
            JSONObject dataObject= jsonResponse.getJSONObject("data");
            return dataObject.getString("id");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * <h2>storeUserData</h2>
     * This method is used to fetch the data for the signUp
     * @param jsonStringResponse json response
     * @param password Password of the user
     * @param preferenceHelperDataSource preference helper interface
     */
    public static void storeUserData(Response<ResponseBody> jsonStringResponse, Context mContext,
                                     String password, PreferenceHelperDataSource preferenceHelperDataSource,
                                     SQLiteDataSource sqLiteDataSource, MQTTManager mqttManager)
    {
        try
        {
            preferenceHelperDataSource.setIsLogin(true);
            JSONObject object = new JSONObject(jsonStringResponse.body().string());
            Utility.printLog("login response "+object.toString());
            JSONObject signUpData = object.getJSONObject("data");
            ((ApplicationClass)mContext.getApplicationContext()).setAuthToken(signUpData.getString("sid"),
                    password, signUpData.getString("token"));
            preferenceHelperDataSource.setUsername(signUpData.getString("firstName"));
            preferenceHelperDataSource.setUserEmail(signUpData.getString("email"));
            if(signUpData.has("profilePic"))
                preferenceHelperDataSource.setImageUrl(signUpData.getString("profilePic"));
            preferenceHelperDataSource.setSid(signUpData.getString("sid"));
            mqttManager.createMQttConnection(new com.karru.util.Utility().getDeviceId(mContext)+"_"+
                    preferenceHelperDataSource.getSid());
            preferenceHelperDataSource.setMobileNo(signUpData.getString("countryCode") +
                    signUpData.getString("phone"));
            preferenceHelperDataSource.setMqttTopic(signUpData.getString("mqttTopic"));
            preferenceHelperDataSource.setFCMTopic(signUpData.getString("fcmTopic"));
            preferenceHelperDataSource.setRequesterId(signUpData.getString("requester_id"));
            if(signUpData.has("loginType"))
            {
                preferenceHelperDataSource.setLoginType(signUpData.getInt("loginType"));
                preferenceHelperDataSource.setHotelDataModel(new HotelDataModel(signUpData.getInt("hotelUserType"),
                        signUpData.getString("hotelLogo"),signUpData.getString("hotelName"),
                        signUpData.getString("partnerUserId")));
            }
            else
                preferenceHelperDataSource.setLoginType(0);

            FirebaseMessaging.getInstance().subscribeToTopic(preferenceHelperDataSource.getFCMTopic());

            ArrayList<CardDetails> cardDetailsDataModels =  new ArrayList<>();
            if(signUpData.has("cards"))
            {
                JSONArray jsonArray = signUpData.getJSONArray("cards");

                if(jsonArray.length()>0)
                {
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        CardDetails cardDetailsDataModel = new CardDetails();
                        JSONObject cardDetails = (JSONObject) jsonArray.get(i);
                        cardDetailsDataModel.setLast4(cardDetails.getString("last4"));
                        cardDetailsDataModel.setExpYear(cardDetails.getString("expYear"));
                        cardDetailsDataModel.setExpMonth(cardDetails.getString("expMonth"));
                        cardDetailsDataModel.setId(cardDetails.getString("id"));
                        cardDetailsDataModel.setBrand(cardDetails.getString("brand"));
                        cardDetailsDataModel.setDefault(cardDetails.getBoolean("isDefault"));
                        cardDetailsDataModels.add(cardDetailsDataModel);

                        if (cardDetailsDataModel.getDefault())
                            preferenceHelperDataSource.setDefaultCardDetails(cardDetailsDataModel);
                    }
                }
                else
                    preferenceHelperDataSource.setDefaultCardDetails(null);
            }
            sqLiteDataSource.insertAllCardsStored(cardDetailsDataModels);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * <h2>fetchDataObjectString</h2>
     * This method is used to fetch the data object from response
     * @param jsonStringResponse response from API
     * @return returns the data string
     */
    public static String fetchDataObjectString(Response<ResponseBody> jsonStringResponse)
    {
        JSONObject object;
        try
        {
            String response = jsonStringResponse.body().string();
            Utility.printLog("data string "+response);
            object = new JSONObject(response);
            return object.getJSONObject("data").toString();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <h2>fetchSuccessResponse</h2>
     * This method is used to fetch the success data response
     * @param jsonStringResponse response from API
     * @return returns the data string
     */
    public static String fetchSuccessResponse(Response<ResponseBody> jsonStringResponse)
    {
        JSONObject object;
        try
        {
            String responseSuccess = jsonStringResponse.body().string();
            Utility.printLog( " getFavAddressApi success: "+responseSuccess);
            object = new JSONObject(responseSuccess);
            return object.toString();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <h1>stringToJsonAndPublish</h1>
     * <p>
     *    This method is used to convert our string into json file and then publish on amazon.
     *    </p>
     * @param fileName contains the name of file.
     * @param uri contains the uri.
     * @return the json object.
     */
    public static JSONObject stringToJsonAndPublish(String fileName, Uri uri) {
        JSONObject message = new JSONObject();
        try {
            message.put("type", "image");
            message.put("filename", fileName);
            message.put("uri", uri.toString());
            message.put("uploaded", "inprocess");
            message.put("confirm", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * <h2>getStringAddress</h2>
     * This method is used to get the address string from address object
     * @param address adress object
     * @return returns the address string
     */
    public static String getStringAddress(Address address)
    {
        String addressString = null;
        if(address.getAddressLine(0)!=null)
            addressString = address.getAddressLine(0);
        if (address.getAddressLine(1)!= null)
            addressString = addressString+","+address.getAddressLine(1);
        if (address.getAddressLine(2)!= null)
            addressString = addressString+","+address.getAddressLine(2);
        if (address.getAddressLine(3)!= null)
            addressString = addressString+","+address.getAddressLine(3);
        if (address.getAddressLine(4)!= null)
            addressString = addressString+","+address.getAddressLine(4);
        if (address.getAddressLine(5)!= null)
            addressString = addressString+","+address.getAddressLine(5);
        if (address.getAddressLine(6)!= null)
            addressString = addressString+","+address.getAddressLine(6);
        return addressString;
    }
}
