package com.karru.booking_flow.scheduled_booking;

import android.os.Bundle;

import org.json.JSONArray;

/**
 * <h1>ScheduledBookingContract</h1>
 * This class is used to add the link between presenter and view
 * @author 3Embed
 * @since on 25-01-2018.
 */
public interface ScheduledBookingContract
{
    interface View
    {
        /**
         * <h2>setUpUI</h2>
         * used to set up the UI with booking details
         */
        void setUpUI(double pickLat,double pickLong,String... params);

        /**
         * <h2>showProgressDialog</h2>
         * This method is used to show the progress dialog
         */
        void showProgressDialog();
        /**
         * <h2>dismissProgressDialog</h2>
         * This method is used to dismiss the progress dialog
         */
        void dismissProgressDialog();

        /**
         * <h2>showPassengerCancelPopup</h2>
         * used to show the popup for passenger cancellation
         * @param message to be shown
         */
        void showPassengerCancelPopup(String message);

        /**
         * <h2>populateCancelReasonsDialog</h2>
         * used to populate dialog iwth reasons
         * @param reasons reasons array
         */
        void populateCancelReasonsDialog(JSONArray reasons);

        /**
         * <h2>enableCancelButton</h2>
         * enable cancel button for cancel dialog
         */
        void enableCancelButton(String reason);

        /**
         * <h2>onclickOfConfirmCancel</h2>
         * triggered when cancel button clicked from dialog
         */
        void onclickOfConfirmCancel();

        /**
         * <h2>finishActivity</h2>
         * to open main activity when passenger cancel the booking
         */
        void finishActivity(String bookingID);
        /**
         * <h2>openLiveTrackingScreen</h2>
         * This method is used to open the live tracking page
         * @param bookingId booking ID to fetch the details
         * @param loginType
         */
        void openLiveTrackingScreen(String bookingId, int loginType);

        /**
         * <h2>showToast</h2>
         * used to show toast
         * @param message message to be show
         */
        void showToast(String message);
    }

    interface Presenter
    {
        /**
         * <h2>extractData</h2>
         * used to extract the data
         * @param bundle bundle of data
         */
        void extractData(Bundle bundle);

        /**
         * <h2>getCancellationReasons</h2>
         * method is used to get cancellation reasons
         */
        void getCancellationReasons();

        /**
         * <h2>cancelBookingAPI</h2>
         * method is used to cancel the current booking
         * @param reason reason for cancellation
         */
        void cancelBookingAPI(String reason);

        /**
         * <h2>disposeObservable</h2>
         * used to dispose the observables
         */
        void disposeObservable();

        /**
         * <h2>bookingDetailsAPI</h2>
         * used to get the booking details API
         */
        void bookingDetailsAPI();

        void checkRTLConversion();
    }
}
