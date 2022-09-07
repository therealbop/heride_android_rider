package com.karru.authentication.signup;

import android.net.Uri;
import android.text.SpannableString;
import com.karru.countrypic.CountryPicker;
import com.karru.managers.location.LocationCallBack;

import java.io.File;

/**
 * <h1>SignUpContract</h1>
 * This interface is used to communicate between View and Model
 * @author  3Embed on 25-11-2017.
 */

public interface SignUpContract
{
    interface View extends LocationCallBack.View
    {
        /**
         * <h>Individual Switch details setter</h>
         * <p>this method is using to set Individual details </p>
         */
        void setIndividualAwitch();
        /**
         * <h2>showProgressDialog</h2>
         * <p>This method is used to show the progress</p>
         */
        void showProgressDialog(String message);
        /**
         * <h2>hideProgressDialog</h2>
         * This method is used to hide the progress dialog
         */
        void hideProgressDialog();
        /**
         * <h2>onValidEmailIdFromDB</h2>
         * This method is triggered when the email ID is valid from database
         */
        void onValidEmailIdFromDB();
        /**
         * <h2>onInvalidEmailIdFromDB</h2>
         * This method is triggered when the email ID is invalid from database
         * @param errorMsg cause for error
         */
        void onInvalidEmailIdFromDB(String errorMsg);
        /**
         * <h2>onValidMobileFromDB</h2>
         * This method is triggered when the phone number is valid
         */
        void onValidMobileFromDB();
        /**
         * <h2>onInvalidMobileFromDB</h2>
         * This method is triggered when the phone number is invalid
         * @param errorMsg cause for error
         */
        void onInvalidMobileFromDB(String errorMsg);

        /**
         * <h2>onReferralError</h2>
         * This method is called when referral is not valid
         */
        void onReferralError(String errorMessage);
        /**
         * <h2>onSuccessOfGettingOTP</h2>
         * <p>
         * This method triggered when user registered and OTP has sent to user
         * </p>
         */
        void onSuccessOfGettingOTP(String otpTime);

        /**
         * <h2>onValidFullName</h2>
         * This method is called to when the full name is valid
         */
        void onValidFullName();
        /**
         * <h2>onInValidFullName</h2>
         * This method is called to when the full name is invalid
         */
        void onInValidFullName();
        /**
         * <h2>onValidFullName</h2>
         * This method is called to when the full name is valid
         */
        void onValidPassword();
        /**
         * <h2>onInValidFullName</h2>
         * This method is called to when the full name is invalid
         */
        void onInValidPassword();
        /**
         * <h2>onValidCompanyAddress</h2>
         * This method is called to when the CompanyAddress is valid
         * @param address address of the company
         */
        void onValidCompanyAddress(String address);
        /**
         * <h2>onInValidCompanyAddress</h2>
         * This method is called to when the CompanyAddress is invalid
         */
        void onInValidCompanyAddress();
        /**
         * <h2>onValidCompanyName</h2>
         * This method is called to when the company name is valid
         */
        void onValidCompanyName();
        /**
         * <h2>onInValidCompanyName</h2>
         * This method is called to when the company name is invalid
         */
        void onInValidCompanyName();

        /**
         * <h2>hideSoftKeyboard</h2>
         * This method is used to hide the keyboard
         */
        void hideSoftKeyboard();

        /**
         * <h2>clearFocus</h2>
         * This method is used to clear the focus on all edit texts
         */
        void clearFocus();

        /**
         * <h2>switchToBusinessAccount</h2>
         * This method is used to show the business account
         */
        void switchToBusinessAccount();
        /**
         * <h2>switchToIndividualAccount</h2>
         * This method is used to show the individual account
         */
        void switchToIndividualAccount();

        /**
         * <h2>enableSignUpButton</h2>
         * This method is used to enable the sign up button
         */
        void enableSignUpButton();
        /**
         * <h2>disableSignUpButton</h2>
         * This method is used to disable the sign up button
         */
        void disableSignUpButton();

