package com.karru.rental.model;

import com.karru.landing.corporate.CorporateProfileData;
import com.karru.landing.home.model.PromoCodeModel;

/**
 * <h1>RentalModel</h1>
 * used tp store the date of rental
 */
public class RentalModel {
    private CorporateProfileData selectedProfile;
    private String selectedPackageId = "";
    private PromoCodeModel promoCodeModel= null;
    private boolean isWalletSelected;
    private int paymentType = 0, payByWallet ;
    private String cardToken , cardLastDigits,cardBrand;
    private int bookingType;
    private String driverPreference = "";
    private String bookingDate ="";
    private String areaZoneId = "",areaZoneTitle="" ,areaPickupId="" ,areaPickupTitle="";
    private String customerName,customerNumber;
    private String vehicleCapacity;
    private int isSomeOneElseBooking;
    private String favoriteDriverId="";

    public String getFavoriteDriverId() {
        return favoriteDriverId;
    }

    public void setFavoriteDriverId(String favoriteDriverId) {
        this.favoriteDriverId = favoriteDriverId;
    }

    public int isSomeOneElseBooking() {
        return isSomeOneElseBooking;
    }

    public void setIsSomeOneElseBooking(int isSomeOneElseBooking) {
        this.isSomeOneElseBooking = isSomeOneElseBooking;
    }

    public String getVehicleCapacity() {
        return vehicleCapacity;
    }

    public void setVehicleCapacity(String vehicleCapacity) {
        this.vehicleCapacity = vehicleCapacity;
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

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getDriverPreference() {
        return driverPreference;
    }

    public void setDriverPreference(String driverPreference) {
        this.driverPreference = driverPreference;
    }

    public int getBookingType() {
        return bookingType;
    }

    public void setBookingType(int bookingType) {
        this.bookingType = bookingType;
    }

    public String getCardToken() {
        return cardToken;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public String getCardLastDigits() {
        return cardLastDigits;
    }

    public void setCardLastDigits(String cardLastDigits) {
        this.cardLastDigits = cardLastDigits;
    }

    public String getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(String cardBrand) {
        this.cardBrand = cardBrand;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public int getPayByWallet() {
        return payByWallet;
    }

    public void setPayByWallet(int payByWallet) {
        this.payByWallet = payByWallet;
    }

    public boolean isWalletSelected() {
        return isWalletSelected;
    }

    public void setWalletSelected(boolean walletSelected) {
        isWalletSelected = walletSelected;
    }

    public PromoCodeModel getPromoCodeModel() {
        return promoCodeModel;
    }

    public void setPromoCodeModel(PromoCodeModel promoCodeModel) {
        this.promoCodeModel = promoCodeModel;
    }

    public String getSelectedPackageId() {
        return selectedPackageId;
    }

    public void setSelectedPackageId(String selectedPackageId) {
        this.selectedPackageId = selectedPackageId;
    }

    public CorporateProfileData getSelectedProfile() {
        return selectedProfile;
    }

    public void setSelectedProfile(CorporateProfileData selectedProfile) {
        this.selectedProfile = selectedProfile;
    }
}
