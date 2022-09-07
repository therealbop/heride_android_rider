package com.karru.authentication.forgot_password;


public interface ForgotPasswordContract
{
    interface ForgotPasswordView
    {

        /**
         * <h>Dispaly Progress</h>
         * <h>this method is using to shoe the progress dailog to user</h>
         */
        void showProgress();

        /**
         * <h>Hide SoftKeyboard</h>
         * <p>this method is using to hide the softkeyboard</p>
         */
        void hideSoftKeyBoard();

        /**
         * <h>Set mail Header</h>
         * <p>this method is using to  set the Mail Header text</p>
         */
        void setMailHeaderText();

        /**
         * <h>Set Phone number Header</h>
         * <p>this method is using to  set the Phone number Header text</p>
         */
        void setPhNumberHeaderText();

        /**
         * <h>Enable Button</h>
         * <p>this method is using to Enable the button if conditions satisfies</p>
         */
        void enableButton();

        /**
         * <h>Disable Button</h>
         * <p>this method is using to disble the button if conditions not satisfies</p>
         */
        void disableButton();

        /**
         * <h>Alert user of Empty text</h>
         * <p>this method is using to alert the user about Empty input</p>
         */
        void showEmptyPhNumberAlert();

        /**
         * <h>Alert user of Wrong input value</h>
         * <p>this method is using to alert the user about wrong input value</p>
         */
        void showWrongPhNumberAlert();

        /**
         * <h2>showAlert</h2>
         * <p>
         * This method is used for showing an alert only with OK button,
         * and if clicked OK then just close the Alert.
         * </p>
         * @param message , contains the actual message.
         */
        void showAlert(String message);

        /**
         * This method is used to open the dialog, when, we call an API for forgot password.
         * @param errorMsg , contains the message, that is getting from Serive caling.
         */
        void openMessageDialog(String errorMsg);


        void checkVersionTOContinue(String phNumTxt);

        /**
         * <h>Print Error Response</h>
         * <h>this method is using to </h>
         * @param errorMsg contains message from server
         */
        void onError(String errorMsg);

        /**
         * <h>Success Message</h>
         * <p>this method is using to dispalay the Success message</p>
         * @param message contains success message
         */
        void showToast(String message);

        /**
         * <H>Dismiss the Progress dialog</H>
         * <p>this method is using to dismiss the Progress Dailog</p>
         */
        void dismissProgressDialog();

        /**
         * <h>Start next Activity</h>
         * <p>this method is using to launch OTP Verify Activity</p>
         * @param phone_Mail is to send to next Activity
         */
        void startOtpClass(String phone_Mail, String countryCode,String otpCode);

        /**
         *  <h>mandatory field Message</h>
         * <p>this method is using to set mandatory error</p>
         */
        void errorMandatoryNotifier();

        /**
         * <h>Invalid Phone number Message</h>
         * <p> this method is using to dispaly invalid user input </p>
         */
        void phoneErrorInvalidNotifier();

        /**
         * <h>Invalid Email id Message</h>
         * <p> this method is using to dispaly invalid user input </p>
         */
        void emailErrorInvalidNotifier();

        /**
         * <h>Invalid Phone id Message</h>
         * <p> this method is using to dispaly invalid user input </p>
         */
        void phoneValidNotifier();

        /**
         * <h>Valid Email  Message</h>
         * <p> this method is using to dispaly Valid user input </p>
         */
        void onValidMail();

    }
    interface  ForgotPasswdPresenter
    {

        /**
         * <h>Email validation</h>
         * <p>this method is using to make User input validation of Phone or Email</p>
         * @param isEmailValidation mailvalidation status
         * @param isPhoneNoValid status of Phone number
         */
        void isEmailValidation(boolean isEmailValidation, boolean isPhoneNoValid);

        /**
         * <h>Check Invalid Email or Phone number</h>
         * <p> this methid is using to check validity of phone number or mail</p>
         * @param isEmailValidation status of input
         * @param mailTxt mail input
         * @param phNumTxt phone number input
         */
        void isValid(boolean isEmailValidation, String mailTxt, String phNumTxt, String countryCode);

        /**
         * <h>Validating Phone or Email</h>
         * <p>this method is using to decide whether the input is mail or phone number</p>
         * @param phone_Mail contains the user input to validate whether its mail or phone number
         */
        void validatePhoneEmail(String phone_Mail, String countryCode);

        /**
         * <h>validation of input format</h>
         * <p>this method is using to validate the format of user input</p>
         * @param isMail mail input to validate
         * @param phone data input
         */
        void phoneMailValidation(boolean isMail, String phone,String countryCode);

        /**
         * <h2>disposeObservable</h2>
         * used to dispose the observable
         */
        void disposeObservable();

        void checkRTLConversion();
    }
}
