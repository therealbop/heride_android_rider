package com.karru.util;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessaging;
import com.karru.ApplicationClass;
import com.karru.data.source.local.sqlite.SQLiteDataSource;
import com.karru.managers.user_vehicles.MQTTManager;
import com.karru.splash.first.SplashActivity;
import com.karru.data.source.local.shared_preference.PreferenceHelperDataSource;

/**
 * <h1>ExpireSession</h1>
 * This class is used to expire the session and refresh the APP
 * @author  3Embed
 * @since on 24-11-2017.
 */

public class ExpireSession
{
    private static final String TAG = "ExpireSession";

    public static void refreshApplication(Context context , MQTTManager mqttManager,
                                          PreferenceHelperDataSource preferenceHelperDataSource,
                                          SQLiteDataSource sqLiteDataSource)
    {
        preferenceHelperDataSource.setIsLogin(false);
        preferenceHelperDataSource.setImageUrl("");
        preferenceHelperDataSource.setDefaultCardDetails(null);
        if(mqttManager.isMQTTConnected())
        {
            mqttManager.unSubscribeToTopic(preferenceHelperDataSource.getMqttTopic());
            mqttManager.disconnect();
            com.karru.utility.Utility.printLog(TAG+" TEST MQTT in disconnect expire sesson ");
        }
        Intent intent = new Intent(context, SplashActivity.class);
        ((ApplicationClass)context.getApplicationContext()).removeAccount(preferenceHelperDataSource.getSid());

        if(!preferenceHelperDataSource.getFCMTopic().equals(""))
            FirebaseMessaging.getInstance().unsubscribeFromTopic(preferenceHelperDataSource.getFCMTopic());

        if(preferenceHelperDataSource.getFcmTopics() != null)
            for(String fcmTopic : preferenceHelperDataSource.getFcmTopics())
                FirebaseMessaging.getInstance().unsubscribeFromTopic(fcmTopic);
        preferenceHelperDataSource.setFcmTopics(null);

        sqLiteDataSource.deleteRecentAddressTable();
        sqLiteDataSource.deleteCardDetailsTable();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
