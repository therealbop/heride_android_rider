package com.karru.landing.profile.edit_phone_number;


public interface EditPhoneNumberActivityContract
{

    interface EditPhoneNumberView
    {
        /**
         * <h>Start OtpActivity</h>
         * <p>this method is using to start VerifyOTPActivity </p>
         */
        void startVerifyOTPActivity(String otpTimer);

        /**
         * <h>set ProgressDialog </h>
         * <p>this method is using to set the Progress Dialog</p>
         */
        void setProgressDialog();

        /**
         * <h>Dismiss Progress Dialog</h>
         * <P>this method is using to dismiss the Progress Dialog</P>
         */
        void dismissDialog();

        /**
         * <h2>loadingAlert</h2>
         * <p> This method is used for loading the alert box </p>
         * @param message contains the actual message to show on alert box.
         * @param flag: false
         */
        void loadingAlert(String message, final boolean flag);

        /**
         * <h>set Error response</h>
         * <p>this method is using to set Api call Error</p>
         */
        void errorMessage(String message);

        /**
         * <h>set Empty Phone number Error</h>
         * <p>this method is using to set Empty Phone number Error</p>
         */
        void setPhoneNumberEmptyError();

        /**
         * <h>Set invalid mail Error</h>
         * <p>this method is using to set invalid phone number Error</p>
         */
        void setInvalidPhoneNumberError();
    }

    interface EditPhoneNuberPresenter
    {
        /**
         * <h>Phone number validator</h>
         * <p>this method is using to check is the phone number is valid or not</p>
         */
        void checkForMobileValidation(String phoneNumber,String countryCode);
        /**
         *<h>Dispose Observable</h>
         * <h>this method is using to dispose the compositeDisposable observer</h>
         */
        void disposeObservable();

        void checkRTLConversion();
    }
}
