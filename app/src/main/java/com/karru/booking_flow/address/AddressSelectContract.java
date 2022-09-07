package com.karru.booking_flow.address;

import android.os.Bundle;
import com.karru.booking_flow.address.model.AddressDataModel;
import com.karru.booking_flow.address.model.FavAddressDataModel;
import com.karru.booking_flow.address.model.PlaceAutoCompleteModel;
import java.util.ArrayList;

/**
 * <h1>AddressSelectContract</h1>
 * This class is used to act as a link between view and presenter
 * @author 3Embed
 * @since on 20-01-2018.
 */

public interface AddressSelectContract
{
    interface View
    {
        /**
         * <h2>showProgressDialog</h2>
         * This method is used to show the progress dialog
         */
        void showProgressDialog();
        /**
         * <h2>dismissProgressDialog</h2>
         * This method is used to dismiss the progress dialog
         */
        void dismissProgressDialog();

        /**
         * <h2>deleteAddressNotify</h2>
         * This method is used to notify the delete address
         * @param position position for deletion
         */
        void deleteAddressNotify(int position);

        /**
         * <h2>openShipmentScreen</h2>
         * This method is used to open the shipment screen
         * @param latitude latitude of the address
         * @param longitude longitude of the address
         * @param address address selected
         */
        void openShipmentScreen(String latitude , String longitude ,String address);

        /**
         * <h2>notifyPickAddressChangeUI</h2>
         * This method is used to handle the change in pick address
         * <p>
         *         This exist only when we call from Home activity Class.
         * </p>
         * @param latitude latitude of the address
         * @param longitude longitude of the address
         * @param address address selected
         */
        void notifyPickAddressChangeUI(String latitude , String longitude ,String address);

        /**
         * <h2>notifySignUpBusinessAddressUI</h2>
         * This method is used to notify address change for signup
         * <p>
         *     This exist only when we call from Sign up Activity
         * </p>
         * @param latitude latitude of the address
         * @param longitude longitude of the address
         * @param address address selected
         */
        void notifySignUpBusinessAddressUI(String latitude , String longitude ,String address);

        /**
         * <h2>notifyLaterBookingClick</h2>
         * This method is used to notify later booking click
         */
        void notifyLaterBookingClick();

        /**
         * <h2>setTitleForPickAddress</h2>
         * This method is used to set the title for pick address
         */
        void setTitleForPickAddress();

        /**
         * <h2>setTitleForDropAddress</h2>
         * This method is used to set the title for drop address
         */
        void setTitleForDropAddress();

        /**
         * <h2>setTitleForBusinessAddress</h2>
         * This method is used to set the title for business address
         */
        void setTitleForBusinessAddress();

        /**
         * <h2>onAddressItemViewClicked</h2>
         * This method is used to trigger when item oof address is clicked
         * @param view view clicked
         * @param position position of view clicked
         * @param listType list type fav or recent
         */
        void onAddressItemViewClicked(android.view.View view, final int position, final int listType);

        /**
         * <h2>replaceDataNotifyAdapter</h2>
         * This method is used to replace the data in adapter and notify it
         * @param addressDataModels recent address data model
         *                          @param favAddressDataModels fav address data model
         */
        void replaceDataNotifyAdapter(ArrayList<AddressDataModel> addressDataModels,
                                      ArrayList <FavAddressDataModel>favAddressDataModels,
                                      ArrayList <AddressDataModel> specialAddressDataModels);

        /**
         * <h2>onPlaceClick</h2>
         * this method is triggered when the place clicked
         * @param mResultList list of address
         * @param position position clicked
         */
        void onPlaceClick(ArrayList<PlaceAutoCompleteModel> mResultList, int position);

        /**
         * <h2>showFavAddressListUI</h2>
         * This method is used to show the fav address list UI
         */
        void showFavAddressListUI();
        /**
         * <h2>hideFavAddressListUI</h2>
         * This method is used to hide the fav address list UI
         */
        void hideFavAddressListUI(int visibility);

        /**
         * <h2>hideKeyboard</h2>
         * This method is used to hide the keyboard
         */
        void hideKeyboard();

        /**
         * <h2>sendAddressBack</h2>
         * usd to send the address back
         * @param bundle bundle with address data
         */
        void sendAddressBack(Bundle bundle);

        /**
         * <h2>filterAddress</h2>
         * used to filter the address with constraint
         */
        void filterAddress();

        /**
         * <h2>hideSpecialAddress</h2>
         * used to hide the special address
         */
        void hideSpecialAddress();
    }

    interface Presenter
    {
        /**
         *<h2>deleteFavAddressAPI</h2>
         * <p>
         * This method is used to call the Delete API
         * </p>
         * @param id: id of selected fav address to be deleted
         */
        void deleteFavAddressAPI(final String id, final int index);

        /**
         *<h2>fetchSpecialAddress</h2>
         * <p>
         * This method is used to call the special address API
         * </p>
         * @param s
         */
        void fetchSpecialAddress(String s);

        /**
         * <h2>initializeVariables</h2>
         * This method is used to set the title according to screen coming from
         * @param comingFrom tells from which it is coming
         */
        void initializeVariables(String comingFrom);

        /**
         * <h2>storeAndHandleAddressSelect</h2>
         * This method is used to store the address and handle the address click
         * @param keyId check from which it is coming
         */
        void storeAndHandleAddressSelect(int keyId,AddressDataModel addressDataModel,String key);

        /**
         * <h2>handleAutoSuggestAddressClick</h2>
         * This method is used to handle the auto search address click
         * @param mAddressList list of address
         * @param position position of the item
         *                 @param keyId id from which it is called
         */
        void handleAutoSuggestAddressClick(ArrayList<PlaceAutoCompleteModel> mAddressList, int position,
                                           ArrayList<AddressDataModel> recentAddressList,int keyId,String key);

        /**
         *<h>toggleFavAddressField</h>
         * <p>
         * This method is used to toggle the fav address icon
         * </p>
         * @param showFavAddressList Tells whether to address is fav
         *                           true means need to change UI for fav
         *                           else change UI for non fav
         */
        void toggleFavAddressField(final boolean showFavAddressList);

        /**
         * <h2>handleClickOfAddress</h2>
         * This method is used to handle the click of address
         * @param listType type of address fav or non fav
         *                 @param favAddressList fav address list
         *                                       @param recentAddressList recent address list
         *                                                                @param position position oc click
         */
        void handleClickOfAddress(int listType,ArrayList<AddressDataModel> recentAddressList,
                                  ArrayList <FavAddressDataModel> favAddressList,ArrayList<AddressDataModel> specialAddress,
                                  int position,int keyId,String key);
        /**
         * <h2>handleResultData</h2>
         * usd to handle result data
         * @param requestCode request code
         *                    @param resultCode result code
         */
        void handleResultData(int requestCode,int resultCode);

        /**
         *<h>Dispose Observable</h>
         * <h>this method is using to dispose the compositeDisposable observer</h>
         */
        void disposeObservable();

        /**
         * <h2>getStoredValues</h2>
         * used to get the stored values
         */
        String getServerKey();

        /**
         * <h2>rotateNextKey</h2>
         * used to rotate to next key
         */
        void rotateNextKey();

        /**
         * <h2>checkForLoginType</h2>
         * used to check Login type
         * @param s
         */
        void checkForLoginType(String s);

        void checkRTLConversion();
    }
}
