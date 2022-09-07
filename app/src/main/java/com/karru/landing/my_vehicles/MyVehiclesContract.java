package com.karru.landing.my_vehicles;

import android.os.Bundle;

public interface MyVehiclesContract
{
    interface View
    {
        /**
         * <h2>showToast</h2
         * used to show the toast>
         * @param string string to be shown
         */
        void showToast(String string);

        /**
         * <h2>showVehiclesList</h2>
         * used to show all list of vehicles
         */
        void showVehiclesList();

        /**
         * <h2>showDeleteVehicleAlert</h2>
         * used to show delete alert
         */
        void showDeleteVehicleAlert(int position);

        /**
         * <h2>showNoVehicles</h2>
         * used to show no vehicles text
         */
        void showNoVehicles();
        /**
         * <h2>showProgressDialog</h2>
         * This method is used to show the progress dialog
         */
        void showProgressDialog();
        /**
         * <h2>dismissProgressDialog</h2>
         * This method is used to hide the progress dialog
         */
        void dismissProgressDialog();

        /**
         * <h2>setDefaultVehicle</h2>
         * used to set the default vehicle
         * @param bundle bundle
         */
        void setDefaultVehicle(String bundle);
    }

    interface Presenter
    {
        /**
         * <h2>disposeObservable</h2>
         * used to dispose
         */
        void disposeObservable();

        /**
         * <h2>fetchReferralCode</h2>
         * to fetch the vehicles
         */
        void fetchMyVehicles();

        /**
         * <h2>deleteVehicle</h2>
         * to delete the vehicles
         */
        void deleteVehicle(int position);
        void addVehicle(String vehicleData);

        /**
         * <h2>handleDefaultVehicle</h2>
         * used to handle default
         * @param myVehiclesDataModel id
         */
        void handleDefaultVehicle(MyVehiclesDataModel myVehiclesDataModel,int parent);

        void checkRTLConversion();
    }
}
