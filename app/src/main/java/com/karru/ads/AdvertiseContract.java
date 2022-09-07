package com.karru.ads;

import android.os.Bundle;

import org.json.JSONArray;

/**
 * <h1>AdvertiseContract</h1>
 * This class is used to add the link between presenter and view
 * @author 3Embed
 * @since on 25-01-2018.
 */
public interface AdvertiseContract
{
    interface View
    {
        /**
         * <h2>showToast</h2>
         * used to show toast
         * @param message message to be show
         */
        void showToast(String message);

        /**
         * <h2>hideAdScreen</h2>
         * used to hide the ads screen
         */
        void hideAdScreen();

        /**
         * <h2>openBrowser</h2>
         * used to open browser for know more link
         */
        void openBrowser();
    }

    interface Presenter
    {
        /**
         * <h2>disposeObservable</h2>
         * used to dispose the observables
         */
        void disposeObservable();

        /**
         * <h2>updateNotificationStatus</h2>
         * used to update the status of notification
         */
        void updateNotificationStatus(int status,String messageId,String knowMoreUrl,boolean hide);

        void checkRTLConversion();
    }
}

