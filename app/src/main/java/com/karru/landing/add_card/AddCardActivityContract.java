package com.karru.landing.add_card;


import com.stripe.android.model.Card;

public class AddCardActivityContract
{
    public interface AddCardView
    {
        /**
         * <h>API Response</h>
         * <p>this method is using to display response to the user</p>
         */
        void onCardAdded();

        /**
         * <h2>onAddCardError</h2>
         * used to show the erro for card add
         * @param errorMsg error msg
         */
        void onAddCardError(String errorMsg);

        /**
         * <h>Card validation success</h>
         * <p>this method is using to define card validation success action</p>
         */
        void onValidOfCard();

        /**
         * <h>Invalid card response</h>
         * <p>this method is using to define invalid card action</p>
         */
        void onInvalidOfCard();

        /**
         * <h>Finish Activity</h>
         * <p>this method is using to finish the current Activity</p>
         */
        void finishActivity();

        /**
         * <h2>updateUI</h2>
         * <p> method to update ui </p>
         * @param b: to set clickable
         * @param color: to set view background color
         * @param color1: to set Text color
         */
        void updateUI(boolean b, int color, int color1);

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
    }

    public interface AddCardPresenter
    {
        /**
         * <h2>addCardAPI</h2>
         *<p> This method is used to call the add card API</p>
         */
        void addCardAPI(String cardToken);

        /**
         * <h2>validateCardDetails</h2>
         * <p> This method is used to validate the card details </p>
         * @param card Card details
         */
        void validateCardDetails(Card card);

        /**
         * <h>Stripe Getter</h>
         * <p>this method is using to get Stripe key from sharedPreference</p>
         * @return stripe key
         */
        String stripeKeyGetter();
        /**
         *<h>Dispose Observable</h>
         * <h>this method is using to dispose the compositeDisposable observer</h>
         */
        void disposeObservable();

        /**
         * <h2>generateStripeToken</h2>
         * used to generate the stripe token
         * @param card card detials
         */
        void generateStripeToken(Card card);

        void checkRTLConversion();
    }
}
