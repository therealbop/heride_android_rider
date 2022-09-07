package com.karru.booking_flow.location_from_map;


import com.google.android.gms.maps.model.LatLng;
import com.karru.managers.location.LocationCallBack;

public interface LocationFromMapContract
{
    interface LocationFromMapView
    {
        /**
         * <h>New Camera position setter</h>
         * <p>this method is using to set new location using Camera option</p>
         * @param newLatitude new latitude to focus
         * @param newLongitude new longitude to update
         */
        void moveGoogleMapToLocation(double newLatitude, double newLongitude);

        /**
         * <h>Update Camera position</h>
         * <p>this method is using to pudate camera position</p>
         * @param currentLat current latitude to focus
         * @param currentLng current longitude to update
         */
        void updateCameraPosition(Double currentLat, Double currentLng);

        /**
         * <h>New Address Value updater</h>
         * <p>thismethod is using to update new address in UI</p>
         * @param address new Address
         */
        void updateAddress(String address);

        /**
         * <h2>setStoredAddresses</h2>
         * use to store the address
         * @param pickAddress pick address
         * @param dropAddress drop address
         */
        void setStoredAddresses(String pickAddress,String dropAddress);
    }

    interface LocationFromMapPresenter extends LocationCallBack
    {
        /**
         * <h>Current Lat,Lng getter</h>
         * <p>this method is using to get current Location</p>
         */
        void getCurrentLocation();

        /**
         *
         */
        void onResumeActivity();

        /**
         *<h>Current Location getter</h>
         * <p>this method is using to find current location</p>
         */
        void findCurrentLocation();

        /**
         * <h>Address Updater</h>
         * <p>this method is using to update new Address</p>
         * @param centerFromPoint current lat,lng
         */
        void verifyAndUpdateNewLocation(LatLng centerFromPoint);

        /**
         *<h>Dispose Observable</h>
         * <h>this method is using to dispose the compositeDisposable observer</h>
         */
        void disposeObservable();

        /**
         * <h2>getStoredAddresses</h2>
         * used to get the stored addresses
         */
        void getStoredAddresses();

        /**
         * <h2>storePickAddress</h2>
         * use to store the pick address
         * @param pickAddress pick address
         */
        void storePickAddress(String pickAddress);

        /**
         * <h2>storeDropAddress</h2>
         * use to store the pick address
         * @param storeDropAddress drop address
         */
        void storeDropAddress(String storeDropAddress);

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

        void checkRTLConversion();
    }
}
