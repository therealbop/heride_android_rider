package com.karru.landing.home;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.karru.booking_flow.ride.request.model.RequestBookingDetails;
import com.karru.countrypic.CountryPicker;
import com.karru.landing.corporate.CorporateProfileData;
import com.karru.managers.location.LocationCallBack;
import com.karru.landing.home.model.OnGoingBookingsModel;
import com.karru.landing.home.model.PickUpGates;
import com.karru.landing.home.model.VehicleTypesDetails;
import com.karru.booking_flow.invoice.model.ReceiptDetails;
import com.karru.util.path_plot.LatLongBounds;

import java.util.ArrayList;

/**
 * <h1>HomeFragmentContract</h1>
 * This interface is used to hide the implementation for presenter and View
 * @author 3Embed
 * @since on 16-12-2017.
 */
public interface HomeFragmentContract
{
    interface View extends LocationCallBack.View
    {
        /**
         * <h2>updateCameraPosition</h2>
         * This method is addDriverPreferencesused to update the map camera position to the location
         * @param latitude latitude to move the center of map
         * @param longitude longitude to move the center of map
         */
        void updateCameraPosition(Double latitude, Double longitude);

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
         * <h2>onVehicleTypesFound</h2>
         * This method is called when the vehicle types more than 0
         */
        void onVehicleTypesFound();
        /**
         * <h2>onVehicleTypesNotFound</h2>
         * This method is called when the vehicle types size is 0
         */
        void onVehicleTypesNotFound();

        /**
         * <h2>enableNowBooking</h2>
         * This method is used to enable the now booking button
         */
        void enableNowBooking();
        /**
         * <h2>disableNowBooking</h2>
         * This method is used to disable the now booking button
         */
        void disableNowBooking();

        /**
         * <h2>enableFavAddress</h2>
         * This method is used to enable the pick up location as fav address
         * @param favAddressName fav address name to be set
         *                       @param isToAnimate is to animate
         */
        void enableFavAddress(String favAddressName, boolean isToAnimate);
        /**
         * <h2>disableFavAddress</h2>
         * This method is used to disable the pick up location as fav address
         * <p>
         *     to clear the fav address title edit text
         * </p>
         * @param pickUpAddress pick address name to be set
         */
        void disableFavAddress(String pickUpAddress);

        /**
         * <h2>updateEachVehicleTypeETA</h2>
         * This method is used to update the eta of each vehicle type
         */
        void updateEachVehicleTypeETA();

        /**
         * <h2>openPickAddressScreen</h2>
         * This method is triggered when the address changes when click of address field
         */
        void openPickAddressScreen();

        /**
         * <h2>showToast</h2>
         * This method is used to show the toast
         * @param message message to be shown
         */
        void showToast(String message);

        /**
         * <h2>setETAOfEachVehicleType</h2>
         * This method is used to set the ETA of each vehicle type
         */
        void setETAOfEachVehicleType(int position, String etaOfNearestDriver);
        /**
         * <h2>setVehicleTypesDetails</h2>
         * This method is used to set the each vehicle types
         * @param position position of the vehicle type
         * @param size size of the vehicle types
         * @param vehicleItem each vehicle details
         * @param vehicleViewClicked view for the vehicle item clicked
         * @param ETA eta of each vehicle type
         */
        void setVehicleTypes(int position, int size, VehicleTypesDetails vehicleItem, String ETA,
                             boolean vehicleViewClicked);
        /**
         * <h2>showRideRateCard</h2>
         * <p>
         *     dialog for fare detail on double click of vehicle icon
         * </p>
         * */
        void showRideRateCard();

        /**
         * <h2>showShipmentRateCardDialog</h2>
         * <p>
         *     dialog for fare detail on double click of vehicle icon
         * </p>
         * @param rateCardDetails rate card details
         *                        @param vehicleTypesModel vehicle details for shipment
         * */
        void showShipmentRateCardDialog(final VehicleTypesDetails vehicleTypesModel, String... rateCardDetails);

