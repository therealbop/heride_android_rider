package com.karru.landing.home.model;

import android.location.Location;

import com.karru.booking_flow.address.model.FavAddressDataModel;
import com.karru.landing.corporate.CorporateProfileData;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

/**
 * <h1>HomeActivityModel</h1>
 * This class is used to hole the data of the Home page
 * @author  3Embed
 * @since on 16-12-2017.
 */
public class HomeActivityModel
{
    private double vehicleImageWidth, VehicleImageHeight;
    private double driverMarkerWidth , driverMarkerHeight;
    private double currentLatitude ,currentLongitude;
    private double mapCenterLatitude ,mapCenterLongitude;
    private String nearestDriverLatLngEachType = "";
    private double prevMapCenterLatitude , prevMapCenterLongitude;
    private Location lastKnownLocation;
    private boolean moveToCurrLocation;
    private HashMap<String, String> etaOfEachType = new HashMap<>();
    private ArrayList<VehicleTypesDetails> vehicleTypesDetails = new ArrayList<>();
    private String selectedVehicleName = "", selectedVehicleImage = "", selectedVehicleId = null;
    // used to store vehicle type id and its respective vehicle image url
    private HashMap<String, String> driversMarkerIconUrls = new HashMap<>();
    private boolean isFromOnCreateView = false;
    private ArrayList<String> vehicleIdsHavingDrivers = new ArrayList<>();
    private boolean isFavAddress;
    private ArrayList<DriverDetailsModel> driversListSelectedType =new ArrayList<>();
    private boolean favFieldShowing=false;
    private boolean toAnimate;
    private int favoriteType;
    private int bookingType;
    private String amount = "0",timeFare = "0",distFare="0",distance="0",duration="0",estimateId ="0";
    private boolean isDropLocationMandatory;
    private String cardToken , cardLastDigits,cardBrand;
    private int paymentType = 0, payByWallet ;
    private String bookingDate ="",advanceFee = "";
    private String areaZoneId = "",areaZoneTitle="" ,areaPickupId="" ,areaPickupTitle="";
    private String customerName,customerNumber;
    private boolean isMapReady;
    private int isSomeOneElseBooking;
    private String vehicleCapacity,favoriteDriverId="";
    private boolean isWalletSelected ,isPreferenceEnabled;
    private CorporateProfileData selectedProfile;
    private PromoCodeModel promoCodeModel= null;
    private String driverPreference = "";
    private boolean isVehicleAdded = false, isTowingEnabled = false, isRentalEnabled = false;
    private int isPartnerUser; //isPartnerUser: 0- Normal User 1- Hotel user
    private int hotelUserType;  //1- Travel Desk 2-KIOSK;
    private String guestName,guestCountryCode,guestNumber,guestRoomNumber,hotelUserId;

    public String getHotelUserId() {
        return hotelUserId;
    }

    public void setHotelUserId(String hotelUserId) {
        this.hotelUserId = hotelUserId;
    }

    public int isPartnerUser() {
        return isPartnerUser;
    }

    public void setPartnerUser(int partnerUser) {
        isPartnerUser = partnerUser;
    }

    public int getHotelUserType() {
        return hotelUserType;
    }

