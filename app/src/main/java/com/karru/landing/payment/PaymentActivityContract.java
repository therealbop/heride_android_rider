package com.karru.landing.payment;

import com.karru.landing.payment.model.CardDetails;

import java.util.ArrayList;

public interface PaymentActivityContract
{
    interface View
    {
        /**
         * <h>Send Back API's Success Response</h>
         * <p>this method is using to send successfull response and setting response to Arraylist </p>
         * @param cardDetailsDataModels parameter contains the response object
         */
        void savedCardsDetails(ArrayList<CardDetails> cardDetailsDataModels);

        /**
         * <h>Error response</h>
         * <p>this method is using to send back the Error response to UI</p>
         */
        void errorResponse(String errorMsg);

        /**
         * <h>Dismiss Progress Dialog</h>
         * <p>this method is using to dismiss the Progress Dialog</p>
         */
        void dismissProgressDialog();

        /**
         * <h>showProgressDialog</h>
         * <p>this method is using to show the Progress Dialog</p>
         */
        void showProgressDialog();

        /**
         * <h>Bad GAteWay Error</h>
         * <p>this method is using to Disaply badGateWay Error to user</p>
         */
        void badGateWayError();

        /**
         * <h2>OnClickOfDeleteButton</h2>
         * This method is triggered when delete icon is clicked
         */
        void onClickOfDelete(int position);

        /**
         * <h2>onClickOfItem</h2>
         * This method is triggered when item layout is clicked
         */
        void onClickOfItem(CardDetails cardDetails);

        /**
         * <h2>onSelectOfCard</h2>
         * used to finish the activity when we select card
         */
        void onSelectOfCard();
        void deleteItemData(int position,ArrayList<CardDetails> cardsDetailsLists);
    }

    interface Presenter
    {
        /**
         * <h>API Call to Get card </h>
         * <p>This method is used to call the service for knowing all the cards that we previously stored in our profiles.</p>
         */
        void extractSavedCards();

        /**
         * <h2>deleteCard</h2>
         * triggered when the card is to be deleted
         * @param cardId  card token to be added
         * @param position position of card to be deleted
         */
        void deleteCard(String cardId, int position);

        /**
         * <h2>disposeObservables</h2>
         * used to dispose observables
         */
        void disposeObservables();

        /**
         * <h2>saveDefaultCard</h2>
         * used to save the default card
         * @param cardDetails  detail of card to be saved
         */
        void saveDefaultCard(CardDetails cardDetails);

        /**
         * <h2>makeCardDefault</h2>
         * used to make the card default in backend by calling API
         * @param cardDetails card details
         */
        void makeCardDefault(CardDetails cardDetails);

        /**
         * <h2>checkForCardSelect</h2>
         * this is used to check for card selection
         * @param cardDetails card details
         * @param isFromBooking is from booking
         */
        void checkForCardSelect(CardDetails cardDetails,boolean isFromBooking,boolean isDefault);

        void checkRTLConversion();
    }
}