        /**
         * <h2>plotOnlineDriverMarkers</h2>
         * This method is used to plot the online drivers markers on google Map
         */
        void plotOnlineDriverMarkers(String driverId, LatLng latLng, double driverMarkerWidth, double driverMarkerHeight,
                                     Bitmap driverMarkerImageUrl, long interval);
        /**
         * <h2>moveGoogleMapToLocation</h2>
         * Method is used to move the google map to new location
         * @param newLatitude: selected address latitude
         * @param newLongitude: selected address longitude
         */
        void moveGoogleMapToLocation(double newLatitude, double newLongitude);

        /**
         * <h2>showAppUpdateAlert</h2>
         * This method is used to show the update alert
         * @param mandateUpdate flag whether the update is mandatory
         */
        void showAppUpdateAlert(boolean mandateUpdate);

        /**
         * <h2>handleFavAddressUI</h2>
         * This method is used to handle the fav address UI
         */
        void handleFavAddressUI();

        /**
         * <h2>handleNonFavAddressUI</h2>
         * This method is used to handle the non fav address UI
         */
        void handleNonFavAddressUI();

        /**
         * <h>startAnimationWhenMapMoves</h>
         * method called when the google map started moving
         */
        void startAnimationWhenMapMoves();
        /**
         * <h>startAnimationWhenMapStops</h>
         * method called when the google map stops moving
         */
        void startAnimationWhenMapStops();

        /*Ride Booking */

        /**
         * <h2>openConfirmScreen</h2>
         * This method is used to open the confirm screen for ride
         * @param pickLat pick up latitude
         *                @param pickLong pickup longitude
         */
        void openConfirmScreen(double pickLat, double pickLong, String confirmText);
        /**
         * <h2>changePickAddressUI</h2>
         * This method is used to show the UI for pick address change
         * @param pickLat pick up latitude
         *                @param pickLang pickup longitude
         *                                @param pickAddress pick up address
         */
        void changePickAddressUI(String pickAddress, double pickLat, double pickLang);
        /**
         * <h2>changeDropAddressUI</h2>
         * This method is used to show the UI for drop address change
         * @param pickLat pick up latitude
         *                @param pickLang pickup longitude
         *                                @param pickAddress pick up address
         */
        void changeDropAddressUI(String pickAddress, double dropLat, double dropLang,
                                 String pickLat, String pickLang, String confirmText);

        /**
         * <h2>addLatLongBounds</h2>
         * This method is used to add the bounds to lat longs for source and destination
         * @param isToPlotPath is to plot the path
         */
        void addLatLongBounds(boolean isToPlotPath);

        /**
         * <h2>showFareEstimation</h2>
         * This method is used to show the approx fare
         */
        void showFareEstimation(ArrayList<ReceiptDetails> listOfBreakDown, String amount);

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
         * <h2>enableOnlyNowBookingType</h2>
         * This method is used to activate the now booking
         */
        void enableOnlyNowBookingType();
        /**
         * <h2>enableOnlyLaterBookingType</h2>
         * This method is used to activate the later booking
         */
        void enableOnlyLaterBookingType();
        /**
         * <h2>enableOnlyLaterBookingType</h2>
         * This method is used to activate both the booking types
         */
        void enableBothBookingType();

        /**
         * <h2>openRequestingScreenNormal</h2>
         * This method is used to open the requesting screen
         * @param bundle bundle with data to be sent to request screen
         */
        void openRequestingScreenNormal(Bundle bundle);

        /**
         * <h2>openRequestingScreenKilled</h2>
         * This method is used to open the requesting screen
         * @param requestBookingDetailsModel bundle with data to be sent to request screen
         */
        void openRequestingScreenKilled(RequestBookingDetails requestBookingDetailsModel);

        /**
         * <h2>notifyOnGoingBookings</h2>
         * This method is used to notify for the change in on going booking details list
         * @param onGoingBookingsModels  on going booking details list
         */
        void notifyOnGoingBookings(ArrayList<OnGoingBookingsModel> onGoingBookingsModels);

