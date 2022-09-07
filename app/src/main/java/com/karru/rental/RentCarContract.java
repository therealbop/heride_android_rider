package com.karru.rental;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.karru.booking_flow.invoice.model.ReceiptDetails;
import com.karru.landing.corporate.CorporateProfileData;
import com.karru.rental.model.CarDataModel;
import com.karru.rental.model.DataModel;

import java.util.ArrayList;
import java.util.List;

public interface RentCarContract {
    interface View {

        /**
         * <h2>notifyPackages</h2>
         * used to set the RecyclerView
         */
        void notifyPackages(List<String> objects);

        /**
         * <h2>showMessageForSelectingPackage</h2>
         * used to show the message to select Package first
         */
        void showMessageForSelectingPackage();

        /**
         * <h2>notifyRentalVehicleTypes</h2>
         * used to set the RecyclerView
         */
        void notifyRentalVehicleTypes();

        /**
         * <h2>showMessage</h2>
         * used to set the RecyclerView
         */
        void showMessage(String message);

        /**
         * <h2>showSurgeBar</h2>
         * used to show surge price bar
         * @param surgePriceText text to be shown
         */
        void showSurgeBar(String surgePriceText);

        /**
         * <h2>hideSurgeBar</h2>
         * used to hide surge price bar
         */
        void hideSurgeBar();

        /**
         * <h2>showAdvanceFee</h2>
         * used to show advance fee layout
         * @param advanceFee advance fee
         */
        void showAdvanceFee(ArrayList<String> advanceFee);

        /**
         * <h2>hideAdvanceFee</h2>
         * used to hide the advance fee layout
         */
        void hideAdvanceFee();

        /**
         * <h2>setCurrency</h2>
         * used to set the Currency
         */
        void setCurrency(String currency);

        /**
         * <h2>showProgressDialog</h2>
         * This method is used to show the progress dialog
         */
        void showProgressDialog();
        /**
         * <h2>dismissProgressDialog</h2>
         * This method is used to hide the progress dialog
         */
        void dismissProgressDialog();

        /**
         * <h2>packageSelected</h2>
         * used to select the package id
         * @param name name of package
         * @param id id of package
         */
        void packageSelected(String name, String id);

        /**
         * <h2>hideBookingViews</h2>
         * this method is used to hide the all layouts below Package Layouts
         */
         void hideBookingViews() ;
        /**
         * <h2>handleClickOfVehicleType</h2>
         * used to handle click of vehicle type
         */
        void handleClickOfVehicleType(Context context, String type, String minutes, double cost,
                                      String offImage,String onImage, String id, int abbr, String symbol) ;

        /**
         * <h2>setSelectedCard</h2>
         * used to set the last digits of card in UI
         * @param lastDigits last 4 digits of card
         */
        void setSelectedCard(String lastDigits, String cardBrand);

        /**
         * <h2>setPromoCode</h2>
         * used to set the promo code
         * @param promo promo code
         */
        void setPromoCode(String promo);

        /**
         * <h2>setCashPaymentOption</h2>
         * used to set cash as payment option
         */
        void setCashPaymentOption();

        /**
         * <h2>disableBooking</h2>
         * This method is used to disable the booking button to book a ride
         * @param reason 1 for payment 2 for vehicle Id
         */
        void disableBooking(int reason);

        /**
         * <h2>enableBooking</h2>
         * This method is used to enable the booking button to book a ride
         */
        void enableBooking();
        /**
         * <h2>disableCashOption</h2>
         * used to disable cash option
         */
        void disableCashOption();

        /**
         * <h2>openPaymentScreen</h2>
         * used to open the payment screen
         */
        void openPaymentScreen();

        /**
         * <h2>addWalletMoneyLayout</h2>
         * used to set wallet recharge
         */
        void addWalletMoneyLayout();

        /**
         * <h2>setWalletPaymentOption</h2>
         * used to set wallet as payment option
         */
        void setWalletPaymentOption(String walletText);

        /**
         * <h2>showHardLimitAlert</h2>
         * used to show the hard limit reach alert
         */
        void showHardLimitAlert();

        /**
         * <h2>hideWalletOption</h2>
         * used to hide wallet option
         */
        void hideWalletOption();

        /**
         * <h2>disableWalletOption</h2>
         * used to disable waller option
         */
        void disableWalletOption();

        /**
         * <h2>setWalletAmount</h2>
         * used to set the wallet amoubnt
         * @param walletAmount amount
         */
        void setWalletAmount(String walletAmount);

        /**
         * <h2>disableCardOption</h2>
         * used to disable card option
         */
        void disableCardOption(boolean disable);

        /**
         * <h2>disableWalletOption</h2>
         * used to disable waller option
         */
        void enableWalletOption(String amount);

        /**
         * <h2>showDefaultCard</h2>
         * used to shwo the default card
         * @param lastDigits last digits of card
         * @param cardBrand card brand
         *                  @param paymentType payment type default
         */
        void showDefaultCard(String lastDigits, String cardBrand, int paymentType, boolean isWalletSelected);

        /**
         * <h2>showWalletExcessAlert</h2>
         * used to show the wallet alert excess amount popup
         */
        void showWalletExcessAlert(int paymentType);

        /**
         * <h2>openRequestingScreenNormal</h2>
         * This method is used to open the requesting screen
         * @param bundle bundle with data to be sent to request screen
         */
        void openRequestingScreenNormal(Bundle bundle);

