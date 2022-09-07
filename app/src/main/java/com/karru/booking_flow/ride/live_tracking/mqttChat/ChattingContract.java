package com.karru.booking_flow.ride.live_tracking.mqttChat;

import android.content.Intent;

import com.karru.booking_flow.ride.live_tracking.mqttChat.model.ChatData;

import java.util.ArrayList;

/**
 * Created by DELL on 28-03-2018.
 */

public interface ChattingContract {

    interface ViewOperations
    {
        void setActionBar(String custName);

        void setViews(String bid);

        void setRecyclerView();

        void updateData(ArrayList<ChatData> chatDataArry);

        void showProgress();
        void hideProgress();

        /**
         * <h2>showDriverCancelDialog</h2>
         * show driver cancel dialog
         * @param message message to be shown
         */
        void showDriverCancelDialog(String message);
    }

    interface PresenterOperations {

        void getData(Intent intent);

        void setActionBar();

        void initViews();

        void getChattingData(int page);

        void message(String message);
        /**
         * <h2>subscribeDriverDetails</h2>
         * This method is used to subscribe to the driver details published
         */
        void subscribeDriverCancelDetails();
        /**
         * <h2>dispose</h2>
         * This method is used to dispose the observables
         */
        void dispose();
    }
}