        /**
         * <h2>notifyZonePickups</h2>
         * This method is used to notify for the change in zone pickup points
         * @param pickUpGates  pickup zone points list
         */
        void notifyZonePickups(ArrayList<PickUpGates> pickUpGates);

        /**
         * <h2>notifyOnGoingItemDetails</h2>
         * This method is used to notify adapter with the row
         * @param onGoingBookingsModels on going booking detail row
         */
        void notifyOnGoingItemDetails(int pos, OnGoingBookingsModel onGoingBookingsModels);

        /**
         * <h2>openLiveTrackingScreen</h2>
         * This method is used to open the live tracking screen
         * @param bookingId booking Id to get the booking details
         */
        void openLiveTrackingScreen(String bookingId);

        /**
         * <h2>enableBooking</h2>
         * This method is used to enable the booking button to book a ride
         */
        void enableBooking();

        /**
         * <h2>disableBooking</h2>
         * This method is used to disable the booking button to book a ride
         * @param reason 1 for payment 2 for vehicle Id
         */
        void disableBooking(int reason);

        /**
         * <h2>disableCashOption</h2>
         * used to disable cash option
         */
        void disableCashOption();
        /**
         * <h2>disableCardOption</h2>
         * used to disable card option
         */
        void disableCardOption(boolean disable);
        /**
         * <h2>disableWalletOption</h2>
         * used to disable waller option
         */
        void disableWalletOption();

        /**
         * <h2>hideWalletOption</h2>
         * used to hide wallet option
         */
        void hideWalletOption();

        /**
         * <h2>disableWalletOption</h2>
         * used to disable waller option
         */
        void enableWalletOption(String amount);

        /**
         * <h2>openPaymentScreen</h2>
         * used to open the payment screen
         */
        void openPaymentScreen();

        /**
         * <h2>setSelectedCard</h2>
         * used to set the last digits of card in UI
         * @param lastDigits last 4 digits of card
         */
        void setSelectedCard(String lastDigits, String cardBrand);

        /**
         * <h2>setCashPaymentOption</h2>
         * used to set cash as payment option
         */
        void setCashPaymentOption();

        /**
         * <h2>setWalletPaymentOption</h2>
         * used to set wallet as payment option
         */
        void setWalletPaymentOption(String walletText);

        /**
         * <h2>addWalletMoneyLayout</h2>
         * used to set wallet recharge
         */
        void addWalletMoneyLayout();

        /**
         * <h2>showDriverCancelDialog</h2>
         * show driver cancel dialog
         * @param message message to be shown
         */
        void showDriverCancelDialog(String message);

        /**
         * <h2>showDefaultCard</h2>
         * used to shwo the default card
         * @param lastDigits last digits of card
         * @param cardBrand card brand
         *                  @param paymentType payment type default
         */
        void showDefaultCard(String lastDigits, String cardBrand, int paymentType, boolean isWalletSelected);

        /**
         * <h2>confirmToBook</h2>
         * used to indicate user has accepted to pay last dues and book a ride
         */
        void confirmToBook();
        /**
         * <h2>clearConfirmationScreen</h2>
         * getActivity() method is used to clear the confirmation screen
         */
        void clearConfirmationScreen();

        /**
         * <h2>showOutstandingDialog</h2>
         * used to show the dialog for the last dues
         */
        void showOutstandingDialog(String title, String businessName, String bookingDate, String pickupAddress);

        /**
         * <h2>setMinDateToPicker</h2>
         * used to set the min date in picker
         * @param hour hour
         * @param minute minute
         * @param day day
         * @param month month
         * @param year year
         */
        void setMinDateToPicker(int day, int month, int year, int hour, int minute);

        /**
         * <h2>showScheduleView</h2>
         * this method is uesed to show the later bookig view
         */
        void showScheduleView(String dateToBeShown);

        /**
         * <h2>showScheduleView</h2>
         * this method is uesed to show the later bookig view
         */
        void hideScheduleView();

