package com.karru.landing.wallet.wallet_transaction;

import com.karru.landing.wallet.wallet_transaction.model.CreditDebitTransactionsModel;
import java.util.ArrayList;

public interface WalletTransactionContract
{

    interface View
    {
        void walletTransactionsApiSuccessViewNotifier();

        /**
         * <h2>showToastNotifier</h2>
         * <p> method to trigger activity/fragment show progress dialog interface </p>
         * @param msg: message to be shown along with the progress dialog
         */
        void showProgressDialog(String msg);

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
         * <h>Set All transaction data to display</h>
         * <p>this method is using to set the all transaction data</p>
         * @param allTransactionsAL all transaction data
         */
        void setAllTransactionsAL(ArrayList<CreditDebitTransactionsModel> allTransactionsAL);

        /**
         * <h>Set debit Transactions data to display</h>
         * <p>this method is using to set the debit  transaction data</p>
         * @param debitTransactionsAL debit transaction data
         */
        void setDebitTransactionsAL(ArrayList<CreditDebitTransactionsModel> debitTransactionsAL);

        /**
         * <h>Set credit Transactions data to display</h>
         * <p>this method is using to set the credit  transaction data</p>
         * @param creditTransactionsAL credit transaction data
         */
        void setCreditTransactionsAL(ArrayList<CreditDebitTransactionsModel> creditTransactionsAL);
    }

    interface WalletTransactionPresenter
    {
        /**
         *<h>Initialize transaction Api call</h>
         * <P>this method is using to initialize the Api call</P>
         */
        void initLoadTransactions();
        /**
         * <h2>disposeObservable</h2>
         * used to dispose the observables
         */
        void disposeObservable();

        void checkRTLConversion();
    }
}
