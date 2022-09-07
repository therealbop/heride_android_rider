package com.karru.landing.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.karru.booking_flow.invoice.model.PaymentClass;
import com.karru.landing.home.model.fare_estimate_model.ExtraFeesModel;
import com.karru.booking_flow.invoice.model.TipDetailsModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 3Embed
 * @since on 02-02-2018..
 */
public class InvoiceDetailsModel implements Serializable
{
    /*"driverName":"Raghavendra V",
  "driverProfilePic":"https://s3-ap-southeast-2.amazonaws.com/karru/Drivers/ProfilePics/1517336515153_0_01.png",
  "pickupAddress":"Creative Villa Apartment, 44 RBI Colony, Vishveshvaraiah Nagar, Ganga Nagar, Bengaluru, Karnataka 560024, India",
  "dropAddress":"Shivaji Nagar, Bengaluru, Karnataka, India",
  "makeModel":"Yamahaa 5000",
  "bookingDate":"2018-02-03 06:31:48",
  "pickupTime":"2018-02-03 06:36:48",
  "dropTime":"2018-02-03 06:36:48",
  "baseFee":30,
  "time":1,
  "timeFee":"2.00",
  "waitingTime":1,
  "distance":"0.00",
  "distanceFee":"0.00",
  "tollFee":"0",
  "airportFees":"0",
  "creditUsed":"0",
  "signature":"",
  "subTotal":"34.00",
  "discount":"0.00",
  "extraFees":[],
  "total":"34.00"
  "paymentMethod":{
"paymentType":1,
"cardLastDigits":"1111",
"cardType":"Visa"
},*/
    @SerializedName("driverName")
    @Expose
    private String driverName;
    @SerializedName("bookingId")
    @Expose
    private String bookingId;
    @SerializedName("makeModel")
    @Expose
    private String makeModel;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("driverProfilePic")
    @Expose
    private String driverProfilePic;
    @SerializedName("driverId")
    @Expose
    private String driverId;
    @SerializedName("dropAddress")
    @Expose
    private String dropAddress;
    @SerializedName("bookingDate")
    @Expose
    private String bookingDate;
    @SerializedName("pickupTime")
    @Expose
    private String pickupTime;
    @SerializedName("pickupAddress")
    @Expose
    private String pickupAddress;
    @SerializedName("dropTime")
    @Expose
    private String dropTime;
    @SerializedName("waitingTime")
    @Expose
    private String waitingTime;
    @SerializedName("minFee")
    @Expose
    private double minFee;
    @SerializedName("baseFee")
    @Expose
    private double baseFee;
    @SerializedName("timeFee")
    @Expose
    private double timeFee;
    @SerializedName("distanceFee")
    @Expose
    private double distanceFee;
    @SerializedName("subTotal")
    @Expose
    private double subTotal;
    @SerializedName("discount")
    @Expose
    private double discount;
    @SerializedName("total")
    @Expose
    private double total;
    @SerializedName("waitingFee")
    @Expose
    private double waitingFee;
    @SerializedName("creditUsed")
    @Expose
    private double creditUsed;
    @SerializedName("lastDue")
    @Expose
    private double lastDue;
    @SerializedName("cancellationFee")
    @Expose
    private double cancellationFee;
    @SerializedName("callAPi")
    @Expose
    private boolean callAPi;
    @SerializedName("isMinFeeApplied")
    @Expose
    private int isMinFeeApplied;
    @SerializedName("tip")
    @Expose
    private TipDetailsModel tip;
    @SerializedName("currencySymbol")
    @Expose
    private String currencySymbol;

    @SerializedName("isRental")
    @Expose
    private boolean isRental;

    @SerializedName("packageTitle")
    @Expose
    private String packageTitle;

    @SerializedName("currencyAbbr")
    @Expose
    private int currencyAbbr;
    @SerializedName("tipType")
    @Expose
    private int tipType;
    @SerializedName("extraFees")
    @Expose
    private ArrayList<ExtraFeesModel> extraFees;
    @SerializedName("paymentMethod")
    @Expose
    private PaymentClass paymentMethod;
    @SerializedName("tipAmount")
    @Expose
    private double tipAmount;
    @SerializedName("isFavouriteDriver")
    @Expose
    private boolean isFavouriteDriver;
    @SerializedName("towTruckServices")
    @Expose
    private ArrayList<ExtraFeesModel> towTruckServices;
    @SerializedName("towTruckBookingService")
    @Expose
    private int towTruckBookingService;
    @SerializedName("towTruckBooking")
    @Expose
    private boolean towTruckBooking;

    public ArrayList<ExtraFeesModel> getTowTruckServices() {
        return towTruckServices;
    }

    public int getTowTruckBookingService() {
        return towTruckBookingService;
    }

    public boolean isTowTruckBooking() {
        return towTruckBooking;
    }

    public boolean isFavouriteDriver() {
        return isFavouriteDriver;
    }
    public String getDriverId() {
        return driverId;
    }
    public double getCancellationFee() {
        return cancellationFee;
    }
    public double getTipAmount() {
        return tipAmount;
    }
    public PaymentClass getPaymentMethod() {
        return paymentMethod;
    }
    public ArrayList<ExtraFeesModel> getExtraFees() {
        return extraFees;
    }
    public String getCurrencySymbol() {
        return currencySymbol;
    }
    public int getCurrencyAbbr() {
        return currencyAbbr;
    }
    public TipDetailsModel getTip() {
        return tip;
    }
    public double getMinFee() {
        return minFee;
    }
    public int getIsMinFeeApplied() {
        return isMinFeeApplied;
    }
    public double getLastDue() {
        return lastDue;
    }
    public double getSubTotal() {
        return subTotal;
    }
    public double getTimeFee() {
        return timeFee;
    }
    public double getDiscount() {
        return discount;
    }
    public double getWaitingFee() {
        return waitingFee;
    }
    public String getWaitingTime() {
        return waitingTime;
    }
    public double getDistanceFee() {
        return distanceFee;
    }
    public double getBaseFee() {
        return baseFee;
    }
    public String getDropTime() {
        return dropTime;
    }
    public String getPickupAddress() {
        return pickupAddress;
    }
    public String getPickupTime() {
        return pickupTime;
    }
    public String getBookingId() {
        return bookingId;
    }
    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }
    public boolean isCallAPi() {
        return callAPi;
    }
    public void setCallAPi(boolean callAPi) {
        this.callAPi = callAPi;
    }
    public String getDriverProfilePic() {
        return driverProfilePic;
    }
    public String getBookingDate() {
        return bookingDate;
    }
    public String getTime() {
        return time;
    }
    public String getDistance() {
        return distance;
    }
    public double getTotal() {
        return total;
    }
    public String getMakeModel() {
        return makeModel;
    }
    public String getDriverName() {
        return driverName;
    }
    public String getDropAddress() {
        return dropAddress;
    }
    public boolean isRental() {
        return isRental;
    }

    public void setRental(boolean rental) {
        isRental = rental;
    }

    public String getPackageTitle() {
        return packageTitle;
    }

    public void setPackageTitle(String packageTitle) {
        this.packageTitle = packageTitle;
    }
}
