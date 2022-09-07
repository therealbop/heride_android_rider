package com.karru.landing.profile.edit_email;



public interface EditEmailActivityContract
{
    interface EditEmailView
    {

        /**
         * This method is used for loading the alert box
         * @param message contains the actual message to show on alert box.
         */
        void loadingAlert(String message);

        /**
         * <p>Set the Progress Dialog</p>
         * <p>this method is using to initialize progress bar </p>
         */
        void showProgressDialog();

        /**
         * <h>Dismiss progress Dialog</h>
         * <p> this method is using to dismiss the Progress Dialog</p>
         */
        void dismissProgressDialog();

        /**
         * <h>set Empty Mail Error</h>
         * <p>this method is using to set Empty Email Error</p>
         */
        void setEmailEmptyError();

        /**
         * <h>Set Invalid mail Error</h>
         * <p>this method is using to set the invalid mail Error</p>
         */
        void setInvalidEmailError();

        /**
         * <h>set Wrong mail Error</h>
         * <p>this method is using to set Wrong Mail format Error</p>
         */
        void wrongMailFormat();

        /**
         * <h>showToast</h>
         * <p>used to show toast </p>
         */
        void showToast(String message);

        /**
         * <h>Clear mail Errors</h>
         * <p>this method is using to  clear the Mail field Error</p>
         */
        void clearMailError();
        void onNameEmpty();
        void onNameNonEmpty();
    }

    interface EditEmailPresenter
    {
        /**
         *<h>Check Mail</h>
         * <p>this method is using to  check whether the mail is present in DB or not</p>
         * @param email to send to server
         */
        void EditEmailService(final String email);

        /**
         * <h>Mail Validator</h>
         * <p>this method is using to validae the Email input from the User</p>
         * @param hasFocus boolean status val
         * @param temp String input
         */
        void emailValidator(boolean hasFocus, String temp);
        void isValidEmail(CharSequence target);

        /**
         *<h>Dispose Observable</h>
         * <h>this method is using to dispose the compositeDisposable observer</h>
         */
        void disposeObservable();

        void checkRTLConversion();
    }
}