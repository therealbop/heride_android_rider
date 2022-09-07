package com.karru.landing.corporate.add_corporate;

public interface AddCorporateProfileAccountContract
{

    interface CorporateProfileAddMailView
    {
        /**
         * <h>set Empty Mail Error</h>
         * <p>This method is using to set Empty Mail error</p>
         */
        void mailEmptyError();

        /**
         * <h>Invalid Mail Error</h>
         * <p>This meyhos is using to  set Invalid Mail Format Error</p>
         */
        void setInvalidMailError(String error);

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
         * used to show the toast
         * @param message message to be shown
         */
        void showToast(String message);

        /**
         * <h2>showAlert</h2>
         * used to show the alert
         * @param message message to be shown
         */
        void showAlert(String message);
    }

    interface CorporateProfileAddMailPresenter
    {
        /**
         * <h>check Mail</h>
         * <P>This method is using to check the whether mail is valid or not</P>
         * @param mail mail  to check
         */
        void checkIsMailEmpty(String mail);

        /**
         * <h2>disposeObservable</h2>
         * used to dispose
         */
        void disposeObservable();

        void checkRTLConversion();
    }
}
