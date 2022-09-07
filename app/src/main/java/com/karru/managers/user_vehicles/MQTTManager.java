package com.karru.managers.user_vehicles;

import android.content.Context;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.reflect.TypeToken;
import com.karru.managers.network.NetworkStateHolder;
import com.karru.managers.network.RxNetworkObserver;
import com.karru.booking_flow.ride.live_tracking.mqttChat.model.ChatData;
import com.karru.booking_flow.ride.live_tracking.mqttChat.ChatDataObservable;
import com.heride.rider.R;
import com.google.gson.Gson;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;
import com.karru.landing.home.model.WalletDataModel;
import com.karru.managers.booking.BookingsManager;
import com.karru.landing.home.model.MQTTResponseDataModel;
import com.karru.landing.home.model.VehicleTypesDetails;
import com.karru.utility.Utility;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * <h1>MQTTManager</h1>
 * This class is used to handle the MQTT data
 * @author 3Embed
 * @since on 21-12-2017.
 */
public class MQTTManager
{
    private static final String TAG = "MQTTManager";
    private IMqttActionListener mMQTTListener;
    private MqttAndroidClient mqttAndroidClient;
    private MqttConnectOptions mqttConnectOptions;
    private Context mContext;
    private MQTTResponseDataModel mqttResponseDataModel;
    private Gson gson;
    private RxCurrentZoneObserver rxCurrentZoneObserver;
    private PreferenceHelperDataSource preferenceHelperDataSource;

    private RxVehicleDetailsObserver rxVehicleTypesObserver;
    private BookingsManager bookingsManager;
    private NetworkStateHolder networkStateHolder;
    private RxNetworkObserver rxNetworkObserver;
    private ChatDataObservable chatDataObservable;

