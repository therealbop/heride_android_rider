package com.karru.landing.emergency_contact;


import com.karru.landing.emergency_contact.model.UserContactInfo;

import java.util.ArrayList;

public interface EmergencyContactContract
{

    interface EmergencyContactView
    {
        /**
         * <h>Set ArrayList For RecyclerView</h>
         * <p>this method is using to set ArrayList, it contains result from server</p>
         * @param contactDeatailsList list of Contact info
         */
        void setList(ArrayList<UserContactInfo> contactDeatailsList);

        /**
         * <h>On delete of Contact</h>
         * <p>operation to be performed after delete operation of Contact</p>
         * @param position position in list
         */
        void onDeleteSuccess(int position);

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
         * <h>Enable Add Contact Button</h>
         * <p>this method is using to enable the Add Contact button</p>
         */
        void enableAddContactButton();

        /**
         * <h>Disable Add Contact Button</h>
         * <p>this method is using to disable the Add Contact button</p>
         */
        void disableAddContactButton();

        /**
         * <h>Show Empty background</h>
         * <p>this method is using to display Empty list background</p>
         */
        void showProfileBackground();

        /**
         * <h>Hide Empty background</h>
         * <p>this method is using to Hide Empty list background</p>
         */
        void hideProfileBackground();

        /**
         * <h2>showAlertForInvalidNumber</h2>
         * alert to be shown for invalid number
         */
        void showAlertForInvalidNumber();

        /**
         * <h2>showAlertForMultipleumber</h2>
         * alert to be shown for multiple number added
         */
        void showAlertForMultipleNumber();

        /**
         * <h>addContact</h>
         * <p>this method is using to set data in List for RecyclerView</p>
         * @param name contact name
         * @param number contact number
         */
        void addContact(String name,String number);

        /**
         * <h2>setLimit</h2>
         * used to set the limit
         * @param limit limit
         */
        void setLimit(int limit);

        /**
         * <h2>showToast</h2>
         * used to show toast
         * @param message message toe be shown
         */
        void showToast(String message);
    }

    interface EmergencyContactPresenter
    {

        /**
         * <h>Add contact on Server</h>
         * <p>thi method is using to add Contact at server</p>
         * @param name name to save
         * @param number number to save
         */
        void addContact(String name, String number);

        /**
         * <h>Get All Contact Details</h>
         * <p>this method is using to get all the Contact Details from server</p>
         */
        void getEmergencyContact();

        /**
         * <h>Delete Contact Details</h>
         * <p>this method is using to delete the Contact Details from server</p>
         */
        void deleteContact(String id, int position);

        /**
         * <h>Update Contact Details</h>
         * <p>this method is using to Update the Contact Details in server</p>
         */
        void updateContact(String id, int status);
        /**
         * <h>Check is ArrayList Empty</h>
         * <p>this method is using to check whether the ArrayList is Empty or not</p>
         * @param contactDeatailsList list to check
         */
        void isArrayEmpty(ArrayList<UserContactInfo> contactDeatailsList);

        /**
         * <h2>clearDisposable</h2>
         * used to clear disposable
         */
        void clearDisposable();

        /**
         * <h2>validateMobileNumber</h2>
         * used to validate the mobile number
         * @param stringToBeValidated string to be validated
         */
        void validateMobileNumber(String stringToBeValidated,String name);

        void checkRTLConversion();
    }
}