        /**
         * <h2>populateProfiles</h2>
         * used to populate the profiles data
         * @param corporateProfileData profiles data
         */
        void populateProfiles(ArrayList<CorporateProfileData> corporateProfileData);

        /**
         * <h2>openSetupCorporateScreen</h2>
         * used to open the setup corporate profile screen
         */
        void openSetupCorporateScreen();

        /**
         * <h2>showInstituteWallet</h2>
         * used to hide the payment methods
         */
        void showInstituteWallet(String walletBal);

        /**
         * <h2>setSelectedProfile</h2>
         * dismiss the dialog
         */
        void setSelectedProfile(String corporateProfile, Drawable isCorporate);

        /**
         * <h2>enablePaymentOptions</h2>
         * used to enable the payment options
         */
        void enablePaymentOptions();
        /**
         * <h2>showSelectedDriverPref</h2>
         * used to show the selected driver pref
         * @param driverPref driver pref string
         */
        void showSelectedDriverPref(String driverPref);

        /**
         * <h2>showDriverPref</h2>
         * used to show driver pref dialog
         */
        void showDriverPref();

        /**
         * <h2>showOutstandingDialog</h2>
         * used to show the dialog for the last dues
         */
        void showOutstandingDialog(String title, String businessName, String bookingDate, String pickupAddress);
        /**
         * <h2>confirmToBook</h2>
         * used to indicate user has accepted to pay last dues and book a ride
         */
        void confirmToBook();

        /**
         * <h2>showWalletUseAlert</h2>
         * used to show the wallet alert for wallet usage amount popup
         */
        void showWalletUseAlert(String walletBal, int paymentType);

        void finishRental();
        void setPickAddress(String address);
    }

    interface Presenter {

        /**
         * <h2>fetchRentalPackages</h2>
         * used to call the API for selecting Package Type(renting a car)
         */
        void fetchRentalPackages();

        /**
         * <h2>fetchRentVehicleTypes</h2>
         * used to call the API for selecting Cab Type(renting a car)
         *
         * @param bookingId : passing the package id
         */
        void fetchRentVehicleTypes(String bookingId);

        /**
         * <h2>initializeCompositeDisposable</h2>
         * used to initialize the CompositeDisposable for the API
         */
        void initializeCompositeDisposable();

        /**
         * <h2>updateSelectedVehicle</h2>
         * used to get the Correct Currency Format
         */
        void updateSelectedVehicle(int abbr,String symbol, String offImage,String onImage, double cost,
                                   String vehicleId,String vehicleName,String eta);

        /**
         * <h2>updateSelectedPackage</h2>
         * used to update the selected id
         * @param id package id
         */
        void updateSelectedPackage(String id);

        /**
         * <h2>checkForDefaultPayment</h2>
         * used to check the default payment option
         */
        void checkForDefaultPayment();

        /**
         * <h2>setCashPaymentOption</h2>
         * used to set cash as payment option
         */
        void setCashPaymentOption();

        /**
         * <h2>setCardPaymentOption</h2>
         * used to set card as payment option
         */
        void setCardPaymentOption();

        /**
         * <h2>checkForPaymentOptions</h2>
         * used to check for payment options enabled/disabled
         */
        void checkForPaymentOptions();

        /**
         * <h2>setWalletPaymentOption</h2>
         * used to set wallet as payment option
         */
        void setWalletPaymentOption(String walletText);

        /**
         * <h2>chooseCardWithWallet</h2>
         * used to choose card option with wallet
         * @param toOpenBooking true if we need to open booking screen else false
         */
        void chooseCardWithWallet(boolean toOpenBooking);

        /**
         * <h2>chooseCashWithWallet</h2>
         * used to choose cash option with wallet
         */
        void chooseCashWithWallet(boolean toOpenBooking);

        /**
         * <h2>openRequest</h2>
         * used to open requesting screen
         */
        void openRequest();

        /**
         * <h2>extractData</h2>
         * used to extract data
         * @param bundle bundle
         */
        void extractData(Bundle bundle);

        /**
         * <h2>makeCardAsDefault</h2>
         * used to check for default card
         */
        void makeCardAsDefault();
        /**
         * <h2>handleCapacityChose</h2>
         * used to handle the capaity for the vehicle
         */
        void handleCapacityChose(String capacity);

        /**
         * <h2>getCorporateProfiles</h2>
         * used to get the corporate profiles
         */
        void getCorporateProfiles();

        /**
         * <h2>setSelectedProfile</h2>
         * used to make the profile selected
         * @param selectedProfile slected profile
         */
        void setSelectedProfile(CorporateProfileData selectedProfile);

        /**
         * <h2>getCorporateProfiles</h2>
         * used to get the corporate profiles
         */
        void fetchDriverPreferences();
        /**
         * <h2>addDriverPreferences</h2>
         * used to add the driver preferences
         */
        void addDriverPreferences(boolean isDoneClicked);

        /**
         * <h2>checkForOutstandingAmount</h2>
         * <p>
         *     This method is used for calling API to check if user has last dues
         * </p>
         */
        void checkForOutstandingAmount();

        /**
         * <h2>handleRequestBooking</h2>
         * This method is used to handle the requesting button click
         */
        void handleRequestBooking();

        /**
         * <h2>subscribeConfirmClick</h2>
         * This method is used to subscribe for the location change
         */
         void subscribeConfirmClick();

        void checkRTLConversion();
    }
}