        /**
         * <h2>plotCurrentZone</h2>
         * used to plot the current zone
         * @param polygonOptions polygon properties
         */
        void plotCurrentZone(PolygonOptions polygonOptions, String zoneTitle, String currentZoneId);

        /**
         * <h2>clearCurrentZone</h2>
         * used to clear current zone
         */
        void clearCurrentZone();

        /**
         * <h2>addMarkerForPickup</h2>
         * used to add the marker for pickup zone
         * @param latLng latlong for the position of pickup
         */
        void addMarkerForPickup(LatLng latLng, String pickZoneId);

        /**
         * <h2>nearestPickupGate</h2>
         * used to change the marker for selected gate
         * @param pickupId pick up id
         */
        void nearestPickupGate(String pickupId, String pickTitle);

        /**
         * <h2>removeOfflineDriverMarker</h2>
         * used to remove the driver which became offline
         * @param driverId driver Id of offline driver
         */
        void removeOfflineDriverMarker(String driverId);

        /**
         * <h2>showSomeOneBookingLayout</h2>
         * used to show the come one booking layout
         */
        void showSomeOneBookingLayout(int bookingType);

        /**
         * <h2>showLaterBookingTimer</h2>
         * used to show the later booking timer
         */
        void showLaterBookingTimer(boolean change);

        /**
         * <h2>askContactPermission</h2>
         * used to ask permission and open directory
         */
        void askContactPermission();

        /**
         * <h2>populateUserDetails</h2>
         * used to populate the user details
         * @param phNumber phone number of usr
         * @param userName user name
         * @param imageUrl user image url
         */
        void populateUserDetails(String phNumber, String userName, String imageUrl);
        /**
         * <h2>populateSeatsSpinner</h2>
         * used to populate the capacity for the user
         * @param maxCapacity max capacity for user
         */
        void populateSeatsSpinner(int maxCapacity);
        /**
         * <h>Add contact on Server</h>
         * <p>thi method is using to add Contact at server</p>
         * @param name name to save
         * @param number number to save
         */
        void addContact(String name, String number);

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
         * <h2>hideBottomConfirm</h2>
         * used to hide the bottom layout
         */
        void hideBottomConfirm();

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
         * <h2>checkForWallet</h2>
         * used to check whether wallet contact tab needs to be shown
         * @param show true if needs to show else false
         */
        void checkForWallet(boolean show, String walletBal);

        /**
         * <h2>checkForCard</h2>
         * used to check whether cards tab needs to be shown
         * @param show true if needs to show else false
         */
        void checkForCard(boolean show);

        /**
         * <h2>checkForFavorite</h2>
         * used to check whether cards tab needs to be shown
         * @param show true if needs to show else false
         */
        void checkForFavorite(boolean show);

        /**
         * <h2>checkForCorporate</h2>
         * used to check whether corporate tab needs to be shown
         * @param show true if needs to show else false
         */
        void checkForCorporate(boolean show);

        /**
         * <h2>checkForReferral</h2>
         * used to check whether referral tab needs to be shown
         * @param show true if needs to show else false
         */
        void checkForReferral(boolean show);

        /**
         * <h2>checkForTowing</h2>
         * used to check whether referral tab needs to be shown
         * @param show true if needs to show else false
         */
        void checkForTowing(boolean show);

        /**
         * <h2>googlePathPlot</h2>
         * used to plot the path
         * @param latLongBounds route configurations
         */
        void googlePathPlot(LatLongBounds latLongBounds);

        /**
         * <h2>showSelectedDriverPref</h2>
         * used to show the selected driver pref
         * @param driverPref driver pref string
         */
        void showSelectedDriverPref(String driverPref);

        /**
         * <h2>showWalletExcessAlert</h2>
         * used to show the wallet alert excess amount popup
         */
        void showWalletExcessAlert(int paymentType);

        /**
         * <h2>showWalletUseAlert</h2>
         * used to show the wallet alert for wallet usage amount popup
         */
        void showWalletUseAlert(String walletBal, int paymentType);

