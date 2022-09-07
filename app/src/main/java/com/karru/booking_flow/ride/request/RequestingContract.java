package com.karru.booking_flow.ride.request;

import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.karru.booking_flow.ride.request.model.RequestBookingDetails;
import com.karru.managers.location.LocationCallBack;

/**
 * <h1>RequestingContract</h1>
 * This class is used to
 * @author 3Embed
 * @since on 24-01-2018.
 */
interface RequestingContract
{
    interface View extends LocationCallBack.View
    {
        /**
         * <h2>openLiveTrackingScreen</h2>
         * This method is used to open the live tracking page
         * @param bookingId booking ID to fetch the details
         * @param loginType
         */
         void openLiveTrackingScreen(String bookingId, int loginType);

         /**
         * <h2>openHotelScreen</h2>
         * This method is used to open the live tracking page
         */
         void openHotelScreen();

         /**
         * <h2>openScheduledBookingScreen</h2>
         * This method is used to open the scheduled booking page
         */
         void openScheduledBookingScreen(Bundle bundle);

        /**
         * <h2>setUIWithAddress</h2>
         * This method is used to show the address in UI
         * @param pickAddress pick address
         * @param dropAddress drop address
         */
         void setUIWithAddress(String pickAddress,String dropAddress,String vehicleImage,String vehicleName);

        /**
         * <h2>showToast</h2>
         * used to show the toast
         * @param message message to be shown
         */
         void showToast(String message);

        /**
         * <h2>setMaxBookingTime</h2>
         * This method is used to set the max timer for the timer
         * @param timeInSec time in sec
         */
        void setMaxBookingTime(int timeInSec);

        /**
         * <h2>setProgressTimer</h2>
         * This method is used to set the progress for the timer
         * @param progress progress
         */
        void setProgressTimer(int progress);

        /**
         * <h2>finishActivity</h2>
         * This method is used to finish the activity
         */
        void finishActivity();
        /**
         * <h2>showProgress</h2>
         * This method is used to show the progress dialog
         */
        void showProgress();
        /**
         * <h2>showProgress</h2>
         * This method is used to show the progress dialog
         */
        void dismissProgress();

        /**
         * <h2>moveMapToCurrentLocation</h2>
         * move to current lat long
         * @param latLng current location
         */
        void moveMapToCurrentLocation(LatLng latLng);

        /**
         * <h2>setUIWithAddress</h2>
         * used to set the address in the text view
         * @param address address got
         */
        void setUIWithAddress(String address);

        /**
         * <h2>showRetryUI</h2>
         * used to show the retry UI
         */
        void showRetryUI();

        /**
         * <h2>setBookingDetails</h2>
         * used to show booking ID and vehicle type name
         * @param bookingId booking iD
         * @param vehicleType vehicle type
         */
        void setBookingDetails(String bookingId,String vehicleType);

        /**
         * <h2>showTick</h2>
         * used to show the tick mark
         */
        void showTick(String textToBeShown);

        /**
         * <h2>enableRequestingScreen</h2>
         * used to enable the requesting screen
         */
        void enableRequestingScreen();

    }
    interface Presenter extends LocationCallBack
    {
        /**
         * <h2>bookingAPI</h2>
         * This method is used to call the booking API
         */
        void bookingAPI();

        /**
         * <h2>extractData</h2>
         * This method is used to extract the data
         * @param bundle bundle with the data
         */
        void extractData(Bundle bundle,RequestBookingDetails requestBookingDetails);

        /**
         * <h2>handleBackgroundState</h2>
         * This method is used to handle the background state
         */
        void handleBackgroundState();

        /**
         * <h2>cancelBooking</h2>
         * This method is used to cancel the booking
         */
        void cancelBooking();

        /**
         * <h2>startCurrLocation</h2>
         * This method is used to start the current location update
         */
        void startCurrLocation();

        /**
         * <h2>getCurrentLocation</h2>
         * This method is used to get the current location update
         */
        void getCurrentLocation();

        /**
         * <h2>getAddressFromLocation</h2>
         * used to get the address from lat long
         * @param latLng map location
         */
        void getAddressFromLocation(LatLng latLng);

        /**
         * <h2>subscribeLocationChange</h2>
         * This method is used to subscribe for the location change
         */
        void subscribeLocationChange();

        /**
         * <h2>subscribeLocationChange</h2>
         * This method is used to subscribe for the location change
         */
        void subscribeAddressChange();

        /**
         * <h2>fetchPickLocation</h2>
         * This method is used to fetch pick location
         */
        void fetchPickLocation();

        void checkRTLConversion();
    }
}
