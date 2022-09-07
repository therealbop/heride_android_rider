package com.karru.landing.history.history_details;

import com.karru.booking_flow.invoice.model.ReceiptDetails;

import java.util.ArrayList;

/**
 * <h1>HistoryDetailsContract</h1>
 * usd to provide contract between view and presenter
 * @author 3Embed
 * @since on 2/22/2018.
 */
public interface HistoryDetailsContract
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
         * <h2>setTitleActionBar</h2>
         * used to set the action bar values
         * @param date date to be set
         * @param bookingId booking ID
         */
        void setTitleActionBar(String date ,String bookingId,String statusText,String cancelReason);

        /**
         * <h2>setCancelStatus</h2>
         * used to check for booking status
         */
        void setCancelStatus();

        /**
         * <h2>setCancelStatus</h2>
         * used to check for booking status
         */
        void setCompletedStatus();

        /**
         * <h2>setDriverDetails</h2>
         * used to set the driver details
         * @param driverPic pic of driver to be set
         * @param name name of driver
         *             @param rate rate of driver
         *                         @param amount amount of booking
         *                                       @param businessName business name
         */
        void setDriverDetails(String driverPic ,String name,double rate,String amount,
                              String businessName);

        /**
         * <h2>setBookingDetails</h2>
         * usd to set the booking details
         * @param model model of vehicle
         *              @param plateNo plate number
         *              @param distanceParameter distanceParameter
         *              @param timeInMinute timeInMinute
         *              @param timeInHour timeInHour
         */
        void setBookingDetails(String plateNo,String model,String distance, String distanceParameter,
                               String timeInMinute,String timeInHour,String pickAddress,String dropAddress);

        /**
         * <h2>showReceiptDetails</h2>
         * usd to show the receipt details in   list
         * @param receiptDetailsList list for receipt
         */
        void showReceiptDetails( ArrayList<ReceiptDetails> receiptDetailsList);

        /**
         * <h2>hideReceiptTab</h2>
         * used to hide the receipt tab
         */
        void hideReceiptTab();

        /**
         * <h2>setPaymentCash</h2>
         * used to set the payment option as cash
         */
        void setPaymentCash(String cashCollected);

        /**
         * <h2>setPaymentCorporate</h2>
         * used to set the payment option as corporate
         */
        void setPaymentCorporate(String totalAmount);

        /**
         * <h2>setPaymentWallet</h2>
         * used to set the payment option as wallet
         */
        void setPaymentWallet(String walletAmount);

        /**
         * <h2>setPaymentCard</h2>
         * used to set the payment option as card
         */
        void setPaymentCard(String cardNumber, String cardType,String cardDeduct);

        /**
         * <h2>showToast</h2>
         * used to show the toast
         * @param message message to be shown
         */
        void showToast(String message);

        /**
         * <h1>setHelpDetailsList</h1>
         * use to update the adapter of help
         */
        void setHelpDetailsList();
    }

    interface Presenter
    {
        /**
         * <h2>getBookingDetails</h2>
         * used to get the booking details
         * @param bookingId booking ID
         */
        void getBookingDetails(String bookingId);

        /**
         * <h2>handleScreenDestroy</h2>
         * used to handle the destroy of screen
         */
        void handleScreenDestroy();

        /**
         * <h2>getHelpDetails</h2>
         * use to get the help list
         */
        void getHelpDetails();

        void checkRTLConversion();
    }
}