        /**
         * <h2>onInvalidEmail</h2>
         * This method is triggered when the email ID is not valid
         * @param errorMsg error message to be shown
         */
        void onInvalidEmail(String errorMsg);
        /**
         * <h2>onValidEmail</h2>
         * This method is triggered when the email ID is valid
         */
        void onValidEmail();
        /**
         * <h2>agreeToSignUpAccount</h2>
         * This method is triggered when all the fields are validated
         * <p>
         *     and user is allowed to create an account
         * </p>
         */
        void agreeToSignUpAccount();
        /**
         * <h2>onValidEmail</h2>
         * This method is triggered when all the fields are invalid
         * <p>
         *     and user is not allowed to create an account
         * </p>
         */
        void disAgreeToSignUpAccount();
        /**
         * <h2>onReferralEntered</h2>
         * This method is triggered when referral is entered
         */
        void onReferralEntered();
        /**
         * <h2>onReferralEmpty</h2>
         * This method is triggered when referral code is blank
         */
        void onReferralEmpty();

        /**
         * <h2>onGettingOfCountryInfo</h2>
         * This method is used to get the country Info
         * @param countryFlag flag of the country
         * @param countryCode country code
         * @param phoneMaxLength phone number max length
         */
        void onGettingOfCountryInfo(int countryFlag, String countryCode, int phoneMaxLength,
                                    boolean isDefault);

        /**
         * <h2>checkForPermissionAndSendOTP</h2>
         * This method is used to verify the permissions and call send OTP API
         */
        void checkForPermissionAndSendOTP();

        /**
         * <h2>captureImage</h2>
         * This method is triggered when we need to open camera to capture image
         */
        void captureImage();
        /**
         * <h2>removeImageCaptured</h2>
         * This method is triggered when we need to remove the image captured
         */
        void removeImageCaptured();

        /**
         * <h2>openPickAddressScreen</h2>
         * This method is used to open the address activity
         * <p>
         *     to get the address
         * </p>
         */
        void openAddressActivity();

        /**
         * <h2>onSuccessOfImageUpload</h2>
         * This method is called when the image uploaded
         * @param imageUrl image url
         */
        void onSuccessOfImageUpload(String imageUrl);
        /**
         * <h2>onFailureOfImageUpload</h2>
         * This method is called when the image upload failed
         */
        void onFailureOfImageUpload();

        /**
         * <h2>onGettingOfCompanyAddress</h2>
         * This method is used to provide the company address
         * @param address address for user company
         */
        void onGettingOfCompanyAddress(String address);


        /**
         * <h2>openWebView</h2>
         * This method is used to open the web view
         */
        void openWebView(String webLink, String title);
        /**
         * <h2>setSpannableString</h2>
         * This method is used to set the spannable string to text view
         * @param spannableString string with spannable
         */
        void setSpannableString(SpannableString spannableString);

        /**
         * <h2>showAlertWithMsg</h2>
         * This method is used to show alert with error message
         * @param errorMsg error message to be shown
         */
        void showAlertWithMsg(String errorMsg);

        /**
         * <h2>showToast</h2>
         * This method is used to show the toast with message
         * @param errorMsg message to be shown on toast
         */
        void showToast(String errorMsg);

        /**
         * <h>Image Upaload Success Message</h>
         * <p>this method is using to display the image upload success message</p>
         * @param imageUrl uploaded image URL
         */
        void onImageUploadSuccess(String imageUrl);

        /**
         * <h>Buisness disagree swich handler</h>
         *<p>this method is using to handle disAgree Button of Buisness</p>
         */
        void disAgreeToSignUpAccountBuisness();

        /**
         * <h>Buisness agree swich handler</h>
         *<p>this method is using to handle Agree Button of Buisness</p>
         */
        void agreeToSignUpAccountBuisness();
    }
    interface Presenter extends LocationCallBack
    {
        /**
         * <h>Shared preference Image URl setter</h>
         * <p>this method is using to set Image URL in Shared Preference</p>
         * @param url URL to set In shared preference
         */
        void setImageUrl(String url);