    @Inject
    public MQTTManager(Context context, RxVehicleDetailsObserver rxVehicleTypesObserver,
                       PreferenceHelperDataSource preferenceHelperDataSource, Gson gson,
                       MQTTResponseDataModel mqttResponseDataModel, BookingsManager bookingsManager,
                       RxCurrentZoneObserver rxCurrentZoneObserver, NetworkStateHolder networkStateHolder,
                       RxNetworkObserver rxNetworkObserver,ChatDataObservable chatDataObservable)
    {
        mContext = context;
        this.gson= gson;
        this.rxNetworkObserver= rxNetworkObserver;
        this.networkStateHolder= networkStateHolder;
        this.chatDataObservable= chatDataObservable;
        this.rxVehicleTypesObserver=rxVehicleTypesObserver;
        this.bookingsManager = bookingsManager;
        this.preferenceHelperDataSource = preferenceHelperDataSource;
        this.rxCurrentZoneObserver = rxCurrentZoneObserver;
        this.mqttResponseDataModel = mqttResponseDataModel;

        mMQTTListener = new IMqttActionListener()
        {
            @Override
            public void onSuccess(IMqttToken asyncActionToken)
            {
                Utility.printLog(TAG+" TEST MQTT : myqtt client "+preferenceHelperDataSource.getMqttTopic());
                subscribeToTopic(preferenceHelperDataSource.getMqttTopic());
            }
            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception)
            {
                Utility.printLog(TAG+" MQTT onFailure "+exception);
            }
        };
    }

    /**
     * <h2>subscribeToTopic</h2>
     * This method is used to subscribe to the mqtt topic
     */
    public void subscribeToTopic(String mqttTopic)
    {
        try
        {
            Utility.printLog(TAG+" topic manager "+mqttTopic);
            if (mqttAndroidClient != null && preferenceHelperDataSource.isLoggedIn())
                mqttAndroidClient.subscribe(mqttTopic, 2);
        }
        catch (MqttException e)
        {
            Utility.printLog(TAG+" MqttException "+e);
            e.printStackTrace();
        }
    }

    /**
     * <h2>unSubscribeToTopic</h2>
     * This method is used to unSubscribe to topic already subscribed
     * @param topic Topic name from which to  unSubscribe
     */
    @SuppressWarnings("TryWithIdenticalCatches")
    public void unSubscribeToTopic(String topic)
    {
        try
        {
            if (mqttAndroidClient != null)
                mqttAndroidClient.unsubscribe(topic);
        }
        catch (MqttException e)
        {
            e.printStackTrace();
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * <h2>isMQTTConnected</h2>
     * This method is used to check whether MQTT is connected
     * @return boolean value whether MQTT is connected
     */
    public boolean isMQTTConnected() {
        return mqttAndroidClient != null && mqttAndroidClient.isConnected();
    }

    /**
     * <h2>createMQttConnection</h2>
     * This method is used to create the connection with MQTT
     * @param clientId customer ID to connect MQTT
     */
    @SuppressWarnings("unchecked")
    public void createMQttConnection(String clientId)
    {
        Utility.printLog(TAG+" createMQttConnection: "+clientId);
        String serverUri = "tcp://" + mContext.getString(R.string.MQTT_HOST) + ":" + mContext.getString(R.string.MQTT_PORT);
        mqttAndroidClient = new MqttAndroidClient(mContext, serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause)
            {
                Utility.printLog(TAG+" MQTT connectionLost "+cause);
            }
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception
            {
                Utility.printLog(TAG+" MQTT TESTING handleVehiclesData messageArrived: "+new String(message.getPayload()));
                Utility.printLog(TAG+" MQTT TESTING messageArrived topic: "+topic);
                JSONObject object = new JSONObject(new String(message.getPayload()));

                if(object.has("status"))
                {
                    switch (object.getInt("status"))
                    {
                        case 0:
                            handleVehiclesData(new String(message.getPayload()));
                            break;

                        case 16:
                            String response = new String(message.getPayload());
                            Utility.printLog("data string "+response);
                            ChatData chatData = gson.fromJson(response,ChatData.class);
                            Utility.printLog(TAG+" chat data "+chatData.getType()+" pro type ");
                            chatDataObservable.emitData(chatData);
                            break;

                        case 17:
                        Log.d("Anurag","Updated Key: "+object.getString("googleMapKey"));
                        preferenceHelperDataSource.setGoogleServerKey(object.getString("googleMapKey"));
                        break;

                        default:
                            bookingsManager.handleBookingsStatus(new String(message.getPayload()));
                            break;
                    }
                }
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Utility.printLog(TAG+" deliveryComplete: "+token);
            }
        });
        mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setKeepAliveInterval(60);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setUserName(mContext.getString(R.string.MQTT_USERNAME));
        mqttConnectOptions.setPassword(mContext.getString(R.string.MQTT_PASSWORD).toCharArray());

        connectMQTTClient(mContext);
    }

    /**
     * <h1>handleVehiclesData</h1>
     * <p>
     *     method to handle & parse config data received from pubnub
     *     and also update the views if it has been changed from
     * </p>
     */
    public void handleVehiclesData(String configDataNew)
    {
        preferenceHelperDataSource.setVehicleDetailsResponse(configDataNew);
        // to store time difference between last received msg and new received msg of a==2
        try
        {
            MQTTResponseDataModel mqttResponseData = gson.fromJson(configDataNew, MQTTResponseDataModel.class);

            if(mqttResponseData.getVehicleTypes().size()>0)
            {
                JSONObject mqttData = new JSONObject(configDataNew);
                JSONObject cityData = mqttData.getJSONObject("cityData");
                JSONObject walletData = mqttData.getJSONObject("walletData");
                JSONArray fcmTopics = mqttData.getJSONArray("fcmTopics");
                preferenceHelperDataSource.setHotelExists(false);//mqttData.getBoolean("isHotelExist"));
                preferenceHelperDataSource.setHotelName( mqttData.getString("hotelName"));
                preferenceHelperDataSource.setCashEnable(cityData.getBoolean("isCashEnable"));
                preferenceHelperDataSource.setTowingEnable(cityData.getBoolean("isTowingEnable"));
                preferenceHelperDataSource.setCityId(cityData.getString("cityId"));
                preferenceHelperDataSource.setCardEnable(cityData.getBoolean("isCardEnable"));
                preferenceHelperDataSource.setCorporateEnable(cityData.getBoolean("isCorporateEnable"));
                preferenceHelperDataSource.setFavDriverEnable(cityData.getBoolean("isFavoriteDriverEnable"));
                preferenceHelperDataSource.setEmergencyContactEnable(cityData.getBoolean("isEmergencyContactEnable"));
                preferenceHelperDataSource.setSomeoneBookingRange((float) cityData.getDouble("radiusforsomeOneElseBooking"));
                preferenceHelperDataSource.setCurrentTimeStamp((float) cityData.getDouble("gmtTime"));
                preferenceHelperDataSource.setWalletSettings(gson.fromJson(walletData.toString(), WalletDataModel.class));

                ArrayList<String> fcmTopicsList = new Gson().fromJson(fcmTopics.toString(), new TypeToken<List<String>>(){}.getType());
                Utility.printLog(TAG+" topics subscribes "+preferenceHelperDataSource.getFcmTopics());
                for(String fcmTopic : fcmTopicsList)
                {
                    if(preferenceHelperDataSource.getFcmTopics() == null)
                        FirebaseMessaging.getInstance().subscribeToTopic(fcmTopic);

                    else if(!preferenceHelperDataSource.getFcmTopics().contains(fcmTopic))
                        FirebaseMessaging.getInstance().subscribeToTopic(fcmTopic);
                }
                if(preferenceHelperDataSource.getFcmTopics() != null)
                {
                    for(String fcmTopic : preferenceHelperDataSource.getFcmTopics())
                    {
                        if(!fcmTopicsList.contains(fcmTopic))
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(fcmTopic);
                    }
                }
                preferenceHelperDataSource.setFcmTopics(fcmTopicsList);

                Utility.printLog(TAG+" FCM topics "+fcmTopics.length());
                Utility.printLog(TAG+" FCM topics pref "+preferenceHelperDataSource.getFcmTopics().size());

                if(mqttResponseDataModel.getVehicleTypes() != null &&
                        !mqttResponseDataModel.getVehicleTypes().toString().equals(mqttResponseData.getVehicleTypes().toString()))
                {
                    Utility.printLog(TAG+ " MQTT TESTING handleVehiclesData() VEHICLE TYPES NOT SAME " +preferenceHelperDataSource.getBusinessType());
                    MQTTResponseDataModel temp = gson.fromJson(configDataNew, MQTTResponseDataModel.class);
                    ArrayList<VehicleTypesDetails> vehicleTypesArrayList = new ArrayList<>();
                    for(int typeCount = 0; typeCount<temp.getVehicleTypes().size(); typeCount++)
                    {
                        switch (preferenceHelperDataSource.getBusinessType())
                        {
                            case 2:
                                switch (temp.getVehicleTypes().get(typeCount).getBusinessType())
                                {
                                    case 3:
                                    case 2:
                                        vehicleTypesArrayList.add(temp.getVehicleTypes().get(typeCount));
                                        break;
                                }
                                break;

                            case 1:
                                switch (temp.getVehicleTypes().get(typeCount).getBusinessType())
                                {
                                    case 3:
                                    case 1:
                                        vehicleTypesArrayList.add(temp.getVehicleTypes().get(typeCount));
                                        break;
                                }
                                break;
                        }
                    }
                    Utility.printLog(TAG+ " MQTT TESTING handleVehiclesData() VEHICLE TYPES  "
                            +vehicleTypesArrayList.toString());
                    mqttResponseDataModel.setVehicleTypes(vehicleTypesArrayList);
                    mqttResponseDataModel.setOngoingBookings(temp.getOngoingBookings());
                    mqttResponseDataModel.setAreaZone(temp.getAreaZone());

                    postNewVehicleTypes();
                }
            }
            else
                rxVehicleTypesObserver.publishVehicleTypes(new ArrayList<>());
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
            Log.d(TAG, " MQTT TESTING handleVehiclesData() exc: "+exc);
        }
    }

    /**
     * <h2>connectMQTTClient</h2>
     * This method is used to connect to MQTT client
     */
    private void connectMQTTClient(Context mContext)
    {
        try
        {
            Utility.printLog(TAG+" connectMQTTClient: ");
            mqttAndroidClient.connect(mqttConnectOptions, mContext, mMQTTListener);
        }
        catch (MqttException e)
        {
            Utility.printLog(TAG+" MqttException: "+e);
            e.printStackTrace();
        }
    }

    /**
     * <h2>disconnect</h2>
     * This method is used To disconnect the MQtt client
     */
    public void disconnect()
    {
        try
        {
            Utility.printLog(TAG+" TEST MQTT in disconnect ");
            if (mqttAndroidClient != null && mqttAndroidClient.isConnected())
            {
                mqttAndroidClient.disconnect();
            }
        } catch (MqttException e)
        {
            Utility.printLog(TAG+" TEST MQTT exception in disconnect "+e);
            e.printStackTrace();
        }
    }

    /**
     * <h2>updateNewVehicleTypesData</h2>
     * <p>
     *     method to parse handle and update the vehicles types in homescreen
     * </p>
     * @param mqttResponseDataModel vehicle details
     */
    public void updateNewVehicleTypesData(MQTTResponseDataModel mqttResponseDataModel)
    {
        Utility.printLog(TAG+" updateNewVehicleTypesData "+mqttResponseDataModel.getVehicleTypes().size());
        this.mqttResponseDataModel = mqttResponseDataModel;
        ArrayList<VehicleTypesDetails> vehicleTypesArrayList = new ArrayList<>();
        for(int typeCount = 0; typeCount<mqttResponseDataModel.getVehicleTypes().size(); typeCount++)
        {
            switch (preferenceHelperDataSource.getBusinessType())
            {
                case 2:
                    switch (mqttResponseDataModel.getVehicleTypes().get(typeCount).getBusinessType())
                    {
                        case 3:
                        case 2:
                            vehicleTypesArrayList.add(mqttResponseDataModel.getVehicleTypes().get(typeCount));
                            break;
                    }
                    break;

                case 1:
                    switch (mqttResponseDataModel.getVehicleTypes().get(typeCount).getBusinessType())
                    {
                        case 3:
                        case 1:
                            vehicleTypesArrayList.add(mqttResponseDataModel.getVehicleTypes().get(typeCount));
                            break;
                    }
                    break;
            }
        }
        this.mqttResponseDataModel.setVehicleTypes(vehicleTypesArrayList);
        this.mqttResponseDataModel.setOngoingBookings(mqttResponseDataModel.getOngoingBookings());
        this.mqttResponseDataModel.setAreaZone(mqttResponseDataModel.getAreaZone());
    }


    /**
     *<h2>postNewVehicleTypes</h2>
     * This method is used to publish the vehicle types
     */
    public void postNewVehicleTypes()
    {
        Utility.printLog(TAG+" MQTT TESTING postNewVehicleTypes posted ");
        if(mqttResponseDataModel == null)
            rxVehicleTypesObserver.publishVehicleTypes(new ArrayList<>());
        else
        {
            rxVehicleTypesObserver.publishVehicleTypes(mqttResponseDataModel.getVehicleTypes());
            if(!mqttResponseDataModel.getVehicleTypes().isEmpty())
            {
                bookingsManager.handleOnGoingBookings(mqttResponseDataModel.getOngoingBookings());
                Utility.printLog(TAG+"postNewVehicleTypes posted id "+ mqttResponseDataModel.getAreaZone().getId());
                rxCurrentZoneObserver.publishAreaZones(mqttResponseDataModel.getAreaZone());
            }
        }
    }
}
