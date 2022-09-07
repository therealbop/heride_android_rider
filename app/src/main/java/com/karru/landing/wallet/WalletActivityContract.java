package com.karru.landing.wallet;


public interface WalletActivityContract
{
    interface WalletView
    {
        /**
         * <h2>showToastNotifier</h2>
         * <p> method to trigger activity/fragment show progress dialog interfac </p>
         */
        void showProgressDialog();

        /**
         * <h2>showToastNotifier</h2>
         * <p> method to trigger activity/fragment showToast interface to show test </p>
         * @param msg: message to be shown in toast
         * @param duration: toast duration
         */
        void showToast(String msg, int duration);

        /**
         * <h2>showAlertNotifier</h2>
         * <p> method to trigger activity/fragment showAlertNotifier interface to show alert </p>
         * @param title: alert title to be setList
         * @param msg: alert message to be displayed
         */
        void showAlert(String title, String msg);

        /**
         * <H>Hide Progress bar</H>
         * <p>This method is using to hide the progress bar</p>
         */
        void hideProgressDialog();

        /**
         * <H>Set Wallet Balance</H>
         * <p>Set the updated Wallet balance</p>
         * @param balance new balance
         * @param hardLimit new hard limit
         * @param softLimit new soft limit
         */
        void setBalanceValues(String balance, String hardLimit, String softLimit,String currency,
                              int currencyAbbr);

        /**
         * <h>Show recharge Confirmation</h>
         * <p>This method is using to  display Confirmation message to User</p>
         * @param amount amount to display
         */
        void showRechargeConfirmationAlert(String amount,String amountToBESent);

        /**
         * <H>Set Card type View</H>
         * <p>this method is using to set card details view</p>
         * @param cardNum card last 4 digits
         * @param cardType card brand
         */
        void setCard(String cardNum, String cardType);

        /**
         * <H>Set no Card View</H>
         * <p>this method is using to set no card view</p>
         */
        void setNoCard();

        /**
         * <h2>prefixCurrency</h2>
         * usd to show the currency prefix
         */
        void prefixCurrency(String currency);

        /**
         * <h2>postfixCurrency</h2>
         * usd to show the currency postfix
         */
        void postfixCurrency(String currency);

        /**
         * <h2>showAlert</h2>
         * used to show the alert
         */
        void showAlert(String message);

        /**
         * <h2>showCardOption</h2>
         * used to show the card option
         */
        void showCardOption();

        /**
         * <h2>hideCardOption</h2>
         * used to hide the card option
         */
        void hideCardOption();
    }

    interface WalletPresenter
    {
        /**
         * <h>Recharge Wallet</h>
         * <p>this method is uisng to recharge the Wallet</p>
         * @param amountToBeSent amount to be rechrged
         */
        void rechargeWallet(String amountToBeSent);

        /**
         * <H>get Wallet limits</H>
         * <p>this method is using to get Wallet limit</p>
         */
        void getWalletDetails();

        /**
         * <h>Get Crad number</h>
         * <p>this method is using to get Card Last four digits</p>
         */
        void getLastCardNo();

        /**
         * <h2>disposeObservable</h2>
         * used to dispose the observables
         */
        void disposeObservable();

        /**
         * <h2>makeCardAsDefault</h2>
         * used to check for default card
         */
        void makeCardAsDefault();

        void checkRTLConversion();
    }
}
