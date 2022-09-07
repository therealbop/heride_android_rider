package com.karru.booking_flow.ride.live_tracking;

import android.os.Bundle;
import com.google.android.gms.maps.model.LatLng;
import com.karru.util.path_plot.LatLongBounds;

import org.json.JSONArray;

/**
 * <h1>LiveTrackingContract</h1>
 * This class is used to add the link between presenter and view
 * @author 3Embed
 * @since on 25-01-2018.
 */
public interface LiveTrackingContract
{
    interface View
    {
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
         * <h2>setBookingUI</h2>
         * This method is used to set the UI for live tracking page
         * @param rating rating
         *                                                                       @param pickUpLatLong pick up lat long object
         * @param dropLatLong drop lat long
         * @param driverLatLong driver lat long object
         * @param isRental
         * @param params params for the booking details
         */
        void setBookingUI(double rating, LatLng pickUpLatLong, LatLng dropLatLong, LatLng driverLatLong,
                          boolean isRental, String... params);

        /**
         * <h2>moveCarIcon</h2>
         * This method is used to move the drivre marker
         * @param driverLocation current driver location
         * @param bearing heading of the driver
         */
        void moveCarIcon(LatLng driverLocation,float bearing);

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
         * <h2>showDriverCancelDialog</h2>
         * show driver cancel dialog
         * @param message message to be shown
         */
        void showDriverCancelDialog(String message);

        /**
         * <h2>onclickOfConfirmCancel</h2>
         * triggered when cancel button clicked from dialog
         */
        void onclickOfConfirmCancel();

        /**
         * <h2>finishActivity</h2>
         * to open main activity when passenger cancel the booking
         */
        void finishActivity();

        /**
         * <h2>shareLink</h2>
         * used to share the eta link
         * @param trackingLink tracking link
         */
        void shareLink(String trackingLink);

        /**
         * <h2>callDriver</h2>
         * used to call the driver
         * @param phoneNumber phone number to be called
         */
        void callDriver(String phoneNumber,boolean isEnable,String driverProfilePic);

        /**
         * <h2>showAlertForAddressUpdate</h2>
         * used to show the drop address alert
         * @param message message to be shown
         */
        void showAlertForAddressUpdate(String message);

        /**
         * <h2>hideCancelOption</h2>
         * used to hide the cancel option
         */
        void hideCancelOption(int childs);

        /**
         * <h2>showToast</h2>
         * used to show toast
         * @param message message to be shown
         */
        void showToast(String message);

        /**
         * <h2>openChatScreen</h2>
         * to open chat screen
         * @param bid bid
         */
        void openChatScreen(String bid,String driverId,String driverName);

        /**
         * <h2>addLatLongBounds</h2>
         * This method is used to add the bounds to lat longs for source and destination
         */
        void pickLatLongBounds(LatLng driverLatLng);

        /**
         * <h2>addLatLongBounds</h2>
         * This method is used to add the bounds to lat longs for source and destination
         */
        void destLatLongBounds(LatLng driverLatLng);

        /**
         * <h2>googlePathPlot</h2>
         * used to plot the path
         * @param latLongBounds route configurations
         */
        void googlePathPlot(LatLongBounds latLongBounds);

        /**
         * <h2>hidePath</h2>
         * used to hide the path
         */
        void hidePath(String driverName);

        /**
         * <h2>enableChatModule</h2>
         * used to show the chat module button
         */
        void enableChatModule();

        /**
         * <h2>disableChatModule</h2>
         * used to hide the chat module button
         */
        void disableChatModule();
    }

    interface Presenter
    {
        /**
         * <h2>bookingDetailsAPI</h2>
         * This method is used to call the API for booking details
         * @param bookingId booking Id for the booking
         */
        void bookingDetailsAPI(String bookingId);

        /**
         * <h2>checkForLocationBounding</h2>
         * This method is used to check for the location bounds according to status
         */
        void checkForLocationBounding();

        /**
         * <h2>handleBackState</h2>
         * This method is used to handle the back state
         */
        void handleBackState();

        /**
         * <h2>confirmCancelBookingAPI</h2>
         * method is used to get if cancellation charges applicable for current booking
         */
        void confirmCancelBookingAPI();

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
         * <h2>handleTrackingShare</h2>
         * used to share the tracking link
         */
        void handleTrackingShare();

        /**
         * <h2>handleDriverCall</h2>
         * used to handle the click of driver call button
         */
        void handleDriverCall();

        /**
         * <h2>handleReturnIntent</h2>
         * used to handle the on activity result
         * @param requestCode request code
         * @param resultCode result code
         * @param bundle bundle
         */
        void handleReturnIntent(int requestCode, int resultCode, Bundle bundle);

        /**
         * <h2>checkForChat</h2>
         * used to check for chat
         */
        void checkForChat();

        /**
         * <h2>handleResume</h2>
         * used to hanle resume state
         */
        void handleResume();

        /**
         * <h2>plotPathRoute</h2>
         * used to plot the path between origin and destination
         * @param origin origin lat lng
         * @param dest dest lat lng
         */
        void plotPathRoute(LatLng origin,LatLng dest);

        void checkRTLConversion();
    }
}