        /**
         * <h2>showHardLimitAlert</h2>
         * used to show the hard limit reach alert
         */
        void showHardLimitAlert();

        /**
         * <h2>openDropAddressScreen</h2>
         * used to open the address selection screen
         */
        void openDropAddressScreen();

        /**
         * <h2>setWalletAmount</h2>
         * used to set the wallet amoubnt
         * @param walletAmount amount
         */
        void setWalletAmount(String walletAmount);

        /**
         * <h2>populateProfiles</h2>
         * used to populate the profiles data
         * @param corporateProfileData profiles data
         */
        void populateProfiles(ArrayList<CorporateProfileData> corporateProfileData);

        /**
         * <h2>setSelectedProfile</h2>
         * dismiss the dialog
         */
        void setSelectedProfile(String corporateProfile, Drawable isCorporate);

        /**
         * <h2>showAlertForOutZone</h2>
         * used to show the alert for out zone
         * @param message message to be shown
         */
        void showAlertForOutZone(String message);

        /**
         * <h2>openSetupCorporateScreen</h2>
         * used to open the setup corporate profile screen
         */
        void openSetupCorporateScreen();

        /**
         * <h2>notifyAdapterWithFavDrivers</h2>
         * used to notify the adapter with fav drivers list
         */
        void notifyAdapterWithFavDrivers();

        /**
         * <h2>showFavDriversDialog</h2>
         * used to show the fav drivers data
         */
        void showFavDriversDialog(int bookingType);

        /**
         * <h2>invalidPromo</h2>
         * used to show invalid promo code
         * @param error error text to be shown
         */
        void invalidPromo(String error);

        /**
         * <h2>validPromo</h2>
         * used to show valid promo code
         */
        void validPromo(String promoCode);

        /**
         * <h2>setPromoCode</h2>
         * used to set the promo code
         * @param promo promo code
         */
        void setPromoCode(String promo);

        /**
         * <h2>showInstituteWallet</h2>
         * used to hide the payment methods
         */
        void showInstituteWallet(String walletBal);

        /**
         * <h2>showCorporate</h2>
         * used to show the corporate methods
         */
        void showCorporate();
        /**
         * <h2>hideCorporate</h2>
         * used to hide the corporate methods
         */
        void hideCorporate();

        /**
         * <h2>showDriverPreferenceView</h2>
         * used to show the driver preference view
         */
        void showDriverPreferenceView();
        /**
         * <h2>hideDriverPreferenceView</h2>
         * used to hide the driver preference view
         */
        void hideDriverPreferenceView();

        /**
         * <h2>enablePaymentOptions</h2>
         * used to enable the payment options
         */
        void enablePaymentOptions();

        /**
         * <h2>showDriverPref</h2>
         * used to show driver pref dialog
         */
        void showDriverPref();

        /**
         * <h2>showSeatsForRide</h2>
         * used to show seats for ride
         */
        void showSeatsForRide();

        /**
         * <h2>showMyVehiclesForTowing</h2>
         * used to show vehicles for towing
         */
        void showMyVehiclesForTowing(String defaultVehicle);

        /**
         * <h2>showHotelUI</h2>
         * used to show the hotel UI
         */
        void showHotelUI(String hotelLogo,String hotelName);

        /**
         * <h2>hideHotelUI</h2>
         * used to hide the hotel UI
         */
        void hideHotelUI();

        /**
         * <h2>onGettingOfCountryInfo</h2>
         * This method is used to get the country Info
         * @param countryFlag flag of the country
         * @param countryCode country code
         */
        void onGettingOfCountryInfo(int countryFlag, String countryCode, boolean isDefault);

        /**
         * <h2>onInvalidMobile</h2>
         * This method is triggered when the phone number is invalid
         * @param errorMsg cause for error
         */
        void onInvalidMobile(String errorMsg);

