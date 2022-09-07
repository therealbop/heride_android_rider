package com.karru.managers.fcm;

import com.appsflyer.AppsFlyerLib;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.karru.utility.Utility;

/**
 * <h>FireBaseInstanceIDService</h>
 * This class is used as a Service which is used for handling FireBase Token Id .
 * @author 3embed
 * @since 6 Apr 2017.
 */
public class FireBaseInstanceIDService extends FirebaseInstanceIdService
{
    private static final String TAG="MyFireBaseInstanceID";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onTokenRefresh() {
        //Get updated InstanceId token.
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Utility.printLog(TAG + " Refreshed token: " + refreshToken);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

        /* Get updated InstanceID token. */
        FirebaseInstanceId instance = FirebaseInstanceId.getInstance();
        String refreshedToken;
        if (instance != null) {
            refreshedToken = instance.getToken();

            sendRegistrationToServer(refreshedToken); /* example for general use case / 3rd party SDK */
            /* pass the token to the AppsFlyer SDK */
            AppsFlyerLib.getInstance().updateServerUninstallToken(getApplicationContext(), refreshedToken);

        }
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token)
    {
    }
}
