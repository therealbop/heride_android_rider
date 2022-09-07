package com.karru.booking_flow.invoice;

import android.os.Bundle;

import com.karru.booking_flow.invoice.model.ReceiptDetails;

import java.util.ArrayList;

/**
 * <h1>InvoiceContract</h1>
 * This interface is used to add the link between model and view
 * @author 3Embed
 * @since on 02-02-2018.
 */

public interface InvoiceContract
{
    interface View
    {

        /**
         * <h2>showProgress</h2>
         * This method is used to show the progress dialog
         */
        void showProgress();
        /**
         * <h2>dismissProgress</h2>
         * This method is used to hide the progress dialog
         */
        void dismissProgress();

        /**
         * <h2>setUIForInvoice</h2>
         * this method is used to set the UI for invoice screen
         * @param driverName driver name to be set
         */
        void setUIForInvoice(String driverName,String makeModel,String totalAmount,String distance,String time,
                             String appntDate,String driverProfilePic);

        /**
         * <h2>setUIForInvoice</h2>
         * this method is used to set the UI for invoice screen
         * @param pickDate pickup date
         * @param isRental
         * @param packageTitle
         */
        void setUIForReceipt(String pickDate, String pickAddress, String dropDate, String dropAddress,
                             boolean isRental, String packageTitle, ArrayList<ReceiptDetails> receiptDetailsList);

        /**
         * <h2>finishActivity</h2>
         * This method is used to finish the activity
         */
        void finishActivity();

        /**
         * <H2>populateFeedbackList</H2>
         * This class is used to populate the feedback list
         * @param supportDataModels  feedback list to be populated
         */
        void populateFeedbackList(ArrayList<String> supportDataModels);

        void showToast(String message);

        /**
         * <h2>enableTipOption</h2>
         * used to enable the tip option and populate the tip list
         */
        void enableTipOption(ArrayList<Integer> listOfTips,int tipType,String currency,int currencyType);

        /**
         * <h2>setPaymentCash</h2>
         * used to set the payment option as cash
         */
        void setPaymentCash(String cashAmount);

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
        void setPaymentCard(String cardNumber, String cardType,String cardAmount);

        /**
         * <h2>enableAddFavDriver</h2>
         * used to enable the add fav driver button
         */
        void enableAddFavDriver();

        /**
         * <h2>enableRemoveFavDriver</h2>
         * used to enable the remove fav driver button
         */
        void enableRemoveFavDriver();

        /**
         * <h2>hideFavDriverButton</h2>
         * used to deactivate the fav driver button
         */
        void hideFavDriverButton();
    }
    interface Presenter
    {
        /**
         * <h2>extractData</h2>
         * This method is used to extract model data from bundle
         * @param bundle bundle to be extracted
         */
        void extractData(Bundle bundle);

        /**
         * <h2>updateReviewForDriver</h2>
         * This method is used to update the rating and review for the driver
         */
        void updateReviewForDriver(float rating,String comment);

        /**
         * <h2>userSelectedReasonsList</h2>
         * This method is used to notify the presenter with the user selected reasons
         * @param userSelectedReasons user selected reasons list
         */
        void userSelectedReasonsList(StringBuilder userSelectedReasons);
        /**
         * <h2>getInvoiceDetailsData</h2>
         * This method is used to call the API to get the invoice details
         */
        void updateTip(double tip);

        /**
         * <h2>onRatingChanged</h2>
         * used to check if rate is changed
         * @param ratingPoints rating points
         */
        void onRatingChanged(int ratingPoints);

        /**
         * <h2>handleBackState</h2>
         * used to handle the back state
         */
        void handleBackState();

        /**
         * <h2>handleFavDriverClick</h2>
         * used to handle the click of fav driver button
         */
        void handleFavDriverClick();

        void checkRTLConversion();
    }
}