    public void setHotelUserType(int hotelUserType) {
        this.hotelUserType = hotelUserType;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestCountryCode() {
        return guestCountryCode;
    }

    public void setGuestCountryCode(String guestCountryCode) {
        this.guestCountryCode = guestCountryCode;
    }

    public String getGuestNumber() {
        return guestNumber;
    }

    public void setGuestNumber(String guestNumber) {
        this.guestNumber = guestNumber;
    }

    public String getGuestRoomNumber() {
        return guestRoomNumber;
    }

    public void setGuestRoomNumber(String guestRoomNumber) {
        this.guestRoomNumber = guestRoomNumber;
    }

    public boolean isTowingEnabled() {
        return isTowingEnabled;
    }

    public void setTowingEnabled(boolean towingEnabled) {
        isTowingEnabled = towingEnabled;
    }

    public boolean isRentalEnabled() {
        return isRentalEnabled;
    }

    public void setRentalEnabled(boolean isRentalEnabled) {
        this.isRentalEnabled = isRentalEnabled;
    }

    public boolean isVehicleAdded() {
        return isVehicleAdded;
    }

    public void setVehicleAdded(boolean vehicleAdded) {
        isVehicleAdded = vehicleAdded;
    }

    public boolean isPreferenceEnabled() {
        return isPreferenceEnabled;
    }

    public void setPreferenceEnabled(boolean preferenceEnabled) {
        isPreferenceEnabled = preferenceEnabled;
    }

    public String getDriverPreference() {
        return driverPreference;
    }

    public void setDriverPreference(String driverPreference) {
        this.driverPreference = driverPreference;
    }

    public PromoCodeModel getPromoCodeModel() {
        return promoCodeModel;
    }

    public void setPromoCodeModel(PromoCodeModel promoCodeModel) {
        this.promoCodeModel = promoCodeModel;
    }

    public CorporateProfileData getSelectedProfile() {
        return selectedProfile;
    }

    public void setSelectedProfile(CorporateProfileData selectedProfile) {
        this.selectedProfile = selectedProfile;
    }

    @Inject
    public HomeActivityModel() { }

    public String getFavoriteDriverId() {
        return favoriteDriverId;
    }

    public void setFavoriteDriverId(String favoriteDriverId) {
        this.favoriteDriverId = favoriteDriverId;
    }

    public boolean isWalletSelected() {
        return isWalletSelected;
    }

    public void setWalletSelected(boolean walletSelected) {
        isWalletSelected = walletSelected;
    }

    public int getPayByWallet() {
        return payByWallet;
    }

    public void setPayByWallet(int payByWallet) { this.payByWallet = payByWallet; }

    public int isSomeOneElseBooking() { return isSomeOneElseBooking; }

    public void setSomeOneElseBooking(int someOneElseBooking) { isSomeOneElseBooking = someOneElseBooking; }

    public String getVehicleCapacity() {
        return vehicleCapacity;
    }

    public void setVehicleCapacity(String vehicleCapacity) {
        this.vehicleCapacity = vehicleCapacity;
    }

    public boolean isMapReady() {
        return isMapReady;
    }

    public void setMapReady(boolean mapReady) {
        isMapReady = mapReady;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getAreaZoneId() {
        return areaZoneId;
    }

    public void setAreaZoneId(String areaZoneId) {
        this.areaZoneId = areaZoneId;
    }

    public String getAreaZoneTitle() {
        return areaZoneTitle;
    }

    public void setAreaZoneTitle(String areaZoneTitle) {
        this.areaZoneTitle = areaZoneTitle;
    }

    public String getAreaPickupId() {
        return areaPickupId;
    }

    public void setAreaPickupId(String areaPickupId) {
        this.areaPickupId = areaPickupId;
    }

    public String getAreaPickupTitle() {
        return areaPickupTitle;
    }

    public void setAreaPickupTitle(String areaPickupTitle) {
        this.areaPickupTitle = areaPickupTitle;
    }

    public String getAdvanceFee() {
        return advanceFee;
    }

    public void setAdvanceFee(String advanceFee) {
        this.advanceFee = advanceFee;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(String cardBrand) {
        this.cardBrand = cardBrand;
    }

    public String getEstimateId() {
        return estimateId;
    }

    public void setEstimateId(String estimateId) {
        this.estimateId = estimateId;
    }

    public String getCardToken() {
        return cardToken;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public String getCardLastDigits() {
        return cardLastDigits;
    }

    public void setCardLastDigits(String cardLastDigits) {
        this.cardLastDigits = cardLastDigits;
    }

    public boolean isDropLocationMandatory() {
        return isDropLocationMandatory;
    }

    public void setDropLocationMandatory(boolean dropLocationMandatory) {
        isDropLocationMandatory = dropLocationMandatory;
    }

    public String getAmount() {
        return amount;
    }

    public String getTimeFare() {
        return timeFare;
    }

    public String getDistFare() {
        return distFare;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setDistFare(String distFare) {
        this.distFare = distFare;
    }

    public void setTimeFare(String timeFare) {
        this.timeFare = timeFare;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getBookingType() {
        return bookingType;
    }

    public void setBookingType(int bookingType) {
        this.bookingType = bookingType;
    }

    public int getFavoriteType() {
        return favoriteType;
    }

    public void setFavoriteType(int favoriteType) {
        this.favoriteType = favoriteType;
    }

    public boolean isFavFieldShowing() {
        return favFieldShowing;
    }

    public void setFavFieldShowing(boolean favFieldShowing) {
        this.favFieldShowing = favFieldShowing;
    }

    public boolean isToAnimate() {
        return toAnimate;
    }

    public void setToAnimate(boolean toAnimate) {
        this.toAnimate = toAnimate;
    }

    public HashMap<String, String> getDriversMarkerIconUrls() {
        return driversMarkerIconUrls;
    }

    public ArrayList<FavAddressDataModel> getFavAddressDataList() {
        return favAddressDataList;
    }

    public void setFavAddressDataList(ArrayList<FavAddressDataModel> favAddressDataList) {
        this.favAddressDataList = favAddressDataList;
    }

    private ArrayList<FavAddressDataModel> favAddressDataList = new ArrayList<>();

    public ArrayList<VehicleTypesDetails> getVehicleTypesDetails() {
        return vehicleTypesDetails;
    }

    public void setVehicleTypesDetails(ArrayList<VehicleTypesDetails> vehicleTypesDetails) {
        this.vehicleTypesDetails = vehicleTypesDetails;
    }

    public HashMap<String, String> getEtaOfEachType() {
        return etaOfEachType;
    }

    public boolean isMoveToCurrLocation() {
        return moveToCurrLocation;
    }

    public void setMoveToCurrLocation(boolean moveToCurrLocation) {
        this.moveToCurrLocation = moveToCurrLocation;
    }

    public Location getLastKnownLocation() {
        return lastKnownLocation;
    }

    public void setLastKnownLocation(Location lastKnownLocation) {
        this.lastKnownLocation = lastKnownLocation;
    }

    public double getPrevMapCenterLatitude() {
        return prevMapCenterLatitude;
    }

    public void setPrevMapCenterLatitude(double prevMapCenterLatitude) {
        this.prevMapCenterLatitude = prevMapCenterLatitude;
    }

    public double getPrevMapCenterLongitude() {
        return prevMapCenterLongitude;
    }

    public void setPrevMapCenterLongitude(double prevMapCenterLongitude) {
        this.prevMapCenterLongitude = prevMapCenterLongitude;
    }

    public double getMapCenterLatitude() {
        return mapCenterLatitude;
    }

    public void setMapCenterLatitude(double mapCenterLatitude) {
        this.mapCenterLatitude = mapCenterLatitude;
    }

    public double getMapCenterLongitude() {
        return mapCenterLongitude;
    }

    public void setMapCenterLongitude(double mapCenterLongitude) {
        this.mapCenterLongitude = mapCenterLongitude;
    }

    public double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public String getNearestDriverLatLngEachType() {
        return nearestDriverLatLngEachType;
    }

    public void setNearestDriverLatLngEachType(String nearestDriverLatLngEachType) {
        this.nearestDriverLatLngEachType = nearestDriverLatLngEachType;
    }

    public String getSelectedVehicleName() {
        return selectedVehicleName;
    }

    public void setSelectedVehicleName(String selectedVehicleName) {
        this.selectedVehicleName = selectedVehicleName;
    }
    public String getSelectedVehicleImage() {
        return selectedVehicleImage;
    }

    public void setSelectedVehicleImage(String selectedVehicleImage) {
        this.selectedVehicleImage = selectedVehicleImage;
    }

    public String getSelectedVehicleId() {
        return selectedVehicleId;
    }

    public void setSelectedVehicleId(String selectedVehicleId) {
        this.selectedVehicleId = selectedVehicleId;
    }
    public boolean isFromOnCreateView() {
        return isFromOnCreateView;
    }

    public void setFromOnCreateView(boolean fromOnCreateView) {
        isFromOnCreateView = fromOnCreateView;
    }
    public ArrayList<String> getVehicleIdsHavingDrivers() {
        return vehicleIdsHavingDrivers;
    }

    public boolean isFavAddress() {
        return isFavAddress;
    }

    public void setFavAddress(boolean favAddress) {
        isFavAddress = favAddress;
    }
    public ArrayList<DriverDetailsModel> getDriversListSelectedType()
    {
        return driversListSelectedType;
    }

    public void setDriversListSelectedType(ArrayList<DriverDetailsModel> driversListSelectedType) {
        this.driversListSelectedType = driversListSelectedType;
    }

    public double getVehicleImageWidth() {
        return vehicleImageWidth;
    }

    public void setVehicleImageWidth(double vehicleImageWidth) {
        this.vehicleImageWidth = vehicleImageWidth;
    }

    public double getVehicleImageHeight() {
        return VehicleImageHeight;
    }

    public void setVehicleImageHeight(double vehicleImageHeight) {
        this.VehicleImageHeight = vehicleImageHeight;
    }

    public double getDriverMarkerWidth() {
        return driverMarkerWidth;
    }

    public void setDriverMarkerWidth(double driverMarkerWidth) {
        this.driverMarkerWidth = driverMarkerWidth;
    }

    public double getDriverMarkerHeight() {
        return driverMarkerHeight;
    }

    public void setDriverMarkerHeight(double driverMarkerHeight) {
        this.driverMarkerHeight = driverMarkerHeight;
    }
}
