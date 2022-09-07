package com.karru.landing.profile;



import android.view.View;

import com.karru.landing.BasePresenter;

import java.io.File;

public interface ProfileContract
{
    interface ProfileView
    {

        /**
         * <h>Select image</h>
         * <p> this method is using to handle Image</p>
         */
        void selectImage();

        /**
         * <h2>updateUI</h2>
         * <p>
         * This method is used for updating the UI data.
         * </p>
         */
        void updateUI();

        /**
         * <h>Network Error</h>
         * <p>this method is using to dispaly the error if any Internet Error occur</p>
         */
        void onAPICallError(String message);

        /**
         * <h>dismissProgressDialog</h>
         * This method is used to dismiss the progress
         */
        void dismissProgressDialog();

        /**
         * <h2>showProgressDialog</h2>
         * setting the progress dialog
         */
        void showProgressDialog();

        /**
         * <h2>onImageUploadSuccess</h2>
         * This method is triggered when image upload success
         * @param imageUrl uploaded image url
         */
        void onImageUploadSuccess(String imageUrl);
        /**
         * <h2>onImageUploadFailure</h2>
         * This method is triggered when image upload failure
         */
        void onImageUploadFailure();

        /**
         * <h2>showLanguagesDialog</h2>
         * used to show the languages dialog
         */
        void showLanguagesDialog(int indexSelected);

        /**
         * <h2>setLanguage</h2>
         * used to set the language
         * @param language language to be set
         */
        void setLanguage(String language,boolean restart);
    }

    interface Presenter
    {
        /**
         * <h>Upadate profile Photo</h>
         * <p>this method is using to fetch the Profile Pic from Server</p>
         * @param profilePicUrl url of the Image
         */
        void updateProfilePic(String profilePicUrl);

        /**
         * <h2>logoutAPI</h2>
         * This method is used to call logout API
         */
        void logoutAPI();

        /**
         *<h1>uploadToAmazon</h1>
         * <p>This method is used to upload the image on AMAZON bucket. </p>
         * @param image image file to be uploaded
         */
        void uploadToAmazon(File image);

        /**
         * <h>Image Url getter</h>
         * <p>this using to get Image URL from PreferenceDataHelper</p>
         * @return imageUrl
         */
        String getImageUrl();

        /**
         * <h>User Name getter</h>
         * <p>this method using to get User Name from PreferenceDataHelper</p>
         * @return userName
         */
        String getUserName();

        /**
         * <h> USer Email getter</h>
         * <p>this method using to getUser Email from PreferenceDataHelper</p>
         * @return User Email
         */
        String getUserEmail();

        /**
         * <h>User Mobile Number getter</h>
         * <p>this method using to get User Mobile Number from PreferenceDataHelper</p>
         * @return User mobile number
         */
        String getMobileNumber();

        /**
         * <h>User Password getter</h>
         * <p>this method using to get User Password from PreferenceDataHelper</p>
         * @return password
         */
        String getPassword();

        /**
         * <h>Image Url setter</h>
         * <p>this method using to set Image URL in PreferenceDataHelper</p>
         * @param  imageUrl to set imageUrl in PreferenceDataHelper
         */
        void setImageUrl(String imageUrl);

        /**
         * <h2>disposeObservable</h2>
         * used to dispose
         */
        void disposeObservable();

        /**
         * <h2>getLanguages</h2>
         * This method is used to call the API to get all the languages
         */
        void getLanguages();

        /**
         * <h2>changeLanguage</h2
         * used to change the language>
         */
        void changeLanguage(String langCode,String langName,int isRTL);

        void checkRTLConversion();

        int getIsHotelBooking();

        String getHotelName();
    }
}