        /**
         * <h2>openRentalScreen</h2>
         * This method is to open rental screen
         */
        void openRentalScreen(Bundle bundle);
    }
    interface Presenter extends LocationCallBack
    {
        /**
         * <h2>getCurrentLocation</h2>
         * This methos is used to get the current location of user
         */
        void getCurrentLocation();
        /**
         * <h2>handleHomeBackgroundState</h2>
         * This method is called to handle if the home goes into background
         */
        void handleHomeBackgroundState();

        /**
         * <h2>prepareToGetETA</h2>
         * This method is used to prepare to ge the ETA
         */
        void prepareToGetETA();

        /**
         * <h2>onCreateHomeActivity</h2>
         * This method is called when we enter Home onCreate of Home
         */
        void onCreateHomeActivity();
        /**
         * <h2>onResumeActivity</h2>
         * This method is called when the onResume of home page is called
         */
        void onResumeHomeActivity();

        /**
         * <h2>verifyAndUpdateNewLocation</h2>
         * This method is used to get the location center of map
         * @param centerFromPoint latlong from center of map
         */
        void verifyAndUpdateNewLocation(LatLng centerFromPoint);
        /**
         * <gh2>fetchVehicleImageWidth</gh2>
         * This method is used to get the width vehicle Image
         * @return returns the width of vehicle Image
         */
        double fetchVehicleImageWidth();
        /**
         * <gh2>fetchVehicleImageHeight</gh2>
         * This method is used to get the height vehicle Image
         * @return returns the height of vehicle Image
         */
        double fetchVehicleImageHeight();
        /**
         * <h2>handleClickEventForBooking</h2>
         * This method is used for sending control to the next Activity where we will select the pickup address.
         */
        void handleClickEventForBooking(final int rideType, String dateToBeShown,
                                        String dateToBeSent, boolean toShowAddress);
        /**
         * <h2>handleClickEventForAddress</h2>
         * This method is used for opening the drop address activity.
         */
        void handleClickEventForAddress(final int rideType);
        /**
         * <h2>addAsFavAddress</h2>
         * This method is used to add the address as favorite
         * @param favAddress title of the favorite
         */
        void addAsFavAddress(final String favAddress);
        /**
         * <h2>handleCurrLocationButtonClick</h2>
         * This method is used to notify the curr location homepage
         * @param confirmScreen is confirm screen visible
         */
        void handleCurrLocationButtonClick(boolean confirmScreen);
        /**
         * <h>refreshFavAddressList</h>
         * to set the fav address list
         * @param toRefreshAddress if true then refresh the address
         * @since v1.0
         */
        void refreshFavAddressList(boolean toRefreshAddress);

        /**
         * <h2>validateFavAddress</h2>
         * This method is used to check whether the added address is fav
         */
        void validateFavAddress();
        /**
         * <h2>checkForETAOfVehicleTypes</h2>
         * This method is used to check for the eta of nearest driver of each vehicle types
         */
        void checkForETAOfVehicleTypes();
        /**
         * <h2>checkForVehicleTypes</h2>
         * This method is used to check for the vehicle types
         */
        void checkForVehicleTypes();

        /**
         * <h2>handleClickOfVehicleType</h2>
         * This method is used to handle the click of each vehicle type
         * @param vehicleId vehicle ID of clicked vehicle
         * @param typeItemLoc vehicleType item
         */
        void handleClickOfVehicleType(VehicleTypesDetails typeItemLoc, String vehicleId);

        /**
         * <h2>findCurrentLocation</h2>
         * This method is used to find the current location
         */
        void findCurrentLocation();

        /**
         * <h2>triggerVehicleDetails</h2>
         * This method is used to trigger for getting the vehicle details from MQTT
         * @param restartTimer flag for whether to restart the timer
         */
        void triggerVehicleDetails(boolean restartTimer);

        /**
         * <h2>disposeObservables</h2>
         * This method is used to dispose the observables
         */
        void disposeObservables();

        /**
         * <h2>checkForFavAddress</h2>
         * This method is used to check for the fav address
         * @param isToSetAsFavAddress is to set the fav address
         *                            @param isToAnimate check for animate true to animate
         */
        void checkForFavAddress(boolean isToSetAsFavAddress, boolean isToAnimate);

