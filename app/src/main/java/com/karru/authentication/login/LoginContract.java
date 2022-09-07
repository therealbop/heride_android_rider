package com.karru.authentication.login;

import android.text.SpannableString;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.karru.managers.location.LocationCallBack;

/**
 * <h1>LoginContract</h1>
 * This interface is used to interact between model and view
 * @author  3Embed.
 * @since 22-11-2017
 */
public interface LoginContract
{
    interface View extends LocationCallBack.View
    {
        /**
         * <h2>saveUserCreds</h2>
         * This method is used to save the logged in user
         */
        void saveUserCreds(String userId, String password);

        /**
         * <h2>enableLogin</h2>
         * This method is used to enable the login button
         */
        void enableLoginBtn();
        /**
         * <h2>disableLoginBtn</h2>
         * This method is used to enable the login button
         */
        void disableLoginBtn();

        /**
         * <h2>showProgressDialog</h2>
         * This method is used to show the progress dialog
         */
        void showProgressDialog();

        /**
         * <h2>hideProgressDialog</h2>
         * This method is used to hide the progress dialog
         */
        void hideProgressDialog();

        /**
         * <h2>invalidPhoneNumber</h2>
         * This method is triggered when the invalid number entered
         */
        void invalidPhoneNumber();
        /**
         * <h2>invalidEmailId</h2>
         * This method is triggered when the invalid email entered
         */
        void invalidEmailId();
        /**
         * <h2>emptyPassword</h2>
         * This method is triggered when the password is empty
         */
        void emptyPassword();
        /**
         * <h2>emptyPassword</h2>
         * This method is triggered when the password is non empty
         */
        void nonEmptyPassword();

        /**
         * <h2>showErrorFromAPI</h2>
         * This method is used to show the error
         */
        void showErrorFromAPI(String errMsg);

        /**
         * <h2>onLoginSuccessful</h2>
         * This method is used to trigger if the user is logged in successfully
         */
        void onLoginSuccessful();

        /**
         * <h2>onUSerNotLoggedIn</h2>
         * This method is triggered when user is not logged in
         * @param name name of user
         * @param email email of user
         * @param picture picture of user
         * @param login_type login_type of user
         * @param ent_socialMedia_id ent_socialMedia_id of user
         */
        void onUserNotLoggedIn(String name, String email, String picture, int login_type,
                               String ent_socialMedia_id);

        /**
         * <h2>openGoogleActivity</h2>
         * This method is triggered to open the google activity
         */
        void openGoogleActivity(android.content.Intent intent);

        /**
         * <h2>checkLocationPermission</h2>
         * used to check location permission and start location service
         */
        void checkLocationPermission();

        /**
         * <h2>showToast</h2>
         * This method is used to show the toast with message
         * @param errorMsg message to be shown on toast
         */
        void showToast(String errorMsg);

        /**
         * <h2>showPartnerUI</h2>
         * used to show the partner UI
         */
        void showPartnerUI();
    }
    interface Presenter extends LocationCallBack
    {
        /**
         * <h2>checkDefaultUser</h2>
         * This method is used to save the user credentials in shared preference
         */
        void checkDefaultUser();

        /**
         * <h2>validateUserCreds</h2>
         * This method is only used for enable/ disable the Login button and change their look
         * and pass the callback to view class, where we set the look on button..
         */
        void validateUserCreds(String userName, String password);

        /**
         * <h2>normalLogin</h2>
         * This method will call, when user click on Login button and
         * <p>
         *     this method will make a call to CallLoginService() method located in Login Model class.
         * </p>
         */
        void normalLogin();

        /**<h2>handleResultFromGoogle</h2>
         * <p>
         * This method is used when callback called from google plus sign.
         * </p>
         * @param result: retrieved response from google api call
         */
        void handleResultFromGoogle(GoogleSignInResult result);

        /**
         * <h2>fbLogin</h2>
         * <P>
         * This method is used for Facebook login.
         * </P>
         * @param callbackManager: facebook call back manager interface
         */
        void handleResultFromFB(CallbackManager callbackManager);

        /**
         * <h2>googleLogin</h2>
         * This method is used for doing Google login.
         */
        void googleLogin();

        /**
         * <h2>initializeFBGoogle</h2>
         * This method is used to initialize FB and google SDK
         */
        void initializeFBGoogle(CallbackManager callbackManager);

        /**
         * <h2>validateUserName</h2>
         * This method is used to validate the getUsername is mobile or email
         * @param userName email Id or phone number
         */
        void validateUserName(String userName);

        /**
         * <h2>validateNonEmptyPass</h2>
         * This method is used to validate the non empty check
         * @param Password password given
         */
        void validateNonEmptyPass(String Password);

        /**
         * <h2>storeLoginType</h2>
         * This method is used to store the login type
         * @param loginType login type 1 for facebook and 2 for google
         */
        void storeLoginType(int loginType);

        /**
         * <h2>checkIfLocationEnabled</h2>
         * used to check if user current location is enabled
         * @param mailId contains the mail id.
         * @param password contain the password.
         */
        void checkIfLocationEnabled(String mailId, String password );

        /**
         * <h2>initializeFacebook</h2>
         * This method is used to initialize the facebook
         */
        void initializeFacebook();
        void isEmailError(String userName);

        /**
         * <h2>startLocationService</h2>
         * used to start the location service
         */
        void startLocationService(String emailID, String password);

        /**
         * <h2>disposeObservable</h2>
         * used to dispose the observable
         */
        void disposeObservable();

        /**
         * <h2>checkLoginType</h2>
         * used to check login type
         * @param loginType 1 for normal , 2 for partner
         */
        void checkLoginType(int loginType);

        void checkRTLConversion();
    }
}
