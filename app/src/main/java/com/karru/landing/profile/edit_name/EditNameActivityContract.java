package com.karru.landing.profile.edit_name;


public interface EditNameActivityContract
{
    interface EditNameView
    {
        /**
         * <h> Set Error </h>
         * <p> this method is using to set the Error message if input is Empty </p>
         */
        void onNameEmpty();

        /**
         *<h> Clear Error </h>
         * <p> this method is using to clear error if input is correct </p>
         */
        void onNameNonEmpty();

        /**
         * <h> Set ProgressBar </h>
         * <p> setting the progress bar properties </p>
         */
        void showProgressDialog();

        /**
         * <h> Dismiss ProgressBar </h>
         * <p> this method is using to dismiss the Progress bar</p>
         */
        void dismissProgressDialog();

        /**
         * <h> Alert </h>
         * <p> this method is using to set the Alert message </p>
         * @param message value to set alert message
         */
        void loadingAlert(String message);

        /**
         * <h> Response Error </h>
         * <p> this method is using to alert user about Error in response </p>
         * @param errorMessage message to be shown
         */
        void onResponseError(String errorMessage);
    }

    interface EditNamePresenter
    {
        /**
         * <h> Check isEmpty and Has focus </h>
         * <p> this method is using to check whether the input is empty or not and has focus </p>
         * @param nameEntered contains text input value
         */
        void isNameEmpty(String nameEntered);
        /**
         * <h> Edit Profile Name </h>
         * <p> this method is using to edit the Users profile Name </p>
         * @param editName is name be changed in Prifile
         */
        void nameUpdateRequest(String editName);

        /**
         *<h>Dispose Observable</h>
         * <h>this method is using to dispose the compositeDisposable observer</h>
         */
        void disposeObservable();

        void checkRTLConversion();
    }
}
