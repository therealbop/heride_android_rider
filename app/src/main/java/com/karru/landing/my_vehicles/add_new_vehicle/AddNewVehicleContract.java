package com.karru.landing.my_vehicles.add_new_vehicle;

import java.util.ArrayList;

public interface AddNewVehicleContract {
    interface View
    {
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
        void showToast(String message);

        /*
        * <h1>populateYearsSpinner</h1>
        * use to populate the year spinner*/
        void populateYearsSpinner(ArrayList<String> yearData);
        /*
        * <h1>populateMakeSpinner</h1>
        * use to populate the make spinner*/
        void populateMakesSpinner(ArrayList<String> yearData);
        /*
        * <h1>populateModelSpinner</h1>
        * use to populate the model spinner*/
        void populateModelsSpinner(ArrayList<String> yearData);

        /**
         * <h2>enableSaveOption</h2>
         * used top enable the save option
         */
        void enableSaveOption();

        /**
         * <h2>disableSaveOption</h2>
         * used top enable the save option
         */
        void disableSaveOption();
        void finishActivity(String response);
    }

    interface Presenter {
        /**
         * <h2>fetchVehicleDetailList</h2>
         * used to fetch year ,make,model
         * @param type 1 for year , 2 for make , 3 for model
         */
         void fetchVehicleDetailList(int type,int year,int make);

         /**
         * <h2>saveVehicleDetails</h2>
         * used to save vehicle details
         */
         void saveVehicleDetails();

        /**
         * <h2>validateSavingVehicle</h2>
         * used to validate the enable/disable of save
         * @param modelIndex model index
         * @param colorText color
         */
         void validateSavingVehicle(int yearIndex,int makeIndex,int modelIndex,String colorText);

        void checkRTLConversion();
    }
}