        /**
         * <h>method upload profile pic to AMAZON</h>
         * <p>this method is using to upload image to AMAZONs</p>
         * @param image iamge file to upload to AMAZON
         */
        void uploadToAmazon(Uri image);
        /**
         * <h2>onBackPressed</h2>
         * This method is used to trigger when the back button is pressed
         */
        void onBackPressed();
        /**
         * <h2>validateEmailAvailability</h2>
         * This method is used to validate the email ID
         * @param emailId email Id to be validated
         */
        void validateEmailAvailability(String emailId);
        /**
         * <h2>validateEmailAvailability</h2>
         * This method is used to validate the email ID
         * @param phone phone to be validated
         * @param countryCode country code of the mobile number
         */
        void validateMobileAvailability(final String phone, String countryCode);

        /**
         * <h2>startLocationService</h2>
         * This method is used to start the location service
         */
        void startLocationService();

        /**
         * <h2>validateFullName</h2>
         * This method is used to validate full name
         * @param fullName full name
         */
        void validateFullName(String fullName);

        /**
         * <h2>validatePassword</h2>
         * This method is used to validate password
         * @param password password
         */
        void validatePassword(String password);

        /**
         * <h2>validateCompanyName</h2>
         * This method is used to validate company name
         * @param companyName company name
         */
        void validateCompanyName(String companyName);

        /**
         * <h2>changeAccountType</h2>
         * This method is used to change the account type
         * @param isItBusinessAccount gives the boolean whether it is business account
         */
        void changeAccountType(boolean isItBusinessAccount);

        /**
         * <h2>handleSignUpBtnStateEnabling</h2>
         * This method is used to handle the sign up button
         * @param isTermsAndCondsAccepted whether the terms and condition checked
         * @param isTermsAndCondsEnabled whether the terms and condition enabled
         */
        void handleSignUpBtnStateEnabling(boolean isTermsAndCondsAccepted, boolean isTermsAndCondsEnabled, boolean state);

        /**
         * <h2>validateMobileFormat</h2>
         * This method is used to validate the empty and length
         * @param mobileNumber mobile number to be validated
         */
        void validateMobileFormat(String mobileNumber,String countryCode);

        /**
         * <h2>validateEmailFormat</h2>
         * This method is used to validate the empty and format
         * @param emailID email ID to be validated
         */
        void validateEmailFormat(String emailID, boolean isToCallAPI);

        /**
         * <h2>validateMobileField</h2>
         * This method is used to validate the mobile number field
         * @param mobileNumber number to be validated
         * @param isToCallAPI standard max length according to country
         */
        void validateMobileField(String mobileNumber, String countryCode, boolean isToCallAPI);

        /**
         * <h2>validateAllFieldsFlags</h2>
         * This method is used to maintain all fields flags
         */
        void validateAllFieldsFlags();
        /**
         * <h2>validateCompanyAddress</h2>
         * This method is called to when the CompanyAddress to be validated
         * @param address address chosen
         */
        void validateCompanyAddress(String address);

        /**
         * <h2>restoreAccountType</h2>
         * This method is used to restore the account type and set the type
         * @param accountType account type true if business account
         * @param loginType the type of login stored
         */
        void restoreAccountType(boolean accountType, int loginType);

        /**
         * <h2>verifyReferralEntered</h2>
         * This method is called to check whether referral code is entered
         * @param referralCode referral code to be validated
         */
        void verifyReferralEntered(String referralCode);

        /**
         * <h2>getCountryInfo</h2>
         * This method is used to get the country information
         * @param countryPicker country picker
         */
        void getCountryInfo(CountryPicker countryPicker);

        /**
         * <h2>addListenerForCountry</h2>
         * This method is used to add the listener for events for picker
         */
        void addListenerForCountry(CountryPicker countryPicker);

