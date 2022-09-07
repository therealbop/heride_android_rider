package com.karru.authentication.change_password;


import com.karru.util.DataParser;

public interface ChangePasswordActivityContract
{
    interface View
    {
        /**
         * <h>set New password Empty Error</h>
         * <p>this method is using to set New password Empty Error</p>
         */
        void newPasswordDataEmpty();

        /**
         * <h>set New password Invalid Error</h>
         * <p>this method is using to set New password Invalid Error</p>
         */
        void newPasswordDataNotValid();

        /**
         *  <h>set New password clear Error</h>
         * <p>this method is using to set New password clear Error</p>
         */
        void newPasswordDataValid();

        /**
         * <h>set Re Entered password Empty Error</h>
         * <p>this method is using to set Re Entered password Empty Error</p>
         */
        void reEnterPasswordDataEmpty();

        /**
         * <h>set Re Entered password Invalid Error</h>
         * <p>this method is using to set Re Entered password Invalid Error</p>
         */
        void reEnterPasswordDataNotValid();

        /**
         * <h>set Re Entered password clear Error</h>
         * <p>this method is using to set Re Entered password clear Error</p>
         */
        void reEnterPasswordDataValid();

        /**
         *<h>Dispaly Alert</h>
         * <p>this method is using to Dispaly success Alert</p>
         */
        void loadAlert();

        /**
         ***
         *<h>Dispaly Error Alert</h>
         * <p>this method is using to Dispaly Error Alert</p>
         * @param message error message
         */
        void loadErrorAlert(String message);

        /**
         ***
         *<h>showToast</h>
         * <p>this method is using to Dispaly Error Alert</p>
         * @param message error message
         */
        void showToast(String message);

        /**
         *<h>password Getter</h>
         * <p>this method is using to get password from input field</p>
         */
        void getPassword();

        /**
         *<h>Mismatch Error</h>
         * <p>this method is using to set Mismatch Error</p>
         */
        void missMatchError();

        /**
         * <h>set Old password Empty Error</h>
         * <p>this method is using to set Re Entered password Empty Error</p>
         */
        void oldPasswordDataEmpty();

        /**
         * <h>set Old password Invalid Error</h>
         * <p>this method is using to set New password Invalid Error</p>
         */
        void oldPasswordDataValid();

        /**
         * <h>Visible old Password</h>
         * <p>this method is using to enable old password field </p>
         */
        void enableOldPasswod();

        /**
         * <h>get pasword for profile change</h>
         * <p>this method is using to get old  and new password if this Activity called from Profile</p>
         */
        void getPasswordForProfile();

        /**
         * <h>set Old Password Error</h>
         * <p>this method is using to set old Password incoreect Error</p>
         * @param message error message
         */
        void setOldPaswdError(String message);

        /**
         * <h>Start progressBar</h>
         * <p>this method is using to start the progress bar</p>
         */
        void showProgressDialog();

        /**
         * <h>stop progress bar</h>
         * <p>this method is using to stop the progress bar</p>
         */
        void hideProgressDialog();
        void setSamePasswordError();
        void setForgotPasswordViews();
    }
    interface Presenter
    {
        /**
         * <h>validation of new password</h>
         * <p>this method is using for validation of new password</p>
         * @param temp is the new password value
         */
        void validateInputValue(String temp);

        /**
         * <h>validation of Re entered password</h>
         * <p>this method is using for validation of Re entered password</p>
         * @param temp is the Re entered password value
         */
        void validateReEnterInputValue(String temp);

        /**
         * <h>check both passwords are valid</h>
         * <p>this method is using to validate whether both password in valid format</p>
         */
        void isValidPassword(String comingFrom);

        /**
         * <h>Check password mismatch Error</h>
         * <p>this method is using to check whether new password and confirm password are same or not</p>
         * @param pass is the new password value
         * @param rePass is the confirm password value
         */
        void checkPasswordMatch(String pass, String rePass);

        /**
         * <h>check source of ChangePAsswordActvity call</h>
         * <p>this method is using to check the source from where this Activity is being called </p>
         * @param comingFrom source value
         */
        void checkSource(String comingFrom);

        /**
         * <h> validate the Old pasword</h>
         * <p>this method is using to validate old password</p>
         * @param number old password value
         */
        void validateOldPasswordInputValue(String number);

        /**
         * <h>check new and confirmation password are same</h>
         * <p>this method is using to  check whether the new password and confirmation password are same</p>
         * @param newPassword new password value
         * @param reEnteredPassword confirmation password value
         * @param oldPassword old pssword value
         */
        void checkPasswordMatchforProfile(String newPassword, String reEnteredPassword, String oldPassword);
        /**
         *<h>Dispose Observable</h>
         * <h>this method is using to dispose the compositeDisposable observer</h>
         */
        void disposeObservable();

        void checkRTLConversion();
    }
}