        /**
         * <h2>handleGoogleMapIdle</h2>
         * This method is used to handle the google map idle
         */
        void handleGoogleMapIdle();
        /**
         * <h2>handleGoogleMapStartedMove</h2>
         * This method is used to handle the google map move started
         * @param reason reason for move
         */
        void handleGoogleMapStartedMove(int reason);

        /**
         * <h2>storeFirstTimeAnimation</h2>
         * This method is used to store the 1st time animation false
         * @param firstTime is first time animation true if first time
         */
        void storeFirstTimeAnimation(boolean firstTime);

        /**
         * <h2>setFavoriteType</h2>
         * This method is used to set the fav type
         * @param favoriteType type of favorite
         *                     1 for home
         *                     2 for work
         *                     3 for others
         */
        void setFavoriteType(int favoriteType);

        /**
         * <h2>handleResultFromIntents</h2>
         * This method is used to hanlde the on activity result of the activity
         * @param requestCode result code
         * @param latitude latitude
         * @param longitude longitude
         * @param address address
         */
        void handleResultFromIntents(int requestCode, int resultCode, String latitude, String longitude, String address,
                                     Bundle bundle);

        /**
         * <h2>clearDropAddress</h2>
         * This method is used to clear the drop address
         */
        void clearDropAddress();
        /**
         * <h2>getApproxFareEstimation</h2>
         * <p>
         *     This method is used for getting the fare estimation .
         * </p>
         */
        void getApproxFareEstimation();

        /**
         * <h2>checkForBookingType</h2>
         * This method is used to check for booking types present
         * @param vehicleItem vehicle item details
         */
        void checkForBookingType(VehicleTypesDetails vehicleItem);

        /**
         * <h2>handleRequestBooking</h2>
         * This method is used to handle the requesting button click
         */
        void handleRequestBooking();

        /**
         * <h2>checkForPaymentOptions</h2>
         * used to check for payment options enabled/disabled
         */
        void checkForPaymentOptions();
        /**
         * <h2>setCashPaymentOption</h2>
         * used to set cash as payment option
         */
        void setCashPaymentOption();

        /**
         * <h2>setWalletPaymentOption</h2>
         * used to set wallet as payment option
         */
        void setWalletPaymentOption(String walletText);

        /**
         * <h2>setCardPaymentOption</h2>
         * used to set card as payment option
         */
        void setCardPaymentOption();

        /**
         * <h2>checkForOutstandingAmount</h2>
         * <p>
         *     This method is used for calling API to check if user has last dues
         * </p>
         */
        void checkForOutstandingAmount();

        /**
         * <h2>calculateMinDate</h2>
         * used to set the min date to be set to picker
         */
        void calculateMinDate();

        /**
         * <h2>setPickUpZoneDetails</h2>
         * @param pickId
         * @param pickTttle
         */
        void setPickUpZoneDetails(String pickId, String pickTttle);

        /**
         * <h>Attach CallbakView</h>
         * <p>this method is using to attach the view callback object to presenter</p>
         * @param view reference to View
         */
        void attachView(Object view);

        /**
         * <h>Detach View</h>
         * <p>this method is using to detach the object what we gave in atttach view to avoid possible memory leak</p>
         */
        void detachView();

        /**
         * <h2>checkSomeOneBookingEligible</h2>
         * used to validate if some one booking is enabled
         */
        void checkSomeOneBookingEligible(int bookingType, boolean change);

        /**
         * <h2>checkForBookingType</h2>
         * used to check for booking type
         * @param bookingType booking type
         */
        void checkForBookingType(int bookingType, boolean change);

        /**
         * <h2>setSomeOneDetails</h2>
         * used to set the details in model
         * @param name name of other customer
         * @param phNumber number of other customer
         */
        void setSomeOneDetails(String name, String phNumber, int bookingType);

