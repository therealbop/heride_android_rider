package com.karru.booking_flow.ride.request.model;

/**
 * <h1>RequestingModel</h1>
 * This method is used to hold the data related with requesting screen
 * @author 3Embed
 * @since on 24-01-2018.
 */
public class RequestingModel
{
    private String vehicleTypeId ,amount ,timeFare,distFare,distance,duration,pickupAddress,bookingDate,
            pickupLongitude,pickupLatitude, cardToken,cardType,estimateId,selectedVehicleImage,selectedVehicleName;
    private int paymentType ,bookingType,payByWallet =0;
    private String areaZoneId = "",areaZoneTitle="" ,areaPickupId="" ,areaPickupTitle="";
    private String customerName = "",customerNumber="" ;
    private String numOfPassanger = "" ;
    private int isSomeOneElseBooking;
    private String instituteId = "",favoriteDriverId="" ;
    private String promoCode;
    private String driverPreference="";
    private boolean  isTowingEnabled = false;
    private int isPartnerUser; //isPartnerUser: 0- Normal User 1- Hotel user
    private int hotelUserType;  //1- Travel Desk 2-KIOSK;
    private String guestName,guestCountryCode,guestNumber,guestRoomNumber,hotelUserId;
    private String packageId ="";

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getHotelUserId() {
        return hotelUserId;
    }

    public void setHotelUserId(String hotelUserId) {
        this.hotelUserId = hotelUserId;
    }

    public int getIsPartnerUser() {
        return isPartnerUser;
    }

    public void setIsPartnerUser(int isPartnerUser) {
        this.isPartnerUser = isPartnerUser;
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

    public String getDriverPreference() {
        return driverPreference;
    }

    public void setDriverPreference(String driverPreference) {
        this.driverPreference = driverPreference;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getFavoriteDriverId() {
        return favoriteDriverId;
    }

    public void setFavoriteDriverId(String favoriteDriverId) {
        this.favoriteDriverId = favoriteDriverId;
    }

    public String getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(String instituteId) {
        this.instituteId = instituteId;
    }

    public int getIsSomeOneElseBooking() {
        return isSomeOneElseBooking;
    }

    public void setIsSomeOneElseBooking(int isSomeOneElseBooking) {
        this.isSomeOneElseBooking = isSomeOneElseBooking;
    }

    public int getPayByWallet() {
        return payByWallet;
    }

    public void setPayByWallet(int payByWallet) {
        this.payByWallet = payByWallet;
    }

    public int isSomeOneElseBooking() {
        return isSomeOneElseBooking;
    }

    public void setSomeOneElseBooking(int someOneElseBooking) {
        isSomeOneElseBooking = someOneElseBooking;
    }

    public String getNumOfPassanger() {
        return numOfPassanger;
    }

    public void setNumOfPassanger(String numOfPassanger) {
        this.numOfPassanger = numOfPassanger;
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
    public String getBookingDate() {
        return bookingDate;
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

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
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

    public String getCardToken() {
        return cardToken;
    }

    public String getEstimateId() {
        return estimateId;
    }

    public void setEstimateId(String estimateId) {
        this.estimateId = estimateId;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public String getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(String vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTimeFare() {
        return timeFare;
    }

    public void setTimeFare(String timeFare) {
        this.timeFare = timeFare;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getDistFare() {
        return distFare;
    }

    public void setDistFare(String distFare) {
        this.distFare = distFare;
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

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getPickupLongitude() {
        return pickupLongitude;
    }

    public void setPickupLongitude(String pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
    }

    public String getPickupLatitude() {
        return pickupLatitude;
    }

    public void setPickupLatitude(String pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public int getBookingType() {
        return bookingType;
    }

    public void setBookingType(int bookingType) {
        this.bookingType = bookingType;
    }
}
