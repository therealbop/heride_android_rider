package com.karru.landing.card_details;


public interface CardDetailsActivityContract {

    interface View
    {
        /**
         * <h2>hideProgressBar</h2>
         * used to hide the progress dialog
         */
        void hideProgressBar();

        /**
         * <h2>showProgressBar</h2>
         * used to show the progress dialog
         */
        void showProgressBar();

        void onError(String message);

        /**
         * <h2>onResponse</h2>
         * triggered when added card as default
         * @param message success message
         */
        void onResponse(String message);
    }

    interface DeleteCardPresenter
    {

        /**
         * <h2>setAsDefaultCard</h2>
         * <p> to make an api call to get default car </p>
         */
        void makeCardDefault(String cardId);

        /**
         *<h>Dispose Observable</h>
         * <h>this method is using to dispose the compositeDisposable observer</h>
         */
        void disposeObservable();

        void checkRTLConversion();
    }
}