        /**
         * <h2>getUserDetails</h2>
         * used to get the used details
         */
        void getUserDetails();
        /**
         * <h2>setMapReady</h2>
         * used to check if the google MAp Ready
         * @param ready true if ready
         */
        void setMapReady(boolean ready);
        /**
         * <h2>handleCapacityChose</h2>
         * used to handle the capaity for the vehicle
         */
        void handleCapacityChose(String capacity);
        /**
         * <h2>validateMobileNumber</h2>
         * used to validate the mobile number
         * @param stringToBeValidated string to be validated
         */
        void validateMobileNumber(String stringToBeValidated, String name, ArrayList<String> listOfContacts);

        /**
         * <h2>makeCardAsDefault</h2>
         * used to check for default card
         */
        void makeCardAsDefault();

        /**
         * <h2>plotPathRoute</h2>
         * used to plot the path between origin and destination
         * @param origin origin lat lng
         * @param dest dest lat lng
         */
        void plotPathRoute(LatLng origin, LatLng dest);

        /**
         * <h2>addDriverPreferences</h2>
         * used to add the driver preferences
         */
        void addDriverPreferences(boolean isDoneClicked);

        /**
         * <h2>checkForDefaultPayment</h2>
         * used to check the default payment option
         */
        void checkForDefaultPayment();

        /**
         * <h2>chooseCashWithWallet</h2>
         * used to choose cash option with wallet
         */
        void chooseCashWithWallet(boolean toOpenBooking);

        /**
         * <h2>chooseCardWithWallet</h2>
         * used to choose card option with wallet
         * @param toOpenBooking true if we need to open booking screen else false
         */
        void chooseCardWithWallet(boolean toOpenBooking);
        /**
         * <h2>openRequest</h2>
         * used to open requesting screen
         */
        void openRequest();

        /**
         * <h2>getCorporateProfiles</h2>
         * used to get the corporate profiles
         */
        void getCorporateProfiles();

        /**
         * <h2>getCorporateProfiles</h2>
         * used to get the corporate profiles
         */
        void fetchDriverPreferences();

        /**
         * <h2>setSelectedProfile</h2>
         * used to make the profile selected
         * @param selectedProfile slected profile
         */
        void setSelectedProfile(CorporateProfileData selectedProfile);

        /**
         * <h2>chooseAllFavDrivers</h2>
         * choose all fav drivers
         */
        void chooseAllFavDrivers(int bookingType);
        /**
         * <h2>chooseAFavDriver</h2>
         * choose one fav drivers
         */
        void chooseAFavDriver(String masterId, int bookingType);

        /**
         * <h2>chooseAllDrivers</h2>
         * used to choose all drivers
         */
        void chooseAllDrivers(int bookingType);

        /**
         *<h2>validatePromoCode</h2>
         * <p>
         *     method to make an api call to validate promo code
         * </p>
         * @param promoCode: promo code to be validated
         */
        void validatePromoCode(String promoCode);

        /**
         * <h2>getSavedPromo</h2>
         * used to get the saved promo
         */
        String getSavedPromo();

        /**
         * <h2>handleFinalBreakDown</h2>
         * used to handle the breakdown of fare
         */
        void handleFinalBreakDown();

        /**
         * <h2>checkForDefaultVehicle</h2>
         * used to check for default vehicle
         */
         void checkForDefaultVehicle();

         /**
         * <h2>checkForGuestData</h2>
         * used to check for guest data
         */
         void checkForGuestData(String guestName,String guestNumber,String guestRoomNo,String countryCode,
                                int visibility);

        /**
         * <h2>getCountryInfo</h2>
         * This method is used to get the country information
         * @param countryPicker country picker
         */
        void getCountryInfo(CountryPicker countryPicker);

        /**
         * <h2>validateMobileField</h2>
         * This method is used to validate the mobile number field
         * @param mobileNumber number to be validated
         */
        void validateMobileNumber(String mobileNumber, String countryCode);

        /**
         * <h2>addListenerForCountry</h2>
         * This method is used to add the listener for events for picker
         */
        void addListenerForCountry(CountryPicker countryPicker);
    }
}
