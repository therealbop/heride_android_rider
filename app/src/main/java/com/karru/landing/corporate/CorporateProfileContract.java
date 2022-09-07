package com.karru.landing.corporate;

import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.List;

public interface CorporateProfileContract
{
    interface View
    {
        /**
         * <h>Show Progress Bar</h>
         * <p>this method is using to show the progressBar</p>
         */
        void showProgressDialog();

        /**
         * <h>Dismiss Progress Bar</h>
         * <p>this method is using to dismiss the progressBar</p>
         */
        void dismissProgressDialog();

        /**
         * <h2>showToast</h2>
         * used to ow toast
         * @param message message to be shown
         */
        void showToast(String message);

        /**
         * <h>hideEmptyScreen</h>
         * <p>this method is using to hide the Background when recycler is not empty</p>
         */
        void hideEmptyScreen();
        /**
         * <h>showEmptyScreen</h>
         * <p>this method is using to hide the Relative layout when recycler is empty</p>
         */
        void showEmptyScreen();

        /**
         * <h2>populateListWithProfiles</h2>
         * used to populate the ;list with profiles

         */
        void populateListWithProfiles(ArrayList<CorporateProfileData> corporateProfileData);
    }

    interface Presenter
    {

        /**
         * <h2>disposeObservable</h2>
         * used to dispose
         */
        void disposeObservable();

        /**
         * <h2>getCorporateProfiles</h2>
         * used to get the corporate profiles
         */
        void getCorporateProfiles();

        void checkRTLConversion();
    }
}
