package com.karru.splash.first;

import android.location.Location;

import com.karru.managers.location.LocationCallBack;

/**
 * <h1>SplashContract</h1>
 * This interface has the contract of presenter and view
 * @author  Akbar
 * @since 07-11-2017
 */
public interface SplashContract
{
    interface View extends LocationCallBack.View
    {
        /**
         * <h2>onGettingOfVehicleDetails</h2>
         * This method is triggered when we get the vehicle details
         */
        void onGettingOfVehicleDetails();

        /**
         * <h2>ifUserNotRegistered</h2>
         * This method is called is the user is not registered
         * <p>
         *     method to start login activity
         * </p>
         */
        void ifUserNotRegistered();

        /**
         * <h2>showToast</h2>
         * This method is used to show the toast
         */
        void showToast(String message);

        /**
         * <h2>startLocationService</h2>
         * used to start the location service
         */
        void startLocationService();

        /**
         * <h2>showAppUpdateAlert</h2>
         * This method is used to show the update alert
         * @param mandateUpdate flag whether the update is mandatory
         */
        void showAppUpdateAlert(boolean mandateUpdate);

        /**
         * <h2>showLanguagesDialog</h2>
         * used to show the languages dialog
         */
        void showLanguagesDialog(int indexSelected);

        /**
         * <h2>setLanguage</h2>
         * used to set the language
         * @param language language to be set
         */
        void setLanguage(String language,boolean restart);
    }

    interface Presenter extends LocationCallBack
    {
        /**
         * <h2>generateFCMPushToken</h2>
         * This method is triggered to generate the FCM push token
         */
        void generateFCMPushToken();

        /**
         * <h2>callAPIIfLocationUpdate</h2>
         * This method is triggered when curr location updates
         * @param location Location object
         */
        void callAPIIfLocationUpdate(Location location);

        /**
         * <h2>startLocationService</h2>
         * This method is used to start updating the location
         */
        void startLocationService();

        /**
         * <h2>stopLocationUpdate</h2>
         * This method is used to stop the location update
         */
        void stopLocationUpdate();

        /**
         * <h2>checkForUserLoggedIn</h2>
         * used to check if the user is logged in
         */
        void checkForUserLoggedIn();

        /**
         * <h2>locationDisposable</h2>
         * used to dispose observables
         */
        void disposeObservables();

        /**
         * <h2>getLanguages</h2>
         * This method is used to call the API to get all the languages
         */
        void getLanguages();

        /**
         * <h2>changeLanguage</h2
         * used to change the language>
         */
        void changeLanguage(String langCode,String langName,int isRTL);

        /**
         * <h2>checkForDirection</h2>
         * used to check the layout direction
         */
        int checkForDirection();

        void checkRTLConversion();
    }
}