        /**
         * <h2>storeSocialMediaID</h2>
         * This method is used to store the social media ID
         * @param socialMediaId social media ID to be stored
         */
        void storeSocialMediaID(String socialMediaId);

        /**
         * <h2>fetchLoginType</h2>
         * This method is used to provide
         * @return  loginType user Login type
         */
        int fetchLoginType();
        /**
         * <h2>fetchAccountType</h2>
         * This method is used to provide
         * @return  accountType user account Type
         */
        boolean fetchAccountType();
        /**
         * <h2>fetchProfilePicUrl</h2>
         * This method is used to provide profile pic url
         * @return  userProfile Pic Url
         */
        String fetchProfilePicUrl();

        /**
         * <2>storeProfilePicUrl</2>
         * This method is used to store the profile pic url
         * @param url profile pic url
         */
        void storeProfilePicUrl(String url);
        /**
         * <h2>fetchProfilePicUrl</h2>
         * This method is used to provide address of user company
         * @return  address of user company
         */
        String fetchCompanyAddress();

        /**
         * <2>storeCompanyAddress</2>
         * This method is used to store the address of user company
         * @param address address of user company
         */
        void storeCompanyAddress(String address);

        /**
         * <h2>performImageOperation</h2>
         * This method is used to perform image operations
         * <p>
         *     like capture image ,take image from gallery or remove image
         * </p>
         */
        void performImageOperation();

        /**
         * <h2>hideKeyboardAndClearFocus</h2>
         * This method is used to hide and clear the focus of edit texts
         */
        void hideKeyboardAndClearFocus();

        /**
         * <h2>getUserCompanyAddress</h2>
         * This method is used to get the company address
         */
        void getUserCompanyAddress();

        /**
         * <h2>storeIfProfilePicSelected</h2>
         * This method is used to store
         * <p>
         *     whether profile pic is selected
         * </p>
         * @param isSelected true if selected else false
         */
        void storeIfProfilePicSelected(boolean isSelected);


        /**
         * <h2>uploadImageToAmazon</h2>
         * This method is used to upload the image to amazon
         */
        void uploadImageToAmazon(File file);

        /**
         * <h2>storeIfImageUploaded</h2>
         * This method is used to store
         * <p>
         *     whether profile pic is uploaded to amazon
         * </p>
         * @param isSelected true if image uploaded else false
         */
        void storeIfImageUploaded(boolean isSelected);


        /**
         * <h2>storeIfAllPermissionsGranted</h2>
         * This method is called to check whether all
         * <p>
         *     permissions granted
         * </p>
         * @param isGranted true if all permissions granted else false
         */
        void storeIfAllPermissionsGranted(boolean isGranted);
        /**
         * <h2>fetchIfAllPermissionsGranted</h2>
         * This method is called to get whether all
         * <p>
         *     permissions granted
         * </p>
         * @return  true if all permissions granted else false
         */
        boolean fetchIfAllPermissionsGranted();

        /**
         * <h2>handleResult</h2>
         * This method is used to handle the result from activity
         * @param requestCode need to give the request code.
         * @param resultCode need to give the result code.
         */
        void handleResult(int requestCode, int resultCode, String address, Uri imageUri);



        /**
         * <h2>getSpannableString</h2>
         * This method is used to get the spannable string
         */
        void getSpannableString(String termsCondsString);

        /**
         * <h2>storeUserDetails</h2>
         * This method is used to store the user details
         * @param params needs the params to be stored
         */
        void storeUserDetails(String... params);
        /**
         * <h2>signUpAPI</h2>
         * This method is used to call the API to register the user
         */
        void registerUser();
        void isIndividual(boolean isTermsAndCondsAccepted, boolean state);

        /**
         * <h2>checkIfLocationEnabled</h2>
         * used to check if user current location is enabled
         */
        void checkIfLocationEnabled( );

        /**
         * <h2>disposeObservable</h2>
         * used to dispose the observable
         */
        void disposeObservable();

        void checkRTLConversion();
    }
}
