package com.karru.landing.rate;

import com.karru.landing.rate.model.RateCardDetail;
import java.util.ArrayList;

public interface RateCardActivityContract
{
    interface RateCardView
    {
        /**
         * <h>Initialize City Name</h>
         * <h>This method is using to initialize the City names to List</h>
         * @param cities contains city names
         * @param rateCardDetail rate card details
         */
        void initCities(ArrayList<String> cities, RateCardDetail rateCardDetail);

        /**
         * <h2>showProgress</h2>
         * used to show the progress dialog
         */
        void showProgress();

        /**
         * <h2>hideProgress</h2>
         * used to hide the progress dialog
         */
        void hideProgress();

        /**
         * <h2>showToast</h2>
         * used to show toast
         * @param message message to be shown
         */
        void showToast(String message);
    }

    interface RateCardPresenter
    {
        /**
         *<h>Get Rate card from server</h>
         * <p>this method is using to get all the rate cards fron the server</p>
         */
        void getRateCard();

        /**
         * <h2>disposeObservable</h2>
         * used to dispose the observable
         */
        void disposeObservable();

        void checkRTLConversion();
    }
}
