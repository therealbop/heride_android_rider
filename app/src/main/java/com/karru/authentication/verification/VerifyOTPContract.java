package com.karru.authentication.verification;


/**
 * <h1>VerifyOTPContract</h1>
 * This interface is used to provide the contract between view and model
 * @author  3Embed
 * @since on 02-12-2017.
 */
public interface VerifyOTPContract
{
    interface View
    {
        /**
         * This methos is is used to get the OTP digits
         * @param otpDigits OTP digits filtered
         */
        void onGettingAutomaticOTP(String... otpDigits);

        /**
         * <h2>enableResendButton</h2>
         * This method is used to enable the resend option
         */
        void enableResendButton();

        /**
         * <h2>setElapsedTime</h2>
         * This method is used to set the elapsed time to be shown
         */
        void setElapsedTime(String elapsedTime);

        /**
         * <h2>showProgressDialog</h2>
         * This method is used to show the progress dialog
         */
        void showProgressDialog(String message);
        /**
         * <h2>hideProgressDialog</h2>
         * This method is used to hide the progress dialog
         */
        void hideProgressDialog();

        /**
         * <h2>showAlertWithMsg</h2>
         * This method is used to show the alert message
         * @param errorMsg error message to be shown
         */
        void showAlertWithMsg(String errorMsg);

        /**
         * <h2>clearFields</h2>
         * This method is used to clear the text OTP fields
         * <p>
         *     And request focus to 1st OTP field
         * </p>
         */
        void clearFields();

        /**
         * <h2>openSecondSplashScreen</h2>
         * This method is used to open second splash and wait for vehicle details
         */
        void openSecondSplashScreen();

        /**
         * <h2>showToast</h2>
         * This method is used to show the TOast with the message
         * @param message message to be shown
         */
        void showToast(String message);

        /**
         * <h2>setTitleForScreen</h2>
         * This method is used to set the title
         * @param phone phone number to be set the title
         */
        void setTitleForScreen(String phone);

        /**
         * <h2>openChangePasswordActivity</h2>
         * This method is used to call Change password activity
         * @param otp otp to be validated
         * @param phone phone number
         * @param comingFrom from where it is coming
         */
        void openChangePasswordActivity(String otp, String phone, String comingFrom);

        /**
         * <h2>openMainActivityWithoutClear</h2>
         * This method is called when we need to open the main activity without clearing tooth_top tasks
         */
        void openMainActivityWithoutClear();

        /**
         * <h2>requestFocus</h2>
         * This method is used to give focus to the edit text
         * @param position position of the text view
         */
        void requestFocus(int position);

        void loadingAlert(String message);
        /**
         * <h2>disableResend</h2>
         * This method is used for disabling the Resend button and start the timer on each 60 seconds.
         */
        void disableResend();
    }
    interface Presenter
    {
        /**
         * <h2>extractAndStoreData</h2>
         * This method is used to extract data and store the data in particular model
         * @param params needs the params to be stored
         */
        void extractAndStoreData(String... params);

        /**
         * <h2>handleResendOTP</h2>
         * This method is used to handle the resend the OTP visible/hide
         */
        void handleResendOTP();

        /**
         * <h2>verifyOTPApi</h2>
         * This method is used to call the verify OTP API
         * @param phone phone number
         * @param otp otp to be verified
         * @param comingFrom coming from which activity
         */
        void verifyOTPApi(String phone, final String otp, final String comingFrom);
        /**
         * <h2>getVerificationCode</h2>
         * This method is used for getting the OTP and calling another method i.e., resides in Model class.
         */
        void getVerificationCode();

        /**
         * <h2>addDataAndCallAPI</h2>
         * This method is used to add the data in model and call API
         * @param otp OTP to be verified
         */
        void addDataAndCallAPI(String otp);

        /**
         * <h2>validateEnteredDigit</h2>
         * This method is used to validated the OTP digit entered
         * @param currDigit otp digit to be validated
         * @param nextDigitPos next otp digit position to be request for focus
         * @param prevDigitPos prevDigit otp digit position to be request for focus
         * @param callAPI boolean whether to call API
         */
        void validateEnteredDigit(String currDigit, int nextDigitPos, int prevDigitPos, boolean callAPI);

        /**
         * <h2>disposeObservable</h2>
         * used to dispose the observable
         */
        void disposeObservable();

        void checkRTLConversion();
    }
}
